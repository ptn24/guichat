package datatype;

/**
 * This class represents the datatype for a message within a conversation. Each Dialogue
 * contains information about who sent the message, the message text, and the time the message
 * was sent.
 * @author Peter
 *
 */
public class Dialogue {
	private final String userID;
	private final String text;
	private final String timeStamp;

	/**
	 * Create a new Dialogue
	 * @param userID The userID that sent the message.
	 * @param text The actual message. 
	 * @param timeStamp The time when the message was submitted.
	 */
	public Dialogue(String userID, String text, String timeStamp){
		this.timeStamp = timeStamp;
		this.userID = userID;
		this.text = text; 
	}
	
	/**
	 * Return the string representation of the dialogue.
	 */
	public String toString(){		
		String result = ("USER_ID " + this.userID + " _TIME_ " + this.timeStamp + " _TEXT_ " + 
				this.text);
		
		return result;
	}
	
}
