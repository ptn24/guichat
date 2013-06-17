package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import main.*;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class maingui extends JFrame {

	private JPanel contentPane;
	static DefaultListModel listModel;
	static DefaultListModel listModel2;
	static DefaultListModel listModel3;
	private JList conversationlist;
	private JList invitelist;
	private JList userlist;
	private invitegui window;
	static String[] test2 = {"car", "boat","plane", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a"};
	private conversationnamegui window2;
	private static Client client;
	private JTextField textField;
	private String historyval;


	
	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					maingui frame = new maingui(client);
					frame.setVisible(true);
					//inviteList(test2);
					//conversationList(test2);
					//userList(test2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/
	
	
	public void conversationList(String[] array){
		listModel.removeAllElements();
		for(String item: array){
			listModel.addElement(item);
		}
		conversationlist.setModel(listModel);	
	}
	public void inviteList(String[] array){
		
		listModel2.removeAllElements();
		for(String item: array){
			listModel2.addElement(item);
		}
		invitelist.setModel(listModel2);		
	}
	public void userList(String[] array){
		
		listModel3.removeAllElements();
		for(String item: array){
			listModel3.addElement(item);
		}
		userlist.setModel(listModel3);		
	}

	/**
	 * Create the frame.
	 */
	public maingui(final Client client) {
		listModel = new DefaultListModel();
		listModel2 = new DefaultListModel();
		listModel3 = new DefaultListModel();
		conversationlist = new JList(listModel);
		invitelist = new JList(listModel2);
		userlist = new JList(listModel3);
		userlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		window = new invitegui(client);
		window2 = new conversationnamegui(client);
		this.client = client;
		this.setVisible(true);
		historyval = "";
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 530);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		JButton createRoom = new JButton("Create Room");
		createRoom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				window2.setVisible(true);
			}
		});
		createRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton logoff = new JButton("Log off");
		logoff.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				client.logoff();
				setVisible(false);
				new Client();
			}
		});
		logoff.setMinimumSize(new Dimension(95, 23));
		logoff.setMaximumSize(new Dimension(95, 23));
		
		
		JLabel lblOnlineUsers = new JLabel("Online Users");
		lblOnlineUsers.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel lblConversations = new JLabel("Conversations");
		lblConversations.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLabel lblInvites = new JLabel("Invites");
		lblInvites.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
			
		
		conversationlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		conversationlist.addListSelectionListener(
				new ListSelectionListener(){
					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (e.getValueIsAdjusting()){
							System.out.println(conversationlist.getSelectedValue().toString());
							String enter = conversationlist.getSelectedValue().toString();
							System.out.println("hereliest +" + enter);
							client.enterChat(enter);
							//System.out.println(conversationlist.getSelectedValue());
						}
					}
			
		});
		
		invitelist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		invitelist.addListSelectionListener(
				new ListSelectionListener(){
					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (e.getValueIsAdjusting()){
							System.out.println(invitelist.getSelectedValue());
							String val  = invitelist.getSelectedValue().toString();
							window.setTitle(val);
							window.setVisible(true);
						}
					}
			
		});
		
		
		scrollPane_1.setViewportView(conversationlist);
		scrollPane_2.setViewportView(invitelist);
		scrollPane.setViewportView(userlist);
		contentPane.add(lblOnlineUsers);
		contentPane.add(lblConversations);
		contentPane.add(lblInvites);
		contentPane.add(scrollPane);
		contentPane.add(scrollPane_1);
		contentPane.add(scrollPane_2);
		contentPane.add(createRoom);
		contentPane.add(logoff);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				int key = arg0.getKeyCode();
                if (key == KeyEvent.VK_ENTER){
                	historyval = textField.getText();
                	new historygui(client.gethistory(historyval));
                	
                	
                	
                }
			}
		});
		textField.setColumns(10);
		
		JLabel lblHi = new JLabel("History");
		lblHi.setFont(new Font("Tahoma", Font.BOLD, 14));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(71)
							.addComponent(lblOnlineUsers)
							.addGap(86)
							.addComponent(lblConversations)
							.addGap(107)
							.addComponent(lblInvites, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(25)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(75)
									.addComponent(createRoom)
									.addGap(50)
									.addComponent(logoff, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
									.addGap(13)
									.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
									.addGap(10)
									.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblHi))))
					.addContainerGap(22, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(37)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblOnlineUsers)
						.addComponent(lblConversations)
						.addComponent(lblInvites))
					.addGap(6)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 321, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 321, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 321, GroupLayout.PREFERRED_SIZE))
					.addGap(25)
					.addComponent(lblHi)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(createRoom)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(logoff, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
