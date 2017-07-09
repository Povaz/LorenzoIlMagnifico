package it.polimi.ingsw.pc34.SocketRMICongiunction;

import it.polimi.ingsw.pc34.Controller.Game;
import it.polimi.ingsw.pc34.Model.Player;
import it.polimi.ingsw.pc34.RMI.ServerRMIImpl;
import it.polimi.ingsw.pc34.Socket.ServerHandler;
import it.polimi.ingsw.pc34.Socket.ServerSOC;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

/**
 * Created by Povaz on 10/06/2017.
 */
public class Server {
    private ServerRMIImpl serverLoginRMI;
    private ServerSOC serverSoc;
    private Timer checkUsers;
    public static List <Game> gamesOnGoing = new ArrayList<>();
    
    public Server (ServerRMIImpl serverLoginRMI, ServerSOC serverSoc) {
        this.serverLoginRMI = serverLoginRMI;
        serverLoginRMI.setServer(this);
        this.serverSoc = serverSoc;
        serverSoc.setServer(this);
    }

    public boolean isDisconnected(String username){
    	for (Game game : gamesOnGoing){
    		List<String> usernames = game.getUsernames();
    		for(String username1 : usernames){
    			if(username1.equals(username)){
    				return game.isDisconnected(username1);
    			}
    		}
    	}
    	return false;
    }
    
    public boolean searchLogged(String username){
    	for (Game game : gamesOnGoing){
    		List<String> usernames = game.getUsernames();
    		for(String username1 : usernames){
    			if(username1.equals(username)){
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    public void disconnectPlayerSoc(String username) throws IOException{
    	for (Game game : gamesOnGoing){
    		List<String> usernames = game.getUsernames();
    		for(String username1 : usernames){
    			if(username1.equals(username)){
    				game.getGameController().deleteServerHandler(username);
    			}
    		}
    	}
    }
    
    public void reconnectedSoc(String username, ServerHandler newHandler) throws IOException{
    	//Game game = reconnected(username);
    	//game.getGameController().addServerHandler(newHandler);
    	//game.getGameController().sendMessageChat(" has reconnected!", username);
    }
    
    public Game reconnected(String username, ClientType clientType, ConnectionType connectionType) throws IOException{
    	for (Game game : gamesOnGoing){
    		List<Player> players = game.getPlayers();
    		for(Player player : players){
    			if(player.getUsername().equals(username)){
    				player.setClientType(clientType);
    				player.setConnectionType(connectionType);
    				player.setDisconnected(false);
    				return game;
    			}
    		}
    	}
		return null;
    }

    public void removeGame (Game game){
    	gamesOnGoing.remove(game);
    }

    private void startServers () throws RemoteException, AlreadyBoundException{
        try {
			System.out.println("Constructing server implementation...");

			System.out.println("Binding Server implementation to registry...");
			Registry registry = LocateRegistry.createRegistry(8000);
			registry.bind("serverRMI", this.serverLoginRMI);

			System.out.println("Waiting for invocations from clients...");

			Thread serverSoc = new Thread(this.serverSoc);
			serverSoc.start();
		} catch (Exception e) {
        	System.out.println("Server is already opened");
		}
    }

    private void checkUsersLogged() throws RemoteException{
    	this.checkUsers = new Timer();
    	this.checkUsers.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					serverLoginRMI.checkUsersLogged();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}, 0, 1000);
	}

    public static void main (String[] args) throws RemoteException, AlreadyBoundException {
        Lobby lobby = new Lobby();
        ServerRMIImpl serverLoginRMI = new ServerRMIImpl(lobby);
        ServerSOC serverSoc = new ServerSOC(1337, lobby);
        Server server = new Server(serverLoginRMI, serverSoc);
        server.startServers();

        server.checkUsersLogged();
    }
}
