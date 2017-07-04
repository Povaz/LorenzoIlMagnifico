package it.polimi.ingsw.pc34.SocketRMICongiunction;

import it.polimi.ingsw.pc34.Controller.Game;
import it.polimi.ingsw.pc34.RMI.ServerRMIImpl;
import it.polimi.ingsw.pc34.Socket.ServerSOC;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Created by Povaz on 13/06/2017.
 */
public class Lobby {
    private HashMap<String, ClientInfo> users;
    private Timer timer;
    private ServerRMIImpl serverRMI;
    private ServerSOC serverSoc;
    
    public Lobby () {
        this.users = new HashMap<>();
        this.timer = new Timer ();
    }

    public HashMap<String, ClientInfo> getUsers() {
        return users;
    }

    public ArrayList<String> getRMIUsers () {
        ArrayList<String> rmiUsers = new ArrayList<>();
        for (Map.Entry<String, ClientInfo> entry : users.entrySet()) {
            if (entry.getValue().getConnectionType().equals(ConnectionType.RMI)) {
                rmiUsers.add(entry.getKey());
            }
        }
        return rmiUsers;
    }

    public ArrayList<String> getSocketUsers () {
        ArrayList<String> socketUsers = new ArrayList<>();
        for (Map.Entry<String, ClientInfo> entry : users.entrySet()) {
            if (entry.getValue().getConnectionType().equals(ConnectionType.SOCKET)) {
                socketUsers.add(entry.getKey());
            }
        }
        return socketUsers;
    }

    public void setServerRMI (ServerRMIImpl serverRMI) {
        this.serverRMI = serverRMI;
    }

    public void setServerSOCKET (ServerSOC serverSoc) {
        this.serverSoc = serverSoc;
    }
    
    public void setUsers(HashMap<String, ClientInfo> users) {
        this.users = users;
    }

    public void setUser(String username, ClientInfo clientInfo) {
        users.put(username, clientInfo);
    }

    public boolean searchUser (String username) {
        Set<String> usernames = users.keySet();
        for (String user : usernames) {
            if (username.equals(user)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void removeUser (String username) {
        if(this.searchUser(username)) {
            this.getUsers().remove(username);
        }
    }

    public void notifyAllUsers(NotificationType notificationType, String message) throws RemoteException {
        switch (notificationType) {
        	case TIMERSTARTED:
        		message = "The timer is starting";
        		break;
        	case STARTGAME:
                message = "The game is starting";
                break;
            case USERLOGIN:
                message += " joined the lobby";
                break;
            case USERLOGOUT:
                message += " left the lobby";
                break;
            case TIMERBLOCKED:
            	message = "Timer deleted, you are now back to the lobby waiting for new players";
                break;
            default:
                break;
        }
        this.serverRMI.notifyRMIPlayers(message);
        this.serverSoc.notifySOCPlayers(message);
    }

    public void checkUsersLogged() throws IOException {
        serverRMI.checkUsersLogged();
        serverSoc.checkUsersLogged();
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void inizializeTimer() {
        this.timer = new Timer();
    }

    public void startTimer () {
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    //Socket Start
                    serverSoc.throwInGame();

                    //RMI Start
                    System.out.println(getRMIUsers().toArray().toString());
                    serverRMI.throwInGameGUI(getRMIUsers());

                    //Game Start
                    notifyAllUsers(NotificationType.STARTGAME, "The Game is Starting");
                    Game game = new Game(users, serverRMI, serverSoc);
                    serverSoc.fromLobbytoInGame();
                    users.clear();
                    Server.gamesOnGoing.add(game);
                    Thread threadGame = new Thread (game);
                    threadGame.start();
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                } 
            }
        }, 10000);
    }

    public void stopTimer() {
        timer.cancel();
    }
}
