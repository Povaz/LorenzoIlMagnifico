package it.polimi.ingsw.pc34.RMI;

import it.polimi.ingsw.pc34.Controller.ActionInput;
import it.polimi.ingsw.pc34.Model.ActionType;
import it.polimi.ingsw.pc34.View.TerminalInput;

/**
 * Created by Povaz on 17/06/2017.
 */
public class ActionInputProducer extends Thread{
    private ActionInputCreated actionInputCreated;
    private ActionInput actionInput;

    public ActionInputProducer (ActionInputCreated actionInputCreated) {
        this.actionInputCreated = actionInputCreated;
    }

    public void setActionInput (ActionInput actionInput) {
        this.actionInput = actionInput;
    }

    public void run () {
        actionInputCreated.put(actionInput);
        try {
            sleep((int) (Math.random() * 100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
