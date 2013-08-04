package gui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class InviteTab extends JPanel{
	private final JTable inviteTable;
	private final JScrollPane scrollPane;
	
	public InviteTab(){
		this.inviteTable = new JTable(new InviteTableModel());
		this.inviteTable.setTableHeader(null);
		
		this.scrollPane = new JScrollPane(this.inviteTable);
		this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		this.add(scrollPane);
		
		InviteTableModel tm = (InviteTableModel) this.inviteTable.getModel();
		tm.addInvite("dummy");
		//TODO:where do we need to store the mapping of conversation name -> invite panel?
		//TODO: gui is not showing the panel as we want it to...
	}
	
}

class InviteTableModel extends AbstractTableModel{
	LinkedHashSet<InvitePanel> data = new LinkedHashSet<InvitePanel>();
	HashMap<String, InvitePanel> conversationNameToInvitePanel = new HashMap<String, InvitePanel>();

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return this.data.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return this.data.toArray()[arg0];
	}
	
	public void addInvite(String conversationID){
		InvitePanel invitePanel = new InvitePanel(conversationID);
		this.conversationNameToInvitePanel.put(conversationID, invitePanel);
		this.data.add(invitePanel);
	}
	
	public void removeInvite(String conversationID){
		InvitePanel invitePanel = this.conversationNameToInvitePanel.remove(conversationID);
		this.data.remove(invitePanel);
	}
	
}