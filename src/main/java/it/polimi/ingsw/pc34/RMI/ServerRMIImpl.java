package it.polimi.ingsw.pc34.RMI;


import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.Controller.GameController;
import it.polimi.ingsw.pc34.Model.Player;
import it.polimi.ingsw.pc34.SocketRMICongiunction.*;

import it.polimi.ingsw.pc34.View.GUI.BoardView;
import org.json.JSONException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by Povaz on 24/05/2017.
 **/
@SuppressWarnings("serial")
public class ServerRMIImpl extends UnicastRemoteObject implements ServerRMI {
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

    private boolean searchUserLobby(UserRMI userRMI) throws RemoteException { //Tells if a User RMI is already in the lobby
        Set<String> usernames = lobby.getUsers().keySet();
        for (String username : usernames) {
            if ((userRMI.getUsername().equals(username))) {
                return true;
            }
        }
        return false;
    }

    public void checkUsersLogged () throws RemoteException { //Checks if Users RMI are still connected to the server
        ArrayList<String> usersRMI = lobby.getRMIUsers();       //If they're not connected, they will be logged out
        if (usersRMI.size() == 0) {
            return;
        }
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


    private void forcedLogoutServer (String username, int i) throws RemoteException { //Force the logout from ServerRMI and Lobby
        lobby.removeUser(username);                                                     //Of the player "username" who is the "i"th user
        this.removeRMIUser(i);                                                          // in RMI Server
        lobby.notifyAllUsers(NotificationType.USERLOGOUT, username);

        if (lobby.getUsers().size() == 1) {
            lobby.stopTimer();
        }
    }

    @Override
    public boolean loginServer (UserRMI userRMI) throws JSONException, IOException { //Login procedures for RMI Users: it returns true if login
        try {                                                                           //was successful, false if not
            if (JSONUtility.checkLogin(userRMI.getUsername(), userRMI.getKeyword())) {
                if (!searchUserLobby(userRMI)) {
                    if (!server.searchLogged(userRMI.getUsername())) { //Check if a player was already in a game
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
                        }
                        return true;
                    } else {    //If it was in game before, it will be reconnected to that game
                        if (server.isDisconnected(userRMI.getUsername())) {
                            this.addRMIUser(userRMI);
                            if (userRMI.isGUI()) {
                                server.reconnected(userRMI.getUsername(), ClientType.GUI, ConnectionType.RMI);
                                userRMI.sendMessage("Reconnected");
                                userRMI.setMessageForGUI("Login successful");
                            }
                            else {
                                server.reconnected(userRMI.getUsername(), ClientType.CLI, ConnectionType.RMI);
                                userRMI.sendMessage("Reconnected");
                            }
                            return true;
                        } else {
                            if (userRMI.isGUI()) {
                                userRMI.setMessageForGUI("User already logged");
                            }
                            else {
                                userRMI.sendMessage("User already logged");
                            }
                            return false;
                        }
                    }
                }
                if (userRMI.isGUI()) {
                    userRMI.setMessageForGUI("User already logged");
                }
                else {
                    userRMI.sendMessage("User already logged");
                }
                return false;
            }
            if (userRMI.isGUI()) {
                userRMI.setMessageForGUI("Incorrect username or password");
            }
            else {
                userRMI.sendMessage("Incorrect username or password");
            }
            return false;
        } catch (RemoteException e) {
            System.out.println("Client has disconnected: login interrupted");
            return false;
        }
    }

    @Override
    public void registrationServer (UserRMI userRMI) throws JSONException, IOException { //Registration procedures for RMI Users
        try {
            if (JSONUtility.checkRegister(userRMI.getUsername(), userRMI.getKeyword())) {
                if (userRMI.isGUI()) {
                    userRMI.setMessageForGUI("Registration Successful");
                }
                else {
                    userRMI.sendMessage("Registration Successful");
                }
            } else {
                if (userRMI.isGUI()) {
                    userRMI.setMessageForGUI("Registration Failed: Username already taken");
                }
                else {
                    userRMI.sendMessage("Registration Failed: Username already taken");
                }
            }
        } catch (RemoteException e) {
            System.out.println("Client has disconnected: registration successful");
        }
    }

    @Override
    public boolean logoutServer (UserRMI userRMI) throws RemoteException { //Logout procedure for RMI Users
        Set<String> usernames = lobby.getUsers().keySet();
        boolean GUI = userRMI.isGUI();
        for (String user : usernames) {
            try {
                if (userRMI.getUsername().equals(user)) {

                    this.removeRMIUser(userRMI);

                    lobby.removeUser(user);
                    lobby.notifyAllUsers(NotificationType.USERLOGOUT, userRMI.getUsername());

                    if (GUI) {
                        userRMI.setMessageForGUI("Logout successful");
                        userRMI.setMessageToChangeWindow("/login");
                        userRMI.getMessageByGUI();
                    }
                    else {
                        userRMI.sendMessage("Logout successful");
                    }

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
        if (GUI) {
            userRMI.setMessageForGUI("Logout Failed");
        }
        else {
            userRMI.sendMessage("Logout Failed");
        }
        return true;
    }

    // FINE GESTIONE LOBBY


    public void notifyRMIPlayers (String m) throws RemoteException { //Sends a Notification to RMI Users
        int i;
        for (i = 0; i < usersLoggedRMI.size(); i++) {
            try {
                usersLoggedRMI.get(i).sendMessage(m);
            } catch (RemoteException e) {
                this.removeRMIUser(i);
            }
        }
    }

    public void throwInGameGUI(ArrayList<String> userStarting) throws RemoteException { //Tells RMI GUI Users that a game is started and unlocks the
        int j = 0;                                                                      //waiting room lock in order to open the game Window
        try {
            for (int i = 0; i < userStarting.size(); i++) {
                for (j = 0; j < usersLoggedRMI.size(); j++) {
                    if (userStarting.get(i).equals(usernames.get(j))) {
                        if (usersLoggedRMI.get(j).isGUI()) {
                            usersLoggedRMI.get(j).setMessageByGUI("start"); //PUT
                        }
                    }
                }
            }
        } catch (RemoteException e) {
            this.removeRMIUser(j);
        }

    }

    //INIZIO GESTIONE GAME

    private GameController searchGame (UserRMI userRMI) throws RemoteException { //Returns the GameController associated with the game UserRMI is in
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
    public void sendInput (String input, UserRMI userRMI) throws IOException {  //Manages the input from RMI Users
        int i = 0;
        GameController gameController;
        try {
            gameController = searchGame(userRMI);
            try {
                for (i = 0; i < usersLoggedRMI.size(); i++) {
                    if (userRMI.getUsername().equals(usersLoggedRMI.get(i).getUsername())) {
                        if (input.startsWith("/chat")) {
                            String message = input.substring(5);
                            gameController.sendMessageChat(message, userRMI.getUsername());
                            if(userRMI.isGUI()) {
                                userRMI.setMessageForGUI("Message accepted: Synchro");
                            }
                        }
                        else if (userRMI.getGameState() == null) { //If GameState is Null (this is the first input), evaluates the Input and set it
                            switch (input) {                    //as the new GameState
                                case "/playturn":
                                    if (userRMI.isGUI()) {
                                        String response = gameController.flow(input, userRMI.getUsername());
                                        if (response.equals("It's not your turn")) {
                                            userRMI.setMessageForGUI("No");
                                        }
                                        else {
                                            userRMI.setMessageForGUI("Yes");
                                        }
                                    }
                                    else {
                                        userRMI.sendMessage(gameController.flow(input, userRMI.getUsername()));
                                    }
                                    userRMI.setGameState(input);
                                    break;
                                case "/update":
                                    if (userRMI.isGUI()) {
                                        gameController.updateRequested(userRMI.getUsername());
                                    }
                                    else {
                                        userRMI.sendMessage("Command Unknown");
                                    }
                                    break;
                                case "/afk":
                                    gameController.flow(input, userRMI.getUsername());
                                    break;
                                default:
                                    userRMI.sendMessage("Command Unknown");
                            }
                        } else {    //If GameState isn't null (this is not the first input), evaluates the Input accordingly to the GameState chosen
                            if (input.equals("/afk")) {
                                gameController.flow(input, userRMI.getUsername());
                            }
                            switch (userRMI.getGameState()) {
                                case "/playturn":
                                    if (userRMI.isGUI()) {
                                        String response = gameController.flow(input, userRMI.getUsername());
                                        if (response.equals("It's not your turn") || response.equals("Input error") || response.equals("Input error, Retry!")) {
                                            userRMI.setMessageForGUI("No");
                                        }
                                        else {
                                            userRMI.setMessageForGUI("Yes");
                                        }
                                    }
                                    else {
                                        userRMI.sendMessage(gameController.flow(input, userRMI.getUsername()));
                                    }
                                    break;
                                case "/vaticansupport":
                                    if (userRMI.isGUI()) {
                                        String response = gameController.flow(input, userRMI.getUsername());
                                        if (response.equals("It's not your turn") || response.equals("Input error") || response.equals("Input error, Retry!")) {
                                            userRMI.setMessageForGUI("No");
                                        }
                                        else {
                                            userRMI.setMessageForGUI("Yes");
                                        }
                                    }
                                    else {
                                        userRMI.sendMessage(gameController.flow(input, userRMI.getUsername()));
                                    }
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
            e.printStackTrace();
            userRMI.sendMessage("The Game isn't started yet");
        }
    }

    public void sendMessageChat (Player player, String message) throws RemoteException {
        int i;
        for (i = 0; i < usersLoggedRMI.size(); i++) {
            try {
                if (usersLoggedRMI.get(i).getUsername().equals(player.getUsername())) {
                    usersLoggedRMI.get(i).setMessageChat(message);
                }
            } catch (RemoteException e) {
                this.removeRMIUser(i);
            }
        }
    }

    public void openNewWindow(Player player, String message) throws RemoteException {
        int i;
        for (i = 0; i < usersLoggedRMI.size(); i++) {
            try {
                if (usersLoggedRMI.get(i).getUsername().equals(player.getUsername())) {
                    usersLoggedRMI.get(i).setMessageToChangeWindow(message);
                    usersLoggedRMI.get(i).setMessageInfo(Integer.toString(player.getPlayerBoard().getCounter().getServant().getQuantity()));
                }
            } catch (RemoteException e) {
                this.removeRMIUser(i);
            }
        }
    }
    
    public void openNewWindow (Player player, String message, String toSynchro) {
        int i;
        for (i = 0; i < usersLoggedRMI.size(); i++) {
            try {
                if (usersLoggedRMI.get(i).getUsername().equals(player.getUsername())) {
                    usersLoggedRMI.get(i).setMessageToChangeWindow(message);
                    usersLoggedRMI.get(i).setMessageInfo(toSynchro);
                }
            } catch (RemoteException e) {
                this.removeRMIUser(i);
            }
        }
    }

    public void openNewWindowAtTheEnd (Player player, String infoGUI, String messageServer) {
        int i;
        for (i = 0; i < usersLoggedRMI.size(); i++) {
            try {
                if (usersLoggedRMI.get(i).getUsername().equals(player.getUsername())) {
                    usersLoggedRMI.get(i).setMessageByGUI("skipCommand");
                    usersLoggedRMI.get(i).setMessageInfo(infoGUI);
                    this.sendMessage(player, messageServer);
                }
            } catch (RemoteException e) {
                this.removeRMIUser(i);
            }
        }
    }

    public void openNewWindow(Player player, String message, int numberOfCP) {
        int i;
        for (i = 0; i < usersLoggedRMI.size(); i++) {
            try {
                if (usersLoggedRMI.get(i).getUsername().equals(player.getUsername())) {
                    usersLoggedRMI.get(i).setMessageToChangeWindow(message);
                    usersLoggedRMI.get(i).setMessageInfo(Integer.toString(numberOfCP));
                }
            } catch (RemoteException e) {
                this.removeRMIUser(i);
            }
        }
    }

    public void updateUserRMIView (BoardView boardView, String username) throws RemoteException {
        int i;
        for (i = 0; i < usersLoggedRMI.size(); i++) {
            try {
                if (usersLoggedRMI.get(i).getUsername().equals(username) && usersLoggedRMI.get(i).isGUI()) {
                    usersLoggedRMI.get(i).setMessageToChangeWindow("/update");
                    usersLoggedRMI.get(i).setBoardView(boardView);
                }
            } catch (RemoteException e) {
                this.removeRMIUser(i);
            }
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
                    if (message.equals("This Client has been disconnected") || message.equals("This game is finished")) {
                        usersLoggedRMI.get(i).setLogged(false);
                        usersLoggedRMI.get(i).setStartingGame(false);
                        if (usersLoggedRMI.get(i).isGUI()) {
                            usersLoggedRMI.get(i).setMessageByGUI("restart");
                        }
                        usersLoggedRMI.get(i).sendMessage(message);
                        removeRMIUser(i);
                        break;
                    }
                    else {
                        usersLoggedRMI.get(i).sendMessage(message);
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
