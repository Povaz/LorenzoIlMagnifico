package it.polimi.ingsw.pcXX.RMI;

import it.polimi.ingsw.pcXX.JSONUtility;
import org.json.JSONException;

import java.io.IOException;
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
        usersLogged = new ArrayList<>();
    }

    @Override
    public boolean controlUser (UserLogin userLogin) throws RemoteException, JSONException, IOException {
        if (JSONUtility.checkLogin(userLogin.getUsername(), userLogin.getPassword())) {
            usersLogged.add(userLogin);
            return true;
        }
        return false;
    }

    @Override
    public boolean saveUser (UserLogin userLogin) throws RemoteException, JSONException, IOException {
        return JSONUtility.checkRegister(userLogin.getUsername(), userLogin.getPassword());
    }

    @Override
    public boolean logoutUser(UserLogin userLogin) throws RemoteException {
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
