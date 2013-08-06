package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import client.Client;
import datatype.Invite;
import datatype.InviteTableModel;

public class InvitePanel extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Client client;
	private final JTable inviteTable;
	
	private final JPanel conversationPanel;
	private final JLabel conversationLabel;
	private final JButton acceptButton, declineButton;
	
	private Invite invite;
	
	public InvitePanel(Client client, JTable table){
		this.client = client;
		this.inviteTable = table;
		
		//Left-align the panel.
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//Instantiate an empty label in a panel.
		this.conversationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.conversationLabel = new JLabel();
		this.conversationPanel.add(this.conversationLabel);
		this.conversationPanel.setSize(new Dimension(250, 0));		
		
		//An invisible gap in the frame.
		Component verticalGap = Box.createRigidArea(new Dimension(200, 0));
		
		this.acceptButton = new JButton("Accept");
		this.acceptButton.addActionListener(this);
		this.declineButton = new JButton("Decline");
		this.declineButton.addActionListener(this);
		
		this.add(this.conversationPanel);
		this.add(verticalGap);
		this.add(this.acceptButton);
		this.add(this.declineButton);
	}
	
	/**
	 * Set the text for the conversation label and update the invite.
	 * @param invite
	 */
	public void updateData(final Invite invite){
		this.conversationLabel.setText(invite.getConversationID());
		this.invite = invite;
	}
	
	/**
	 * Remove the invitation from the table's model.
	 * @param invite
	 */
	private void removeInviteFromTable(Invite invite){
		InviteTableModel tm = (InviteTableModel) this.inviteTable.getModel();
		tm.removeInvite(invite);
		
		this.client.removeInvite(invite);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		
		if(ae.getActionCommand().equals("Accept")){
			//Request the server to enter the conversation.
			String conversationID = this.invite.getConversationID();
			this.client.requestEnterChat(conversationID);
			
			this.removeInviteFromTable(this.invite);
		}
		
		else if(ae.getActionCommand().equals("Decline")){
			this.removeInviteFromTable(this.invite);
		}
	}
}
