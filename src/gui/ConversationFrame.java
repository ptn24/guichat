package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import datatype.MyUserTable;
import datatype.UserTableModel;

public class ConversationFrame extends JFrame implements MyUserTable, KeyListener{
	private final GroupLayout layout;
	private final JPanel conversationPanel, userPanel;
	private final JTextPane conversationTextPane;
	private final JTextArea messageTextArea;
	//private final JTextField messageTextField;
	private final JSplitPane splitPane;
	private final JScrollPane conversationScrollPane, messageScrollPane, userScrollPane;
	private final JTable userTable;
	private final JButton inviteFriendButton;
	
	private final String newline = "\n";
	
	public ConversationFrame(){
		//Instantiate the text pane.
		this.conversationTextPane = new JTextPane();
		this.conversationTextPane.setEditable(false);
		
		this.conversationScrollPane = new JScrollPane(this.conversationTextPane);
		this.conversationScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.conversationScrollPane.setPreferredSize(new Dimension(500, 300));
		
		this.conversationPanel = new JPanel();
		this.conversationPanel.setMinimumSize(new Dimension(500, 300));
		this.conversationPanel.add(conversationScrollPane);
		this.conversationPanel.setBorder(BorderFactory.createTitledBorder("Messages"));
		
		//Instantiate the table.
		this.userTable = new JTable(new UserTableModel());
		this.userTable.setTableHeader(null);
		
		this.userScrollPane = new JScrollPane(this.userTable);
		this.userScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.userScrollPane.setPreferredSize(new Dimension(150, 300));
		
		this.userPanel = new JPanel();
		this.userPanel.setMinimumSize(new Dimension(150, 300));
		this.userPanel.add(this.userScrollPane);
		this.userPanel.setBorder(BorderFactory.createTitledBorder("Users"));
		
		//Instantiate the text area.
		
		this.messageTextArea = new JTextArea(2, 1);
		this.messageTextArea.setLineWrap(true);
		this.messageTextArea.setWrapStyleWord(true);
		
		//TODO: is this the solution?
		this.messageTextArea.addKeyListener(this);
		this.messageScrollPane = new JScrollPane(this.messageTextArea);
		
		
		//TODO: what to do about the text field? want it to wrap properly
		/*
		this.messageTextField = new JTextField(null, null, 5);
		this.messageScrollPane = new JScrollPane(this.messageTextField);
		*/
		
		//Instantiate the split pane.
		this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.conversationPanel,
				this.userPanel);
		this.splitPane.setResizeWeight(0.5);
				
		//Instantiate the button.
		this.inviteFriendButton = new JButton("Invite Friend");
		
		//Initialize the layout.
		Container cp = this.getContentPane();
		this.layout = new GroupLayout(cp);
		cp.setLayout(this.layout);
		
		//Set the horizontal group.
		this.layout.setHorizontalGroup(this.layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent(this.splitPane)
				.addComponent(this.inviteFriendButton)
				.addComponent(this.messageScrollPane)
		);
		
		//Set the vertical group.
		this.layout.setVerticalGroup(this.layout.createSequentialGroup()
				.addComponent(this.splitPane)
				.addComponent(this.inviteFriendButton)
				.addComponent(this.messageScrollPane)
		);
		
		//Set the gaps to auto.
		this.layout.setAutoCreateGaps(true);
		this.layout.setAutoCreateContainerGaps(true);
		
		//Setup the window.
		setTitle("Conversation dummy");
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//WILL CHANGE!
	}
	
	public void addMessage(String userID, String time, String text){
		StyledDocument doc = this.conversationTextPane.getStyledDocument();
		this.addStylesToDocument(doc);
		
		String[] initString = {
				userID + " ",
				"(" + time + ")",
				" : ",
				text + this.newline
		};
		
		String[] initStyles = {
				"username", "time", "username", "regular"
		};
		
		for(int i = 0; i < initString.length; i++){
			try {
				doc.insertString(doc.getLength(), initString[i], doc.getStyle(initStyles[i]));
			} catch (BadLocationException e) {
				System.err.println("Couldn't insert initial text into text pane.");
			}
		}
	}
	
	private void addStylesToDocument(StyledDocument doc){
		//Initialize the default style.
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setFontFamily(def, "SansSerif");
		
		Style regular = doc.addStyle("regular", def);
		//Do we want background colors?
		//StyleConstants.setBackground(regular, Color.LIGHT_GRAY);
		
		//Initialize the bold style.
		Style username = doc.addStyle("username", regular);
		StyleConstants.setBold(username, true);
		
		//Initialize the small style.
		Style time = doc.addStyle("time", regular);
		StyleConstants.setFontSize(time, 10);
	}
	
	public void addUser(String userID){
		UserTableModel tm = (UserTableModel) this.userTable.getModel();
		tm.add(userID);
		tm.fireTableDataChanged();
		this.userTable.setModel(tm);
	}
	
	public void removeUser(String userID){
		UserTableModel tm = (UserTableModel) this.userTable.getModel();
		tm.remove(userID);
		tm.fireTableDataChanged();
		this.userTable.setModel(tm);
	}
	
	private static void create(){
		ConversationFrame frame = new ConversationFrame();
		frame.setVisible(true);
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				create();
			}
		});
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
