package it.polimi.ingsw.pcXX.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private String name;
	private String ip;
	private int port;
	
	public Client(String name, String ip, int port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
	}
	
	public void startClient() throws IOException {
		Socket socket = new Socket(ip, port);
		System.out.println("Connection established");
		System.out.println("Attesa inizio partita");
		PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
		socketOut.println(name);
		socketOut.flush();
		
		
		//sempre pronto a ricevere notifiche
		Scanner notify = new Scanner(socket.getInputStream());
		while (true){
			String line = notify.nextLine();
			System.out.println(line);
		}
	}	
	
	public static void main(String[] args) {
		//chiede nome e inizializza
		Scanner in = new Scanner(System.in);
		System.out.println("nome User?");
		String name = in.nextLine();
		Client client = new Client(name, "127.0.0.1", 1337);
		try {
			client.startClient();
		}
		catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}