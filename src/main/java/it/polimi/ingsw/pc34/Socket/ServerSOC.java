package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.polimi.ingsw.pc34.Controller.Game;
import it.polimi.ingsw.pc34.Controller.GameController;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientInfo;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;
import it.polimi.ingsw.pc34.SocketRMICongiunction.NotificationType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Server;

public class ServerSOC implements Runnable {
	private int port;
	private ServerSocket serverSocket;
	private static ArrayList <ServerHandler> users;
	private static ArrayList <ServerHandler> usersInLobby; 
	private Server server;
	private Lobby lobby;
	private ExecutorService executor = Executors.newCachedThreadPool();
	
	public ServerSOC(int port, Lobby lobby) {
		this.port = port;
		ServerSOC.users = new ArrayList <>();
		ServerSOC.usersInLobby = new ArrayList <>();
		this.lobby = lobby;
        this.lobby.setServerSOCKET(this);
	}
	
	//set and get
	public Server getServer () {
		return this.server;
	}
	
	
	public void setServer(Server server){
		this.server = server;
	}
	
	
	//giving an username return his linked ServerHandler
	public ServerHandler getServerHandler (String username){
		for(ServerHandler serverHandler : users){
			if ((serverHandler.getName()).equals(username)){
				return serverHandler;
			}
		}
		return null;
	}
	
	//return all users of the Socket Server
	public ArrayList<ServerHandler> getUsers(){
		return users;
	}
	
	//set the Gamecontroller related to a specific ServerHandler(when enter in a game from a lobby) and move user from lobby's arrylist to ingame's arraylist
	public static void setGameControllerSoc(GameController gameController){
		for (ServerHandler user : usersInLobby){
			user.setGameController(gameController);
		}
		return;
	}
	
	//move elements of usersInLobby in users(entrano in quelli in game)
	public synchronized void fromLobbytoInGame(){
		for (ServerHandler user : usersInLobby){
			users.add(user);
		}
		usersInLobby.clear();
		return;
	}
	
	//call when a player login 
	synchronized public void addPlayer (ServerHandler last, String username) throws RemoteException {
		
		//crea nuovo utente nella Lobby
		ClientType clientType = null;
		if(last.getGraphicType().equals("1")){
			clientType=ClientType.CLI;
		}
		else{
			clientType=ClientType.GUI;
		}
		ClientInfo clientInfo = new ClientInfo(ConnectionType.SOCKET, clientType);
		
		lobby.getUsers().put(username, clientInfo);
	
		//notifica a tutti i giocatori
		lobby.notifyAllUsers(NotificationType.USERLOGIN, last.getName());
		
		//add user in arraylist of players in lobby
		usersInLobby.add(last);
		
		if(lobby.getUsers().size() == 2){
			lobby.notifyAllUsers(NotificationType.TIMERSTARTED, "");
			lobby.inizializeTimer();
            lobby.startTimer();
		}
		
		if(lobby.getUsers().size() == 5){
			lobby.stopTimer();
			lobby.inizializeTimer();
			last.setSendGUI(true);
			last.sendToClient("Login successful");
            lobby.startGameImmediately();
		}
		
	}

	//call when a player reconnect
	synchronized public void reconnect (String username, ServerHandler newHandler) throws IOException{
		Game game = null;
		if(newHandler.getGraphicType().equals("1")){
			game = server.reconnected(username, ClientType.CLI, ConnectionType.SOCKET);
		}
		else{
			game = server.reconnected(username, ClientType.CLI, ConnectionType.SOCKET);
		}
		game.getGameController().addServerHandler(newHandler);
		game.getGameController().sendMessageChat(" has reconnected!", username);
	}
	
	//remove a player from the list if disconnected
	synchronized public void removePlayer (String username){
		for (ServerHandler user: users) {
            if(username.equals(user.getName())){
            	users.remove(user);
            	return;
            }
        }
	}
	
	synchronized public void removePlayerLobby (ServerHandler user){
		usersInLobby.remove(user);
	}
	
	//notify players in lobby
	synchronized public void notifySOCPlayers (String message){
		for (ServerHandler user: usersInLobby) {
            user.sendToClient(message);
        }
	}		

	//setFase for ServerHandler so they reach the Game's Handler
	public void throwInGame(){
		for(ServerHandler user : usersInLobby){
			user.setFase(1);
		}
	}
	
	public void checkUsersLogged(){
		ArrayList <ServerHandler> toRemove = new ArrayList<>();
		for (ServerHandler user : usersInLobby){
			boolean notConnected = user.isNotConnected();
			if(notConnected){
				toRemove.add(user);
			}
		}
		for(ServerHandler userToRemove : toRemove){
			lobby.removeUser(userToRemove.getName());
			removePlayerLobby(userToRemove);
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
		
		//always ready to accept Socket's client
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				ServerHandler last = new ServerHandler(socket, lobby, this); 
				executor.submit(last);
			} 
			catch(IOException e) {
				break; // entrerei qui se serverSocket venisse chiuso
			}
		}
	}
}
