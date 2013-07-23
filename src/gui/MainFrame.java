package gui;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame{
	private final GroupLayout layout;
	
	private final JPanel mainPanel;
	private final JTabbedPane mainTabbedPane;
	
	private final JPanel bottomPanel;
	private final JButton createNewChatButton;
	private final JButton logOutButton;
	
	public MainFrame(){
		//Setup the main panel.
		this.mainPanel = new JPanel();
		this.mainTabbedPane = new JTabbedPane();
		this.mainPanel.add(this.mainTabbedPane);
		
		//TODO: make the tabs
		
		//Setup the bottom panel.
		this.bottomPanel = new JPanel();
		this.createNewChatButton = new JButton("Create New Chat");
		this.createNewChatButton.setName("Create New Chat");
		this.createNewChatButton.setPreferredSize(new Dimension(150, 35));
		
		this.logOutButton = new JButton("Log Out");
		this.logOutButton.setName("Log Out");
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new MainFrame().setVisible(true);
			}
		});
	}
}


