package it.polimi.ingsw.pc34.RMI;

import it.polimi.ingsw.pc34.Controller.ActionInput;
import it.polimi.ingsw.pc34.Model.Game;
import it.polimi.ingsw.pc34.Model.GameController;

/**
 * Created by Povaz on 17/06/2017.
 */
public class ActionInputConsumer extends Thread {
    private ActionInputCreated actionInputCreated;
    private GameController gameController;

    public ActionInputConsumer (ActionInputCreated actionInputCreated, GameController gameController) {
        this.actionInputCreated = actionInputCreated;
        this.gameController = gameController;
    }

    public void run() {
        ActionInput actionInput;
        actionInput = actionInputCreated.get();
        gameController.setActionInput(actionInput);
    }
}
