package it.polimi.ingsw.pcXX.RMI;

import org.json.JSONException;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Povaz on 24/05/2017.
 */
public interface ServerLogin extends Remote{
    boolean controlUser (UserLogin userLogin) throws RemoteException, JSONException, IOException;
    boolean saveUser (UserLogin userLogin) throws RemoteException, JSONException, IOException;
    boolean logoutUser(UserLogin userLogin) throws RemoteException;
}
