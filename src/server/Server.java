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
    
    //TODO: create a public method for getting to handleRequest to use for testing.
    //Want to receive input from a client socket and respond appropriately. 
    //Want to check that the response is correct.
	//Want to be able to close the serverSocket.
	
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
	 * A separate thread that processes the client requests on the 'queue'.
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
        		//Create the ClientRequest and add it to the queue.
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
			this.handleSendInvite(clientRequest.getConversationID(), clientRequest.getInvitees(), 
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
			//TODO: what is server-to-client message?
			out.println("LOG_ON FAIL USERNAME " + userID + " ALREADY EXISTS");
		}
		
		//User already logged on.
		else if (this.socketToUser.containsKey(socket)){
			PrintWriter out = this.socketToUser.get(socket).getPrintWriter();
			//TODO: what is server-to-client message?
			out.println("LOG_ON FAIL ALREADY LOGGED ON");
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
			//TODO: what is server-to-client message?
			out.println("LOG_ON SUCCESS USER_ID " + userID);
			
			//Send the list of all available conversations.
			String allConversations = this.getAllConversationsMessage();
			out.println(allConversations);
			
			/*
			 * To everyone.
			 */			        			
			this.sendAllOnlineUsersMessage();
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
			//TODO: what is server-to-client message?
			out.println("LOG_OFF FAIL ERROR1");
		}	
		
		else{
			//Get the list of conversations in which this user is involved.
			User user = this.socketToUser.get(socket);
			List<Conversation> conversations = user.getConversations();
			
			//Update mappings.
			String timeStamp = this.getTime();
			for(int i = 0; i < conversations.size(); i++){
				Conversation conversation = conversations.get(i);
				conversation.removeUser(user, timeStamp);
				user.removeConversation(conversation);
			}
			
			/*
			 * To the client who logged off.
			 */
			PrintWriter out = user.getPrintWriter();
			//TODO: what is server-to-client message?
			out.println("LOG_OFF SUCCESS");
			
			//Remove the user from the system.
			this.userIDToUser.remove(user.getUserID());
			this.socketToUser.remove(socket);			
			socket.close();
			
			/*
			 * To everyone else.
			 */
			this.sendAllOnlineUsersMessage();
			this.sendAllConversationsMessage();
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
			//TODO: what is server-to-client message?
			out.println("START_CHAT FAIL " + Error.error1);
		}
		
		else{
			//Get the output stream.
			User user = this.socketToUser.get(socket);
			PrintWriter out = user.getPrintWriter();	
						
			//The conversationID already exists.
			if(this.conversationIDToConversation.containsKey(conversationID)){
				//TODO: what is server-to-client message?
				out.println("START_CHAT FAIL " + conversationID + " ALREADY EXISTS");
			}
							
			else{
				/*
				 * To the creator.
				 */
				//TODO: what is server-to-client message?
				out.println("START_CHAT SUCCESS CONVERSATION_ID " + conversationID);
				
				//Add the new conversation to the system and update the mappings.
				String timeStamp = this.getTime();
				Conversation conversation = new Conversation(conversationID, user, timeStamp);
				this.conversationIDToConversation.put(conversationID, conversation);
				user.addConversation(conversation);
				
				/*
				 * To everyone.
				 */
				this.sendAllConversationsMessage();
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
			//TODO: what is server-to-client message?
			out.println("EXIT_CHAT FAIL " + Error.error1);
		}
		
		else{
			//Get the output stream.
			User user = this.socketToUser.get(socket);
			PrintWriter out = user.getPrintWriter();
						
			//If the conversation does not exist.
			if(!this.conversationIDToConversation.containsKey(conversationID)){
				//TODO: what is server-to-client message?
				out.println("EXIT_CHAT FAIL CONVERSATION_ID " + conversationID + " DOES NOT EXIST");
			}
			
			else{
				Conversation conversation = this.conversationIDToConversation.get(conversationID);
				
				//The client is not in the conversation.
				if(!user.getConversations().contains(conversation)){
					//TODO: what is server-to-client message?
					out.println("EXIT_CHAT FAIL " + Error.error4);
				}
				
				else{
					//Update mappings.
					String timeStamp = this.getTime();
					conversation.removeUser(user, timeStamp);
					user.removeConversation(conversation);
					
					/*
					 * To the client who is exiting a conversation.
					 */
					//TODO: what is server-to-client message?
					out.println("EXIT_CHAT SUCCESS CONVERSATION_ID " + conversationID);
									
					/*
					 * To everyone.
					 */
					this.sendAllConversationsMessage();	
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
			//TODO: what is server-to-client message?
			out.println("ENTER_CHAT FAIL " + Error.error1);
		}
		
		else{
			User user = this.socketToUser.get(socket);
			PrintWriter out = user.getPrintWriter();
						
			//If the conversation does not exist.
			if(!this.conversationIDToConversation.containsKey(conversationID)){
				//TODO: what is server-to-client message?
				out.println("ENTER_CHAT FAIL CONVERSATION_ID " + conversationID + " DOES NOT EXIST");
			}
			
			else{
				Conversation conversation = this.conversationIDToConversation.get(conversationID);
				
				//If the client is already in the conversation.
				if(user.getConversations().contains(conversation)){
					//TODO: what is server-to-client message?
					out.println("ENTER_CHAT FAIL " + Error.error5);
				}			
				
				else{
					/*
					 * To the client who entered the conversation.
					 */
					//TODO: what is server-to-client message?
					out.println("ENTER_CHAT SUCCESS CONVERSATION_ID " + conversationID);
					
					//Add mappings.	
					String timeStamp = this.getTime();	
					conversation.addUser(user, timeStamp);
					user.addConversation(conversation);
					
					/*
					 * To everyone.
					 */
					this.sendAllConversationsMessage();
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
			//TODO: what is server-to-client message?
			out.println("SEND_MESSAGE FAIL " + Error.error1);
		}
		
		else{
			User user = this.socketToUser.get(socket);
			PrintWriter out = user.getPrintWriter();
			
			//If the conversation does not exist.
			if(!this.conversationIDToConversation.containsKey(conversationID)){
				//TODO: what is server-to-client message?
				out.println("SEND_MESSAGE FAIL CONVERSATION_ID " + conversationID + " DOES NOT EXIST");
			}
			
			else{
				Conversation conversation = this.conversationIDToConversation.get(conversationID);
				
				//The client is not in the conversation.
				if(!conversation.contains(user)){
					//TODO: what is server-to-client message?
					out.println("SEND_MESSAGE FAIL " + Error.error4);
				}
				
				else{					
					/*
					 * To the client who sent the message.
					 */
					//TODO: what is server-to-client message?
					out.println("SEND_MESSAGE SUCCESS CONVERSATION_ID " + conversationID + " TEXT " + 
							message);

					//Add the dialogue to the conversation.
					String timeStamp = this.getTime();
					conversation.addMessage(user.getUserID(), message, timeStamp);
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
	private void handleSendInvite(String conversationID, String[] invitees, Socket socket) throws IOException{		
		//The client is not logged on.
		if(!this.socketToUser.containsKey(socket)){
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			//TODO: what is server-to-client message?
			out.println("SEND_INVITE FAIL " + Error.error1);
		}
		
		else{
			User user = this.socketToUser.get(socket);
			PrintWriter out = user.getPrintWriter();
			
			//If the conversation does not exist.
			if(!this.conversationIDToConversation.containsKey(conversationID)){
				//TODO: what is server-to-client message?
				out.println("SEND_INVITE FAIL CONVERSATION_ID " + conversationID + " DOES NOT EXIST");
			}
			
			else{
				Conversation conversation = this.conversationIDToConversation.get(conversationID);
				
				//The client is not in the conversation.
				if(!conversation.contains(user)){
					//TODO: what is server-to-client message?
					out.println("SEND_INVITE FAIL " + Error.error4);
				}
				
				else{
					/*
					 * To the client who sent out the invites.
					 */
					//TODO: what is server-to-client message?
					out.println("SEND_INVITE SUCCESS");
					
					//Invite the users who are not in the conversation.
					//TODO: if the invitee is already in the conversation, send a fail back to the
					//inviter.
					for(int i = 0; i < invitees.length; i++){
						if(!conversation.contains(invitees[i])){
							User userToSendInvite = this.userIDToUser.get(invitees[i]);
							PrintWriter outToSendInvite = userToSendInvite.getPrintWriter();
							outToSendInvite.println("SEND_INVITE CONVERSATION_ID " + conversationID);
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
		//The client is not logged on.
		if(!this.socketToUser.containsKey(socket)){
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println("INVALID INPUT");
		}
		
		else{
			User user = this.socketToUser.get(socket);
			PrintWriter out = user.getPrintWriter();
			out.println("INVALID INPUT");
		}
	}
	
	/**
	 * @return The current time in the format HH:MM:SS.
	 */
	private String getTime(){
		this.date = new Date();
		return this.date.toString().split(" ")[3];
	}
	
	/**
	 * Create the "UPDATE_USERS" message to be sent to all clients. This message will
	 * send a list of all userIDs who are currently online.
	 * @return The message that will be sent to all clients.
	 */
	private String getAllOnlineUsersMessage(){
		StringBuilder result = new StringBuilder();
		result.append("UPDATE_USERS");
		
		//Append the userIDs to the message.
		Iterator<String> iterator = this.userIDToUser.keySet().iterator();
		while(iterator.hasNext()){
			String nextUser = iterator.next();
			result.append(" " + nextUser);
		}
		
		return result.toString();
	}
	
	/**
	 * Send the "UPDATE_USERS" message to all clients.
	 * @throws IOException
	 */
	private void sendAllOnlineUsersMessage() throws IOException{
		String updatedUsers = this.getAllOnlineUsersMessage();
		
		Iterator<User> iteratorUsers = this.userIDToUser.values().iterator();
		while(iteratorUsers.hasNext()){
			User nextUser = iteratorUsers.next();
			PrintWriter nextOut = nextUser.getPrintWriter();
			nextOut.println(updatedUsers);
		}
	}
	
	/**
	 * Create the "UPDATE_CONVERSATIONS" message. This message will send a list of all conversations 
	 * (with their respective IDs and list of users) that have been created. Requires regex to be 
	 * "UPDATE_CONVERSATIONS[ CONVERSATION_ID conversationID USER_ID [userID]*]*"
	 * @return The message that will be sent to the clients.
	 */
	private String getAllConversationsMessage(){
		StringBuilder result = new StringBuilder();
		result.append("UPDATE_CONVERSATIONS");
		
		Iterator<Conversation> iterator = this.conversationIDToConversation.values().iterator();
		while(iterator.hasNext()){
			Conversation nextConversation = iterator.next();
			result.append(" CONVERSATION_ID " + nextConversation.getConversationID() + " USER_ID");
			
			List<User> users = nextConversation.getListUsers();
			for(User user : users){
				String userID = user.getUserID();
				result.append(" " + userID);
			}
		}
		
		return result.toString();
	}
	
	/**
	 * Send the "UPDATE_CONVERSATIONS" message to all clients.
	 * @throws IOException
	 */
	private void sendAllConversationsMessage() throws IOException{
		String allConversations = this.getAllConversationsMessage();
		
		Iterator<User> iterator = this.userIDToUser.values().iterator();
		while(iterator.hasNext()){
			User nextUser = iterator.next();
			PrintWriter nextOut = nextUser.getPrintWriter();
			nextOut.println(allConversations);
		}
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
