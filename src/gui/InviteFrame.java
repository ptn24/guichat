package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import client.Client;

public class InviteFrame extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Client client;
	private final String conversationID;
	private final GroupLayout layout;
	private final JPanel topPanel;
	private final JLabel topLabel;
	private final JTextField userEntryTextField;
	private final ErrorPanel errorPanel;
	private final JButton cancelButton, inviteButton;
	
	public InviteFrame(Client client, String conversationID){
		this.client = client;
		this.conversationID = conversationID;
		
		//Instantiate the top label.
		this.topLabel = new JLabel("Please enter the name of the user you wish to invite:");
		
		//Instantiate the top panel.
		this.topPanel = new JPanel();
		this.topPanel.add(this.topLabel);
		
		//Instantiate the text field.
		this.userEntryTextField = new JTextField(10);
		this.userEntryTextField.setActionCommand("userEntryTextField");
		this.userEntryTextField.addActionListener(this);
		this.userEntryTextField.requestFocus();
		
		//Instantiate the error panel.
		this.errorPanel = new ErrorPanel();
		
		//Instantiate the buttons.
		this.cancelButton = new JButton("Cancel");
		this.cancelButton.addActionListener(this);
		this.inviteButton = new JButton("Invite");
		this.inviteButton.addActionListener(this);
		
		//An invisible gap in the frame.
		Component verticalGap = Box.createRigidArea(new Dimension(25, 0));
		
		//Initialize the layout.
		Container cp = this.getContentPane();
		this.layout = new GroupLayout(cp);
		cp.setLayout(this.layout);
		
		//Setup the horizontal group.
		this.layout.setHorizontalGroup(this.layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(this.topPanel)
				.addGroup(this.layout.createSequentialGroup()
						.addComponent(this.cancelButton)
						.addComponent(verticalGap)
						.addComponent(this.userEntryTextField)
						.addComponent(verticalGap)
						.addComponent(this.inviteButton)
				)
				.addComponent(this.errorPanel)
		);
		
		//Setup the vertical group.
		this.layout.setVerticalGroup(this.layout.createSequentialGroup()
				.addComponent(this.topPanel)
				.addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(verticalGap)
						.addComponent(this.userEntryTextField)
						.addComponent(verticalGap)
				)
				.addComponent(this.errorPanel)
				.addGroup(this.layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(this.cancelButton)
						.addComponent(this.inviteButton)
				)
		);
		
		//Set the gaps to auto.
    	this.layout.setAutoCreateGaps(true);
    	this.layout.setAutoCreateContainerGaps(true);
    	
    	//Setup the window.
    	setTitle("GUI CHAT - Invite Friend");
    	pack();
    	setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    	
    	this.addWindowListener(new WindowAdapter(){
    		public void windowClosing(WindowEvent e){
    			userEntryTextField.setText("");
    			errorPanel.clearErrorLabel();
    			dispose();
    		}
    	});
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		
		if(ae.getActionCommand().equals(this.inviteButton.getActionCommand()) ||
				ae.getActionCommand().equals("userEntryTextField")){
			String userID = this.userEntryTextField.getText();
			this.userEntryTextField.setText("");
			this.check(userID);
		}
				
		else if(ae.getActionCommand().equals(this.cancelButton.getActionCommand())){
			this.errorPanel.clearErrorLabel();
			this.userEntryTextField.setText("");
			this.dispose();
		}
		
	}
	
	private void check(String entry){
		if(!this.client.isUserLoggedOn(entry)){
			this.errorPanel.setErrorLabel("This user does not exist.");
			this.userEntryTextField.setText(entry);
			this.userEntryTextField.selectAll();
			this.userEntryTextField.requestFocus();
		}
		
		else{
			if(this.client.isUserInChat(this.conversationID, entry)){
				this.errorPanel.setErrorLabel("This user is already in the conversation.");
				this.userEntryTextField.setText(entry);
				this.userEntryTextField.selectAll();
				this.userEntryTextField.requestFocus();
			}
			
			else{
				this.errorPanel.clearErrorLabel();
				this.dispose();
				
				//TODO: send to server.
				//fix the conversation id for each new frame.
				this.client.requestSendInvite(this.conversationID, entry);
			}
			
		}
	}
}
