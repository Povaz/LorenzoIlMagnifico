package it.polimi.ingsw.pc34.SocketRMICongiunction;

import it.polimi.ingsw.pc34.Model.Game;
import it.polimi.ingsw.pc34.RMI.ServerLoginImpl;
import it.polimi.ingsw.pc34.Socket.ServerSOC;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

/**
 * Created by Povaz on 10/06/2017.
 */
public class Server {
    private ServerLoginImpl serverLoginRMI;
    private ServerSOC serverSoc;
    public static List <Game> gamesOnGoing = new ArrayList<>();
    
    public Server (ServerLoginImpl serverLoginRMI, ServerSOC serverSoc) {
        this.serverLoginRMI = serverLoginRMI;
        this.serverSoc = serverSoc;
    }

    private void startServers () throws RemoteException, AlreadyBoundException{
        System.out.println("Constructing server implementation...");

        System.out.println("Binding Server implementation to registry...");
        Registry registry = LocateRegistry.createRegistry(8000);
        registry.bind("serverLogin", this.serverLoginRMI);

        System.out.println("Waiting for invocations from clients...");

        Thread serverSoc = new Thread(this.serverSoc);
        serverSoc.start();
    }

    public static void main (String[] args) throws RemoteException, AlreadyBoundException {
        Lobby lobby = new Lobby();
        ServerLoginImpl serverLoginRMI = new ServerLoginImpl(lobby);
        ServerSOC serverSoc = new ServerSOC(1337, lobby);
        Server server = new Server(serverLoginRMI , serverSoc);
        server.startServers();
    }
}
