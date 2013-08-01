package datatype;

public interface MyUserTable {
	/**
	 * Add a user to the table model.
	 * @param userID
	 */
	public void addUser(String userID);
	
	/**
	 * Remove a user from the table model.
	 * @param userID
	 */
	public void removeUser(String userID);
}
