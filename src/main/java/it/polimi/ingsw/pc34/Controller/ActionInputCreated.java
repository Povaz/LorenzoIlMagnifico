package it.polimi.ingsw.pc34.Controller;

import it.polimi.ingsw.pc34.Controller.ActionInput;
import it.polimi.ingsw.pc34.Model.ActionType;

/**
 * Created by Povaz on 17/06/2017.
 */
public class ActionInputCreated {
    private ActionInput actionInput;
    private boolean available = false;

    public synchronized ActionInput get() {
        while (available == false) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        available = false;
        notifyAll();
        return actionInput;
    }

    public synchronized void put(ActionInput actionInput) {
        while(available == true) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.actionInput = actionInput;
        available = true;
        notifyAll();
    }
}
