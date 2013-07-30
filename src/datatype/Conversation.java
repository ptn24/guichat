package datatype;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * The conversation class represents a conversation between any number of users. A conversation can
 * be mapped to any number of users (one-to-many).
 * @author Peter
 *
 */
public class Conversation {
	private final String conversationID;	
	private Set<User> users;
	private ArrayList<Dialogue> history;

	/**
	 * Constructor for the conversation from the server. Add the creator to the conversation and 
	 * update the history of the conversation.
	 * @param conversationID The ID of the conversation.
	 * @param user The user who created the conversation.
	 * @param timeStamp The time this conversation was created.
	 */
	public Conversation(String conversationID){
		this.conversationID = conversationID;
		this.users = Collections.synchronizedSet(new HashSet<User>());
		this.history = new ArrayList<Dialogue>();
		
	}
	
	/**
	 * 
	 * @return The conversation ID.
	 */
	public String getConversationID(){
		return this.conversationID;
	}
	
	/**
	 * 
	 * @return The list of users in the conversation.
	 */
	public List<User> getListUsers(){
		List<User> output = new ArrayList<User>();
		Iterator<User> iterator = this.users.iterator();
		
		while(iterator.hasNext()){
			User user = iterator.next();
			output.add(user);
		}
		
		return output;
	}
	
	/**
	 * Add a message from the client to the history of this conversation with the appropriate
	 * timestamp. 
	 * @param userID The client's userID.
	 * @param message The String submitted by the client.
	 * @param timeStamp The time at which the message was sent.
	 */
	public void addMessage(String userID, String message, String timeStamp){
		Dialogue dialogue = new Dialogue(userID, message, timeStamp);
		this.history.add(dialogue);
	}
	
	/**
	 * Add a user to the conversation.
	 * @param user The user to be added.
	 * Requires that this user is not currently in the conversation.
	 */
	public void addUser(User user){
		this.users.add(user);
	}
	
	
	/**
	 * Remove the user from the conversation.
	 * @param user The user to be removed. Requires that this user is in the conversation.
	 */
	public void removeUser(User user){
		this.users.remove(user);
	}
	
	/**
	 * Send the complete history to the user who entered the conversation.
	 * @param out The output stream to the client who recently entered the conversation.
	 */
	public void sendHistoryToUser(PrintWriter out){
		for(Dialogue dialogue : this.history){
			String message = "SEND_MESSAGE CONVERSATION_ID " + this.conversationID + " " + 
								dialogue.toString();
			out.println(message);
		}	
	}
	
	/**
	 * 
	 * @return the last message that was added to the conversation.
	 */
	public Dialogue getLastMessage(){
		return this.history.get(this.history.size() - 1);
	}
	
	/**
	 * 
	 * @param user The user object.
	 * @return Whether or not the user is in the conversation.
	 */
	public boolean contains(User user){
		return this.users.contains(user);
	}
	
	/**
	 * 
	 * @param userID The unique identifier for the user.
	 * @return Whether or not the userID is in the conversation.
	 */
	public boolean contains(String userID){
		Iterator<User> iterator = this.users.iterator();
		
		while(iterator.hasNext()){
			User user = iterator.next();
			if(user.getUserID().equals(userID)){
				return true;
			}
		}
		
		return false;
	}	
}
