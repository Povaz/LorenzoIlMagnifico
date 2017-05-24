package it.polimi.ingsw.pcXX.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Povaz on 24/05/2017.
 */
public interface ServerLogin extends Remote{
    boolean controlUser (UserLoginImpl userLoginImpl) throws RemoteException;
    boolean saveUser (UserLoginImpl userLoginImpl) throws RemoteException;
}
