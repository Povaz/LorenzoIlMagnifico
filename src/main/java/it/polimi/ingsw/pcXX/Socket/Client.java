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
	
	
	public void setName(String username){
		this.username=username;
	}
	
	synchronized public void startClient() throws IOException {
		System.out.println("Attesa Inizio Game . . .");
		System.out.println("");
		
		//sempre pronto a ricevere notifiche
		String line;
		synchronized(this){while (true){
			line = receiveFromServer();
			System.out.println(line);
			System.out.println("");
			if(line.equals("Game Iniziato")){
				break;
			}
		}}
		return;
	}
	
	synchronized public static void sendToServer(String message) throws IOException{
		PrintWriter socketOut = new PrintWriter(socketServer.getOutputStream(), true);
		socketOut.println(message);
		socketOut.flush();
	}
	
	synchronized public static String receiveFromServer() throws IOException{
		Scanner socketIn = new Scanner(socketServer.getInputStream());
		String received = socketIn.nextLine();
		PrintWriter socketOut = new PrintWriter(socketServer.getOutputStream(), true);
		socketOut.println("ok");
		socketOut.flush();
		return received;
		
	}
	
	synchronized private static String login() throws IOException{
		String decision;
	    Scanner in = new Scanner(System.in);
		while(true){
			System.out.println("Inserisci numero: \r 1-login \r 2-register");
			decision = in.nextLine();
			//prima era qua l'invio al server
			if(decision.equals("1")){
				System.out.println("LOGIN:");
				break;
			}
			else if(decision.equals("2")){
				System.out.println("REGISTER:");
				break;
			}
			System.out.println("Inserimento errato: riprova");
		}
		sendToServer(decision);
		
		String username;
		String password;
		
		while(true){
			
			System.out.println("Username : ");	
			username = in.nextLine();
			sendToServer(username);		
			System.out.println("Password : ");
			password = in.nextLine();
			sendToServer(password);
			String receivedRespPass=receiveFromServer();
			System.out.println("");
			System.out.println(receivedRespPass);
			System.out.println("");
			
			if(receivedRespPass.equals("confirmed")){
				if(decision.equals("1")){
					System.out.println("Accesso effettuato! Bentornato " + username + "!");
					System.out.println("");
				}
				else{
					System.out.println("Registrazione effettuata! Benvenuto " + username + "!");
					System.out.println("");
				}
				break;
			}
			else if (receivedRespPass.equals("wrong combination")){
				if(decision.equals("1")){
					System.out.println("Combinazione errata, riprova");
					System.out.println("");
				}
				else{
					System.out.println("Registrazione fallita! Ritenta");
					System.out.println("");
				}
			}
		}
		in.close();
		return username;
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		//Fa il login
		
		Client client = new Client();
		Client.socketServer = new Socket (client.ip, client.port);
		
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