package it.polimi.ingsw.pcXX.SocketRMICongiunction;

import it.polimi.ingsw.pcXX.RMI.ServerLoginImpl;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Created by Povaz on 13/06/2017.
 */
public class Lobby {
    private HashMap<String, ConnectionType> users;
    private Timer timer;
    private ServerLoginImpl serverRMI;
    //Dichiara ServerSocket

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

    //setServerSocket

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
        String message;
        switch (notificationType) {
            case STARTGAME:
                message = "The game is starting";
                this.serverRMI.notifyRMIPlayers(message);
                //this.ServerSocket
                break;
            case USERLOGIN:
                message = "A user is logged";
                this.serverRMI.notifyRMIPlayers(message);
                //this.ServerSocket
                break;
            case USERLOGOUT:
                message = "A user logged out";
                this.serverRMI.notifyRMIPlayers(message);
                //this.ServerSocket
                break;
            default:
                break;
        }
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
