package it.polimi.ingsw.pcXX.RMI;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Povaz on 24/05/2017.
 */
public class UserLoginImpl extends UnicastRemoteObject implements  UserLogin{
    private String username;
    private String password;

    public UserLoginImpl(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static void main (String[] args) throws RemoteException, NotBoundException{
        Registry registry = LocateRegistry.getRegistry();


    }
}
