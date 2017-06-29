package it.polimi.ingsw.pc34.RMI;


import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.Controller.GameController;
import it.polimi.ingsw.pc34.Model.Player;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.NotificationType;

import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Server;
import org.json.JSONException;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;
import java.util.*;

/**
 * Created by Povaz on 24/05/2017.
 **/
public class ServerRMIImpl extends UnicastRemoteObject implements ServerRMI, Unreferenced {
    private HashMap<UserRMI, String> usersLoggedRMI;
    private Lobby lobby;

    public ServerRMIImpl(Lobby lobby) throws RemoteException {
        this.usersLoggedRMI = new HashMap<>();
        this.lobby = lobby;
        this.lobby.setServerRMI(this);
    }

    @Override
    public void unreferenced() {

    }

    private boolean searchUserLogged (UserRMI userRMI) throws RemoteException {
        Set<String> usernames = lobby.getUsers().keySet();
        for (String username : usernames) {
            if ((userRMI.getUsername().equals(username))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean loginServer (UserRMI userRMI) throws JSONException, IOException {
        if (!searchUserLogged(userRMI)) {
            if (JSONUtility.checkLogin(userRMI.getUsername(), userRMI.getKeyword())) {
                this.usersLoggedRMI.put(userRMI, null);

                lobby.getUsers().put(userRMI.getUsername(), ConnectionType.RMI);
                userRMI.sendMessage("Login Successful");
                lobby.notifyAllUsers(NotificationType.USERLOGIN, userRMI.getUsername());

                if (lobby.getUsers().size() == 2) {
                	userRMI.sendMessage("Timer Started");
                    lobby.inizializeTimer();
                    lobby.startTimer();
                }

                if (lobby.getUsers().size() == 5) {
                    lobby.stopTimer();
                    System.out.println("Start Game"); //TODO CREARE TIMER PERSONALIZZABILE PER FARLO SCATTARE IMMEDIATAMENTE
                }

                return true;
            }
            userRMI.sendMessage("Incorrect Username or password");
            return false;
        }
        userRMI.sendMessage("User already logged");
        return false;
    }

    @Override
    public void registrationServer (UserRMI userRMI) throws JSONException, IOException {
        if (JSONUtility.checkRegister(userRMI.getUsername(), userRMI.getKeyword())) {
            userRMI.sendMessage("Registration Successful");
        }
        else {
            userRMI.sendMessage("Registration Failed: Username already taken");
        }
    }

    @Override
    public boolean logoutServer (UserRMI userRMI) throws RemoteException {  //TODO IMPLEMENTAZIONE NUOVA
        Set<String> usernames = lobby.getUsers().keySet();
        for (String user : usernames) {
            if ((userRMI.getUsername().equals(user))) {
                usersLoggedRMI.remove(userRMI);

                lobby.removeUser(user);

                lobby.notifyAllUsers(NotificationType.USERLOGOUT, userRMI.getUsername());
                userRMI.sendMessage("Logout successful");

                if (lobby.getUsers().size() == 1) {
                    System.out.println(userRMI.getUsername() + "left the room");
                    lobby.stopTimer();
                }

                return false;
            }
        }
        userRMI.sendMessage("Logout Failed");
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
        for (UserRMI user: usersLoggedRMI.keySet()) {
            user.sendMessage(m);
        }
    }

    //INIZIO GESTIONE GAME

    public GameController searchGame (UserRMI userRMI) throws RemoteException {
        for (int i = 0; i < Server.gamesOnGoing.size(); i++) {
            for (int j = 0; j < Server.gamesOnGoing.get(i).getPlayers().size(); j++) {
                if (userRMI.getUsername().equals(Server.gamesOnGoing.get(i).getPlayers().get(j).getUsername())) {
                    return Server.gamesOnGoing.get(i).getGameController();
                }
            }
        }
        return null;
    }

    public void throwInGame (Set<String> users) throws IOException {
        Set<UserRMI> usersRMI = usersLoggedRMI.keySet();
        for(String user : users) {
            for (UserRMI userRMI : usersRMI) {
                if (user.equals(userRMI.getUsername())) {
                    userRMI.startGameHandler();
                }
            }
        }
    }

    @Override
    public void sendInput (String input, UserRMI userRMI) throws IOException {
            GameController gameController = null;
            try {
                gameController = searchGame(userRMI);
                try {
                    for (Map.Entry<UserRMI, String> entry : usersLoggedRMI.entrySet()) {
                        UserRMI currentUser = entry.getKey();
                        if (userRMI.getUsername().equals(entry.getKey().getUsername())) {
                            if (entry.getValue() == null) {
                                switch (input) {
                                    case "/playturn":
                                        userRMI.sendMessage(gameController.flow(input, entry.getKey().getUsername()));
                                        entry.setValue(input);
                                        break;
                                    case "/chat":
                                        userRMI.sendMessage("Insert a Message: ");
                                        gameController.sendMessageChat(input);
                                        entry.setValue(input);
                                        break;
                                    case "/stampinfo":
                                        userRMI.sendMessage("Not implemented yet, man, eccheccazzo");
                                        break;
                                    default:
                                        userRMI.sendMessage("Command Unknown");
                                }
                            } else {
                                switch (entry.getValue()) {
                                    case "/playturn":
                                        userRMI.sendMessage(gameController.flow(input, entry.getKey().getUsername()));
                                        break;
                                    case "/chat":
                                        entry.setValue(null);
                                        gameController.sendMessageChat(input);
                                        userRMI.sendMessage("Type: /playturn for an action; /chat to send message; /stampinfo to stamp info");
                                        break;
                                    case "/vaticansupport":
                                        userRMI.sendMessage(gameController.flow(input, entry.getKey().getUsername()));
                                        break;
                                    default:
                                        userRMI.sendMessage("Wrong state game. How did you do it? Explain it.");
                                        break;
                                }
                            }
                        }
                    }
                } catch (ServerException e) {
                    e.printStackTrace();
                }
            } catch (NullPointerException e) {
                userRMI.sendMessage("The Game isn't started yet");
            }
    }

    public void sendMessage(Player player, String message) throws RemoteException {
        for (Map.Entry<UserRMI, String> entry: usersLoggedRMI.entrySet()) {
            if (player.getUsername().equals(entry.getKey().getUsername())) {
                if (message.equals("Action has been executed")) {
                    entry.setValue(null);
                }
                entry.getKey().sendMessage(message);
            }
        }
    }
}
