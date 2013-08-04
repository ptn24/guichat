package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import client.Client;

public class ConversationTab extends JPanel implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Client client;
	private String myUsername;
	
	private final JScrollPane scrollPane;
	private final DefaultMutableTreeNode activeConversationsNode;
	private JTree conversationTree;
	private JPopupMenu rightClickPopup;
	
	private final HashMap<String, DefaultMutableTreeNode> conversationNameToConversationNode;
	private final HashMap<String, HashMap<String, DefaultMutableTreeNode>> userNameToConversationNameToUserNode;
	
	public ConversationTab(Client client){
		this.client = client;
		this.conversationNameToConversationNode = new HashMap<String, DefaultMutableTreeNode>();
		this.userNameToConversationNameToUserNode = new HashMap<String, HashMap<String, DefaultMutableTreeNode>>();
		
		//Instantiate the tree.
		this.activeConversationsNode = new DefaultMutableTreeNode("Active");
		this.conversationTree = new JTree(this.activeConversationsNode);
		
		//Add listener.
		this.conversationTree.addMouseListener(this);
		
		//Only one node can be selected at a time.
		this.conversationTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		//Set up the scroll pane.
		this.scrollPane = new JScrollPane(this.conversationTree);
		this.scrollPane.setPreferredSize(new Dimension(460, 420));
		this.add(this.scrollPane);
	}
	
	/**
	 * Add a node for each new conversation.
	 * @param conversationID
	 */
	public void addConversationNode(String conversationID){
		//Update mappings.
		DefaultMutableTreeNode conversationNode = new DefaultMutableTreeNode(conversationID);
		this.conversationNameToConversationNode.put(conversationID, conversationNode);
		
		//Insert the node into the tree.
		DefaultTreeModel tm = (DefaultTreeModel) this.conversationTree.getModel();
		tm.insertNodeInto(conversationNode, 
				this.activeConversationsNode, 
				tm.getChildCount(this.activeConversationsNode));
	}
	
	/**
	 * Add a node to a conversation node each time a client joins the conversation.
	 * @param conversationID The name of the conversation node. Requires that this node has already
	 * 						been created.
	 * @param userID The name of the user node to be added to the conversation node.
	 */
	public void addUserNode(String conversationID, String userID){
		DefaultMutableTreeNode conversationNode = this.conversationNameToConversationNode.get(conversationID);
		DefaultMutableTreeNode userNode;
		
		//Differentiate between this client's user node and other clients' user nodes.
		if(userID.equals(this.myUsername)){
			userNode = new DefaultMutableTreeNode(userID + " (me)");
		}
		
		else{
			userNode = new DefaultMutableTreeNode(userID);
		}
		
		if(!this.userNameToConversationNameToUserNode.containsKey(userID)){
			HashMap<String, DefaultMutableTreeNode> conversationNameToUserNode = new HashMap<String, DefaultMutableTreeNode>();
			conversationNameToUserNode.put(conversationID, userNode);
			this.userNameToConversationNameToUserNode.put(userID, conversationNameToUserNode);
		}
		
		else{
			HashMap<String, DefaultMutableTreeNode> conversationNameToUserNode = this.userNameToConversationNameToUserNode.get(userID);
			conversationNameToUserNode.put(conversationID, userNode);
		}		
		
		DefaultTreeModel tm = (DefaultTreeModel) this.conversationTree.getModel();
		tm.insertNodeInto(userNode, conversationNode, conversationNode.getChildCount());
	}
	
	/**
	 * Remove a node from a conversation node each time a client exits the conversation.
	 * @param conversationID
	 * @param userID
	 */
	public void removeUserNode(String conversationID, String userID){
		DefaultMutableTreeNode userNode = this.userNameToConversationNameToUserNode.get(userID).remove(conversationID);
				
		//Remove the node from the tree.
		DefaultTreeModel tm = (DefaultTreeModel) this.conversationTree.getModel();
		tm.removeNodeFromParent(userNode);
	}
	
	/**
	 * Set the client's username.
	 * @param userID
	 */
	public void setUsername(String userID){
		this.myUsername = userID;
	}
	
	private static void create(){
		JFrame frame = new JFrame("Conversation Tab");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Add content to the window.
		frame.add(new ConversationTab(new Client()));
		
		//Display the window.
		frame.pack();
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
	public void mouseClicked(MouseEvent arg0) {
		if(SwingUtilities.isRightMouseButton(arg0)){			
			int row = this.conversationTree.getRowForLocation(arg0.getX(), arg0.getY());
			
			if(row != -1){
				this.conversationTree.setSelectionRow(row);
				TreePath path = this.conversationTree.getSelectionPath();
				
				if(path.getPathCount() == 2){					
					//Instantiate the popup menu.
					this.rightClickPopup = new JPopupMenu();
					
					//Instantiate the "Enter Conversation" menu item.
					JMenuItem menuItem = new JMenuItem("Enter Conversation");
					this.rightClickPopup.add(menuItem);
					
					//Add listener to the menu item.
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
					menuItem.addActionListener(new PopupListener(node.toString()));

					this.rightClickPopup.show(arg0.getComponent(), arg0.getX(), arg0.getY());
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	class PopupListener implements ActionListener{
		private final String conversationID;
		
		public PopupListener(String conversationID){
			this.conversationID = conversationID;
		}
		
		/**
		 * 
		 * @param arg0 The ActionEvent on the popup menu item.
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//TODO: CHANGE
			//TODO: if the client is in the conversation, make the conversation window the
			//focus. else request to enter the chat.
			client.requestEnterChat(this.conversationID);
			
			//TODO: add exit chat functionality...
		}
		
	}
}


