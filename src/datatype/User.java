package datatype;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class represents a user on the chat system. Each user can be mapped to any number of
 * conversations (one-to-many).
 * @author Peter
 *
 */
public class User {
	private final String userID;
	private Socket socket;
	private PrintWriter out;
	private Set<Conversation> conversations;
	
	/**
	 * The constructor for the User from the Server.
	 * @param userID The String representation of the ID.
	 * @param socket The socket to which this user is connected.
	 * @throws IOException 
	 */
	public User(String userID, Socket socket) throws IOException{
		this.userID = userID;
		this.socket = socket;
		this.out = new PrintWriter(socket.getOutputStream(), true);
		this.conversations = Collections.synchronizedSet(new HashSet<Conversation>());
	}
	
	/**
	 * The constructor for the User from the Client.
	 * @param userID The String representation of the ID.
	 */
	public User(String userID){
		this.userID = userID;
	}
	
	/**
	 * 
	 * @return The userID.
	 */
	public String getUserID(){
		return this.userID;
	}
	
	/**
	 * 
	 * @return The socket to which this user is connected.
	 */
	public Socket getSocket(){
		return this.socket;
	}
	
	/**
	 * 
	 * @return The output stream.
	 */
	public PrintWriter getPrintWriter(){
		return this.out;
	}
	
	/**
	 * Add a conversation to this user's list.
	 * @param conversation
	 */
	public void addConversation(Conversation conversation){
		this.conversations.add(conversation);
	}
	
	/**
	 * Remove a conversation from this user's list.
	 * @param conversation
	 */
	public void removeConversation(Conversation conversation){
		this.conversations.remove(conversation);
	}
	
	/**
	 * @return The list of conversations in which this user is involved.
	 */
	public List<Conversation> getConversations(){
		List<Conversation> result = new ArrayList<Conversation>();
		
		Iterator<Conversation> iterator = this.conversations.iterator();
		while(iterator.hasNext()){
			Conversation conversation = iterator.next();
			result.add(conversation);
		}
		
		return result;
	}	
}
