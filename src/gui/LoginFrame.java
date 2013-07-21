package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import client.LoginListener;
import client.PortListener;
import client.ServerIPListener;

public class LoginFrame extends JFrame{
	private final GroupLayout layout;
	
	private final JLabel topLabel;
	
	private final JTextField userEntryTextField;
	private final ErrorPanel errorPanel;
	private final JButton backButton;
	private final JButton nextButton;
	
	private LoginListener currentListener;
	
	private int userEnteredIP;
	private int userEnteredPort;
	private String userEnteredUsername;
	
	public LoginFrame(){
		this.topLabel = new JLabel("Server IP:");
		this.topLabel.setName("topLabel");
		
		this.userEntryTextField = new JTextField(10);
		this.userEntryTextField.setName("userEntryTextField");
	
		this.errorPanel = new ErrorPanel();
		this.errorPanel.setName("errorPanel");
		
		//The 'Back' button is originally inactive.
		this.backButton = new JButton("Back");
		this.backButton.setName("backButton");
		this.backButton.setEnabled(false);
		
		this.nextButton = new JButton("Next");
		this.nextButton.setName("nextButton");
		
		//An invisible gap in the frame.
		Component verticalGap = Box.createRigidArea(new Dimension(10, 0));
		
		//Register the first listener.
		this.currentListener = new ServerIPListener(this);
		this.userEntryTextField.addActionListener(this.currentListener);
		this.nextButton.addActionListener(this.currentListener);
				
		//Initialize the layout.
		Container cp = this.getContentPane();
		this.layout = new GroupLayout(cp);
		cp.setLayout(this.layout);
		
		//Set the horizontal group.
		this.layout.setHorizontalGroup(this.layout.createSequentialGroup()
				.addComponent(this.backButton)
				.addComponent(verticalGap)
				.addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(this.topLabel)
						.addComponent(this.userEntryTextField)
						.addComponent(errorPanel)
				)
				.addComponent(verticalGap)
				.addComponent(this.nextButton)
		);
		
		//Set the vertical group.
		this.layout.setVerticalGroup(this.layout.createSequentialGroup()
				.addGroup(this.layout.createParallelGroup()
						.addComponent(verticalGap)
						.addGroup(this.layout.createSequentialGroup()
								.addComponent(this.topLabel)
								.addComponent(this.userEntryTextField)
								.addComponent(errorPanel)
						)
						.addComponent(verticalGap)
				)
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
	public void setUserEnteredIP(int ip){
		this.userEnteredIP = ip;
	}
	
	/**
	 * 
	 * @return The value of the IP field (used for testing purposes).
	 */
	public int getUserEnteredIP(){
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
	 */
	public void createIPView(){
		this.topLabel.setText("Server IP:");
		this.userEntryTextField.setText(String.valueOf(this.userEnteredIP));
		this.backButton.setEnabled(false);
		this.errorPanel.clearErrorLabel();
		
		this.nextButton.removeActionListener(this.currentListener);
		this.userEntryTextField.removeActionListener(this.currentListener);
		this.backButton.removeActionListener(this.currentListener);
		
		this.currentListener = new ServerIPListener(this);
		this.nextButton.addActionListener(this.currentListener);
		this.userEntryTextField.addActionListener(this.currentListener);
	}
	
	/**
	 * Create the port view once the user enters a valid IP address.
	 */
	public void createPortView(){
		this.topLabel.setText("Port Number:");
		this.backButton.setEnabled(true);
		this.errorPanel.clearErrorLabel();
		
		this.nextButton.removeActionListener(this.currentListener);
		this.userEntryTextField.removeActionListener(this.currentListener);
		
		this.currentListener = new PortListener(this);
		this.nextButton.addActionListener(this.currentListener);
		this.userEntryTextField.addActionListener(this.currentListener);
		this.backButton.addActionListener(this.currentListener);
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				LoginFrame loginFrame = new LoginFrame();
				
				loginFrame.setVisible(true);
			}
		});
	}
}
