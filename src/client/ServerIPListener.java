package client;

import gui.LoginFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

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
	
	public void check(String userEntry){
		try{
			int ip = Integer.valueOf(userEntry);
			this.loginFrame.setUserEnteredIP(ip);
			this.loginFrame.createPortView();
		}
		
		catch (NumberFormatException nfe){
			if(userEntry.equals("localhost")){
				this.loginFrame.setUserEnteredIP(-1);
				this.loginFrame.createPortView();
				
			}
			
			else{
				this.loginFrame.setErrorPanelLabel("ERROR!");
			}
			
		}
	}
}
