package it.polimi.ingsw.pc34.RMI;

import it.polimi.ingsw.pc34.Controller.ActionInput;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Povaz on 24/05/2017.
 **/
public interface UserRMI extends Remote{
    String getUsername () throws RemoteException;
    String getKeyword() throws RemoteException;
    void sendMessage (String message) throws RemoteException;
    void setLogged (boolean logged) throws RemoteException;
    void setGameState(String gameState) throws RemoteException;
    String getGameState() throws RemoteException;
}
