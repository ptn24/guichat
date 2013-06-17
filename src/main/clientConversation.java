package main;

import gui.conversationgui;
import javax.swing.SwingUtilities;

public class clientConversation {
	
	private final String conversationID;
	private String[] users;
	private String history = "";
	private String[] allusers;
	private conversationgui conversationIDgui;
	private Client client;
	
	
	/**
	 * Create a new Conversation with the Client.
	 * @param conversationID The unique identifier for the Conversation.
	 * @param userID The unique identifier for the User who created this Conversation.
	 */
	public clientConversation(Client client, String conversationID){
		this.conversationID = conversationID;
		this.client = client;
		
		//Instantiate the 'users' list and add the creator ID.
		this.users = new String[0];
		this.allusers = new String[0];
				
		//Instantiate the history.
		this.history = "";
		conversationIDgui = new conversationgui(client);
		conversationIDgui.setVisible(true);
		updatetitle();
		
	}
	
	public void updateUsers(String[] array){
		this.users = array.clone();
				
		Runnable update = new Runnable(){
			public void run(){
				System.out.println("udate USERS in conversation reached for convesation");
				conversationIDgui.usersInConversationList(users);
				}
		};
		
		SwingUtilities.invokeLater(update);
	}
	
	public void updateHistory(String message){
		this.history += message;
		Runnable update = new Runnable(){
			public void run(){
				System.out.println("update HISTORY reached for convesation");
				conversationIDgui.messageList(history);
				}
		};
		SwingUtilities.invokeLater(update);
	}
	
	public void updateAllUsers(String[] array){
		this.allusers = array.clone();
		
		Runnable update = new Runnable(){
			public void run(){
				conversationIDgui.allUsersList(allusers);
				}
		};
		SwingUtilities.invokeLater(update);
	}

	public void updatetitle(){
		Runnable update = new Runnable(){
			public void run(){
				conversationIDgui.setnewTitle(conversationID);
				}
		};
		SwingUtilities.invokeLater(update);
	}
	
	public String getHistory(){
		return conversationIDgui.getTextFromField();
		
	}
	
	
	// for testing
	public static void main(String[] args) {
		
	}
	
}
