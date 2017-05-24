package it.polimi.ingsw.pcXX.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Povaz on 24/05/2017.
 */
public interface ServerLogin extends Remote{
    void controlUser (UserLoginImpl userLoginImpl) throws RemoteException;
    void saveUser (UserLoginImpl userLoginImpl) throws RemoteException;
    void printUsers () throws RemoteException;
}
