package it.polimi.ingsw.pcXX.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	private String username;
	private final String ip = "127.0.0.1";
	private final int port = 1337;
	private static Socket socketServer;
	
	public Client() throws UnknownHostException, IOException {
		socketServer = new Socket (ip, port);
	}
	
	public void setName(String username){
		this.username=username;
	}
	
	public void startClient() throws IOException {
		System.out.println("Attesa Inizio Game");
		
		//sempre pronto a ricevere notifiche
		while (true){
			String line = receiveFromServer();
			System.out.println(line);
			if(line.equals("Inizio Game")){
				break;
			}
		}
		return;
	}
	
	public static void sendToServer(String message) throws IOException{
		PrintWriter socketOut = new PrintWriter(socketServer.getOutputStream());
		socketOut.println(message);
		socketOut.flush();
	}
	
	public static String receiveFromServer() throws IOException{
		Scanner socketIn = new Scanner(socketServer.getInputStream());
		String received = socketIn.nextLine();
		socketIn.close();
		return received;
	}
	
	private static String login() throws IOException{
		System.out.println("LOGIN:");
		Scanner in = new Scanner(System.in);
		String username;
		String password;
		
		System.out.println("Username : ");	
		username = in.nextLine();
		sendToServer(username);
		
		while(true){
			
			System.out.println("Password : ");
			password = in.nextLine();
			sendToServer(password);
		
			if(receiveFromServer().equals("confirmed")){
				System.out.println("Bentornato " + username + "!");
				break;
			}
			else if (receiveFromServer().equals("wrong password")){
				System.out.println("Password sbagliata, ritenta!");
			}
			else{
				System.out.println("Nuovo Utente registrato! Benvenuto " + username + "!");
				break;
			}
		}
		in.close();
		return username;
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		//Fa il login
		
		Client client = new Client();
		
		System.out.println("Connection established");
		System.out.println("");
		
		client.setName(login());
		
		try {
			client.startClient();
		}
		catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}