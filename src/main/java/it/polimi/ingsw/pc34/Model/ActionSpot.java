package it.polimi.ingsw.pc34.Model;

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

	public boolean isPlaceable(FamilyMember familyMember, boolean canPlaceInBusyActionSpot){
		if(familyMember.isUsed()){
			return false;
		}
		if(busy && !canPlaceInBusyActionSpot){
			return false;
		}
		if(diceValue > familyMember.getValue() + familyMember.getServantUsed().getQuantity()){
			return false;
		}
		return true;
	}

	public void placeFamilyMember(FamilyMember familyMember){
		familyMember.setUsed(true);
		occupiedBy.add(familyMember);
		if(!unrestricted){
			busy = true;
		}
	}

	@Override
	public String toString(){
		String string = "";
		string += "  busy: " + busy + "\n";
		string += "  unrestricted: " + unrestricted + "\n";
		string += "  diceValue: " + diceValue + "\n";
		string += "  occupiedBy: ";
		for(FamilyMember fM : occupiedBy){
			string += "\n";
			string += fM.toString1();
		}
		return string;
	}
}
