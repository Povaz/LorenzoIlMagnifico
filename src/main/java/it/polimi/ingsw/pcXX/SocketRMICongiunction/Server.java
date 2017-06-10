package it.polimi.ingsw.pcXX.SocketRMICongiunction;

import it.polimi.ingsw.pcXX.RMI.ServerLoginImpl;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Timer;

/**
 * Created by Povaz on 10/06/2017.
 */
public class Server {
    private ServerLoginImpl serverLoginRMI;
    // private ServerSocket
    public static HashMap<String, ConnectionType> usersInGame;
    public static HashMap<String, ConnectionType> usersInLobby;
    public static Timer timer;

    public Server (ServerLoginImpl serverLoginRMI /*ServerSocket,*/) {
        this.serverLoginRMI = serverLoginRMI;
        /* ServerSocket*/
        usersInGame = new HashMap<String, ConnectionType>();
        usersInLobby = new HashMap<String, ConnectionType>();
    }

    private void startServers () {
       Thread serverLoginRMI = new Thread(this.serverLoginRMI);
       serverLoginRMI.start();
    }

    public static void main (String[] args) throws RemoteException {
        ServerLoginImpl serverLoginRMI = new ServerLoginImpl();
        //ServerSocket
        Server server = new Server(serverLoginRMI /*, serverSocket */);
        server.startServers();
    }
}
