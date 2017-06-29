package it.polimi.ingsw.pc34.Controller.Action;

import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Created by trill on 10/06/2017.
 */
public interface CommandPattern{
    public boolean canDoAction() throws TooMuchTimeException, RemoteException, IOException;
    public void doAction() throws TooMuchTimeException, RemoteException, IOException;
}
