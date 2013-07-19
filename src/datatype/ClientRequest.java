package datatype;

import java.net.Socket;

/**
 * This datatype represents the client requests that are stored on the Server. Each client request is
 * associated with a type and a socket connection.
 * @author Peter
 *
 */
public class ClientRequest {
	/**
	 * All the types of ClientRequest's.
	 * @author Peter
	 *
	 */
	public static enum Type{
		LOG_ON,
		LOG_OFF,
		START_CHAT,
		EXIT_CHAT,
		ENTER_CHAT,
		SEND_MESSAGE,
		SEND_INVITE,
		INVALID
	}
	
	private final Type type;
	private final Socket socket;
	
	//May be null depending on the type of ClientRequest.
	private final String userID;
	private final String conversationID;
	private final String message;
	private final String invitee;
	
	/**
	 * The constructor for a ClientRequest.
	 * @param type The type of ClientRequest.
	 * @param socket The socket on which the client is connected.
	 * @param userID The submitted userID from the client (can be null).
	 * @param conversationID The submitted conversationID from the client (can be null).
	 * @param message The submitted message from the client (can be null).
	 * @param invitee The username of the client to invite to a conversation (can be null).
	 */
	public ClientRequest(Type type, Socket socket, String userID, String conversationID,
			String message, String invitee){
		this.type = type;
		this.socket = socket;
		this.userID = userID;
		this.conversationID = conversationID;
		this.message = message;
		this.invitee = invitee;
	}
	
	public Type getType(){
		return this.type;
	}
	
	public Socket getSocket(){
		return this.socket;
	}
	
	public String getUserID(){
		return this.userID;
	}
	
	public String getConversationID(){
		return this.conversationID;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public String getInvitee(){
		return this.invitee;
	}

}
