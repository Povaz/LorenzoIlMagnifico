package it.polimi.ingsw.pcXX.RMI;

import java.rmi.Remote;

/**
 * Created by Povaz on 24/05/2017.
 */
public interface UserLogin extends Remote{
    void getResponse(boolean);
}
