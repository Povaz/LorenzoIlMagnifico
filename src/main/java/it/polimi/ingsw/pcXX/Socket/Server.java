package it.polimi.ingsw.pcXX.Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private int port;
	private ServerSocket serverSocket;
	private ArrayList <Person> utenti = new ArrayList <Person>(); 
	private int counter = 0;
	
	public Server(int port) {
		this.port = port;
	}
	
	
	public void startServer() {
		
		ExecutorService executor = Executors.newCachedThreadPool();
		
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
		} 
		catch (IOException e) {
			System.err.println(e.getMessage()); // porta non disponibile
			return;
		}
		
		System.out.println("Server ready");
		
		CreateGameHandler newGame = new CreateGameHandler(); 
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				utenti.add(new Person(socket, newGame));
				executor.submit(utenti.get(counter));
				counter++;
			} 
			catch(IOException e) {
				break; // entrerei qui se serverSocket venisse chiuso
			}
		}
	}
		
	
	public static void main(String[] args) {
		Server server = new Server(1337);
		server.startServer();
	}
	
}
