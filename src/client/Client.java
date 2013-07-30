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

import javax.swing.SwingUtilities;

import datatype.Conversation;
import datatype.ServerResponse;
import datatype.User;

import gui.LoginFrame;
import gui.MainFrame;
import gui.UserTab;

/**
 * GUI chat client runner.
 * @author Peter
 *
 */
public class Client implements Runnable{
	private final LoginFrame loginFrame;
	private final MainFrame mainFrame;
	
	private Socket mySocket;
	private PrintWriter myOut;
	private String myUsername;
	
	private HashMap<String, User> userNameToUser;
	private HashMap<String, Conversation> conversationNameToConversation;
	private HashSet<String> myInvites;
	
	public Client(){		
		this.loginFrame = new LoginFrame(this);
		this.mainFrame = new MainFrame(this);
		
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
			System.out.print("logging off unexpectedly...\n");
		}
		//TODO: implement.
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
		}
	}
	
	private void handleUserLogOn(String userID){
		this.userNameToUser.put(userID, new User(userID));
		
		System.out.print(this.userNameToUser + "\n");
		//TODO: update gui.
		UserTab userTab = this.mainFrame.getUserTab();
		userTab.addUser(userID);
	}
	
	private void handleUserLogOff(String userID){
		this.userNameToUser.remove(userID);
		
		System.out.print(this.userNameToUser + "\n");
		//TODO: update gui.
	}
	
	private void handleAddConversation(String conversationID){
		this.conversationNameToConversation.put(conversationID, new Conversation(conversationID));
		
		//TODO: update gui.
	}
	
	private void handleUserEnterChat(String conversationID, String userID){
		Conversation conversation = this.conversationNameToConversation.get(conversationID);
		User user = this.userNameToUser.get(userID);
		conversation.addUser(user);
		
		//TODO: update gui.
	}
	
	private void handleUserExitChat(String conversationID, String userID){
		Conversation conversation = this.conversationNameToConversation.get(conversationID);
		User user = this.userNameToUser.get(userID);
		conversation.removeUser(user);
		
		//TODO: update gui.
	}
	
	private void handleSendMessage(String conversationID, String userID, String time, String text){
		Conversation conversation = this.conversationNameToConversation.get(conversationID);
		conversation.addMessage(userID, text, time);
		
		System.out.print(conversation.getLastMessage().toString());
		//TODO: update gui.
	}
	
	private void handleReceivedInvite(String conversationID){
		this.myInvites.add(conversationID);
		
		//TODO: update gui.
	}
	
	private void handleLogOn(String userID){
		this.myUsername = userID;
		this.userNameToUser = new HashMap<String, User>();
		this.conversationNameToConversation = new HashMap<String, Conversation>();
		this.myInvites = new HashSet<String>();
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				loginFrame.setVisible(false);
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
			this.mySocket.close();
		} 
		
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		//TODO: implement
	}
	
	public static void main(String[] args){
		System.out.print("Running client...\n");
		Client client = new Client();
	}
	
}
