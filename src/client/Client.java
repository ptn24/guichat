package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;

import datatype.Conversation;
import datatype.Invite;
import datatype.ServerResponse;
import datatype.User;

import gui.ConversationFrame;
import gui.ConversationTab;
import gui.CreateConversationFrame;
import gui.InviteTab;
import gui.LoginFrame;
import gui.MainFrame;
import gui.UserTab;

/**
 * GUI chat client runner.
 * @author Peter
 *
 */
public class Client {
	private final LoginFrame loginFrame;
	private MainFrame mainFrame;
	private CreateConversationFrame createConversationFrame;
	
	private Socket mySocket;
	private PrintWriter myOut;
	private User myUser;
	private String myUsername;
	
	private HashMap<String, User> userNameToUser;
	private HashMap<String, Conversation> conversationNameToConversation;
	private HashMap<String, ConversationFrame> conversationNameToConversationFrame;
	private HashSet<Invite> myInvites;
	
	public Client(){		
		this.loginFrame = new LoginFrame(this);
		
		//Display the login window.
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				loginFrame.setVisible(true);
			}
		});
	}
	
	/**
	 * Attempt to log into the chat system.
	 * @param ip The server IP address.
	 * @param port The port number on the server.
	 * @param username The client's username.
	 * @return 0 if a connection was able to be established, and -1 otherwise.
	 */
	public int login(String ip, int port, String username){
		try {
			//TODO: implement logging into system on a different server (change ip).
			
			this.mySocket = new Socket(InetAddress.getByName(ip), port);
			this.myOut = new PrintWriter(this.mySocket.getOutputStream(), true);
			this.myOut.println("LOG_ON USER_ID " + username);
			
			//Create a thread to handle the client's connection to the server.
			Thread thread = new Thread(new Runnable(){
				public void run(){
					try {
						handleConnection(mySocket);
					} 
					
					catch (IOException e) {
						e.printStackTrace();
						//TODO: implement.
					}
				}
			});
			thread.start();
			return 0;
		} 
		
		catch (UnknownHostException e) {
			return -1;
		} 
		
		catch (IOException e) {
			return -1;
		}
	}
	
	private void handleConnection(Socket socket) throws IOException{		
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		try{
			for(String line = in.readLine(); line != null; line = in.readLine()){
				System.out.print("From server: " + line + "\n");
				
				Lexer lexer = new Lexer(line);
				ServerResponse serverResponse = lexer.lex();
				this.handleResponse(serverResponse);
			}
		}
		
		finally{
			in.close();
			this.mySocket.close();
			
			System.out.print("logging off unexpectedly...\n");
		}
	}
	
	private void handleResponse(ServerResponse serverResponse){
		switch(serverResponse.getType()){
		case USER_LOG_ON:
			this.handleUserLogOn(serverResponse.getUserID());
			break;
			
		case USER_LOG_OFF:
			this.handleUserLogOff(serverResponse.getUserID());
			break;
			
		case ADD_CONVERSATION:
			this.handleAddConversation(serverResponse.getConversationID());
			break;
			
		case USER_ENTER_CHAT:
			this.handleUserEnterChat(serverResponse.getConversationID(), 
					serverResponse.getUserID());
			break;
			
		case USER_EXIT_CHAT:
			this.handleUserExitChat(serverResponse.getConversationID(), 
					serverResponse.getUserID());
			break;
			
		case SEND_MESSAGE:
			this.handleSendMessage(serverResponse.getConversationID(), serverResponse.getUserID(), 
					serverResponse.getTime(), serverResponse.getText());
			break;
			
		case RECEIVED_INVITE:
			this.handleReceivedInvite(serverResponse.getConversationID());
			break;
			
		case LOG_ON:
			this.handleLogOn(serverResponse.getUserID());
			break;
			
		case LOG_ON_FAIL0:
			System.out.print("USER ALREADY LOGGED ON \n");
			break;
			
		case LOG_ON_FAIL1:
			this.handleLogOnFail1();
			break;
			
		case LOG_OFF:
			this.handleLogOff();
			break;
			
		case LOG_OFF_FAIL:
			this.handleLogOffFail();
			break;
			
		case START_CHAT:
			this.handleStartChat(serverResponse.getConversationID());
			break;
			
		case START_CHAT_FAIL0:
			//TODO: implement.
			break;
			
		case START_CHAT_FAIL1:
			this.handleStartChatFail1(serverResponse.getConversationID());
			break;
						
		case ENTER_CHAT:
			this.handleEnterChat(serverResponse.getConversationID());
			break;
			
		case ENTER_CHAT_FAIL0:
			//TODO: implement.
			break;
			
		case ENTER_CHAT_FAIL1:
			//TODO: implement.
			break;
		
		case ENTER_CHAT_FAIL2:
			//TODO: implement.
			break;
		
		case EXIT_CHAT:
			this.handleExitChat(serverResponse.getConversationID());
			break;
			
		case EXIT_CHAT_FAIL0:
			//TODO: implement.
			break;
			
		case EXIT_CHAT_FAIL1:
			//TODO: implement.
			break;
		
		case EXIT_CHAT_FAIL2:
			//TODO: implement.
			break;
			
		case SEND_MESSAGE_FAIL0:
			//TODO: implement.
			break;
			
		case SEND_MESSAGE_FAIL1:
			//TODO: implement.
			break;
			
		case SEND_MESSAGE_FAIL2:
			//TODO: implement.
			break;
			
		case SEND_INVITE_FAIL0:
			//TODO: implement.
			break;
			
		case SEND_INVITE_FAIL1:
			//TODO: implement.
			break;
			
		case SEND_INVITE_FAIL2:
			//TODO: implement.
			break;
			
		case SEND_INVITE_FAIL3:
			//TODO: implement.
			break;
			
		case INVALID_INPUT:
			//TODO: implement.
			break;
		}
	}
	
	private void handleUserLogOn(String userID){
		this.userNameToUser.put(userID, new User(userID));
		
		//Update main gui.
		UserTab userTab = this.mainFrame.getUserTab();
		userTab.addUser(userID);
	}
	
	private void handleUserLogOff(String userID){
		this.userNameToUser.remove(userID);
		
		//Update main gui.
		UserTab userTab = this.mainFrame.getUserTab();
		userTab.removeUser(userID);
		
		/*
		 * Do not need to update the conversation gui because the server will send a separate
		 * message for each conversation.
		 */
	}
	
	private void handleAddConversation(String conversationID){
		this.conversationNameToConversation.put(conversationID, new Conversation(conversationID));
		
		//Update main gui.
		ConversationTab conversationTab = this.mainFrame.getConversationTab();
		conversationTab.addConversationNode(conversationID);
	}
	
	private void handleUserEnterChat(String conversationID, String userID){
		Conversation conversation = this.conversationNameToConversation.get(conversationID);
		User user;
		
		if(userID.equals(this.myUsername)){
			user = this.myUser;
		}
		
		else{
			user = this.userNameToUser.get(userID);
		}
		
		conversation.addUser(user);
		
		//Update main gui's conversation tab.
		ConversationTab conversationTab = this.mainFrame.getConversationTab();
		conversationTab.addUserNode(conversationID, userID);
		
		//Update the conversation gui's user table if this client is in the conversation.
		if(this.conversationNameToConversationFrame.containsKey(conversationID)){
			ConversationFrame conversationFrame = this.conversationNameToConversationFrame.get(conversationID);
			
			if(userID.equals(this.myUsername)){
				conversationFrame.addUser(userID + " (me)");
			}
			
			else{
				conversationFrame.addUser(userID);
			}
		}
	}
	
	private void handleUserExitChat(String conversationID, String userID){
		Conversation conversation = this.conversationNameToConversation.get(conversationID);
		User user;
		
		if(userID.equals(this.myUsername)){
			user = this.myUser;
		}
		
		else{
			user = this.userNameToUser.get(userID);
		}
		
		conversation.removeUser(user);
		
		//Update main gui.
		ConversationTab conversationTab = this.mainFrame.getConversationTab();
		conversationTab.removeUserNode(conversationID, userID);
		
		/*
		 * Update the conversation gui if the conversation window is open (this client is
		 * currently in the conversation).
		 */
		if(this.conversationNameToConversationFrame.containsKey(conversationID)){
			this.conversationNameToConversationFrame.get(conversationID).removeUser(userID);
		}
	}
	
	private void handleSendMessage(String conversationID, String userID, String time, String text){
		Conversation conversation = this.conversationNameToConversation.get(conversationID);
		conversation.addMessage(userID, text, time);
		
		//Update the conversation gui.
		this.conversationNameToConversationFrame.get(conversationID).addMessage(userID, time, text);
	}
	
	private void handleReceivedInvite(String conversationID){
		Invite invite = new Invite(conversationID, null);
		this.myInvites.add(invite);
		
		//Update the invite tab of the main gui.
		InviteTab inviteTab = this.mainFrame.getInviteTab();
		inviteTab.addInvite(invite);
	}
	
	private void handleLogOn(String userID){
		this.mainFrame = new MainFrame(this);
		this.mainFrame.getConversationTab().setUsername(userID);
		
		this.myUsername = userID;
		this.myUser = new User(userID);
		
		this.userNameToUser = new HashMap<String, User>();
		this.conversationNameToConversation = new HashMap<String, Conversation>();
		this.conversationNameToConversationFrame = new HashMap<String, ConversationFrame>();
		this.myInvites = new HashSet<Invite>();
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				loginFrame.dispose();
				mainFrame.setVisible(true);
			}
		});
	}
	
	private void handleLogOnFail1(){
		this.loginFrame.getUserEntryTextField().setText(this.loginFrame.getUserEnteredUsername());
		this.loginFrame.setErrorPanelLabel("This username is already in use.");
	}
	
	private void handleLogOff(){
		try {
			//Close the main window and all conversation windows. Open the login window.
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					mainFrame.dispose();
					
					//Close all conversation windows.
					Iterator<String> it = conversationNameToConversationFrame.keySet().iterator();
					while(it.hasNext()){
						String nextConversationID = it.next();
						conversationNameToConversationFrame.get(nextConversationID).dispose();
					}
					
					loginFrame.setVisible(true);
				}
			});
			
			this.mySocket.close();
		} 
		
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleLogOffFail(){
		//TODO: implement.
	}
	
	private void handleStartChat(String conversationID){
		this.handleAddConversation(conversationID);
		this.handleEnterChat(conversationID);
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				createConversationFrame.dispose();
			}
		});
	}
	
	private void handleStartChatFail1(String conversationID){
		this.createConversationFrame.getErrorPanel().setErrorLabel(
				"Conversation name is already in use.");
		this.createConversationFrame.getUserEntryTextField().setText(conversationID);
		this.createConversationFrame.getUserEntryTextField().selectAll();
		this.createConversationFrame.getUserEntryTextField().requestFocus();
	}
	
	private void handleEnterChat(String conversationID){
		//Instantiate the conversation frame.
		final ConversationFrame conversationFrame = new ConversationFrame(this, conversationID);
		this.conversationNameToConversationFrame.put(conversationID, conversationFrame);
		
		//Update the conversation frame's user table.
		List<User> users = this.conversationNameToConversation.get(conversationID).getListUsers();
		for(User user : users){
			conversationFrame.addUser(user.getUserID());
		}
		
		//Open the conversation frame.
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				conversationFrame.setVisible(true);
			}
		});
				
		this.handleUserEnterChat(conversationID, this.myUsername);		
	}
	
	private void handleExitChat(String conversationID){
		this.conversationNameToConversationFrame.remove(conversationID);
		this.handleUserExitChat(conversationID, this.myUsername);
	}
	
	/**
	 * Request the server to start a conversation. This is activated from the create 
	 * conversation frame's action listener.
	 * @param conversationID
	 */
	public void requestStartChat(String conversationID){
		this.myOut.println("START_CHAT CONVERSATION_ID " + conversationID);
	}
	
	/**
	 * Request the server to enter a conversation. This is activated from the conversation tab's
	 * popup listener.
	 * @param conversationID
	 */
	public void requestEnterChat(String conversationID){
		this.myOut.println("ENTER_CHAT CONVERSATION_ID " + conversationID);
	}
	
	/**
	 * Request the server to exit a conversation. This is activated from the conversation frame's
	 * window listener.
	 * @param conversationID
	 */
	public void requestExitChat(String conversationID){
		this.myOut.println("EXIT_CHAT CONVERSATION_ID " + conversationID);
	}
	
	public void requestLogOff(){
		this.myOut.println("LOG_OFF");
	}
	
	/**
	 * Request the server to send a message to a conversation. This is activated from the 
	 * conversation frame's text area's action listener.
	 * @param conversationID
	 * @param text
	 */
	public void requestSendMessage(String conversationID, String text){
		this.myOut.println("SEND_MESSAGE CONVERSATION_ID " + conversationID + " _TEXT_ " + text);
	}
	
	public void requestSendInvite(String conversationID, String userID){
		this.myOut.println("SEND_INVITE CONVERSATION_ID " + conversationID + " USER_ID " + userID);
	}
	
	public void openCreateConversationWindow(){
		this.createConversationFrame = new CreateConversationFrame(this);
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				createConversationFrame.setVisible(true);
			}
		});
	}
	
	public void removeInvite(Invite invite){
		this.myInvites.remove(invite);
	}
	
	/**
	 * 
	 * @return The client's username.
	 */
	public String getMyUsername(){
		return this.myUsername;
	}
	
	/**
	 * 
	 * @param conversationID
	 * @return The conversation whose name is 'conversationID'.
	 */
	public Conversation getConversation(String conversationID){
		return this.conversationNameToConversation.get(conversationID);
	}
	
	/**
	 * 
	 * @param userID
	 * @return whether or not a client with the username 'userID' is in the system.
	 */
	public boolean isUserLoggedOn(String userID){
		return this.userNameToUser.keySet().contains(userID);
	}
	
	/**
	 * 
	 * @param conversationID
	 * @param userID
	 * @return whether or not a client with the username 'userID' is in the chat 
	 * 		'conversationID'.
	 */
	public boolean isUserInChat(String conversationID, String userID){
		Conversation conversation = this.conversationNameToConversation.get(conversationID);
		return conversation.contains(userID);
	}
	
	public static void main(String[] args){
		new Client();
	}
	
}
