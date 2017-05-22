package Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private int port;
	private ServerSocket serverSocket;
	
	public Server(int port) {
		this.port = port;
	}
	
	
	public void startServer() {
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
				newGame.createPlayer(socket);
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
