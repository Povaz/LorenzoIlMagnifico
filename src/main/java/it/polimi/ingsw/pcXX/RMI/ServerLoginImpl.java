package it.polimi.ingsw.pcXX.RMI;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by Povaz on 24/05/2017.
 */
public class ServerLoginImpl extends UnicastRemoteObject implements ServerLogin {
    private ArrayList<UserLoginImpl>

    public ServerLoginImpl () throws RemoteException {

    }

    public boolean controlUser (UserLoginImpl userLoginImpl) throws RemoteException {
        //TO-DO JSONLogin
    }

    public boolean saveUser (UserLoginImpl userLoginImpl) throws RemoteException {
        //TO-DO JSONLogin
    }

    public static void main (String[] args) throws RemoteException, AlreadyBoundException {

    }
}
