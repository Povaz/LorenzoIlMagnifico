package it.polimi.ingsw.pc34.RMI;


import it.polimi.ingsw.pc34.Controller.ActionInput;
import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.NotificationType;

import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;
import it.polimi.ingsw.pc34.View.TerminalInput;
import org.json.JSONException;

import javax.swing.*;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by Povaz on 24/05/2017.
 **/
public class ServerLoginImpl extends UnicastRemoteObject implements ServerLogin {
    private ArrayList<UserLogin> usersLoggedRMI;
    private Lobby lobby;

    private String currentPlayer;
    private boolean doAction;

    public ServerLoginImpl (Lobby lobby) throws RemoteException {
        this.usersLoggedRMI = new ArrayList<>();
        this.lobby = lobby;
        this.lobby.setServerRMI(this);
        this.currentPlayer = "null";
        this.doAction = false;
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
                this.usersLoggedRMI.add(userLogin);

                lobby.getUsers().put(userLogin.getUsername(), ConnectionType.RMI);
                userLogin.sendMessage("Login Successful");
                lobby.notifyAllUsers(NotificationType.USERLOGIN, userLogin.getUsername());

                if (lobby.getUsers().size() == 2) {
                	userLogin.sendMessage("Timer Started");
                    lobby.inizializeTimer();
                    lobby.startTimer();
                }

                if (lobby.getUsers().size() == 5) {
                    lobby.stopTimer();
                    System.out.println("Start Game"); //TODO CREARE TIMER PERSONALIZZABILE PER FARLO SCATTARE IMMEDIATAMENTE
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
                usersLoggedRMI.remove(userLogin);

                lobby.removeUser(user);

                lobby.notifyAllUsers(NotificationType.USERLOGOUT, userLogin.getUsername());
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
    public void printLoggedUsers () throws RemoteException{ // TODO DA ELIMINARE ALLA FINE
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

    public boolean checkCurrentPlayer (UserLogin userLogin) throws RemoteException {
        if (userLogin.getUsername().equals(currentPlayer)) {
            return true;
        }
        else {
            return false;
        }
    }


    @Override
    public void checkAction(UserLogin userLogin) throws RemoteException {
        if (this.checkCurrentPlayer(userLogin)) {
            this.doAction = true;
        }
        else {
            userLogin.sendMessage("Non è il tuo turno, colione.");
        }
    }


    public ActionInput askAction (int playerNumber, String username) throws RemoteException { //TODO PROVA
        this.currentPlayer = username;
        this.doAction = false;
        while(!doAction){}

        int choose = -1;
        for (UserLogin user: usersLoggedRMI) {
            if (user.getUsername().equals(username)) {
                choose = user.chooseAction();
            }
        }
        ActionInput actionInput = TerminalInput.chooseAction(playerNumber, choose);
        this.doAction = false;
        return actionInput;
    }
}
