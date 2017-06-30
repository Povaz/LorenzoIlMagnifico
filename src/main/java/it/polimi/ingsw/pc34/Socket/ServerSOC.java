package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.polimi.ingsw.pc34.Controller.GameController;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;
import it.polimi.ingsw.pc34.SocketRMICongiunction.NotificationType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Server;

public class ServerSOC implements Runnable {
	private int port;
	private ServerSocket serverSocket;
	private static ArrayList <ServerHandler> utenti;
	//private static ArrayList <ServerLoginUser> utenti; 
	private static int counter;
	private Server server;
	private Lobby lobby;
	private ExecutorService executor = Executors.newCachedThreadPool();
	
	public ServerSOC(int port, Lobby lobby) {
		this.port = port;
		this.utenti = new ArrayList <>();
        this.lobby = lobby;
        this.lobby.setServerSOCKET(this);
        this.counter = 0;
	}
	
	public Server getServer () {
		return this.server;
	}
	
	public void setServer(Server server){
		this.server = server;
	}
	
	public ServerHandler getServerHandler (String username){
		for(ServerHandler serverHandler : utenti){
			if ((serverHandler.getName()).equals(username)){
				return serverHandler;
			}
		}
		return null;
	}
	
	public ArrayList<ServerHandler> getUsers(){
		return utenti;
	}

	public static void setGameControllerSoc(GameController gameController){
		for (ServerHandler user : utenti){
			user.setGameController(gameController);
		}
	}
	
	synchronized public void addPlayer (ServerHandler last, String username) throws RemoteException {
		
		//crea nuovo utente nella Lobby
		lobby.getUsers().put(username, ConnectionType.SOCKET);
	
		//notifica a tutti i giocatori
		lobby.notifyAllUsers(NotificationType.USERLOGIN, last.getName());
		
		utenti.add(last);
		
		if(lobby.getUsers().size() == 2){
			lobby.notifyAllUsers(NotificationType.TIMERSTARTED, "");
			System.out.println("Timer Started");
			lobby.inizializeTimer();
            lobby.startTimer();
		}
		
		if(lobby.getUsers().size()==5){
			lobby.stopTimer();
			lobby.notifyAllUsers(NotificationType.TIMERSTARTED, "");
            System.out.println("Start Game");
		}
		
	}

	synchronized public void reconnect (String username, ServerHandler newHandler) throws IOException{
		server.reconnectedSoc(username, newHandler);
	}
	
	synchronized public void removePlayer (String username){
		for (ServerHandler user: utenti) {
            if(username.equals(user.getName())){
            	utenti.remove(user);
            	counter--;
            	return;
            }
        }
	}
	
	synchronized public void notifySOCPlayers (String message){
		System.out.println("INVIO NOTIFICA : " + message);
		System.out.println("");
		Socket instance;
		PrintWriter out = null;
		for (ServerHandler user: utenti) {
            instance = (user).getSocket();
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
		counter=0;
	}
	
	public void throwInGame(){
		for(ServerHandler user : utenti){
			user.setFase(1);
		}
	}
	
	public void run() {
		
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("Server Socket ready");
		System.out.println("");
		
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				ServerHandler last = new ServerHandler(socket, lobby, this); 
				executor.submit(last);
				counter++;
			} 
			catch(IOException e) {
				break; // entrerei qui se serverSocket venisse chiuso
			}
		}
	}
}
