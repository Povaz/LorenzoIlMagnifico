package it.polimi.ingsw.pcXX;

import java.util.LinkedList;

public abstract class ActionSpot {
	private boolean busy;
	private final boolean active;
	private final boolean unrestricted;
	private final int diceValue;
	private final LinkedList<FamilyMember> occupiedBy;

	public ActionSpot(boolean active, boolean unrestricted, int diceValue){
		this.busy = false;
		this.active = active;
		this.unrestricted = unrestricted;
		this.diceValue = diceValue;
		this.occupiedBy = new LinkedList<FamilyMember>();
	}
}
