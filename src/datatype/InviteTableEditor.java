package datatype;

import gui.InvitePanel;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import client.Client;

public class InviteTableEditor extends AbstractCellEditor implements TableCellEditor{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InvitePanel invitePanel;
	
	public InviteTableEditor(Client client, JTable table){
		this.invitePanel = new InvitePanel(client, table);
	}
	
	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		// TODO Auto-generated method stub
		
		Invite invite = (Invite) value;
		this.invitePanel.updateData(invite);
		this.invitePanel.setBackground(table.getSelectionBackground());
		
		return this.invitePanel;		
	}

}
