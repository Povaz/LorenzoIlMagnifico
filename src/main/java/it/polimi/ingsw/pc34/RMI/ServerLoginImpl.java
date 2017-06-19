package it.polimi.ingsw.pc34.RMI;


import it.polimi.ingsw.pc34.Controller.ActionInput;
import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.Model.ActionType;
import it.polimi.ingsw.pc34.Model.FamilyColor;
import it.polimi.ingsw.pc34.Model.GameController;
import it.polimi.ingsw.pc34.Model.Player;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.NotificationType;

import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Server;
import org.json.JSONException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by Povaz on 24/05/2017.
 **/
public class ServerLoginImpl extends UnicastRemoteObject implements ServerLogin{
    private ArrayList<UserLogin> usersLoggedRMI;
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
    public boolean logoutServer (UserLogin userLogin) throws RemoteException {  //TODO IMPLEMENTAZIONE NUOVA
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

    // FINE GESTIONE LOBBY


    public void notifyRMIPlayers (String m) throws RemoteException {
        for (UserLogin user: usersLoggedRMI) {
            user.sendMessage(m);
        }
    }

    //INIZIO GESTIONE GAME

    @Override
    public void sendInput (String input, UserLogin userLogin) throws RemoteException{
        //TODO COLLEGAMENTO AL GAMEFLOW
        /*switch (input) {
            case "/playTurn":
                userLogin.sendMessage("Not Implemented yet");
                break;
            case "/chat":
                userLogin.sendMessage("Not implemented yet");
                break;
            case "/stampinfo":
                userLogin.sendMessage("Not implemented yet");
                break;
            default: userLogin.sendMessage("Command Unknown");
        }*/
    }

    public void sendMessage(Player player, String message) throws RemoteException {
        for(int i = 0; i < usersLoggedRMI.size(); i++) {
            if (player.getUsername().equals(usersLoggedRMI.get(i).getUsername())) {
                usersLoggedRMI.get(i).sendMessage(message);
            }
        }
    }
}
