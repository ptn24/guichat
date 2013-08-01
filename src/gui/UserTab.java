package gui;

import java.util.TreeSet;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import datatype.UserTableModel;

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
		this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
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


