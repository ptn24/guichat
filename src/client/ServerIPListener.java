package client;

import gui.LoginFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

/**
 * This class listens to user input on the IP view of the Login window.
 * @author Peter
 *
 */
public class ServerIPListener implements ActionListener, LoginListener{
	private final LoginFrame loginFrame;
	
	public ServerIPListener(LoginFrame loginFrame){
		this.loginFrame = loginFrame;
	}
	
	public void actionPerformed(ActionEvent ae){
		JTextField userTextField = this.loginFrame.getUserEntryTextField();
		String userEntry = userTextField.getText();
		userTextField.setText("");
		
		this.check(userEntry);
	}
	
	//TODO: implement proper checks
	public void check(String userEntry){
		this.loginFrame.setUserEnteredIP(userEntry);
		this.loginFrame.createPortView();
	}
}
