package server;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import datatype.*;


/**
 * Chat server runner for the IM client. 
 */
public class Server{
	private final ServerSocket serverSocket;
	private final Map<String, User> userIDToUser;
	private final Map<Socket, User> socketToUser;
	private final Map<String, Conversation> conversationIDToConversation;
	private Date date;
		
	//BlockingQueue for the messages from the clients.
	private final BlockingQueue<ClientRequest> queue;	
			
	/**
	 * Make a chat server that listens for connections on port.
	 * @param port Port number, requires 0 <= port <= 65535.
	 * @throws IOException
	 */
	public Server(int port) throws IOException{
		if (port < 0){
		    throw new IllegalArgumentException("Port number must be positive.");
		}
		
	    this.serverSocket = new ServerSocket(port);
	    this.userIDToUser = Collections.synchronizedMap(new HashMap<String, User>());
	    this.socketToUser = Collections.synchronizedMap(new HashMap<Socket, User>());
	    this.conversationIDToConversation = Collections.synchronizedMap(new HashMap<String, Conversation>());	    
	    this.queue = new LinkedBlockingQueue<ClientRequest>();
	}
	
	/**
	 * Run the server, listening for client connections and handling them.
	 * Also start the processing thread to read the client requests.
	 * Never returns unless an exception is thrown.
	 * @throws IOException If the main server socket is broken.
	 * (IOExceptions from individual clients do *not* terminate serve()).
	 */
	public void serve() throws IOException{
		//Start the processing thread.
		this.processRequests();
		
		while(true) {
			//This blocks until someone connects.
			final Socket socket = this.serverSocket.accept();
			
			System.out.println("New connection...");
			
			//Create a new thread for each client.
			Thread thread = new Thread(new Runnable() {
				public void run(){					
					try {
						handleConnection(socket);
					} 
            		
            		catch (IOException e) {
						e.printStackTrace();
					} 
            		
            		catch (InterruptedException e) {
						e.printStackTrace();
					}            		
            		
            		//Close the connection.
            		finally {
            			try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
            		}           		
				}
			});			
			thread.start();
		}
	}
	
	/**
	 * A separate thread that processes the client requests and takes them off the 'queue'.
	 */
	private void processRequests(){
		Thread processingThread = new Thread(new Runnable(){
			public void run(){
				while(true){
					try {
						//Get the client request or block until a client request is put on the queue.
						ClientRequest clientRequest = queue.take();
						handleRequest(clientRequest);		
					} 
					
					catch (InterruptedException e) {
						e.printStackTrace();
					} 
					
					catch (IOException e) {
						e.printStackTrace();
					}					
				}
			}
		});
		processingThread.start();
	}
	
	/**
	 * Handle a single client connection by adding any client requests to the 'queue'.
	 * Returns when the client disconnects.
	 * @param socket Socket where the client is connected.
	 * @throws IOException If connection has an error or terminates unexpectedly.
	 * @throws InterruptedException If interrupted while waiting.
	 */
	private void handleConnection(Socket socket) throws IOException, InterruptedException{
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        try{
        	for (String line = in.readLine(); line != null; line = in.readLine()) {
        		Lexer lexer = new Lexer(line, socket);
        		ClientRequest clientRequest = lexer.lex();
        		this.queue.put(clientRequest);
        	}
        }
        
        //To handle an unexpected log-off or poor internet connection.
        finally {
        	Lexer lexer = new Lexer("LOG_OFF", socket);
        	ClientRequest clientRequest = lexer.lex();
        	this.queue.put(clientRequest);
        	in.close();
        }
	}
		
	/**
	 * Handle a client request from the 'queue'.
	 * @param clientRequest The client's request.
	 * @throws IOException
	 */
	private void handleRequest(ClientRequest clientRequest) throws IOException{
		switch(clientRequest.getType()){
		case LOG_ON:
			this.handleLogOn(clientRequest.getUserID(), clientRequest.getSocket());
			break;
			
		case LOG_OFF:
			this.handleLogOff(clientRequest.getSocket());
			break;
			
		case START_CHAT:
			this.handleStartChat(clientRequest.getConversationID(), clientRequest.getSocket());
			break;
			
		case EXIT_CHAT:
			this.handleExitChat(clientRequest.getConversationID(), clientRequest.getSocket());
			break;
			
		case ENTER_CHAT:
			this.handleEnterChat(clientRequest.getConversationID(), clientRequest.getSocket());
			break;
			
		case SEND_MESSAGE:
			this.handleSendMessage(clientRequest.getConversationID(), clientRequest.getMessage(), 
					clientRequest.getSocket());
			break;
			
		case SEND_INVITE:
			this.handleSendInvite(clientRequest.getConversationID(), clientRequest.getInvitee(), 
					clientRequest.getSocket());
			break;
			
		case INVALID:
			this.handleInvalidInput(clientRequest.getSocket());
			break;
		}
	}
	
	/**
	 * Handles a log-on request.
	 * @param userID The submitted userID from the client.
	 * @param socket The client's socket.
	 * @throws IOException
	 */
	private void handleLogOn(String userID, Socket socket) throws IOException{		
		//UserID already in use.
		if (this.userIDToUser.containsKey(userID)){
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println("LOG_ON_FAIL1 USER_ID " + userID);
		}
		
		//TODO: delete this after testing.
		//User already logged on.
		else if (this.socketToUser.containsKey(socket)){
			PrintWriter out = this.socketToUser.get(socket).getPrintWriter();
			out.println("LOG_ON_FAIL0");
		}
		
		else{
			//Update mappings.
			User user = new User(userID, socket);
			this.userIDToUser.put(userID, user);
			this.socketToUser.put(socket, user);
			
			/*
			 * To the client who logged on.
			 */								
			PrintWriter out = user.getPrintWriter();
			out.println("LOG_ON USER_ID " + userID);
			this.updateAllUsers(user);
			this.updateAllConversations(out);
			
			/*
			 * To everyone else.
			 */			        			
			this.sendMessageToAllExceptOneClient(userID, "USER_LOG_ON USER_ID " + userID);
		}	
	}
	
	/**
	 * Handles a log-off request.
	 * @param socket The client's socket.
	 * @throws IOException
	 */
	private void handleLogOff(Socket socket) throws IOException{
		//If the client is not logged on.
		if(!this.socketToUser.containsKey(socket)){
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println("LOG_OFF_FAIL");
		}	
		
		else{
			//Get the list of conversations in which this user is involved.
			User user = this.socketToUser.get(socket);
			List<Conversation> conversations = user.getConversations();
			
			String timeStamp = this.getTime();
			
			for(int i = 0; i < conversations.size(); i++){
				Conversation conversation = conversations.get(i);
				this.removeUserFromConversation(user, conversation, timeStamp);
			}
			
			/*
			 * To the client who logged off.
			 */
			PrintWriter out = user.getPrintWriter();
			out.println("LOG_OFF");
			
			/*
			 * To everyone else.
			 */
			this.sendMessageToAllExceptOneClient(user.getUserID(), "USER_LOG_OFF USER_ID " + 
					user.getUserID());
			
			//Remove the user from the system.
			this.userIDToUser.remove(user.getUserID());
			this.socketToUser.remove(socket);			
			socket.close();
		}			
	}
	
	/**
	 * Handles a start chat request.
	 * @param conversationID The conversationID submitted by the client.
	 * @param socket The client's socket.
	 * @throws IOException
	 */
	private void handleStartChat(String conversationID, Socket socket) throws IOException{
		//Client not logged on.
		if(!socketToUser.containsKey(socket)){
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println("START_CHAT_FAIL0");
		}
		
		else{
			//Get the output stream.
			User user = this.socketToUser.get(socket);
			PrintWriter out = user.getPrintWriter();	
						
			//The conversationID already exists.
			if(this.conversationIDToConversation.containsKey(conversationID)){
				out.println("START_CHAT_FAIL1 CONVERSATION_ID " + conversationID);
			}
							
			else{
				/*
				 * To the creator.
				 */
				out.println("START_CHAT CONVERSATION_ID " + conversationID);
				
				/*
				 * To everyone else.
				 */
				this.sendMessageToAllExceptOneClient(user.getUserID(), 
						"ADD_CONVERSATION CONVERSATION_ID " + conversationID);
				
				//Add the new conversation to the system and update the mappings.
				String timeStamp = this.getTime();
				Conversation conversation = new Conversation(conversationID);
				this.conversationIDToConversation.put(conversationID, conversation);
				this.addUserToConversation(user, conversation, timeStamp);
			}
		}		
	}
	
	/**
	 * Handles an exit chat request.
	 * @param conversationID The conversationID submitted by the client.
	 * @param socket The client's socket.
	 * @throws IOException
	 */
	private void handleExitChat(String conversationID, Socket socket) throws IOException{		
		//Client is not logged on.
		if(!this.socketToUser.containsKey(socket)){
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println("EXIT_CHAT_FAIL0");
		}
		
		else{
			User user = this.socketToUser.get(socket);
			PrintWriter out = user.getPrintWriter();
						
			//If the conversation does not exist.
			if(!this.conversationIDToConversation.containsKey(conversationID)){
				out.println("EXIT_CHAT_FAIL1 CONVERSATION_ID " + conversationID);
			}
			
			else{
				Conversation conversation = this.conversationIDToConversation.get(conversationID);
				
				//If the client is not in the conversation.
				if(!user.getConversations().contains(conversation)){
					out.println("EXIT_CHAT_FAIL2");
				}
				
				else{
					out.println("EXIT_CHAT CONVERSATION_ID " + conversationID);
					String timeStamp = this.getTime();
					this.removeUserFromConversation(user, conversation, timeStamp);
				}	
			}			
		}
	}
	
	/**
	 * Handles an enter chat request.
	 * @param conversationID The conversationID submitted by the client.
	 * @param socket The client's socket.
	 * @throws IOException
	 */
	private void handleEnterChat(String conversationID, Socket socket) throws IOException{		
		//The client is not logged on.
		if(!this.socketToUser.containsKey(socket)){
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println("ENTER_CHAT_FAIL0");
		}
		
		else{
			User user = this.socketToUser.get(socket);
			PrintWriter out = user.getPrintWriter();
						
			//If the conversation does not exist.
			if(!this.conversationIDToConversation.containsKey(conversationID)){
				out.println("ENTER_CHAT_FAIL1 CONVERSATION_ID " + conversationID);
			}
			
			else{
				Conversation conversation = this.conversationIDToConversation.get(conversationID);
				
				//If the client is already in the conversation.
				if(user.getConversations().contains(conversation)){
					out.println("ENTER_CHAT_FAIL2");
				}			
				
				else{
					out.println("ENTER_CHAT CONVERSATION_ID " + conversationID);
					conversation.sendHistoryToUser(out);
					String timeStamp = this.getTime();
					this.addUserToConversation(user, conversation, timeStamp);
				}
			}						
		}
	}
	
	/**
	 * Handles a send message request.
	 * @param conversationID The conversationID submitted by the client.
	 * @param message The message to be sent to the conversation.
	 * @param socket The client's socket.
	 * @throws IOException
	 */
	private void handleSendMessage(String conversationID, String message, Socket socket) throws IOException{
		//Client is not logged on.
		if(!this.socketToUser.containsKey(socket)){
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println("SEND_MESSAGE_FAIL0");
		}
		
		else{
			User user = this.socketToUser.get(socket);
			PrintWriter out = user.getPrintWriter();
			
			//If the conversation does not exist.
			if(!this.conversationIDToConversation.containsKey(conversationID)){
				out.println("SEND_MESSAGE_FAIL1 CONVERSATION_ID " + conversationID);
			}
			
			else{
				Conversation conversation = this.conversationIDToConversation.get(conversationID);
				
				//If the client is not in the conversation.
				if(!conversation.contains(user)){
					out.println("SEND_MESSAGE_FAIL2 CONVERSATION_ID " + conversationID);
				}
				
				else{
					//Add the dialogue to the conversation.
					String timeStamp = this.getTime();
					conversation.addMessage(user.getUserID(), message, timeStamp);
					
					this.sendMessageToAllUsersInConversation(conversation,
							"SEND_MESSAGE CONVERSATION_ID " + conversation.getConversationID() +
							" " + conversation.getLastMessage().toString());
				}
			}		
		}
	}
	
	/**
	 * Handles a send invite request.
	 * @param conversationID The conversationID submitted by the client.
	 * @param invitees The users to be invited to the conversation 'conversationID'.
	 * @param socket The client's socket.
	 * @throws IOException
	 */
	private void handleSendInvite(String conversationID, String invitee, Socket socket) throws IOException{		
		//The client is not logged on.
		if(!this.socketToUser.containsKey(socket)){
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println("SEND_INVITE_FAIL0");
		}
		
		else{
			User user = this.socketToUser.get(socket);
			PrintWriter out = user.getPrintWriter();
			
			//If the conversation does not exist.
			if(!this.conversationIDToConversation.containsKey(conversationID)){
				out.println("SEND_INVITE_FAIL1 CONVERSATION_ID " + conversationID);
			}
			
			else{
				Conversation conversation = this.conversationIDToConversation.get(conversationID);
				
				//The client is not in the conversation.
				if(!conversation.contains(user)){
					out.println("SEND_INVITE_FAIL2 CONVERSATION_ID " + conversationID);
				}
				
				else{
					if(!conversation.contains(invitee)){
						User userToInvite = this.userIDToUser.get(invitee);
						
						if(userToInvite != null){
							PrintWriter outToInvite = userToInvite.getPrintWriter();
							outToInvite.println("RECEIVED_INVITE CONVERSATION_ID " + conversationID);
						}
						
						else{
							out.println("SEND_INVITE_FAIL3 USER_ID " + invitee);
						}
					}
				}	
			}		
		}
	}
	
	/**
	 * Handles an invalid client request.
	 * @param socket The client's socket.
	 * @throws IOException
	 */
	private void handleInvalidInput(Socket socket) throws IOException{
		//If the client is not logged on.
		if(!this.socketToUser.containsKey(socket)){
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println("INVALID_INPUT");
		}
		
		else{
			User user = this.socketToUser.get(socket);
			PrintWriter out = user.getPrintWriter();
			out.println("INVALID_INPUT");
		}
	}
	
	/**
	 * When a client logs on, send all available users (except himself) to the client.
	 * @param user The client who logged on.
	 */
	private void updateAllUsers(User user){
		PrintWriter out = user.getPrintWriter();
		Iterator<User> users = this.userIDToUser.values().iterator();
		
		while(users.hasNext()){
			User nextUser = users.next();
			
			if(!nextUser.getUserID().equals(user.getUserID())){
				out.println("USER_LOG_ON USER_ID " + nextUser.getUserID());
			}
		}
	}
	
	/**
	 * When a client logs on, send all available conversations as well as the users in those
	 * respective conversations to the client. 
	 * @param out The client's output stream.
	 */
	private void updateAllConversations(PrintWriter out){
		Iterator<Conversation> conversations = this.conversationIDToConversation.values().iterator();
		
		while(conversations.hasNext()){
			Conversation conversation = conversations.next();
			out.println("ADD_CONVERSATION CONVERSATION_ID " + conversation.getConversationID());
			List<User> users = conversation.getListUsers();
			
			//Update the client to every user in the conversation.
			for(int i = 0; i < users.size(); i++){
				User user = users.get(i);
				out.println("USER_ENTER_CHAT CONVERSATION_ID " + conversation.getConversationID() +
						" USER_ID " + user.getUserID());
			}
		}
	}
	
	/**
	 * Update all clients when a client does something (log on, log off, etc.).
	 * @param userID The username of the client who did something.
	 * @param message The message to send to all clients except 'user'.
	 */
	private void sendMessageToAllExceptOneClient(String userID, String message){
		Iterator<User> users = this.userIDToUser.values().iterator();
		
		while(users.hasNext()){
			User nextUser = users.next();
			
			//Do not send the message to the user who did something.
			if(!nextUser.getUserID().equals(userID)){
				PrintWriter out = nextUser.getPrintWriter();
				out.println(message);
			}
		}
	}
	
	/**
	 * Send a message to all users in a conversation.
	 * @param conversation The conversation in which the message is to be sent.
	 * @param message The message to be sent.
	 */
	private void sendMessageToAllUsersInConversation(Conversation conversation, String message){
		List<User> users = conversation.getListUsers();
		for(User user : users){
			PrintWriter out = user.getPrintWriter();
			out.println(message);
		}
	}
	
	/**
	 * Add a client to a conversation, updating the conversation history and system mappings.
	 * @param user The client to be added to the conversation.
	 * @param conversation The conversation to which the client will be added.
	 * @param timestamp The time at which the client is added to the conversation.
	 */
	private void addUserToConversation(User user, Conversation conversation, String timeStamp){
		conversation.addUser(user);
		user.addConversation(conversation);
		conversation.addMessage("MASTER", user.getUserID() + " has entered the room.", timeStamp);
		this.sendMessageToAllUsersInConversation(conversation, "SEND_MESSAGE CONVERSATION_ID " + 
				conversation.getConversationID() + " " + conversation.getLastMessage().toString());
		this.sendMessageToAllExceptOneClient(user.getUserID(), "USER_ENTER_CHAT CONVERSATION_ID " +
				conversation.getConversationID() + " USER_ID " + user.getUserID());
	}
	
	/**
	 * Remove a client from a conversation, updating the conversation history and system mappings.
	 * @param user The client to be removed from the conversation.
	 * @param conversation The conversation from which the client will be removed.
	 * @param timeStamp The time at which the client is removed from the conversation.
	 */
	private void removeUserFromConversation(User user, Conversation conversation, String timeStamp){
		conversation.removeUser(user);
		user.removeConversation(conversation);
		conversation.addMessage("MASTER", user.getUserID() + " has left the room.", timeStamp);
		this.sendMessageToAllUsersInConversation(conversation, "SEND_MESSAGE CONVERSATION_ID " + 
				conversation.getConversationID() + " " + conversation.getLastMessage().toString());
		
		this.sendMessageToAllExceptOneClient(user.getUserID(), "USER_EXIT_CHAT CONVERSATION_ID " + 
				conversation.getConversationID() + " USER_ID " + user.getUserID());
	}
	
	/**
	 * @return The current time in the format HH:MM:SS.
	 */
	private String getTime(){
		this.date = new Date();
		return this.date.toString().split(" ")[3];
	}
	
    /**
     * Start a chat server.
     */
    public static void main(String[] args) {    	
    	//Default port, do not change.
    	final int port = 4444;
    	
    	//Initiate the server.
    	try {
			Server server = new Server(port);
			server.serve();
		} 
    	
    	catch (IOException e) {
			e.printStackTrace();
		}
    }
}
