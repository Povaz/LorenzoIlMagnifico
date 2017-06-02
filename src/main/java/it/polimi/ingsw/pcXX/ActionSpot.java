package it.polimi.ingsw.pcXX;

import java.util.LinkedList;
import java.util.List;

public abstract class ActionSpot {
	private boolean busy;
	private final boolean active;
	private final boolean unrestricted;
	private final int diceValue;
	protected List<FamilyMember> occupiedBy;

	public ActionSpot(boolean active, boolean unrestricted, int diceValue){
		this.busy = false;
		this.active = active;
		this.unrestricted = unrestricted;
		this.diceValue = diceValue;
		this.occupiedBy = new LinkedList<>();
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isUnrestricted() {
		return unrestricted;
	}

	public int getDiceValue() {
		return diceValue;
	}

	public List<FamilyMember> getOccupiedBy() {
		return occupiedBy;
	}

	public void reinitialize(){
		this.busy = false;
		this.occupiedBy = new LinkedList<>();
	}

	public boolean isPlaceable(FamilyMember familyMember){
		if(busy){
			return false;
		}
		if(diceValue > familyMember.getValue()){
			return false;
		}
		return true;
	}

	public boolean place(FamilyMember familyMember){
		occupiedBy.add(familyMember);
		if(!unrestricted){
			busy = true;
		}
		return true;
	}
}
