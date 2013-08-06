package datatype;

import gui.InvitePanel;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import client.Client;

public class InviteTableRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InvitePanel invitePanel;
	
	public InviteTableRenderer(Client client, JTable table){
		this.invitePanel = new InvitePanel(client, table);
		this.setHorizontalAlignment(SwingConstants.RIGHT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		// TODO Auto-generated method stub
		
		Invite invite = (Invite) value;
		this.invitePanel.updateData(invite);
		
		if(isSelected){
			this.invitePanel.setBackground(table.getSelectionBackground());
		}
		
		return this.invitePanel;
	}

}
