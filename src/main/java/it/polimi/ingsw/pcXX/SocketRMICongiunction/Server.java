package it.polimi.ingsw.pcXX.SocketRMICongiunction;

import it.polimi.ingsw.pcXX.RMI.ServerLoginImpl;
import it.polimi.ingsw.pcXX.Socket.ServerSOC;

import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Timer;

/**
 * Created by Povaz on 10/06/2017.
 */
public class Server {
    private ServerLoginImpl serverLoginRMI;
    private ServerSOC serverSoc;
    public static HashMap<String, ConnectionType> usersInGame;
    public static HashMap<String, ConnectionType> usersInLobby;
    public static Timer timer;
    
    public Server (ServerLoginImpl serverLoginRMI , ServerSOC serverSoc) {
        this.serverLoginRMI = serverLoginRMI;
        this.serverSoc = serverSoc;
        usersInGame = new HashMap<String, ConnectionType>();
        usersInLobby = new HashMap<String, ConnectionType>();
    }

    private void startServers () {
       Thread serverLoginRMI = new Thread(this.serverLoginRMI);
       serverLoginRMI.start();
       Thread serverSoc = new Thread(this.serverSoc);
       serverSoc.start();
    }

    public static void main (String[] args) throws RemoteException {
        ServerLoginImpl serverLoginRMI = new ServerLoginImpl();
        ServerSOC serverSoc = new ServerSOC(1337);
        Server server = new Server(serverLoginRMI , serverSoc);
        server.startServers();
    }
}
