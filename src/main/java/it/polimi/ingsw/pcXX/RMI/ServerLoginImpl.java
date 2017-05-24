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
    private ArrayList<UserLogin> usersLogged;

    public ServerLoginImpl () throws RemoteException {
        usersLogged = new ArrayList<UserLogin>();
    }

    @Override
    public boolean controlUser (UserLogin userLogin) throws RemoteException {
        for (UserLogin user : usersLogged) {
            if (userLogin.getUsername() == user.getUsername()) {
                if (userLogin.getPassword() == user.getPassword()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean saveUser (UserLogin userLogin) throws RemoteException {
        for (UserLogin user: usersLogged) {
            if (userLogin.getUsername() == user.getUsername()) {
                return false;
            }
        }
        usersLogged.add(userLogin);
        return true;
    }

    @Override
    public void printUsers () throws RemoteException{
        for (UserLogin user: usersLogged) {
            System.out.println("Username: " + user.getUsername());
            System.out.println("Password: " + user.getPassword() + "\n");
        }
    }

    @Override
    public boolean deleteUser (UserLogin userLogin) throws RemoteException {
        for (UserLogin user : usersLogged) {
            if (userLogin.getUsername() == user.getUsername()) {
                if (userLogin.getPassword() == user.getPassword()) {
                    usersLogged.remove(user);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public static void main (String[] args) throws RemoteException, AlreadyBoundException {
        System.out.println("Constructing server implementation...");
        ServerLoginImpl serverLoginImpl = new ServerLoginImpl();

        System.out.println("Binding Server implementation to registry...");
        Registry registry = LocateRegistry.getRegistry();
        registry.bind("serverLogin", serverLoginImpl);

        System.out.println("Waiting for invocations from clients...");
    }
}
