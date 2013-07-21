package oldgui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.Client;

public class inviteusersgui extends JFrame {

	private JPanel contentPane;
	static DefaultListModel listModel;
	private static JList list;
	private Client client;
	private conversationgui con;

	
	public void onusersList(String[] array){
		
		listModel.removeAllElements();
		for(String item: array){
			listModel.addElement(item);
		}
		list.setModel(listModel);		
	}

	/**
	 * Create the frame.
	 */
	public inviteusersgui(final conversationgui con,final Client client) {
		this.con = con;
		listModel = new DefaultListModel();
		list = new JList(listModel);
		this.client = client;
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 250, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		onusersList(con.allusers);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel lblOnlineUsers = new JLabel("Online Users");
		lblOnlineUsers.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel lblClickUserTo = new JLabel("Click user to add user  to chat");
		lblClickUserTo.setFont(new Font("Tahoma", Font.BOLD, 12));
		GroupLayout groupLayout = new GroupLayout(contentPane);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(75)
							.addComponent(lblOnlineUsers))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(29)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(25)
							.addComponent(lblClickUserTo)))
					.addContainerGap(24, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(42)
					.addComponent(lblOnlineUsers)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(lblClickUserTo)
					.addContainerGap(45, Short.MAX_VALUE))
		);
		
		
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(
				new ListSelectionListener(){
					@Override
					public void valueChanged(ListSelectionEvent e) {
						
						if (e.getValueIsAdjusting()){
							//System.out.println(con.getTitle().replaceAll("Conversation - ", "") + "|||" +list.getSelectedValue().toString());
							client.sendInvite(con.getTitle().replaceAll("Conversation - ", ""), list.getSelectedValue().toString());
						}
							
					}
			
		});
		
		scrollPane.setViewportView(list);
		contentPane.setLayout(groupLayout);
	}

}
