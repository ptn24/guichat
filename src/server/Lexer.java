package server;

import java.io.IOException;
import java.net.Socket;
import datatype.*;

/**
 * This lexer takes a client request from the input stream on the server and creates the appropriate 
 * ClientRequest datatype.
 * @author Peter
 *
 */
public class Lexer {
	private final String line;
	private final Socket socket;
	
	//Client-to-Server grammar.
	private final String regex = "(LOG_ON\\sUSER_ID\\s\\p{Alnum}+)|" +
			"(LOG_OFF)|" +
			"(START_CHAT\\sCONVERSATION_ID\\s\\p{Alnum}+(\\sUSER_ID\\s\\p{Alnum}+)*)|" +
			"(EXIT_CHAT\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
			"(ENTER_CHAT\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
			"(SEND_MESSAGE\\sCONVERSATION_ID\\s\\p{Alnum}+\\s_TEXT_\\s.*)|" +
			"(SEND_INVITE\\sCONVERSATION_ID\\s\\p{Alnum}+\\sUSER_ID\\s\\p{Alnum}+)";
	
	/**
	 * Create the lexer for the client's request.
	 * @param line The String taken from the input stream on the server.
	 * @param socket The client's socket.
	 */
	public Lexer(String line, Socket socket){
		this.line = line;
		this.socket = socket;
	}
	
	/**
	 * Lex the information from the client's request and create the proper ClientRequest.
	 * @return The appropriate ClientRequest for the client's request.
	 * @throws IOException 
	 */
	public ClientRequest lex() throws IOException{
		if(!this.line.matches(regex)){
			return new ClientRequest(ClientRequest.Type.INVALID, socket, null, null, null, null);
		}
		
		else{
			String[] tokens = this.line.split(" ", 2);
			//The client's command (eg. LOG_ON, START_CHAT, etc.).
			String command = tokens[0];
			
			if(command.equals("LOG_ON")){
				String userID = tokens[1].split(" ")[1];
				return new ClientRequest(ClientRequest.Type.LOG_ON, socket, userID, null, null, null);
			}
			
			else if(command.equals("LOG_OFF")){
				return new ClientRequest(ClientRequest.Type.LOG_OFF, socket, null, null, null, null);
			}
			
			else if(command.equals("START_CHAT")){
				String conversationID = tokens[1].split(" ")[1];
				return new ClientRequest(ClientRequest.Type.START_CHAT, socket, null, conversationID, 
						null, null);
			}
			
			else if(command.equals("EXIT_CHAT")){
				String conversationID = tokens[1].split(" ")[1];
				return new ClientRequest(ClientRequest.Type.EXIT_CHAT, socket, null, conversationID, 
						null, null);
			}
			
			else if(command.equals("ENTER_CHAT")){
				String conversationID = tokens[1].split(" ")[1];
				return new ClientRequest(ClientRequest.Type.ENTER_CHAT, socket, null, conversationID, 
						null, null);
			}
			
			else if(command.equals("SEND_MESSAGE")){
				String remainderTokens = tokens[1];
				String conversationID = remainderTokens.split(" ")[1];
				String message = remainderTokens.split("_TEXT_ ", 2)[1];
				return new ClientRequest(ClientRequest.Type.SEND_MESSAGE, socket, null, 
						conversationID, message, null);
			}
			
			else{
				String remainderTokens = tokens[1];
				String conversationID = remainderTokens.split(" ")[1];
				String invitees = remainderTokens.split("USER_ID ", 2)[1];
				return new ClientRequest(ClientRequest.Type.SEND_INVITE, socket, null, 
						conversationID, null, invitees);
			}
		}
	}
}
