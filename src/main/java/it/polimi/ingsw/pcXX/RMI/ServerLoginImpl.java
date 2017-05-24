package it.polimi.ingsw.pcXX.RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Povaz on 24/05/2017.
 */
public class ServerLoginImpl extends UnicastRemoteObject implements ServerLogin {



    public boolean controlUsername (String username) throws RemoteException {
        // TO-DO LoginJSON
    }

    public boolean controlPassword (String password) throws RemoteException {
        // TO-DO LoginJSON
    }

    public boolean saveUsername (String username) throws RemoteException  {
        // TO-DO LoginJSON
    }

    public boolean savePassword (String password) throws RemoteException {
        //TO-DO LoginJSON
    }

    public boolean saveEmail (String email) throws RemoteException {
        //TO-DO LoginJSON
    }

    public boolean setPersonalQuestion (String answer) throws RemoteException {
        //TO-DO LoginJSON
    }

    public boolean askPersonalQuestion () throws RemoteException {
        //TO-DO LoginJSON
    }
}
