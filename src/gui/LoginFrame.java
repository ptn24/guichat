package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import client.Client;
import client.LoginListener;
import client.PortListener;
import client.ServerIPListener;
import client.UsernameListener;

public class LoginFrame extends JFrame{
	private final Client client;
	private final GroupLayout layout;
	
	private final JLabel topLabel;
	
	private final JTextField userEntryTextField;
	private final ErrorPanel errorPanel;
	private final JButton backButton;
	private final JButton nextButton;
	
	private LoginListener currentListener;
	
	private String userEnteredIP = "localhost";
	private int userEnteredPort = 4444;
	private String userEnteredUsername = "ptn24";
	
	public LoginFrame(Client client){
		this.client = client;
		
		/*
		 * Set up the initial IP view for the Login window.
		 */
		this.topLabel = new JLabel("Server IP:");
		this.topLabel.setName("topLabel");
		
		this.userEntryTextField = new JTextField(10);
		this.userEntryTextField.setName("userEntryTextField");
		this.userEntryTextField.setActionCommand("userEntryTextField");
		
		//If the IP is already set, place the value in the text field.
		if(this.userEnteredIP != null){
			this.userEntryTextField.setText(this.userEnteredIP);
			this.userEntryTextField.selectAll();
		}
	
		this.errorPanel = new ErrorPanel();
		this.errorPanel.setName("errorPanel");
		
		//The 'Back' button is originally inactive.
		this.backButton = new JButton("Back");
		this.backButton.setName("backButton");
		this.backButton.setEnabled(false);
		
		this.nextButton = new JButton("Next");
		this.nextButton.setName("nextButton");
		
		//An invisible gap in the frame.
		Component verticalGap = Box.createRigidArea(new Dimension(25, 0));
		
		//Register the first listener.
		this.currentListener = new ServerIPListener(this);
		this.userEntryTextField.addActionListener(this.currentListener);
		this.nextButton.addActionListener(this.currentListener);
				
		//Initialize the layout.
		Container cp = this.getContentPane();
		this.layout = new GroupLayout(cp);
		cp.setLayout(this.layout);
		
		//Set the horizontal group.		
		this.layout.setHorizontalGroup(this.layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(this.topLabel)
				.addComponent(errorPanel)
				.addGroup(this.layout.createSequentialGroup()
						.addComponent(this.backButton)
						.addComponent(verticalGap)
						.addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(this.userEntryTextField)
						)
						.addComponent(verticalGap)
						.addComponent(this.nextButton)
				)
		);
		
		//Set the vertical group.
		this.layout.setVerticalGroup(this.layout.createSequentialGroup()
				.addComponent(this.topLabel)
				.addGroup(this.layout.createParallelGroup()
						.addComponent(verticalGap)
						.addGroup(this.layout.createSequentialGroup()
								.addComponent(this.userEntryTextField)
								//.addComponent(errorPanel)
						)
						.addComponent(verticalGap)
				)
				.addComponent(errorPanel)
				.addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(this.backButton)
						.addComponent(this.nextButton)
				)
				
				
		);
		
		//Set the gaps to auto.
    	this.layout.setAutoCreateGaps(true);
    	this.layout.setAutoCreateContainerGaps(true);
    	
    	//Setup the window.
    	setTitle("GUI Chat - Login");
    	pack();
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * 
	 * @return The text field that contains the user's entries.
	 */
	public JTextField getUserEntryTextField(){
		return this.userEntryTextField;
	}
	
	/**
	 * Set the field value to the value of the user's entry from the text field.
	 * Set the value to -1 if the user's entry was 'localhost'.
	 * @param ip The IP value.
	 */
	public void setUserEnteredIP(String ip){
		this.userEnteredIP = ip;
	}
	
	/**
	 * Set the field value to the value of the user's entry from the text field.
	 * @param port The port number. Requires 0 <= port <= 65535.
	 */
	public void setUserEnteredPort(int port){
		this.userEnteredPort = port;
	}
	
	/**
	 * Set the field value to the value of the user's entry from the text field.
	 * @param username The client's proposed username. Requires that 'username' contains only
	 * 				alphanumeric characters.
	 */
	public void setUserEnteredUsername(String username){
		this.userEnteredUsername = username;
	}
	
	/**
	 * 
	 * @return The value of the Username field.
	 */
	public String getUserEnteredUsername(){
		return this.userEnteredUsername;
	}
	
	/**
	 * 
	 * @return The value of the IP field (used for testing purposes).
	 */
	public String getUserEnteredIP(){
		return this.userEnteredIP;
	}
	
	/**
	 * Set the error panel label.
	 * @param message The new text for the error panel label.
	 */
	public void setErrorPanelLabel(String message){
		this.errorPanel.setErrorLabel(message);
	}
	
	/**
	 * Create the IP view when the user presses 'Back' from the port view.
	 * Requires that the transition is from Port view -> IP view.
	 */
	public void createIPView(){
		//Setup the components.
		this.topLabel.setText("Server IP:");
		this.backButton.setEnabled(false);
		this.errorPanel.clearErrorLabel();
		
		//If the IP is already set, place the value in the text field.
		if(this.userEnteredIP != null){
			this.userEntryTextField.setText(this.userEnteredIP);
			this.userEntryTextField.selectAll();
		}
		
		this.nextButton.removeActionListener(this.currentListener);
		this.userEntryTextField.removeActionListener(this.currentListener);
		this.backButton.removeActionListener(this.currentListener);
		
		this.currentListener = new ServerIPListener(this);
		this.nextButton.addActionListener(this.currentListener);
		this.userEntryTextField.addActionListener(this.currentListener);
	}
	
	/**
	 * Create the port view.
	 */
	public void createPortView(){
		//Setup the components.
		this.topLabel.setText("Port Number:");
		this.backButton.setEnabled(true);
		this.errorPanel.clearErrorLabel();
		
		//If the port number is already set, place this value in the text box.
		if(this.userEnteredPort != -1){
			this.userEntryTextField.setText(String.valueOf(this.userEnteredPort));
			this.userEntryTextField.selectAll();
		}
		
		this.nextButton.removeActionListener(this.currentListener);
		this.backButton.removeActionListener(this.currentListener);
		this.userEntryTextField.removeActionListener(this.currentListener);
		
		this.currentListener = new PortListener(this);
		this.nextButton.addActionListener(this.currentListener);
		this.userEntryTextField.addActionListener(this.currentListener);
		this.backButton.addActionListener(this.currentListener);
	}
	
	public void createUsernameView(){
		//Setup the components.
		this.topLabel.setText("Please enter your username:");
		this.errorPanel.clearErrorLabel();
		
		//If the username is already set, place this value in the text box.
		if(this.userEnteredUsername != null){
			this.userEntryTextField.setText(this.userEnteredUsername);
			this.userEntryTextField.selectAll();
		}
		
		this.nextButton.removeActionListener(this.currentListener);
		this.backButton.removeActionListener(this.currentListener);
		this.userEntryTextField.removeActionListener(this.currentListener);
		
		this.currentListener = new UsernameListener(this);
		this.nextButton.addActionListener(this.currentListener);
		this.backButton.addActionListener(this.currentListener);
		this.userEntryTextField.addActionListener(this.currentListener);
	}
	
	public void login(){
		int serverResponse = this.client.login(this.userEnteredIP, 
				this.userEnteredPort, 
				this.userEnteredUsername);
		
		
		if(serverResponse == -1){
			this.userEntryTextField.setText(this.userEnteredUsername);
			this.userEntryTextField.selectAll();
			this.errorPanel.setErrorLabel("Invalid IP and/or port number.");
		}
	}
	
	public static void main(String[] args){
		Client client = new Client();
		final LoginFrame loginFrame = new LoginFrame(client);
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				loginFrame.setVisible(true);
			}
		});
	}
}
