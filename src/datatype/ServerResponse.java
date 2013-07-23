package datatype;

public class ServerResponse {
	public static enum Type{
		USER_LOG_ON,
		USER_LOG_OFF,
		ADD_CONVERSATION,
		USER_ENTER_CHAT,
		USER_EXIT_CHAT,
		SEND_MESSAGE,
		RECEIVED_INVITE,
		LOG_ON,
		LOG_ON_FAIL0,
		LOG_ON_FAIL1,
		LOG_OFF,
		LOG_OFF_FAIL,
		START_CHAT,
		START_CHAT_FAIL0,
		START_CHAT_FAIL1,
		ENTER_CHAT,
		ENTER_CHAT_FAIL0,
		ENTER_CHAT_FAIL1,
		ENTER_CHAT_FAIL2,
		EXIT_CHAT,
		EXIT_CHAT_FAIL0,
		EXIT_CHAT_FAIL1,
		EXIT_CHAT_FAIL2,
		SEND_MESSAGE_FAIL0,
		SEND_MESSAGE_FAIL1,
		SEND_MESSAGE_FAIL2,
		SEND_INVITE_FAIL0,
		SEND_INVITE_FAIL1,
		SEND_INVITE_FAIL2,
		SEND_INVITE_FAIL3,
		INVALID_INPUT
	}
}
