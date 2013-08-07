package datatype;

import java.awt.event.ActionListener;

/**
 * An interface for all the listeners in the login window.
 * @author Peter
 *
 */
public interface LoginListener extends ActionListener{
	
	/**
	 * Checks whether the user entry matches the criteria of the listener.
	 * @param userEntry The user input.
	 */
	public void check(String userEntry);
}
