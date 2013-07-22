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
	private final LoginFrame loginFrame;
	
	public Client(){
		//TODO: implement
		this.loginFrame = new LoginFrame(this);
		loginFrame.setVisible(true);
	}
	
	public int login(String ip, int port, String username){
		try {
			Socket socket = new Socket(InetAddress.getByName(ip), port);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			out.println("LOG_ON USER_ID " + username);
			
			String line = "";
			while(line.equals("")){
				line = in.readLine();
			}
			System.out.print(line);
			System.out.print("Connected to server...");
			return 0;
		} 
		
		catch (UnknownHostException e) {
			return -1;
		} 
		
		catch (IOException e) {
			return -1;
		}
	}
	
	private void handleConnection(Socket socket) throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
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
