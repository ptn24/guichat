package gui;

import java.util.TreeSet;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class UserTab extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JScrollPane scrollPane;
	private JTable userTable;
		
	public UserTab(){
		this.userTable = new JTable(new UserTableModel());
		this.userTable.setTableHeader(null);
		
		this.scrollPane = new JScrollPane(this.userTable);
		this.add(this.scrollPane);
	}
	
	/**
	 * Add the client to the list of online users to be displayed.
	 * @param userID The client's username.
	 */
	public void addUser(String userID){		
		UserTableModel myTableModel = (UserTableModel) this.userTable.getModel();
		myTableModel.add(userID);
		myTableModel.fireTableDataChanged();
		this.userTable.setModel(myTableModel);
	}
	
	/**
	 * Remove the client from the list of online users to be displayed.
	 * @param userID The client's username.
	 */
	public void removeUser(String userID){
		UserTableModel myTableModel = (UserTableModel) this.userTable.getModel();
		myTableModel.remove(userID);
		myTableModel.fireTableDataChanged();
		this.userTable.setModel(myTableModel);
	}
}

/**
 * This abstract table model represents the data for the online users table. In order to make
 * searching easy, we organized the data in this table by alphabetical order. The cells in this
 * table are not editable.
 * @author Peter
 *
 */
class UserTableModel extends AbstractTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TreeSet<String> data = new TreeSet<String>();

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public int getRowCount() {
		return this.data.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		//return this.data.get(rowIndex);
		return this.data.toArray()[rowIndex];
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
