package it.polimi.ingsw.pcXX;

/**
 * Created by Povaz on 07/06/2017.
 */
public class Action {
    private ActionType actionType;
    private int spot;

    public Action (ActionType actionType, int spot) {
        this.actionType = actionType;
        this.spot = spot;
    }

    public Action () {
        this.actionType = ActionType.ALL;
        this.spot = 0;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public int getSpot() {
        return spot;
    }

    public void setSpot(int spot) {
        this.spot = spot;
    }

    @Override
    public String toString() {
        String actionString = actionType.toString() + spot;
        return actionString;
    }
}
