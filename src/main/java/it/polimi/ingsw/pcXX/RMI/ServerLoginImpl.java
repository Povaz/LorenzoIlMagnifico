package it.polimi.ingsw.pcXX.RMI;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by Povaz on 24/05/2017.
 */
public class ServerLoginImpl extends UnicastRemoteObject implements ServerLogin {
    private ArrayList<UserLoginImpl> usersLogged;

    public ServerLoginImpl () throws RemoteException {
        usersLogged = new ArrayList<UserLoginImpl>();
    }

    public synchronized void controlUser (UserLoginImpl userLoginImpl) throws RemoteException {
        for (UserLoginImpl user : usersLogged) {
            if (userLoginImpl.getUsername() == user.getUsername()) {
                if (userLoginImpl.getPassword() == user.getPassword()) {
                    userLoginImpl.setResponse(true);
                    notify();
                } else {
                    userLoginImpl.setResponse(false);
                    notify();
                }
            }
        }
        userLoginImpl.setResponse(false);
        notify();
    }

    public synchronized void saveUser (UserLoginImpl userLoginImpl) throws RemoteException {
        for (UserLoginImpl user: usersLogged) {
            if (userLoginImpl.getUsername() == user.getUsername()) {
                userLoginImpl.setResponse(false);
                notify();
            }
        }
        usersLogged.add(userLoginImpl);
        userLoginImpl.setResponse(true);
        notify();
    }

    public void printUsers () {
        for (UserLoginImpl user: usersLogged) {
            System.out.println("Username: " + user.getUsername());
            System.out.println("Password: " + user.getPassword() + "\n");
        }
    }

    public static void main (String[] args) throws RemoteException, AlreadyBoundException {
        System.out.println("Constructing server implementation...");
        ServerLoginImpl serverLogin = new ServerLoginImpl();

        System.out.println("Binding Server implementation to registry...");
        Registry registry = LocateRegistry.getRegistry();
        registry.bind("serverLogin", serverLogin);

        System.out.println("Waiting for invocations from clients...");
    }
}
