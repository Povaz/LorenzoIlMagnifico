package it.polimi.ingsw.pcXX.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Povaz on 24/05/2017.
 */
public interface UserLogin extends Remote{
    String getUsername () throws RemoteException;
    String getPassword () throws RemoteException;
    void sendMessage (String message) throws RemoteException;
}
