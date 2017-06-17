package it.polimi.ingsw.pc34.RMI;


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

    void sendInput (String input, UserLogin userLogin) throws RemoteException;
}
