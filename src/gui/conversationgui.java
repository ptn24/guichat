package gui;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JList;
import javax.swing.JLabel;

import main.Client;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class conversationgui extends JFrame {


	private JPanel contentPane;
	private final JTextArea messagebox;
	private DefaultListModel listModel;
	private JList userlist;
	private JEditorPane messageconv;
	public String[] allusers;
	private inviteusersgui window;
	private Client client;
	private String thetitle;
	
	/**
	 * Create the frame.
	 */
	public conversationgui(final Client client) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				client.exitChat(thetitle);
			}
		});
		
		this.contentPane = new JPanel();
		this.messagebox = new JTextArea();
		this.listModel = new DefaultListModel();
		this.userlist = new JList(listModel);
		this.messageconv = new JEditorPane();
		this.allusers = new String[0];
		this.window = new inviteusersgui(this, client);
		this.client = client;
		this.thetitle = "";
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 600, 520);
		//contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JButton sendButton = new JButton("Send");
		sendButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
            	String message = messagebox.getText();      
            	
            	client.sendMessage(thetitle, message);
            	messagebox.setText("");
			}
		});
		
		JScrollPane scrollPane_2 = new JScrollPane();
		
		final JButton invitebutton = new JButton("Invite");
		invitebutton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				for(String item: allusers);{
					
				}
				window.onusersList(allusers);
				//invitebutton.setEnabled(false);
				window.setVisible(true);
				
				//window.
				
			}
		});
		
		JButton btnNewButton = new JButton("Leave chat");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			client.exitChat(thetitle);
			client.removeInConversation(thetitle);
			setVisible(false);
			}
		});
		
		JLabel lblUsersInChat = new JLabel("Users in Chat");
		lblUsersInChat.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JLabel lblConversation = new JLabel("Conversation");
		lblConversation.setFont(new Font("Tahoma", Font.BOLD, 11));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(28)
									.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 417, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(29)
									.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 413, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(187)
									.addComponent(lblConversation, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(27)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
										.addComponent(invitebutton, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
										.addComponent(sendButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblUsersInChat)
									.addGap(10))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(472)
							.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(16)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUsersInChat)
						.addComponent(lblConversation))
					.addGap(1)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(invitebutton))
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))
					.addGap(25)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(sendButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
					.addGap(18)
					.addComponent(btnNewButton)
					.addGap(25))
		);
		
		
		messageconv.setEditable(false);
		
		//messageconv.setLineWrap(true);
		//messageconv.setWrapStyleWord(true);
		
		
		scrollPane_1.setViewportView(messageconv);
		
		//JList list = new JList();
		scrollPane_2.setViewportView(userlist);
		
		
		messagebox.addKeyListener(new KeyAdapter() {
			@Override
			
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER){
                	String message = messagebox.getText();
                	client.sendMessage(thetitle, message);
                	messagebox.setText("");
                	e.consume();
                }
                
			}
		});
		messagebox.setLineWrap(true);
		messagebox.setWrapStyleWord(true);
		scrollPane.setViewportView(messagebox);
		contentPane.setLayout(gl_contentPane);
		
	}
	public String getTextFromField(){
		return messageconv.getText();
	}
	
	public void usersInConversationList(String[] array){
		//remove and add new array
		listModel.removeAllElements();
		
		for(String item: array){
			//System.out.println("element added IT WAS HEREREE"+ item);
			listModel.addElement(item);
		}
		userlist.setModel(listModel);
		
	}
	public void allUsersList(String[] array){
		//remove and add new array
		allusers = array.clone();

		
	}
	public void messageList(String add){
		//conversation message
		//process w/ html
		messageconv.setContentType("text/html");
		messageconv.setText("<html>" + add + "</html>");
	}

	public void setnewTitle(String title){
		setTitle("Conversation - " + title);
		thetitle  = title;
		
	}

	
}
