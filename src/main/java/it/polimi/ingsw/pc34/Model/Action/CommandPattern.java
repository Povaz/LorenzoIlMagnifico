package it.polimi.ingsw.pc34.Model.Action;

import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;

/**
 * Created by trill on 10/06/2017.
 */
public interface CommandPattern{
    public boolean canDoAction() throws TooMuchTimeException;
    public void doAction() throws TooMuchTimeException;
}
