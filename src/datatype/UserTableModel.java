package datatype;

import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.table.AbstractTableModel;

/**
 * This abstract table model represents the data for the online users table. In order to make
 * searching easy, we organized the data in this table by alphabetical order. The cells in this
 * table are not editable.
 * @author Peter
 *
 */
public class UserTableModel extends AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//TODO: improve data structure for quick "get".
	private TreeSet<String> data = new TreeSet<String>();

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		return this.data.size();
	}

	/**
	 * Get the value at the specified index in the data structure.
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		/*
		Iterator<String> it = this.data.iterator();
		String value = null;
		for(int i = 0; i < rowIndex + 1; i++){
			value = it.next();
		}
		return value;
		*/
		return (this.data == null) ? null : this.data.toArray()[rowIndex];
	}
	
	/**
	 * Controls whether or not a user may edit the cells in the table.
	 * @param row The row number of the cell (starting at 0).
	 * @param col The column number of the cell (starting at 0).
	 * @return false in order to prevent a user from editing the table.
	 */
	@Override
	public boolean isCellEditable(int row, int col){
		return false;
	}
	
	/**
	 * Add a user to the dataset.
	 * @param userID
	 */
	public void add(String userID){
		this.data.add(userID);		
	}
	
	/**
	 * Remove a user from the dataset.
	 * @param userID
	 */
	public void remove(String userID){
		this.data.remove(userID);
	}
}