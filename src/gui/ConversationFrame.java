package gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import client.Client;

import datatype.MyUserTable;
import datatype.UserTableModel;

public class ConversationFrame extends JFrame 
								implements MyUserTable, WindowListener, ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Client client;
	private final String conversationID;
	
	private final GroupLayout layout;
	private final JPanel conversationPanel, userPanel;
	private final JTextPane conversationTextPane;
	private final JTextArea messageTextArea;
	private final JSplitPane splitPane;
	private final JScrollPane conversationScrollPane, messageScrollPane, userScrollPane;
	private final JTable userTable;
	private final JButton inviteFriendButton;
	
	private InviteFrame inviteFrame;
	
	private final String newline = "\n";
		
	public ConversationFrame(Client client, String conversationID){
		this.client = client;
		this.conversationID = conversationID;
		
		//Instantiate the text pane.
		this.conversationTextPane = new JTextPane();
		this.conversationTextPane.setEditable(false);
		
		//Put the text pane into a scroll pane.
		this.conversationScrollPane = new JScrollPane(this.conversationTextPane);
		this.conversationScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.conversationScrollPane.setPreferredSize(new Dimension(500, 300));
		
		//Put the scroll pane into a panel.
		this.conversationPanel = new JPanel();
		this.conversationPanel.setMinimumSize(new Dimension(500, 300));
		this.conversationPanel.add(conversationScrollPane);
		this.conversationPanel.setBorder(BorderFactory.createTitledBorder("Messages"));
		
		//Instantiate the table.
		this.userTable = new JTable(new UserTableModel());
		this.userTable.setTableHeader(null);
		
		//Put the table into a scroll pane.
		this.userScrollPane = new JScrollPane(this.userTable);
		this.userScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.userScrollPane.setPreferredSize(new Dimension(150, 300));
		
		//Put the scroll pane into a panel.
		this.userPanel = new JPanel();
		this.userPanel.setMinimumSize(new Dimension(150, 300));
		this.userPanel.add(this.userScrollPane);
		this.userPanel.setBorder(BorderFactory.createTitledBorder("Users"));
		
		//Instantiate the text area.
		this.messageTextArea = new JTextArea(2, 1);
		this.messageTextArea.setLineWrap(true);
		this.messageTextArea.setWrapStyleWord(true);
		
		//Setup the key bindings for the text area.
		this.messageTextArea.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), 
				"myMessageTextAreaAction");
		this.messageTextArea.getInputMap().put(KeyStroke.getKeyStroke("shift ENTER"), 
				"myMessageTextAreaAction");
		this.messageTextArea.getActionMap().put("myMessageTextAreaAction", 
				new MyMessageTextAreaAction(this.client, 
						this,
						"myMessageTextAreaAction"));

		//Put the text area into a scroll pane.
		this.messageScrollPane = new JScrollPane(this.messageTextArea);
		
		//Instantiate the split pane.
		this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.conversationPanel,
				this.userPanel);
		this.splitPane.setResizeWeight(0.5);
				
		//Instantiate the button.
		this.inviteFriendButton = new JButton("Invite Friend");
		this.inviteFriendButton.addActionListener(this);
		
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
		this.setTitle("GUI CHAT - " + conversationID);
		this.pack();
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);		//WILL CHANGE!
		this.addWindowListener(this);
	}
	
	/**
	 * Add a message to the text pane.
	 * @param userID The username of the client who sent the message.
	 * @param time The time the message was sent.
	 * @param text The text of the message.
	 */
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
		
		this.conversationTextPane.setCaretPosition(doc.getLength());
	}
	
	/**
	 * Add the style definitions to the text pane.
	 * @param doc
	 */
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
	
	/**
	 * Add a user to the user table.
	 */
	public void addUser(String userID){
		UserTableModel tm = (UserTableModel) this.userTable.getModel();
		tm.add(userID);
		tm.fireTableDataChanged();
		this.userTable.setModel(tm);
	}
	
	/**
	 * Remove a user from the user table.
	 */
	public void removeUser(String userID){
		UserTableModel tm = (UserTableModel) this.userTable.getModel();
		tm.remove(userID);
		tm.fireTableDataChanged();
		this.userTable.setModel(tm);
	}
	
	public String getConversationID(){
		return this.conversationID;
	}
	
	public JTextArea getMyMessageTextArea(){
		return this.messageTextArea;
	}
	
	/**
	 * This action is applied to the message text area in the conversation window. When the client
	 * presses the "ENTER" key, the text in the text area is submitted to the server. When the
	 * client presses the "SHIFT+ENTER" key, a newline is inserted into the text area.
	 */
	class MyMessageTextAreaAction extends AbstractAction {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final Client client;
		private final ConversationFrame conversationFrame;

		public MyMessageTextAreaAction(Client client, ConversationFrame conversationFrame, String name){
			super(name);
			this.client = client;
			this.conversationFrame = conversationFrame;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getModifiers() == 0){
				String text = this.conversationFrame.getMyMessageTextArea().getText();
				this.conversationFrame.getMyMessageTextArea().setText("");
				
				//Submit the message to the server.
				this.client.requestSendMessage(conversationID, text);
			}
			
			else if(e.getModifiers() == 1){
				this.conversationFrame.getMyMessageTextArea().append(newline);
			}
		}
	}


	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		//Submit exit conversation to the server.
		this.client.requestExitChat(this.conversationID);
		
		//Close the window.
		this.dispose();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		
		if(ae.getActionCommand().equals("Invite Friend")){
			//JOptionPane.showMessageDialog(null, "HA-HA!");
			
			//TODO: implement the actions.
			this.inviteFrame = new InviteFrame(this.client);
			
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					inviteFrame.setVisible(true);
				}
			});
		}
	}
}


