package gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import client.Client;

public class MainFrame extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Client client;
	private final GroupLayout layout;
	
	private final JPanel mainPanel;
	private final JTabbedPane mainTabbedPane;
	private final UserTab userTab;
	private final ConversationTab conversationTab;
	private final InviteTab inviteTab;
	
	private final JPanel bottomPanel;
	private final JButton createNewChatButton;
	private final JButton logOutButton;
	
	public MainFrame(final Client client){
		this.client = client;
		
		//Setup the main panel.
		this.mainPanel = new JPanel();
		this.mainTabbedPane = new JTabbedPane();
		this.userTab = new UserTab();
		this.conversationTab = new ConversationTab(this.client);
		this.inviteTab = new InviteTab();
		
		this.mainTabbedPane.addTab("Users", this.userTab);
		this.mainTabbedPane.addTab("Chats", this.conversationTab);
		this.mainTabbedPane.addTab("Invites", this.inviteTab);
		
		this.mainPanel.add(this.mainTabbedPane);
		
		//TODO: make the tabs
		
		//Setup the bottom panel.
		this.bottomPanel = new JPanel();
		this.createNewChatButton = new JButton("Create New Chat");
		this.createNewChatButton.setName("Create New Chat");
		this.createNewChatButton.addActionListener(this);
		this.createNewChatButton.setPreferredSize(new Dimension(150, 35));
		
		this.logOutButton = new JButton("Log Out");
		this.logOutButton.setName("Log Out");
		this.logOutButton.addActionListener(this);
		this.logOutButton.setPreferredSize(new Dimension(150, 35));
		
		this.bottomPanel.add(this.createNewChatButton);
		this.bottomPanel.add(this.logOutButton);
		
		//Initialize the layout.
		Container cp = this.getContentPane();
		this.layout = new GroupLayout(cp);
		cp.setLayout(this.layout);
		
		//Set the horizontal group.
		this.layout.setHorizontalGroup(this.layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(this.mainPanel)
				.addComponent(this.bottomPanel)
		);
		
		//Set the vertical group.
		this.layout.setVerticalGroup(this.layout.createSequentialGroup()
				.addComponent(this.mainPanel)
				.addComponent(this.bottomPanel)
		);
		
		//Set the gaps to auto.
		this.layout.setAutoCreateGaps(true);
		this.layout.setAutoCreateContainerGaps(true);
		
		//Setup the window.
		setTitle("GUI Chat - Main");
		pack();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				client.requestLogOff();
			}
		});
	}
	
	public UserTab getUserTab(){
		return this.userTab;
	}
	
	public ConversationTab getConversationTab(){
		return this.conversationTab;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		if(arg0.getActionCommand().equals("Create New Chat")){
			this.client.openCreateConversationWindow();
		}
		
		else if(arg0.getActionCommand().equals("Log Out")){
			this.client.requestLogOff();
		}
		
	}
}


