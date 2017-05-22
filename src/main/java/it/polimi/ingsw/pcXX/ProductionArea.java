package it.polimi.ingsw.pcXX;

public class ProductionArea extends ActionSpot{
	private final int diceModifier;

	public ProductionArea(boolean active, boolean unrestricted, int diceModifier){
	    super(active, unrestricted, 1);
	    this.diceModifier = diceModifier;
    }
}
