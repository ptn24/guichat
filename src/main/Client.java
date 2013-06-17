package main;

import gui.logingui;
import gui.maingui;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

/**
 * GUI chat client runner.
 */
public class Client implements MouseListener{
	
	private static PrintWriter out;
	private String username;
	private Set<String> activeUsers;
	private Set<String> invites;
	private Set<String> conversations;
	private Map<String, String> historystorage;
	private Map<String, clientConversation> nameToConversation;
	//private static String ip = "localhost";
	//private static String port = "4444";
	private boolean online;
	private static Socket socket;
	//private final static logingui test;
	private static maingui main;
	
	private JButton btnNewButton;
	private String usernametest;
	private String iptest;
	private String porttest;
	private final logingui logingui;

    /**
     * Start a GUI chat client.
     * @throws IOException 
     * @throws UnknownHostException 
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        // YOUR CODE HERE
        // It is not required (or recommended) to implement the client in
        // this runner class.
    	//Socket socket = new Socket(ip, 4444);
    	//handleConnection(socket);
    	//Client c = new Client("localhost", "4444");
    	//test = new logingui();
    	//test.setVisible(true);
    	//test.
    	//handleConnection(socket);
    	Client c = new Client();
    }
    
    public Client(){
    	this.logingui = new logingui();
    	logingui.setVisible(true);
    	
    	//Get the button from the GUI.
    	btnNewButton = logingui.getButton();
    	btnNewButton.addMouseListener(this);
    	
	    this.activeUsers = new TreeSet<String>();
	    this.invites = new TreeSet<String>();
	    this.online = false;
	    this.conversations = new TreeSet<String>();

	    this.nameToConversation = Collections.synchronizedMap(new HashMap<String, clientConversation>());
    	this.historystorage = new HashMap<String, String>(); 
    	
    }
    
    public void mouseClicked(MouseEvent e){
    	
    	//set to values from logingui
		usernametest = logingui.logval()[0];
		iptest = logingui.logval()[1];
		porttest = logingui.logval()[2];
		
		
		try {

			socket = new Socket(iptest, Integer.parseInt(porttest));
			
    		Thread c = new Thread(new Runnable(){
    			public void run(){
    				try {
						handleConnection(socket);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    		});
    		c.start();
    		
    		
    		logon(usernametest);
			
		} catch (UnknownHostException e1) {
			
			Runnable update = new Runnable(){
				public void run(){
					logingui.setResult("hostname incorrect");
					}
			};
			SwingUtilities.invokeLater(update);
			
			//e1.printStackTrace();
		} catch (IOException e2) {
			Runnable update = new Runnable(){
				public void run(){
					logingui.setResult("port incorrect");
					}
			};
			SwingUtilities.invokeLater(update);

		}catch(NumberFormatException e3){
			
			Runnable update = new Runnable(){
				public void run(){
					logingui.setResult("port must be a number");
					}
			};
			SwingUtilities.invokeLater(update);

		}


    }
    

    
    public void sendOut(String toserver){
    	//System.out.println("sending to server: " + toserver);
    	out.println(toserver);
    }
	
    private void handleConnection(Socket socket) throws IOException{		
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        try{
        	for (String line = in.readLine(); line != null; line = in.readLine()) {        		
        		handleRequest(line);        		
        	}
        }
        
        finally {
        	out.close();
        	in.close();
        }
	}
	// Client to Server
	//	"(LOG_ON\\sUSER_ID\\s\\p{Alnum}+)|" +
	//	"(LOG_OFF)|" +
	//	
	//	"(START_CHAT\\sCONVERSATION_ID\\s\\p{Alnum}|" +
	//				
	//	"(SEND_MESSAGE\\sCONVERSATION_ID\\s\\p{Alnum}+\\sTEXT\\s.*)|"+
	//
	//	"(ENTER_CHAT\\sCONVERSATION_ID\\s\\p{Alnum}+|"+
	//  
	//	"(EXIT_CHAT\\sCONVERSATION_ID\\s\\p{Alnum}+|"+
	//	
	//	"(SEND_INVITE\\sCONVERSATION_ID\\s\\p{Alnum}+)|"
    
    /**
     * username cannot be any of the toknens we use use. ie. <USER_ID>, <TEXT>.... 
     * @param usernametest
     */
    public void logon(final String usernametest) {
		
		//check if input is alphanumeric
		if(usernametest.matches("[a-zA-Z0-9]+")){
			//String[] badnames = new String[]{"USER_ID", "LOG_ON", "SEND_MESSAGE"};
			
			sendOut("LOG_ON <USER_ID> " + usernametest);	
		}
		else{
			Runnable update = new Runnable(){
				public void run(){
					logingui.setResult(usernametest + " not alphanumeric");
					}
			};
			SwingUtilities.invokeLater(update);	
		}
			
    }
    
    public void logoff(){
    	sendOut("LOG_OFF");
    	online = false;
    }
    
    public void createChat(String chatname){
    	if(chatname.matches("[a-zA-Z0-9]+")){
    		sendOut("START_CHAT CONVERSATION_ID " + chatname);
    	}
    	else{
    		
    	}
    }
    
    //sends enter chat request  to server
    public void enterChat(String conversation_ID){
    	sendOut("ENTER_CHAT CONVERSATION_ID " + conversation_ID);
    }
    
    //sends start chat request  to server
    public void startChat(String conversation_ID){
    	sendOut("START_CHAT CONVERSATION_ID " + conversation_ID);
    }
    
    //sends exit chat request  to server
    public void exitChat(String conversation_ID){
    	//saves history when user exits chat
    	String history = nameToConversation.get(conversation_ID).getHistory();
    	historystorage.put(conversation_ID, history);
    	
    	//remove from set 
    	nameToConversation.remove(conversation_ID);
		sendOut("EXIT_CHAT CONVERSATION_ID " + conversation_ID );
		
    	
    }
      
    //
    public void sendMessage(String CONVERSATION_ID, String TEXT ){
    	sendOut("SEND_MESSAGE CONVERSATION_ID " + CONVERSATION_ID + " <TEXT> " + TEXT); 
    }
    
    public void sendInvite(String conversation_ID, String user ){
    	sendOut("SEND_INVITE CONVERSATION_ID " + conversation_ID + " <USER_ID> " + user);
    	
    }
    //handle returns statements
    
	protected void handleRequest(String input) {
		// good for testing
		//System.out.println("from server|" + input);
		
		//Server to Client grammar.
		String regex = 
				"(LOG_ON\\sSUCCESS\\s<USER_ID>\\s\\p{Alnum}+)|" +
				"(LOG_ON\\sFAIL)|" +
				
				"(LOG_OFF\\sSUCCESS)|" +
				"(LOG_OFF\\sFAIL)|" +
				
				"(START_CHAT\\sSUCCESS\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
				"(START_CHAT\\sFAIL)|" +
							
				"(SEND_MESSAGE\\sCONVERSATION_ID\\s\\p{Alnum}+\\s<USER_ID>\\s\\p{Alnum}+\\s<TIME>\\s[0-9]{2}:[0-9]{2}:[0-9]{2}\\s<TEXT>\\s.*)|" +
				"(SEND_MESSAGE\\sSUCCESS\\sCONVERSATION_ID)\\s\\p{Alnum}+\\s<TEXT>\\s.*|" +
				"(SEND_MESSAGE\\sFAIL)|" +
		
				"(UPDATE_STATUS(\\s\\p{Alnum}+)*)|" +
				"(UPDATE_CONVERSATIONS(\\sCONVERSATION_ID\\s\\p{Alnum}+\\s<USER_ID>(\\s\\p{Alnum}+)*)*)|" +
				"(UPDATE_CONVERSATION_HISTORY\\sCONVERSATION_ID\\s\\p{Alnum}+(\\s<USER_ID>\\s\\p{Alnum}+\\s<TIME>\\s[\\p{Alnum}\\p{Punct}]+\\s<TEXT>\\s.*)*)|" +
				
				"(ENTER_CHAT\\sSUCCESS\\sCONVERSATION_ID\\s\\p{Alnum}+)|"+
				"(ENTER_CHAT\\sFAIL)|" +
				
				"(EXIT_CHAT\\sSUCCESS)|"+
				"(EXIT_CHAT\\sFAIL)|" +
				
				"(SEND_INVITE\\sSUCCESS\\sCONVERSATION_ID\\s\\p{Alnum}+\\s<USER_ID>\\s\\p{Alnum}+)|" +
				"(SEND_INVITE\\sCONVERSATION_ID\\s\\p{Alnum}+)|" +
				"(SEND_INVITE\\sFAIL)|";
		
				
		
		String[] tokens = input.split(" ");
		if(!input.matches(regex)){
			System.out.println("regex failed");
			//Invalid input
		}
				
		else if (tokens[0].equals("LOG_ON")){
			
			String result = tokens[1];
			if(result.equals("SUCCESS")){
				
				//if not online
				if(online == false){
					username = tokens[3];
		
					logingui.setVisible(false);
					main = new maingui(this);
					main.setTitle("Main Window - " + username);				
					
				}
			}
			else{
				logingui.setResult("login failed");
				
			}
		}
		
		else if (tokens[0].equals("LOG_OFF")){
			if(tokens[1].equals("SUCCESS")){
				
			}
			else{
				
			}
			//Return to logon window
		}
		
		
		else if (tokens[0].equals("START_CHAT")){
			String result = tokens[1];
			if(result.equals("SUCCESS")){
				//add name to map and create conversation
				
				if (!(nameToConversation.containsKey(tokens[3]))){
					
					nameToConversation.put(tokens[3], new clientConversation(this, tokens[3]));
				}
			}
			else{
				//ask for new name
				//logingui2.setResult("usermame is already used");
			}
		}
		
		

		
		else if (tokens[0].equals("EXIT_CHAT")){
			//Add name to map and create conversation window
			if (nameToConversation.containsKey(tokens[2])){
				nameToConversation.remove(tokens[2]);
			}
			//Remove user from a chat room.
		}
		
		else if (tokens[0].equals("ENTER_CHAT")){
			//Add name to map and create conversation window
			if(tokens[1].equals("SUCCESS")){
				//System.out.println(tokens[3]);
				String newval = tokens[3];
				if (!(nameToConversation.containsKey(newval))){
					nameToConversation.put(newval, new clientConversation(this,newval));
					String[] toarray = activeUsers.toArray(new String[0]);
					nameToConversation.get(newval).updateAllUsers(toarray);

					//nameToConversation.get(newval).updateAllUsers(activeusers.)
				}
			}
			else{
				
				
			}
			
		}
		
		else if (tokens[0].equals("SEND_MESSAGE")){
			if(tokens[1].equals("CONVERSATION_ID")){
				String conversationID =tokens[2];
				String userID = tokens[4];
				String timestamp = input.substring(input.indexOf("<TIME>")+7, input.indexOf(" <TEXT>"));
				//System.out.println("the time is" + timestamp);
				
				String message = input.substring(input.indexOf("<TEXT>") + 7);
				String toadd = addMessage(userID, message, timestamp);
				
				// check already in conversation
				if (nameToConversation.containsKey(conversationID)){
					//add message
					nameToConversation.get(conversationID).updateHistory(toadd);
				}	
			}
			else if(tokens[1].equals("SUCESS")){
			
			}
			else{
				
			}
		}
		
		
		else if(tokens[0].equals("UPDATE_CONVERSATIONS")){
			//gets string of first occurence of conversation id onwards
			//System.out.println("got here update");
			//splits by conversation id
			String[] val = input.split(" CONVERSATION_ID ");
			String[] conv = Arrays.copyOfRange(val, 1, val.length );
			//System.out.println(conv.length);
			for(String item: conv){
				//System.out.println("this point here" +item);
				String val2 = item.split(" ")[0];
				//updates lists of conversations
				//System.out.println(val2);
				conversations.add(val2);
				
				final String[] toarray = conversations.toArray(new String[0]);
				//System.out.println(toarray.length);
				Runnable update = new Runnable(){
					public void run(){
						//System.out.println(toarray.length);
						main.conversationList(toarray);
						//System.out.println("after main");
						}
				};
				SwingUtilities.invokeLater(update);
				
				
				if(nameToConversation.containsKey(val2)){
					//updates conversation user lists
					//System.out.println("this is val2" + val2);
					String[]val3 = item.split(" ");
					String[]array = Arrays.copyOfRange(val3,2, val3.length);
					//System.out.println("THE ARRAY IS OF THIS SIZEEEEE" + array.length);
					nameToConversation.get(val2).updateUsers(array);
				}
			}			
		}
			
		else if(tokens[0].equals("UPDATE_STATUS")){
			
			for(int user = 1; user < tokens.length; user++){
				//System.out.println(tokens[user]);
				activeUsers.add(tokens[user]);

			}
			activeUsers.remove(username);
			final String[] toarray = activeUsers.toArray(new String[0]);
			Runnable update = new Runnable(){
				public void run(){
					//System.out.println("update HISTORY reached for conversation");
					main.userList(toarray);
					}
			};
			SwingUtilities.invokeLater(update);
					
			
			//updates active users with list of strings
			for(String item: nameToConversation.keySet()){
				nameToConversation.get(item).updateAllUsers(toarray);

			
			}
			
		}
		//send invites	
		else if(tokens[0].equals("SEND_INVITE")){
			if(tokens[1].equals("CONVERSATION_ID")){
				String toconv = tokens[2];
				//will only add if not in array. takes care of making sure multiple invites are sent to uoe person
				invites.add(toconv);
				final String[] toarray = invites.toArray(new String[0]);
				Runnable update = new Runnable(){
					public void run(){
						
						main.inviteList(toarray);
						}
				};
				SwingUtilities.invokeLater(update);
			}
			//invites.add(tokens[2]);
		}
		
		
		//pevious chat of conversation
		else if(tokens[0].equals("UPDATE_CONVERSATION_HISTORY")){
			String conversationID = tokens[2];
			//split by user ids
			String[] messages = input.split(" <USER_ID> ");
			//process as if seperate send message
			for(int i = 1; i<messages.length; i++ ){
				String userID = messages[i].split(" ")[0];
				String timestamp = messages[i].substring(messages[i].indexOf("<TIME>")+7, messages[i].indexOf(" <TEXT>"));
				String message = messages[i].substring(messages[i].indexOf("<TEXT>") + 7);
				String toadd = addMessage(userID, message, timestamp);
				
				if (nameToConversation.containsKey(conversationID)){
					//add message
					nameToConversation.get(conversationID).updateHistory(toadd);
				}
			}
			
				
		}
			
		else{
			
		}
		//Should never reach here.
		//throw new UnsupportedOperationException();
		
	}
	

	
	//creates message to be added to jtext area
	public static String addMessage(String userID, String text, String timeStamp){	
		
		
		String result = "<b>" + userID + "</b>" +" <font color=\"gray\">(" + timeStamp + ")</font><b>:</b>   " + text + "<br>";
		
		return result;
	}
	public void removeInvite(String invite){
		invites.remove(invite);
		final String[] toarray = invites.toArray(new String[0]);
		Runnable update = new Runnable(){
			public void run(){
				
				main.inviteList(toarray);
				}
		};
		SwingUtilities.invokeLater(update);
	}
	
	public String gethistory(String conversationID){
		if (historystorage.containsKey(conversationID)){
			return historystorage.get(conversationID);
		}
		else{
			return "no history saved";
		}
		
	}
	
	public void removeInConversation(String conversation){
		nameToConversation.remove(conversation);
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
