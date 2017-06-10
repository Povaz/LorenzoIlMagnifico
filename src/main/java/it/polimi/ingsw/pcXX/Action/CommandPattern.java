package it.polimi.ingsw.pcXX.Action;

import it.polimi.ingsw.pcXX.Exception.TooMuchTimeException;

/**
 * Created by trill on 10/06/2017.
 */
public interface CommandPattern{
    public boolean canDoAction() throws TooMuchTimeException;
    public void doAction() throws TooMuchTimeException;
}
