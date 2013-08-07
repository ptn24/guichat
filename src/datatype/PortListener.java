package datatype;

import gui.LoginFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

/**
 * This class listens to user input in the Port view of the Login window.
 * @author Peter
 *
 */
public class PortListener implements ActionListener, LoginListener{
	private final LoginFrame loginFrame;
	
	public PortListener(LoginFrame loginFrame){
		this.loginFrame = loginFrame;
	}
	
	public void actionPerformed(ActionEvent ae){
		String command = ae.getActionCommand();
		
		if(command.equals("Back")){
			this.loginFrame.createIPView();
		}
		
		else if(command.equals("Next") || command.equals("userEntryTextField")){
			JTextField userTextField = this.loginFrame.getUserEntryTextField();
			String userEntry = userTextField.getText();
			userTextField.setText("");
			
			this.check(userEntry);
		}
	}
	
	public void check(String userEntry){
		try{
			int port = Integer.parseInt(userEntry);
			
			//Requires 0 <= port <= 65535.
			if(!(port >= 0 && port <= 65535)){
				this.loginFrame.setErrorPanelLabel("Port error!");
			}
			
			else{
				this.loginFrame.setUserEnteredPort(port);
				this.loginFrame.createUsernameView();
			}
		}
		
		catch (NumberFormatException nfe){
			this.loginFrame.setErrorPanelLabel("Port error!");
		}
	}

}
