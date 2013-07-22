package client;

import gui.LoginFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class UsernameListener implements ActionListener, LoginListener{
	private final LoginFrame loginFrame;
	
	public UsernameListener(LoginFrame loginFrame){
		this.loginFrame = loginFrame;
	}
	
	public void actionPerformed(ActionEvent ae){
		String command = ae.getActionCommand();
		
		if(command.equals("Back")){
			this.loginFrame.createPortView();
		}
		
		else if(command.equals("Next") || command.equals("userEntryTextField")){
			JTextField userTextField = this.loginFrame.getUserEntryTextField();
			String userEntry = userTextField.getText();
			userTextField.setText("");
			
			this.check(userEntry);
		}
	}
	
	public void check(String userEntry){
		this.loginFrame.setUserEnteredUsername(userEntry);
		this.loginFrame.launchGUIChat();
		//TODO: implement proper checks.
	}

}
