package it.polimi.ingsw.pc34.RMI;


import org.json.JSONException;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Povaz on 24/05/2017.
 **/
public interface ServerRMI extends Remote{
    boolean loginServer(UserRMI userRMI) throws JSONException, IOException;
    void registrationServer(UserRMI userRMI) throws JSONException, IOException;
    boolean logoutServer(UserRMI userRMI) throws RemoteException;
    void sendInput (String input, UserRMI userRMI) throws IOException;
}
