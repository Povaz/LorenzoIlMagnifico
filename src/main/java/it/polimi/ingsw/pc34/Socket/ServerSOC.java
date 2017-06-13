package it.polimi.ingsw.pc34.Socket;

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

import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Server;

public class ServerSOC implements Runnable {
	private int port;
	private ServerSocket serverSocket;
	private static ArrayList <SeverLoginUser> utenti = new ArrayList <SeverLoginUser>(); 
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
		
		System.out.println("Server Socket ready");
		System.out.println("");
		
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				utenti.add(new SeverLoginUser(socket));
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
	
		//notifica a tutti i giocatori
		notifyPlayers (username + " joined the lobby!! Now in the lobby there are " + Server.usersInLobby.size() + " players");
		
		if(Server.usersInLobby.size() == 2){
			notifyPlayers("In 10 seconds Game will start!!");
			System.out.println("Timer Started");
            Server.timer = new Timer();
            Server.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Start Game with: " + Server.usersInLobby.size() + "players");
                    notifyPlayers("Game Started!!");
                    clear();
                    return;
                }
            }, 10000);
		}
		
		if(Server.usersInLobby.size()==5){
			notifyPlayers("In 10 seconds Game will start!!");
			Server.timer.cancel();
			Server.timer = new Timer();
			Server.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Start Game with: " + Server.usersInLobby.size() + "players");
                    notifyPlayers("Game Started!!");
                    clear();
                    return;
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
		//MODIFICARE CON PLAYER IN LOBBY (NON POSSO ANCORA FARLO PERCHè LA GESTIONE UTENTI è INCOMPLETA) 
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
			
		}
	}
	
	
	synchronized public static void clear(){
		System.out.println("Creating New Lobby");
		System.out.println("");
		utenti.clear();
		Server.usersInLobby.clear();
		counter=0;
		Server.timer = new Timer();
	}
	
}
