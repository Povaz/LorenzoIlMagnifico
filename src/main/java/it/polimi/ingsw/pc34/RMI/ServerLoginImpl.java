package it.polimi.ingsw.pc34.RMI;

<<<<<<< Updated upstream:src/main/java/it/polimi/ingsw/pc34/RMI/ServerLoginImpl.java
import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.NotificationType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Server;
=======
import it.polimi.ingsw.pcXX.JSONUtility;
import it.polimi.ingsw.pcXX.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pcXX.SocketRMICongiunction.Lobby;
import it.polimi.ingsw.pcXX.SocketRMICongiunction.NotificationType;
import it.polimi.ingsw.pcXX.SocketRMICongiunction.Server;
>>>>>>> Stashed changes:src/main/java/it/polimi/ingsw/pcXX/RMI/ServerLoginImpl.java
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
    private Lobby lobby;

    public ServerLoginImpl (Lobby lobby) throws RemoteException {
        this.usersLoggedRMI = new ArrayList<>();
        this.lobby = lobby;
        this.lobby.setServerRMI(this);
    }

    private boolean searchUserLogged (UserLogin userLogin) throws RemoteException {
        Set<String> usernames = lobby.getUsers().keySet();
        for (String username : usernames) {
            if ((userLogin.getUsername().equals(username))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean loginServer (UserLogin userLogin) throws JSONException, IOException {
        if (!searchUserLogged(userLogin)) {
            if (JSONUtility.checkLogin(userLogin.getUsername(), userLogin.getKeyword())) {
                lobby.getUsers().put(userLogin.getUsername(), ConnectionType.RMI);
                userLogin.sendMessage("Login Successful");
                lobby.notifyAllUsers(NotificationType.USERLOGIN);
                if (lobby.getUsers().size() == 2) {
                	userLogin.sendMessage("Timer Started");
                    lobby.inizializeTimer();
                    lobby.startTimer();
                }

                if (lobby.getUsers().size() == 5) {
                    lobby.stopTimer();
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
        Set<String> usernames = lobby.getUsers().keySet();
        for (String user : usernames) {
            if ((userLogin.getUsername().equals(user))) {
                lobby.removeUser(user);
                usersLoggedRMI.remove(userLogin);
                userLogin.sendMessage("Logout successful");
                if (lobby.getUsers().size() == 1) {
                    System.out.println(userLogin.getUsername() + "left the room");
                    lobby.stopTimer();
                }

                return false;
            }
        }
        userLogin.sendMessage("Logout Failed");
        return true;
    }

    @Override
    public void printLoggedUsers () throws RemoteException{ // DA ELIMINARE ALLA FINE
        Set<String> users = lobby.getUsers().keySet();
        for (String user: users) {
            System.out.println(user);
        }
    }

    public void notifyRMIPlayers (String m) throws RemoteException {
        for (UserLogin user: usersLoggedRMI) {
            user.sendMessage(m);
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
