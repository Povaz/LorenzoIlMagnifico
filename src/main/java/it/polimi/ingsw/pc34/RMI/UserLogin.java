package it.polimi.ingsw.pc34.RMI;

import it.polimi.ingsw.pc34.Controller.ActionInput;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Povaz on 24/05/2017.
 **/
public interface UserLogin extends Remote{
    String getUsername () throws RemoteException;
    String getKeyword() throws RemoteException;
    void sendMessage (String message) throws RemoteException;

    int setActionChoose () throws RemoteException;
}
