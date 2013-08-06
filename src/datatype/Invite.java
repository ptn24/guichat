package datatype;

public class Invite {
	private final String conversationID;
	private String userID;			//TODO: WILL IMPLEMENT ADDITIONAL FEATURES FOR INVITES
	
	public Invite(String conversationID, String userID){
		this.conversationID = conversationID;
		this.userID = userID;
	}
	
	public String getConversationID(){
		return this.conversationID;
	}
	
	public String getUserID(){
		return this.userID;
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Invite)){
			return false;
		}
		
		Invite obj = (Invite) o;
		return (obj == null) ? false : obj.getConversationID().equals(this.conversationID);
	}
	
	@Override
	public int hashCode(){
		int largePrime = 3571;
		return largePrime;
	}
}
