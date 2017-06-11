package it.polimi.ingsw.pcXX.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.polimi.ingsw.pcXX.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pcXX.SocketRMICongiunction.Server;

public class ServerSOC implements Runnable {
	private int port;
	private ServerSocket serverSocket;
	private static ArrayList <LoginUser> utenti = new ArrayList <LoginUser>(); 
	private static int counter = 0;
	
	public ServerSOC(int port) {
		this.port = port;
	}
	
	public void run() {
		
		ExecutorService executor = Executors.newCachedThreadPool();
		
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("Server ready");
		System.out.println("");
		
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				utenti.add(new LoginUser(socket));
				executor.submit(utenti.get(counter));
				counter++;
			} 
			catch(IOException e) {
				break; // entrerei qui se serverSocket venisse chiuso
			}
		}
	}
	
	synchronized public static void addPlayer(String username, Socket socket) {
		
		//crea nuovo utente
		Server.usersInLobby.put(username, ConnectionType.SOCKET);
		
		if(Server.usersInLobby.size() == 2){
			System.out.println("Partito Timer");
            Server.timer = new Timer();
            Server.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Start Game with: " + Server.usersInLobby.size() + "players");
                    notifyPlayers("Game Iniziato!!");
                }
            }, 10000);
		}
		
		//notifica a tutti i giocatori
		notifyPlayers (username + " si è aggiunto alla partita!! Ora sono presenti " + Server.usersInLobby.size() + " giocatori");
		
		if(Server.usersInLobby.size()==5){
			Server.timer.cancel();
			Server.timer = new Timer();
			Server.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Start Game with: " + Server.usersInLobby.size() + "players");
                    notifyPlayers("Game Iniziato!!");
                }
            }, 10000);
		}
		
		//
	}
	
	
	synchronized public static void notifyPlayers (String message){
		System.out.println("INVIO NOTIFICA : " + message);
		System.out.println("");
		Socket instance;
		PrintWriter out = null;
		for(int i=0;i<utenti.size();i++){
			instance = ((utenti.get(i)).getSocket());
			try {
				out = new PrintWriter(instance.getOutputStream(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			out.println(message);
			out.flush();
			Scanner socketIn = null;
			try {
				socketIn = new Scanner(instance.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			String confirm = socketIn.nextLine();
		}
	}
	
	
	synchronized public static void clear(){
		utenti.clear();
		counter=0;
		Server.timer = new Timer();
	}
	
}
