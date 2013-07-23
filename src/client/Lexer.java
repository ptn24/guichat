package client;

public class Lexer {
	private final String line;
	
	//Server-to-Client grammar.
	private final String regex = "(USER_LOG_ON\\sUSER_ID\\s\\p{Alnum}+)|" +
			"(USER_LOG_OFF\\sUSER_ID\\s\\p{Alnum}+)|" +
			"(ADD_CONVERSATION\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
			"(USER_ENTER_CHAT\\sCONVERSATION_ID\\s\\p{Alnum}+\\sUSER_ID\\s\\p{Alnum}+)|" +
			"(USER_EXIT_CHAT\\sCONVERSATION_ID\\s\\p{Alnum}+\\sUSER_ID\\s\\p{Alnum}+)|" +
			"(SEND_MESSAGE\\sCONVERSATION_ID\\s\\p{Alnum}+\\sUSER_ID\\s\\p{Alnum}+\\s_TIME_[0-9]{2}:[0-9]{2}:[0-9]{2}\\s_TEXT_\\s.*)|" +
			"(RECEIVED_INVITE\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
			"(LOG_ON\\sUSER_ID\\s\\p{Alnum}+)|" +
			"(LOG_ON_FAIL0)|" +
			"(LOG_ON_FAIL1\\sUSER_ID\\s\\p{Alnum}+)|" +
			"(LOG_OFF)|" +
			"(LOG_OFF_FAIL)|" +
			"(START_CHAT\\sCONVERSATION_ID\\s\\p{Alnum}+)" +
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
			"(SEND_MESSAGE_FAIL2\\sCONVERSATOIN_ID\\s\\p{Alnum}+)|" +
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
	
	//TODO: implement the lex method.

}
