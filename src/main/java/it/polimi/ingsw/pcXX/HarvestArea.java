package it.polimi.ingsw.pcXX;

public class HarvestArea extends ActionSpot{
    private final int diceModifier;

    public HarvestArea(boolean active, boolean unrestricted, int diceModifier){
        super(active, unrestricted, 1);
        this.diceModifier = diceModifier;
    }
}
