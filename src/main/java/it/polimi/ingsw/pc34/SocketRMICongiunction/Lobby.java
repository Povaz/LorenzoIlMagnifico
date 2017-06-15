package it.polimi.ingsw.pc34.SocketRMICongiunction;

import it.polimi.ingsw.pc34.Model.Game;
import it.polimi.ingsw.pc34.RMI.ServerLoginImpl;
import it.polimi.ingsw.pc34.Socket.ServerSOC;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Created by Povaz on 13/06/2017.
 */
public class Lobby {
    private HashMap<String, ConnectionType> users;
    private Timer timer;
    private ServerLoginImpl serverRMI;
    private ServerSOC serverSoc;
    
    public Lobby () {
        this.users = new HashMap<>();
        this.timer = new Timer ();
    }

    public HashMap<String, ConnectionType> getUsers() {
        return users;
    }

    public void setServerRMI (ServerLoginImpl serverRMI) {
        this.serverRMI = serverRMI;
    }

    public void setServerSOCKET (ServerSOC serverSoc) {
        this.serverSoc = serverSoc;
    }
    
    public void setUsers(HashMap<String, ConnectionType> users) {
        this.users = users;
    }

    public void setUser(String username, ConnectionType connectionType) {
        users.put(username, connectionType);
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

    public void removeUser (String username) {
        if(this.searchUser(username)) {
            this.getUsers().remove(username);
        }
    }

    public void notifyAllUsers(NotificationType notificationType) throws RemoteException {
        String message = null;
        switch (notificationType) {
            case STARTGAME:
                message = "The game is starting";
                break;
            case USERLOGIN:
                message = "A user is logged";
                break;
            case USERLOGOUT:
                message = "A user logged out";
                break;
            default:
                break;
        }
        this.serverRMI.notifyRMIPlayers(message);
        this.serverSoc.notifySOCPlayers(message);
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
                    notifyAllUsers(NotificationType.STARTGAME);
                    Game game = new Game(users);
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
