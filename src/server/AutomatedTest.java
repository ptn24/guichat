package server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner; 
import java.io.*; 

import org.junit.Test;

/**
 * Login Testing:
 * Test: User sends LOG_ON command but the UserID does not conform to the grammar 
 * Outcome: User receives an error message
 * Test: Multiple users log on 
 * Outcome: Users are successfully added to user list 
 * 
 * Logout Testing: 
 * Test: User attempts to create a conversation after logging out
 * Outcome: Error message
 * Test: User attempts to invite another user to conversation after logging out 
 * Outcome:  Error message
 * Test: User attempts to accept invite after logging out 
 * Outcome: Error message
 * Test: User attempts to invite a logged out user
 * Outcome: Error message
 * Test: User logs out of system, but was in a specific conversation with other users before the user had logged out, then other users send messages to this specific conversation
 * Outcome: User should not receive those messages 
 * 
 * Create Conversation Testing:
 * Test: User creates a single conversation
 * Outcome: Conversation is created and user joins 
 * Test: User creates multiple conversations
 * Outcome: Multiple conversations are created and are all considered active
 * Test: User creates conversation without adhering to the conversation grammar 
 * Outcome: Error message
 * 
 * Invite to Conversation Testing:
 * Test: User invites another user to a conversation (user accepts)
 * Outcome: receiving user receives invite message and joins the conversation (after accepting)
 * Test: User invites other user to conversation, then leaves the conversation
 * Outcome: Invited user should be able to join the conversation 
 * Test: Many invites to a single conversation are sent, all invited users accept
 * Outcome: All invited users join the conversation
 * Test: Many invites to a single conversation are sent, all invited users decline 
 * Outcome: All invited users do not join the conversation
 * Test: Many invites to a single conversation are sent, some invited users accept 
 * Outcome: Some invited users join the conversation
 * Test: User is invited multiple times to the same conversation
 * Outcome: User only receives a single message; there is no error
 * Test: User is invited to many conversations
 * Outcome: User receives an invite message for each conversation (goes into queue until the user handles the invites)
 * Test: Nonexistent user (userID) is invited to a conversation
 * Outcome: Error message
 * 
 * Leave Conversation Testing:
 * Test: Single user is in the conversation. User then leaves. 
 * Outcome: Conversation is empty 
 * Test: User attempts to leave a conversation that they are not part of. 
 * Outcome: Error message
 * Test: Multiple users are in the conversation. A single users leaves the conversation.
 * Outcome: The user leaves the conversation. They no longer see that person in the conversation once the person has left.
 *
 * Message Testing:
 * Test: Single user sends message to conversation.. 
 * Outcome: User receives the message.
 * Test: User sends message to conversation with multiple users
 * Outcome: All users receive the message
 * Test: User attempts to send message to conversation that the user is not in
 * Outcome: Error message
 * Test: User attempts to send a message to a conversation that the user has been invited to but has not accepted
 * Outcome: Error message
 * Test: User attempts to send a message to a conversation that the user had been invited to but rejected
 * Outcome:  Error message
 * @author Peter
 *
 */
public class AutomatedTest {
    
    //LOGIN TESTING
    
	/**
	 * Test: A single user logs into the system.
	 * Outcome: The user receives confirmation he has logged in successfully.
	 */
    @Test 
    public void logOnSingleUserServerTest() throws IOException{    	
    	Socket client = new Socket(InetAddress.getByName("localhost"), 4444);
    	BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    	PrintWriter out = new PrintWriter(client.getOutputStream(), true);
    	out.println("LOG_ON USER_ID PTN24");
    	
    	String expectedOutput = "LOG_ON SUCCESS USER_ID PTN24";
    	String output = in.readLine();
    	assertTrue(expectedOutput.equals(output));    	
    	
    	/*
		for (String line = in.readLine(); line != null; line = in.readLine()) {
			System.out.println(line);
		}
		*/
    }
    
    /**
     * Test: User logs into the system and other users are already logged on (multiple users log on).
     * Outcome: User successfully logs on .
     * @throws IOException
     */
    @Test
    public void logOnMultipleUsersServerTest() throws IOException{
    	Socket client1 = new Socket(InetAddress.getByName("localhost"), 4444);
    	BufferedReader in1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));
    	PrintWriter out1 = new PrintWriter(client1.getOutputStream(), true);
    	out1.println("LOG_ON USER_ID PTN24");
    	
    	Socket client2 = new Socket(InetAddress.getByName("localhost"), 4444);
    	BufferedReader in2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));
    	PrintWriter out2 = new PrintWriter(client2.getOutputStream(), true);
    	out2.println("LOG_ON USER_ID DSSUN");
    	
    	String expectedOutput = "LOG_ON SUCCESS USER_ID DSSUN";
    	String output = in2.readLine();
    	assertTrue(expectedOutput.equals(output)); 
    }
    
    /**
     * Test: User attempts to log in, but userID is already taken.
     * Outcome: User receives error message.
     * @throws IOException
     */
    @Test
    public void logOnUserIDTakenTest() throws IOException{
    	Socket client = new Socket(InetAddress.getByName("localhost"), 4444);
    	BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    	PrintWriter out = new PrintWriter(client.getOutputStream(), true);
    	out.println("LOG_ON USER_ID PTN24");
    	
    	Socket client2 = new Socket(InetAddress.getByName("localhost"), 4444);
    	BufferedReader in2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));
    	PrintWriter out2 = new PrintWriter(client2.getOutputStream(), true);
    	out2.println("LOG_ON USER_ID PTN24");
    	
    	String expectedOutput = "LOG_ON FAIL USERNAME PTN24 ALREADY EXISTS";
    	String output = in2.readLine();
    	assertTrue(expectedOutput.equals(output));    
    }
	
    /*
    //User tries to log into the system but the userID is already taken 
	@Test
	public void logOnFailServerTest() throws IOException{
		int port = 5552;
		Socket socket = new Socket();
		String request = "LOG_ON <USER_ID> ptn24";		
		Server server = new Server(port);
		server.addUser("ptn24");
		String expectedOutput = "LOG_ON FAIL";
		String output = server.handleRequest(request, socket);
		assertTrue(expectedOutput.equals(output));
		server.close();
	}
	
    //User tries to log into the system, has invalid characters in userID
    @Test
    public void logOnInvalidCharsFailServerTest1() throws IOException{
        int port = 5551;
        Socket socket = new Socket();
        //Socket socket1 = new Socket();
        String request = "LOG_ON <USER_ID> adaya#";
        //String request1 = "LOG_ON <USER_ID> ptn24";
        Server server = new Server(port);
        //server.addUser("ptn24");
        //String expectedOutput = "LOG_ON SUCCESS <USER_ID>";
        String expectedOutput1 = "INVALID INPUT";
        String output = server.handleRequest(request, socket);
        //System.out.println(output);
        //String output1 = server.handleRequest(request1, socket);
        assertTrue(output.contains(expectedOutput1));
        server.close();
    }
	
	//Invalid LOG_ON client to server grammar
	@Test
	public void invalidLogOnServerTest() throws IOException{
		int port = 5550;
		Socket socket = new Socket();
		String request = "LOG_ON USER_ID";
		String request2 = "LOG_ON";
		Server server = new Server(port);
		
		String expectedOutput = "INVALID INPUT";
		String output = server.handleRequest(request, socket);
		String output2 = server.handleRequest(request2, socket);
		
		assertEquals(expectedOutput, output);
		assertEquals(expectedOutput, output2);
		server.close();
	}
	
	
	//Multiple users log on 
    @Test
    public void multipleUsersLogIn() throws IOException{
        int port = 5548;
        Socket socket = new Socket();
        Socket socket1 = new Socket(); 
        Server server = new Server(port);
        String request = "LOG_ON <USER_ID> ptn24";  
        String request1 = "LOG_ON <USER_ID> adaya";  
        String expectedOutput = "LOG_ON SUCCESS <USER_ID>";
        String output = server.handleRequest(request, socket);
        String output1 = server.handleRequest(request1, socket1);
        assertTrue(output.contains(expectedOutput));
        assertTrue(output1.contains(expectedOutput));
        server.close();
    }
    
    //LOGOUT TESTS 
    
    //Invalid logoff 
    @Test
    public void invalidLogOffServerTest() throws IOException{
        int port = 5549;
        Socket socket = new Socket();
        String request = "LOG_OFF";
        Server server = new Server(port);
        
        String expectedOutput = "LOG_OFF FAIL";
        String output = server.handleRequest(request, socket);
        
        assertTrue(expectedOutput.equals(output));
        server.close();
    }
    
    //Single user logs off 
    @Test
    public void userLogsOff() throws IOException{
        int port = 5547;
        Socket socket = new Socket();
        Server server = new Server(port); 
        String request1 = "LOG_OFF"; 
        String expectedOutput1 = "LOG_OFF";
        server.addUser("adaya");
        String output1 = server.handleRequest(request1, socket);
        assertTrue(output1.contains(expectedOutput1));
        server.close();
    }
    
    //User logs off then attempts to create a conversation 
    @Test
    public void userLogsOffThenCreatesConversation() throws IOException{
        int port = 5500;
        Socket socket = new Socket();
        Server server = new Server(port); 
        String request = "LOG_OFF"; 
        String request1 = "START_CHAT CONVERSATION_ID hello";
        String expectedOutput = "LOG_OFF";
        String expectedOutput1 = "START_CHAT FAIL";
        server.addUser("adaya");
        String output = server.handleRequest(request, socket);
        String output1 = server.handleRequest(request1, socket);
        assertTrue(output.contains(expectedOutput));
        assertTrue(output1.contains(expectedOutput1));
        server.close();
    }
    
    //User attempts to invite a user to a chat after logging out 
    @Test
    public void userLogsOffThenAttemptstoInvite() throws IOException{
        int port = 5500;
        Socket socket = new Socket();
        Server server = new Server(port); 
        String request = "LOG_OFF"; 
        String request1 = "SEND_INVITE CONVERSATION_ID hello <USER_ID> peter";
        String expectedOutput = "LOG_OFF";
        String expectedOutput1 = "SEND_INVITE FAIL";
        server.addUser("adaya");
        server.addUser("peter");
        String output = server.handleRequest(request, socket);
        String output1 = server.handleRequest(request1, socket);
        assertTrue(output.contains(expectedOutput));
        assertTrue(output1.contains(expectedOutput1));
        server.close();
    }
    
    //User attempts to invite a logged out user 
    @Test
    public void userInvitesLoggedOutUser() throws IOException{
        int port = 5500;
        Socket socket = new Socket();
        Server server = new Server(port);
        String request = "SEND_INVITE CONVERSATION_ID hello <USER_ID> peter";
        server.addUser("adaya"); 
        String output = server.handleRequest(request, socket);
        String expectedOutput1 = "SEND_INVITE FAIL";
        assertTrue(output.contains(expectedOutput1));
        server.close();
    }
	
    //CREATE CHAT ROOM TESTING + LEAVE CHAT ROOM TESTING 
    
    //Test making a chat room without adhering to the chat name specifications (incorrect characters) 
    @Test
    public void invalidChatServerTest() throws IOException{
        int port = 5546; 
        Socket socket = new Socket(); 
        Server server = new Server(port);
        String expectedOutput = "INVALID INPUT";
        String output = server.handleRequest("START_CHAT CONVERSATION_ID $$$chat", socket);
        assertTrue(output.contains("INVALID INPUT"));
        server.close();
    }
    //Create a chat room 
    @Test
    public void createChatRoom() throws IOException{
        int port = 5400; 
        Socket socket = new Socket(); 
        Server server = new Server(port);
        String expectedOutput = "START_CHAT SUCCESS";
        server.socketToUserID.put(socket, "adaya");
        String output = server.handleRequest("START_CHAT CONVERSATION_ID chat", socket);
        assertTrue(output.contains(expectedOutput));
        server.close();
    }
    //Create multiple chat rooms
    @Test
    public void createMultipleChatRoom() throws IOException{
        int port = 5400; 
        Socket socket = new Socket(); 
        Server server = new Server(port);
        String expectedOutput = "START_CHAT SUCCESS";
        server.socketToUserID.put(socket, "adaya");
        String output = server.handleRequest("START_CHAT CONVERSATION_ID chat", socket);
        String output1 = server.handleRequest("START_CHAT CONVERSATION_ID chat2", socket);
        assertTrue(output.contains(expectedOutput));
        assertTrue(output1.contains(expectedOutput));
        server.close();
    }
    
    //User creates conversation
    @Test 
    public void userCreatesConversation() throws IOException{
        int port = 5544;
        Socket socket = new Socket();
        String request = "START_CHAT CONVERSATION_ID hello";      
        Server server = new Server(port);
        server.socketToUserID.put(socket, "hi");
        String expectedOutput = "START_CHAT SUCCESS";
        String output = server.handleRequest(request, socket);
        assertTrue(output.contains(expectedOutput));
        server.close();
    }
    
    //Single user exits conversation without ever having entered it 
    @Test
    public void invalidExitChatServerTest() throws IOException{
        int port = 5545; 
        Socket socket = new Socket(); 
        Server server = new Server(port);
        String expectedOutput = "EXIT_CHAT FAIL";
        String output = Server.handleRequest("EXIT_CHAT CONVERSATION_ID myChat", socket);
        assertTrue(output.contains(expectedOutput));
        server.close();
    }
    
    //A single user leaves the conversation
    @Test
    public void userLeavesConversation() throws IOException{
        int port = 5400; 
        Socket socket = new Socket(); 
        Server server = new Server(port);
        Conversation whatsup = new Conversation("whatsup", "caharris");
        server.conversations.add(whatsup);
        String expectedOutput = "EXIT_CHAT SUCCESS";
        server.socketToUserID.put(socket, "caharris");
        String output = server.handleRequest("EXIT_CHAT CONVERSATION_ID whatsup", socket);
        assertTrue(output.contains(expectedOutput));
        server.close();
    }
    
    //User leaves the conversation
    @Test
    public void leaveConversation() throws IOException{
        int port = 5100;
        Socket adayasocket = new Socket();
        Socket petersocket = new Socket(); 
        Socket corysocket = new Socket();      
        Server server = new Server(port);
        server.socketToUserID.put(petersocket, "peter");
        server.socketToUserID.put(adayasocket, "adaya");
        server.socketToUserID.put(corysocket, "cory");
        Conversation hello = new Conversation("hello", "adaya"); 
        server.conversations.add(hello);
        hello.addUser("peter");
        hello.addUser("cory");
        String request = "SEND_MESSAGE CONVERSATION_ID hello <TEXT> HI";
        String request1 = "EXIT_CHAT hello";
        String output = server.handleRequest(request, adayasocket);
        String output1 = server.handleRequest(request, petersocket);
        String output2 = server.handleRequest(request1, adayasocket);
        assertTrue(output.contains("SUCCESS"));
        assertTrue(output1.contains("SUCCESS"));
        assertTrue(output.contains("HI"));
        assertTrue(output1.contains("HI"));
        List users = new ArrayList(); 
        users.add(hello.getUsers());
        boolean chat_does_contain_adaya = users.contains("adaya"); 
        assertTrue(!chat_does_contain_adaya);
        server.close();
    }

	 
    //INVITE TESTING
    
    //User sends invite to non-existent user 
    @Test 
    public void userAcceptsInvite() throws IOException{
        int port = 5543;
        Socket socket = new Socket();
        String request = "SEND_INVITE CONVERSATION_ID hello <USER_ID> peter";      
        Server server = new Server(port);
        server.socketToUserID.put(socket, "ptn24");
        Conversation hello = new Conversation("hello", "hi"); 
        server.conversations.add(hello);
        String expectedOutput = "SEND_INVITE FAIL";
        String output = server.handleRequest(request, socket);
        assertTrue(output.contains(expectedOutput));
        server.close();
    }
    //User invites another user to a conversation and the user accepts 
    @Test 
    public void usertoUserAcceptsInvite() throws IOException{
        int port = 5300;
        Socket adayasocket = new Socket();
        Socket petersocket = new Socket(); 
        String request = "SEND_INVITE CONVERSATION_ID hello <USER_ID> peter";      
        Server server = new Server(port);
        server.socketToUserID.put(petersocket, "peter");
        server.socketToUserID.put(adayasocket, "adaya");
        Conversation hello = new Conversation("hello", "adaya"); 
        server.conversations.add(hello);
        String expectedOutput = "SEND_INVITE SUCCESS";
        String request1 = "ENTER_CHAT CONVERSATION_ID hello";
        String output = server.handleRequest(request, adayasocket);
        String output1 = server.handleRequest(request1, petersocket);
        assertTrue(output.contains(expectedOutput));
        assertTrue(output1.contains("ENTER_CHAT SUCCESS"));
        server.close();
    }
    //User invites other user to conversation, then the original user leaves the conversation
    @Test
    public void usertoUserAcceptsInviteLeaves() throws IOException{
        int port = 5300;
        Socket adayasocket = new Socket();
        Socket petersocket = new Socket(); 
        String request = "SEND_INVITE CONVERSATION_ID hello <USER_ID> peter";      
        Server server = new Server(port);
        server.socketToUserID.put(petersocket, "peter");
        server.socketToUserID.put(adayasocket, "adaya");
        Conversation hello = new Conversation("hello", "adaya"); 
        server.conversations.add(hello);
        String expectedOutput = "SEND_INVITE SUCCESS";
        String request1 = "ENTER_CHAT CONVERSATION_ID hello";
        String request2 = "EXIT_CHAT CONVERSATION_ID hello";
        String output = server.handleRequest(request, adayasocket);
        String output1 = server.handleRequest(request1, petersocket);
        String output2 = server.handleRequest(request2, adayasocket);
        assertTrue(output.contains(expectedOutput));
        assertTrue(output1.contains("ENTER_CHAT SUCCESS"));
        assertTrue(output2.contains("EXIT_CHAT SUCCESS"));
        server.close();
    }
    
    //Many users are invited to the same conversation and accept
    @Test
    public void manyInvites() throws IOException{
        int port = 5299;
        Socket adayasocket = new Socket();
        Socket petersocket = new Socket(); 
        Socket corysocket = new Socket();
        String request0 = "SEND_INVITE CONVERSATION_ID hello <USER_ID> peter"; 
        String request = "SEND_INVITE CONVERSATION_ID hello <USER_ID> cory";      
        Server server = new Server(port);
        server.socketToUserID.put(petersocket, "peter");
        server.socketToUserID.put(adayasocket, "adaya");
        server.socketToUserID.put(corysocket, "cory");
        Conversation hello = new Conversation("hello", "adaya"); 
        server.conversations.add(hello);
        String expectedOutput = "SEND_INVITE SUCCESS";
        String request1 = "ENTER_CHAT CONVERSATION_ID hello";
        String output = server.handleRequest(request0, adayasocket);
        String output1 = server.handleRequest(request, adayasocket);
        String output2 = server.handleRequest(request1, petersocket);
        String output3 = server.handleRequest(request1, corysocket);
        assertTrue(output.contains(expectedOutput));
        assertTrue(output1.contains(expectedOutput));
        assertTrue(output2.contains("ENTER_CHAT SUCCESS"));
        assertTrue(output3.contains("ENTER_CHAT SUCCESS"));
        server.close();
    }
    
    //Many users are invited to the same conversation and all decline
    @Test
    public void manyInvitesbutDecline() throws IOException{
        int port = 5298;
        Socket adayasocket = new Socket();
        Socket petersocket = new Socket(); 
        Socket corysocket = new Socket();
        String request0 = "SEND_INVITE CONVERSATION_ID hello <USER_ID> peter"; 
        String request = "SEND_INVITE CONVERSATION_ID hello <USER_ID> cory";      
        Server server = new Server(port);
        server.socketToUserID.put(petersocket, "peter");
        server.socketToUserID.put(adayasocket, "adaya");
        server.socketToUserID.put(corysocket, "cory");
        Conversation hello = new Conversation("hello", "adaya"); 
        server.conversations.add(hello);
        String expectedOutput = "SEND_INVITE SUCCESS";
        String request1 = "ENTER_CHAT CONVERSATION_ID hello";
        String output = server.handleRequest(request0, adayasocket);
        String output1 = server.handleRequest(request, adayasocket);
        String output2 = server.handleRequest(request1, petersocket);
        assertTrue(output.contains(expectedOutput));
        assertTrue(output1.contains(expectedOutput));
        List users = new ArrayList(); 
        users.add(hello.getUsers());
        boolean chat_does_contain_cory_and_peter = users.contains("cory") && users.contains("peter"); 
        assertTrue(!chat_does_contain_cory_and_peter);
        server.close();
    }
    
    //Many users are invited to the same conversation and all decline
    @Test
    public void manyInvitesSomeDecline() throws IOException{
        int port = 5298;
        Socket adayasocket = new Socket();
        Socket petersocket = new Socket(); 
        Socket corysocket = new Socket();
        String request0 = "SEND_INVITE CONVERSATION_ID hello <USER_ID> peter"; 
        String request = "SEND_INVITE CONVERSATION_ID hello <USER_ID> cory";      
        Server server = new Server(port);
        server.socketToUserID.put(petersocket, "peter");
        server.socketToUserID.put(adayasocket, "adaya");
        server.socketToUserID.put(corysocket, "cory");
        Conversation hello = new Conversation("hello", "adaya"); 
        server.conversations.add(hello);
        String expectedOutput = "SEND_INVITE SUCCESS";
        String request1 = "ENTER_CHAT CONVERSATION_ID hello";
        String output = server.handleRequest(request0, adayasocket);
        String output1 = server.handleRequest(request, adayasocket);
        assertTrue(output.contains(expectedOutput));
        assertTrue(output1.contains(expectedOutput));
        List users = new ArrayList(); 
        users.add(hello.getUsers());
        boolean chat_does_contain_cory_and_peter = users.contains("cory") && users.contains("peter"); 
        assertTrue(!chat_does_contain_cory_and_peter);
        server.close();
    }
    
    //A single user gets invited many times to the same conversation
    @Test
    public void invitedManyTimesSameConversation() throws IOException{
        int port = 5297;
        Socket adayasocket = new Socket();
        Socket petersocket = new Socket(); 
        Socket corysocket = new Socket(); 
        String request = "SEND_INVITE CONVERSATION_ID hello <USER_ID> cory";      
        Server server = new Server(port);
        server.socketToUserID.put(petersocket, "peter");
        server.socketToUserID.put(adayasocket, "adaya");
        server.socketToUserID.put(corysocket, "cory");
        Conversation hello = new Conversation("hello", "adaya"); 
        hello.addUser("peter");
        server.conversations.add(hello);
        String expectedOutput = "SEND_INVITE SUCCESS";
        String request1 = "ENTER_CHAT CONVERSATION_ID hello";
        String output = server.handleRequest(request, adayasocket);
        String output1 = server.handleRequest(request, petersocket);
        assertTrue(output.contains(expectedOutput));
        assertTrue(output1.contains(expectedOutput));
        server.close();
    }
    
    //Invited to many conversations
    @Test
    public void userInvitedtoManyConversations() throws IOException{
        int port = 5297;
        Socket adayasocket = new Socket();
        Socket petersocket = new Socket(); 
        Socket corysocket = new Socket(); 
        String request0 = "SEND_INVITE CONVERSATION_ID hello2 <USER_ID> cory";
        String request = "SEND_INVITE CONVERSATION_ID hello <USER_ID> cory";      
        Server server = new Server(port);
        server.socketToUserID.put(petersocket, "peter");
        server.socketToUserID.put(adayasocket, "adaya");
        server.socketToUserID.put(corysocket, "cory");
        Conversation hello = new Conversation("hello", "adaya"); 
        Conversation hello2 = new Conversation("hello2", "peter"); 
        server.conversations.add(hello);
        server.conversations.add(hello2);
        String expectedOutput = "SEND_INVITE SUCCESS";
        String request1 = "ENTER_CHAT CONVERSATION_ID hello";
        String output = server.handleRequest(request, adayasocket);
        String output1 = server.handleRequest(request0, petersocket);
        assertTrue(output.contains(expectedOutput));
        assertTrue(output1.contains(expectedOutput));
        server.close();
    }
    
    //MESSAGE TESTING 
    //send message to conversation, everyone receives the message
    @Test
    public void sendMessagetoConversation() throws IOException{
        int port = 5296;
        Socket adayasocket = new Socket();
        Socket petersocket = new Socket(); 
        Socket corysocket = new Socket();      
        Server server = new Server(port);
        server.socketToUserID.put(petersocket, "peter");
        server.socketToUserID.put(adayasocket, "adaya");
        server.socketToUserID.put(corysocket, "cory");
        Conversation hello = new Conversation("hello", "adaya"); 
        server.conversations.add(hello);
        hello.addUser("peter");
        hello.addUser("cory");
        String request = "SEND_MESSAGE CONVERSATION_ID hello <TEXT> HI";
        String output = server.handleRequest(request, adayasocket);
        String output1 = server.handleRequest(request, petersocket);
        assertTrue(output.contains("SUCCESS"));
        assertTrue(output1.contains("SUCCESS"));
        assertTrue(output.contains("HI"));
        assertTrue(output1.contains("HI"));
        List users = new ArrayList(); 
        users.add(hello.getUsers());
        boolean chat_does_contain_cory_and_peter = users.contains("cory") && users.contains("peter"); 
        assertTrue(!chat_does_contain_cory_and_peter);
        server.close();
    }
    
    //User receives message
    @Test
    public void sendMessagetoConversationSingleUser() throws IOException{
        int port = 5295;
        Server server = new Server(port);
        Socket adayasocket = new Socket();
        server.socketToUserID.put(adayasocket, "adaya");
        Conversation hello = new Conversation("hello", "adaya"); 
        server.conversations.add(hello);
        String request = "SEND_MESSAGE CONVERSATION_ID hello <TEXT> HI";
        String output = server.handleRequest(request, adayasocket);
        assertTrue(output.contains("SUCCESS"));
        assertTrue(output.contains("HI"));
        server.close();
    }
    
    //User attempts to send a message to a conversation that they are not in
    @Test
    public void sendMessagetoConversationNotIn() throws IOException{
        int port = 5294;
        Server server = new Server(port);
        Socket adayasocket = new Socket();
        Socket petersocket = new Socket();
        server.socketToUserID.put(adayasocket, "adaya");
        server.socketToUserID.put(petersocket, "peter");
        Conversation hello = new Conversation("hello", "adaya"); 
        Conversation sour = new Conversation("sour", "peter"); 
        server.conversations.add(hello);
        String request = "SEND_MESSAGE CONVERSATION_ID sour <TEXT> HI";
        String output = server.handleRequest(request, adayasocket);
        assertTrue(output.contains("FAIL")); //detects that it is sent to conversation adaya is not in
        assertTrue(!output.contains("HI")); //does not see the message
        server.close();
    }
    
    //User attempts to send a message to a conversation that the user has been invited to but has not accepted
    @Test
    public void sendMessagetoUserInvitedNotAccepted() throws IOException{
        int port = 5293;
        Socket adayasocket = new Socket();
        Socket petersocket = new Socket(); 
        Socket corysocket = new Socket(); 
        String request0 = "SEND_INVITE CONVERSATION_ID hello2 <USER_ID> adaya";  
        Server server = new Server(port);
        server.socketToUserID.put(petersocket, "peter");
        server.socketToUserID.put(adayasocket, "adaya");
        server.socketToUserID.put(corysocket, "cory");
        Conversation hello = new Conversation("hello", "adaya"); 
        Conversation hello2 = new Conversation("hello2", "peter"); 
        server.conversations.add(hello);
        server.conversations.add(hello2);
        String request = "SEND_MESSAGE CONVERSATION_ID hello2 <TEXT> HI";
        String output1 = server.handleRequest(request0, petersocket);
        String output2 = server.handleRequest(request, adayasocket);
        assertTrue(output1.contains("SUCCESS"));
        assertTrue(output2.contains("SEND_MESSAGE FAIL"));
        server.close();
    }
    
    //User attempts to send a message to a conversation that the user had been invited to but rejected
    @Test
    public void sendMessagetoInvitedButRejected() throws IOException{
        int port = 5292;
        Socket adayasocket = new Socket();
        Socket petersocket = new Socket(); 
        Socket corysocket = new Socket(); 
        String request0 = "SEND_INVITE CONVERSATION_ID hello2 <USER_ID> adaya";  
        Server server = new Server(port);
        server.socketToUserID.put(petersocket, "peter");
        server.socketToUserID.put(adayasocket, "adaya");
        server.socketToUserID.put(corysocket, "cory");
        Conversation hello = new Conversation("hello", "adaya"); 
        Conversation hello2 = new Conversation("hello2", "peter"); 
        server.conversations.add(hello);
        server.conversations.add(hello2);
        String request = "SEND_MESSAGE CONVERSATION_ID hello2 <TEXT> HI";
        String output1 = server.handleRequest(request0, petersocket);
        String output2 = server.handleRequest(request, adayasocket);
        assertTrue(output1.contains("SUCCESS"));
        assertTrue(output2.contains("SEND_MESSAGE FAIL"));
        server.close();
    }
    */
    
}
