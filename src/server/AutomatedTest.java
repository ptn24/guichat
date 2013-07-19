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
    	
    	String expectedOutput = "LOG_ON USER_ID PTN24";
    	String output = in.readLine();
    	assertTrue(expectedOutput.equals(output)); 
    	
    	client.close();
    	
    	/*
		for (String line = in.readLine(); line != null; line = in.readLine()) {
			System.out.println(line);
		}
		*/
    }
    
    /**
     * Test: User logs into the system with a unique ID with multiple users already logged on.
     * Outcome: User successfully logs on .
     * @throws IOException
     */
    @Test
    public void logOnMultipleUsersServerTest() throws IOException{
    	Socket client1 = new Socket(InetAddress.getByName("localhost"), 4444);
    	PrintWriter out1 = new PrintWriter(client1.getOutputStream(), true);
    	out1.println("LOG_ON USER_ID PTN24");
    	
    	Socket client2 = new Socket(InetAddress.getByName("localhost"), 4444);
    	BufferedReader in2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));
    	PrintWriter out2 = new PrintWriter(client2.getOutputStream(), true);
    	out2.println("LOG_ON USER_ID DSSUN");
    	
    	String expectedOutput = "LOG_ON USER_ID DSSUN";
    	String output = in2.readLine();
    	assertTrue(expectedOutput.equals(output));
    	
    	client1.close();
    	client2.close();
    }    
}
