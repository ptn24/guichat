package client;

import datatype.ServerResponse;

/**
 * This lexer takes a server's response from a client's input stream and creates the appropriate
 * ServerResponse datatype.
 * @author Peter
 *
 */
public class Lexer {
	private final String line;
	
	//Server-to-Client grammar.
	private final String regex = "(USER_LOG_ON\\sUSER_ID\\s\\p{Alnum}+)|" +
			"(USER_LOG_OFF\\sUSER_ID\\s\\p{Alnum}+)|" +
			"(ADD_CONVERSATION\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
			"(USER_ENTER_CHAT\\sCONVERSATION_ID\\s\\p{Alnum}+\\sUSER_ID\\s\\p{Alnum}+)|" +
			"(USER_EXIT_CHAT\\sCONVERSATION_ID\\s\\p{Alnum}+\\sUSER_ID\\s\\p{Alnum}+)|" +
			"(SEND_MESSAGE\\sCONVERSATION_ID\\s\\p{Alnum}+\\sUSER_ID\\s\\p{Alnum}+\\s_TIME_\\s\\p{Digit}{2}+:\\p{Digit}{2}+:\\p{Digit}{2}+\\s_TEXT_\\s.*)|" +
			"(RECEIVED_INVITE\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
			"(LOG_ON\\sUSER_ID\\s\\p{Alnum}+)|" +
			"(LOG_ON_FAIL0)|" +
			"(LOG_ON_FAIL1\\sUSER_ID\\s\\p{Alnum}+)|" +
			"(LOG_OFF)|" +
			"(LOG_OFF_FAIL)|" +
			"(START_CHAT\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
			"(START_CHAT_FAIL0)|" +
			"(START_CHAT_FAIL1\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
			"(ENTER_CHAT\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
			"(ENTER_CHAT_FAIL0)|" +
			"(ENTER_CHAT_FAIL1\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
			"(ENTER_CHAT_FAIL2)|" +
			"(EXIT_CHAT\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
			"(EXIT_CHAT_FAIL0)|" +
			"(EXIT_CHAT_FAIL1\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
			"(EXIT_CHAT_FAIL2)|" +
			"(SEND_MESSAGE_FAIL0)|" +
			"(SEND_MESSAGE_FAIL1\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
			"(SEND_MESSAGE_FAIL2\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
			"(SEND_INVITE_FAIL0)|" +
			"(SEND_INVITE_FAIL1\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
			"(SEND_INVITE_FAIL2\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
			"(SEND_INVITE_FAIL3\\sUSER_ID\\s\\p{Alnum}+)|" +
			"(INVALID_INPUT)";
	
	/**
	 * Create the lexer for the server's response.
	 * @param line The String taken from the input stream on the client.
	 */
	public Lexer(String line){
		this.line = line;
	}
	
	/**
	 * Lex the information from the server's response.
	 * @return The appropriate ServerResponse.
	 */
	public ServerResponse lex(){
		if(!this.line.matches(this.regex)){			
			return new ServerResponse(ServerResponse.Type.INVALID_INPUT, null, null, null, null);
		}
		
		else{
			String[] tokens = this.line.split(" ", 2);
			String command = tokens[0];
			
			if(command.equals("USER_LOG_ON") || command.equals("USER_LOG_OFF") ||
					command.equals("SEND_INVITE_FAIL3")){
				String userID = tokens[1].split("USER_ID ")[1];
				return new ServerResponse(ServerResponse.Type.valueOf(command), userID, null, null, null);
			}
			
			else if(command.equals("ADD_CONVERSATION") || command.equals("RECEIVED_INVITE") ||
					command.equals("START_CHAT") || command.equals("START_CHAT_FAIL1") ||
					command.equals("ENTER_CHAT") || command.equals("ENTER_CHAT_FAIL1") ||
					command.equals("EXIT_CHAT") || command.equals("EXIT_CHAT_FAIL1") ||
					command.equals("SEND_MESSAGE_FAIL1") || command.equals("SEND_MESSAGE_FAIL2") ||
					command.equals("SEND_INVITE_FAIL1") || command.equals("SEND_INVITE_FAIL2")){
				String conversationID = tokens[1].split("CONVERSATION_ID ")[1];
				return new ServerResponse(ServerResponse.Type.valueOf(command), null, conversationID, null, null);
			}
			
			else if(command.equals("USER_ENTER_CHAT") || command.equals("USER_EXIT_CHAT")){
				String[] remainderTokens = tokens[1].split(" ");
				String conversationID = remainderTokens[1];
				String userID = remainderTokens[3];
				return new ServerResponse(ServerResponse.Type.valueOf(command), userID, conversationID, null, null);
			}
			
			else if(command.equals("SEND_MESSAGE")){
				String[] remainderTokens = tokens[1].split(" _TEXT_ ");
				String message = remainderTokens[1];
				
				String[] parsedRemainderTokens = remainderTokens[0].split(" ");
				String conversationID = parsedRemainderTokens[1];
				String userID = parsedRemainderTokens[3];
				String time = parsedRemainderTokens[5];
				
				return new ServerResponse(ServerResponse.Type.valueOf(command), userID, conversationID, time, message);
			}
			
			else if(command.equals("LOG_ON") || command.equals("LOG_ON_FAIL1")){
				String userID = tokens[1].split("USER_ID ")[1];
				return new ServerResponse(ServerResponse.Type.valueOf(command), userID, null, null, null);
			}
			
			else if(command.equals("LOG_ON_FAIL0") || command.equals("LOG_OFF") ||
					command.equals("LOG_OFF_FAIL") || command.equals("START_CHAT_FAIL0") ||
					command.equals("ENTER_CHAT_FAIL0") || command.equals("ENTER_CHAT_FAIL2") ||
					command.equals("EXIT_CHAT_FAIL0") || command.equals("EXIT_CHAT_FAIL2") ||
					command.equals("SEND_MESSAGE_FAIL0") || command.equals("SEND_INVITE_FAIL0")){
				return new ServerResponse(ServerResponse.Type.valueOf(command), null, null, null, null);
			}
			
			else if(command.equals("INVALID_INPUT")){
				return new ServerResponse(ServerResponse.Type.valueOf(command), null, null, null, null);
			}
			
			else{				
				throw new IllegalArgumentException("The program should not reach this point.");
			}
		}
	}

}
