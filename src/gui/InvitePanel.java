package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InvitePanel extends JPanel implements ActionListener{
	private final JLabel conversationLabel;
	private final JButton acceptButton, declineButton;
	
	public InvitePanel(String conversationID){
		this.conversationLabel = new JLabel(conversationID);
		
		//An invisible gap in the frame.
		Component verticalGap = Box.createRigidArea(new Dimension(200, 0));
		
		this.acceptButton = new JButton("Accept");
		this.declineButton = new JButton("Decline");
		
		this.add(this.conversationLabel);
		this.add(verticalGap);
		this.add(this.acceptButton);
		this.add(this.declineButton);
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
