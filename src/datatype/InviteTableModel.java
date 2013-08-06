package datatype;

import java.util.LinkedHashSet;
import javax.swing.table.AbstractTableModel;

public class InviteTableModel extends AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinkedHashSet<Invite> data = new LinkedHashSet<Invite>();
	
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
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		
		return (this.data == null) ? null : this.data.toArray()[rowIndex];
	}
	
	@Override
	public Class<Invite> getColumnClass(int columnIndex){
		return Invite.class;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex){
		return true;
	}
	
	public void addInvite(Invite invite){
		this.data.add(invite);
		this.fireTableStructureChanged();
	}
	
	public void removeInvite(Invite invite){
		this.data.remove(invite);
		this.fireTableStructureChanged();
	}
	
}