package it.polimi.ingsw.pc34.Controller.Action;

import java.io.IOException;

/**
 * Created by trill on 10/06/2017.
 */
public interface CommandPattern{
    public boolean canDoAction() throws IOException;
    public void doAction() throws IOException;
}
