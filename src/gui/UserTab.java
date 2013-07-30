package gui;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class UserTab extends JPanel{
	private final JScrollPane scrollPane;
	private final JTable userTable;
	
	public UserTab(){
		this.userTable = new JTable(0, 1);		
        this.userTable.setFillsViewportHeight(true);
        this.userTable.setTableHeader(null);
        
        DefaultTableModel defaultTableModel = (DefaultTableModel) this.userTable.getModel();
        String[] data = {"dummy"};
        defaultTableModel.addRow(data);
        
        this.userTable.setModel(defaultTableModel);
        
		this.scrollPane = new JScrollPane(this.userTable);
		
		//Add the scroll pane to the panel.
		this.add(this.scrollPane);
	}
	
	//TODO: need to fix to add names in alphabetical order and be able to remove 
	public void addUser(String userID){
		System.out.print("adding user to table...\n");
		DefaultTableModel defaultTableModel = (DefaultTableModel) this.userTable.getModel();
		String[] data = {userID};
		defaultTableModel.addRow(data);
		this.userTable.setModel(defaultTableModel);
	}
	
	public static void create(){
		//Create and set up the window.
        JFrame frame = new JFrame("ScrollDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        JPanel newContentPane = new UserTab();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				UserTab.create();
			}
		});
	}
}
