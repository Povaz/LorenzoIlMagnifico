package it.polimi.ingsw.pcXX.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Povaz on 24/05/2017.
 */
public interface ServerLogin extends Remote{
    boolean controlUser (UserLogin userLogin) throws RemoteException;
    boolean saveUser (UserLogin userLogin) throws RemoteException;
    boolean deleteUser (UserLogin userLogin) throws RemoteException;
    void printUsers () throws RemoteException;
}
