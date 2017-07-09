package it.polimi.ingsw.pc34.RMI;

import it.polimi.ingsw.pc34.View.GUI.BoardView;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Povaz on 24/05/2017.
 **/
public interface UserRMI extends Remote{
    String getUsername () throws RemoteException;
    String getKeyword() throws RemoteException;
    boolean isGUI() throws RemoteException;
    void sendMessage (String message) throws RemoteException;
    void setLogged (boolean logged) throws RemoteException;
    void setGameState(String gameState) throws RemoteException;
    void setStartingGame(boolean startingGame) throws RemoteException;
    void setMessageForGUI(String message) throws RemoteException;
    void setMessageByGUI(String message) throws RemoteException;
    void setMessageToChangeWindow (String message) throws RemoteException;
    void setMessageInfo (String message) throws RemoteException;
    void setMessageChat (String message) throws RemoteException;
    void setBoardView (BoardView boardView) throws RemoteException;
    String getMessageByGUI() throws RemoteException;
    String getGameState() throws RemoteException;
}
