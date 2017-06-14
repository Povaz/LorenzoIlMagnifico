package it.polimi.ingsw.pc34.SocketRMICongiunction;

import it.polimi.ingsw.pc34.Model.Game;
import it.polimi.ingsw.pc34.RMI.ServerLoginImpl;
import it.polimi.ingsw.pc34.Socket.ServerSOC;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Created by Povaz on 10/06/2017.
 */
public class Server {
    private ServerLoginImpl serverLoginRMI;
    private ServerSOC serverSoc;
    public static List <Game> gamesOnGoing;
    public static HashMap<String, ConnectionType> usersInGame;
    
    public Server (ServerLoginImpl serverLoginRMI, ServerSOC serverSoc) {
        this.serverLoginRMI = serverLoginRMI;
        this.serverSoc = serverSoc;
        usersInGame = new HashMap<>();
    }

    private void startServers () {
       Thread serverLoginRMI = new Thread(this.serverLoginRMI);
       serverLoginRMI.start();
       Thread serverSoc = new Thread(this.serverSoc);
       serverSoc.start();
    }

    public static void main (String[] args) throws RemoteException {
        Lobby lobby = new Lobby();
        ServerLoginImpl serverLoginRMI = new ServerLoginImpl(lobby);
        ServerSOC serverSoc = new ServerSOC(1337, lobby);
        Server server = new Server(serverLoginRMI , serverSoc);
        server.startServers();
    }
}
