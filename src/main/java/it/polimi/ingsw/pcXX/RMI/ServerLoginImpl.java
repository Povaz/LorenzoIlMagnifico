package it.polimi.ingsw.pcXX.RMI;

import it.polimi.ingsw.pcXX.JSONUtility;
import it.polimi.ingsw.pcXX.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pcXX.SocketRMICongiunction.Server;
import org.json.JSONException;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by Povaz on 24/05/2017.
 **/
public class ServerLoginImpl extends UnicastRemoteObject implements ServerLogin, Runnable {
    public static ArrayList<UserLogin> usersLoggedRMI;

    public ServerLoginImpl () throws RemoteException {
        this.usersLoggedRMI = new ArrayList<>();
    }

    private boolean searchUserLogged (UserLogin userLogin) throws RemoteException {
        Set<String> usernames = Server.usersInLobby.keySet();
        System.out.println("UsersInLobby: " + usernames.toString());
        for (String username : usernames) {
            System.out.println("Username searched: " + username);
            System.out.println("Username in login: " + userLogin.getUsername());
            if ((userLogin.getUsername().equals(username))) {
                return true;
            }
        }
        System.out.println("Fucked up");
        return false;
    }

    @Override
    public boolean loginServer (UserLogin userLogin) throws JSONException, IOException {
        if (!searchUserLogged(userLogin)) {
            if (JSONUtility.checkLogin(userLogin.getUsername(), userLogin.getKeyword())) {
                Server.usersInLobby.put(userLogin.getUsername(), ConnectionType.RMI);
                usersLoggedRMI.add(userLogin);
                userLogin.sendMessage("Login Successful");

                if (Server.usersInLobby.size() == 2) {
                    Server.timer = new Timer();
                    Server.timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            System.out.println("Start Game with: " + Server.usersInLobby.size() + "players");
                        }
                    }, 90000);
                }

                if (Server.usersInLobby.size() == 5) {
                    Server.timer.cancel();
                    System.out.println("Start Game");
                }

                return true;
            }
            userLogin.sendMessage("Incorrect Username or password");
            return false;
        }
        userLogin.sendMessage("User already logged");
        return false;
    }

    @Override
    public void registrationServer (UserLogin userLogin) throws JSONException, IOException {
        if (JSONUtility.checkRegister(userLogin.getUsername(), userLogin.getKeyword())) {
            userLogin.sendMessage("Registration Successful");
        }
        else {
            userLogin.sendMessage("Registration Failed: Username already taken");
        }
    }

    @Override
    public boolean logoutServer (UserLogin userLogin) throws RemoteException {
        Set<String> usernames = Server.usersInLobby.keySet();
        for (String user : usernames) {
            if ((userLogin.getUsername().equals(user))) {
                Server.usersInLobby.remove(user, ConnectionType.RMI);
                usersLoggedRMI.remove(userLogin);
                userLogin.sendMessage("Logout successful");

                if (Server.usersInLobby.size() == 1) {
                    System.out.println(userLogin.getUsername() + "left the room");
                    Server.timer.cancel();
                }

                return false;
            }
        }
        userLogin.sendMessage("Logout Failed");
        return true;
    }

    @Override
    public void printLoggedUsers () throws RemoteException{
        Set<String> users = Server.usersInLobby.keySet();
        for (String user: users) {
            System.out.println(user);
        }
    }

    public void run (){
        try {
            System.out.println("Constructing server implementation...");

            System.out.println("Binding Server implementation to registry...");
            Registry registry = LocateRegistry.createRegistry(8000);
            registry.bind("serverLogin", this);

            System.out.println("Waiting for invocations from clients...");
        }
        catch(RemoteException e) {
            e.printStackTrace();
        }
        catch(AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
