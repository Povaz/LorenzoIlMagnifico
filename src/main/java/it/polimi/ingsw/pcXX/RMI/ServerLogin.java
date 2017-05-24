package it.polimi.ingsw.pcXX.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Povaz on 24/05/2017.
 */
public interface ServerLogin extends Remote{
    boolean controlUsername (String username) throws RemoteException;
    boolean controlPassword (String password) throws RemoteException;
    boolean saveUsername (String username) throws RemoteException;
    boolean savePassword (String password) throws RemoteException;
    boolean saveEmail (String email) throws RemoteException;
    boolean setPersonalQuestion (String answer) throws RemoteException;
    boolean askPersonalQuestion () throws RemoteException;
}
