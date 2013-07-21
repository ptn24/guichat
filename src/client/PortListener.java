package client;

import gui.LoginFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		
		else if(command.equals("Next")){
			
		}
		
		else{
			
		}
	}
	
	public void check(String userEntry){
		
	}

}
