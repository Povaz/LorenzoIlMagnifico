package it.polimi.ingsw.pcXX.RMI;


import org.json.JSONException;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Povaz on 24/05/2017.
 **/
public interface ServerLogin extends Remote{
    boolean loginServer(UserLogin userLogin) throws JSONException, IOException;
    void registrationServer(UserLogin userLogin) throws JSONException, IOException;
    boolean logoutServer(UserLogin userLogin) throws RemoteException;
    void printLoggedUsers () throws RemoteException;
}
