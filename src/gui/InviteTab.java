package gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import client.Client;

import datatype.Invite;
import datatype.InviteTableEditor;
import datatype.InviteTableModel;
import datatype.InviteTableRenderer;

public class InviteTab extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Client client;
	private final JTable inviteTable;
	private final JScrollPane scrollPane;
	
	public InviteTab(Client client){
		this.client = client;
		
		//Setup the invitation table with a custom renderer and editor.
		this.inviteTable = new JTable(new InviteTableModel());
		this.inviteTable.setTableHeader(null);
		this.inviteTable.setRowHeight(60);
		this.inviteTable.setDefaultRenderer(Invite.class, 
				new InviteTableRenderer(this.client, this.inviteTable));
		this.inviteTable.setDefaultEditor(Invite.class, 
				new InviteTableEditor(this.client, this.inviteTable));
		
		//Setup the scroll pane.
		this.scrollPane = new JScrollPane(this.inviteTable);
		this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		this.add(scrollPane);
	}
	
	/**
	 * Add an invitation to the table.
	 * @param invite
	 */
	public void addInvite(Invite invite){
		InviteTableModel tm = (InviteTableModel) this.inviteTable.getModel();
		tm.addInvite(invite);
	}
	
}

