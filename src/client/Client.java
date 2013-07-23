package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import gui.LoginFrame;

/**
 * GUI chat client runner.
 * @author Peter
 *
 */
public class Client implements Runnable{
	//private final LoginFrame loginFrame;
	
	public Client(){
		//TODO: implement
		LoginFrame.create(this);
	}
	
	
	public String handleLogOn(String ip, int port, String username){
		try {
			final Socket socket = new Socket(InetAddress.getByName(ip), port);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			out.println("LOG_ON USER_ID " + username);
			
			String line = "";
			while(line.equals("")){
				line = in.readLine();
			}
			
			if(line.equals("LOG_ON USER_ID " + username)){
				System.out.print("Success!");
				
				//Create a thread to handle the client's connection to the server.
				Thread thread = new Thread(new Runnable(){
					public void run(){
						try {
							handleConnection(socket);
						} 
						
						catch (IOException e) {
							e.printStackTrace();
							//TODO: implement.
						}
					}
				});
				thread.start();
			}
			
			return line;
		} 
		
		catch (UnknownHostException e) {
			return null;
		} 
		
		catch (IOException e) {
			return null;
		}
	}
	
	private void handleConnection(Socket socket) throws IOException{
		System.out.print("handling connection...");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		try{
			for(String line = in.readLine(); line != null; line = in.readLine()){
				//TODO:implement
			}
		}
		
		finally{
			
		}
		//TODO: implement.
	}
	
	public void run(){
		//TODO: implement
	}
	
	public static void main(String[] args){
		System.out.print("Running client...");
		Client client = new Client();
		//LoginFrame.create(client);
		
		//new Thread(client).start();
	}
	
}
