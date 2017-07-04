package it.polimi.ingsw.pc34.RMI;


import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.Controller.GameController;
import it.polimi.ingsw.pc34.Model.Player;
import it.polimi.ingsw.pc34.SocketRMICongiunction.*;

import org.json.JSONException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by Povaz on 24/05/2017.
 **/
public class ServerRMIImpl extends UnicastRemoteObject implements ServerRMI { //TODO CONTROLLARE BENE CHE IL CLIENT POSSA CRASHARE SEMPRE
    private ArrayList<UserRMI> usersLoggedRMI;
    private ArrayList<String> usernames;
    private Server server;
    private Lobby lobby;

    public ServerRMIImpl(Lobby lobby) throws RemoteException {
        this.usersLoggedRMI = new ArrayList<>();
        this.usernames = new ArrayList<>();
        this.lobby = lobby;
        this.lobby.setServerRMI(this);
    }

    public void setServer (Server server) {
        this.server = server;
    }

    private boolean searchUserLobby(UserRMI userRMI) throws RemoteException {
        Set<String> usernames = lobby.getUsers().keySet();
        for (String username : usernames) {
            if ((userRMI.getUsername().equals(username))) {
                return true;
            }
        }
        return false;
    }

    public void checkUsersLogged () throws RemoteException {
        ArrayList<String> usersRMI = lobby.getRMIUsers();
        HashMap<String, Integer> usersDisconnected = new HashMap<>();
        for (String user : usersRMI) {
            for (int i = 0; i < usernames.size(); i++) {
                try {
                    if (usernames.get(i).equals(user)) {
                        usersLoggedRMI.get(i).sendMessage("Are you still there?");
                    }
                } catch (RemoteException e) {
                    usersDisconnected.put(user, i);
                }
            }
        }
        for (Map.Entry<String, Integer> entry : usersDisconnected.entrySet()) {
            System.out.println(entry.getKey() + " was disconnected with force");
            forcedLogoutServer(entry.getKey(), entry.getValue());
        }
    }


    public void forcedLogoutServer (String username, int i) throws RemoteException {
        System.out.println("ForcedLogoutServer Entered \n");
        lobby.removeUser(username);
        usersLoggedRMI.remove(i);
        usernames.remove(i);
        lobby.notifyAllUsers(NotificationType.USERLOGOUT, username);

        System.out.println(lobby.getUsers().toString() + "\n");
    }

    @Override
    public boolean loginServer (UserRMI userRMI) throws JSONException, IOException {
        try {
            if (JSONUtility.checkLogin(userRMI.getUsername(), userRMI.getKeyword())) {
                if (!searchUserLobby(userRMI)) {
                    if (!server.searchLogged(userRMI.getUsername())) {
                        this.addRMIUser(userRMI);

                        if (userRMI.isGUI()) {
                            ClientInfo clientInfo = new ClientInfo(ConnectionType.RMI, ClientType.GUI);
                            lobby.setUser(userRMI.getUsername(), clientInfo);
                            userRMI.setMessageForGUI("Login successful");
                        }
                        else {
                            ClientInfo clientInfo = new ClientInfo(ConnectionType.RMI, ClientType.CLI);
                            lobby.setUser(userRMI.getUsername(), clientInfo);
                            userRMI.sendMessage("Login successful");
                        }

                        lobby.notifyAllUsers(NotificationType.USERLOGIN, userRMI.getUsername());

                        if (lobby.getUsers().size() == 2) {
                            userRMI.sendMessage("Timer Started");
                            lobby.inizializeTimer();
                            lobby.startTimer();
                        }

                        if (lobby.getUsers().size() == 5) {
                            lobby.stopTimer();
                            System.out.println("The Game is Starting"); //TODO CREARE TIMER PERSONALIZZABILE PER FARLO SCATTARE IMMEDIATAMENTE
                        }
                        return true;
                    } else {
                        if (server.isDisconnected(userRMI.getUsername())) {
                            this.addRMIUser(userRMI);

                            server.reconnected(userRMI.getUsername());
                            userRMI.sendMessage("Reconnected");
                            return true;
                        } else {
                            userRMI.sendMessage("User already logged");
                            return false;
                        }
                    }
                }
                userRMI.sendMessage("User already logged");
                return false;
            }
            userRMI.sendMessage("Incorrect username or password");
            return false;
        } catch (RemoteException e) {
            System.out.println("Client has disconnected: login interrupted");
            return false;
        }
    }

    @Override
    public void registrationServer (UserRMI userRMI) throws JSONException, IOException {
        try {
            if (JSONUtility.checkRegister(userRMI.getUsername(), userRMI.getKeyword())) {
                userRMI.sendMessage("Registration Successful");
            } else {
                userRMI.sendMessage("Registration Failed: Username already taken");
            }
        } catch (RemoteException e) {
            System.out.println("Client has disconnected: registration successful");
        }
    }

    @Override
    public boolean logoutServer (UserRMI userRMI) throws RemoteException {
        Set<String> usernames = lobby.getUsers().keySet();
        for (String user : usernames) {
            try {
                if (userRMI.getUsername().equals(user)) {
                    this.removeRMIUser(userRMI);

                    lobby.removeUser(user);

                    lobby.notifyAllUsers(NotificationType.USERLOGOUT, userRMI.getUsername());
                    userRMI.sendMessage("Logout successful");

                    if (lobby.getUsers().size() == 1) {
                        System.out.println(userRMI.getUsername() + "left the room");
                        lobby.stopTimer();
                    }

                    return false;
                }
            } catch (RemoteException e) {
                this.removeRMIUser(userRMI);
                System.out.println("Client has disconnected: logout successful");
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
        for (int i = 0; i < usersLoggedRMI.size(); i++) {
            try {
                usersLoggedRMI.get(i).sendMessage(m);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void throwInGameGUI(ArrayList<String> userStarting) throws RemoteException {
        int j = 0;
        try {
            for (int i = 0; i < userStarting.size(); i++) {
                for (j = 0; j < usersLoggedRMI.size(); j++) {
                    if (userStarting.get(i).equals(usernames.get(j))) {
                        if (usersLoggedRMI.get(j).isGUI()) {
                            usersLoggedRMI.get(i).setMessageForGUI("start");
                        }
                    }
                }
            }
        } catch (RemoteException e) {
            this.removeRMIUser(j);
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

    @Override
    public void sendInput (String input, UserRMI userRMI) throws IOException {
        int i = 0;
        GameController gameController;
        try {
            gameController = searchGame(userRMI);
            try {
                for (i = 0; i < usersLoggedRMI.size(); i++) {
                    if (userRMI.getUsername().equals(usersLoggedRMI.get(i).getUsername())) {
                        if (userRMI.getGameState() == null) {
                            switch (input) {
                                case "/playturn":
                                    userRMI.sendMessage(gameController.flow(input, userRMI.getUsername()));
                                    userRMI.setGameState(input);
                                    break;
                                case "/chat":
                                    userRMI.sendMessage("Insert a Message: ");
                                    gameController.sendMessageChat(input, userRMI.getUsername());
                                    userRMI.setGameState(input);
                                    break;
                                case "/stampinfo":
                                    userRMI.sendMessage("Not implemented yet, man, eccheccazzo");
                                    break;
                                case "/afk":
                                    gameController.flow(input, userRMI.getUsername());
                                    break;
                                default:
                                    userRMI.sendMessage("Command Unknown");
                            }
                        } else {
                            if (input.equals("/afk")) {
                                gameController.flow(input, userRMI.getUsername());
                            }
                            switch (userRMI.getGameState()) {
                                case "/playturn":
                                    userRMI.sendMessage(gameController.flow(input, userRMI.getUsername()));
                                    break;
                                case "/chat":
                                    userRMI.setGameState(null);
                                    gameController.sendMessageChat(input, userRMI.getUsername());
                                    userRMI.sendMessage("Type: /playturn for an action; /chat to send message; /stampinfo to stamp info");
                                    break;
                                case "/vaticansupport":
                                    userRMI.sendMessage(gameController.flow(input, userRMI.getUsername()));
                                    break;
                                default:
                                    userRMI.sendMessage("Wrong state game. How did you do it? Explain it.");
                                    break;
                            }
                        }
                    }
                }
            } catch (RemoteException e) {
                String tempUsername = usernames.get(i);
                this.removeRMIUser(i);
                gameController.flow("/afk", tempUsername);

                this.sendInput(input, userRMI);
            }
        } catch (NullPointerException e) {
            userRMI.sendMessage("The Game isn't started yet");
        }
    }

    public void sendMessage(Player player, String message) throws RemoteException {
        for (int i = 0; i < usersLoggedRMI.size(); i++) {
            try {
                if (player.getUsername().equals(usersLoggedRMI.get(i).getUsername())) {
                    if (message.equals("Action has been executed")) {
                        usersLoggedRMI.get(i).setGameState(null);
                        usersLoggedRMI.get(i).sendMessage(message);
                        break;
                    }
                    if (message.equals("This Client has been disconnected")) {
                        usersLoggedRMI.get(i).setLogged(false);
                        usersLoggedRMI.get(i).sendMessage(message + "; Press any key to start over");
                        break;
                    }
                }
            } catch (RemoteException e) {
                this.removeRMIUser(i);
            }
        }
    }

    public void setStateGame (Player player, String state) throws RemoteException {
        for (int i = 0; i < usersLoggedRMI.size(); i++) {
            try {
                if (player.getUsername().equals(usersLoggedRMI.get(i).getUsername())) {
                    usersLoggedRMI.get(i).setGameState(state);
                }
            } catch (RemoteException e) {
                this.removeRMIUser(i);
            }
        }
    }

    public synchronized void removeRMIUser (int i) {
        usersLoggedRMI.remove(i);
        usernames.remove(i);
    }

    public void removeRMIUser (UserRMI userRMI) throws RemoteException {
        for (int i = 0; i < usersLoggedRMI.size(); i++) {
            try {
                if (usernames.get(i).equals(userRMI.getUsername())) {
                    this.removeRMIUser(i);
                }
            } catch (RemoteException e) {
                this.removeRMIUser(i);
                System.out.println("Client has been disconnected: removed with successful");
            }
        }
    }

    public synchronized boolean addRMIUser (UserRMI userRMI) throws RemoteException{
        try {
            usersLoggedRMI.add(userRMI);
            usernames.add(userRMI.getUsername());
        } catch (RemoteException e) {
            return false;
        }
        return true;
    }
}
