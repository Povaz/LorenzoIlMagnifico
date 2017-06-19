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

	public boolean isPlaceable(FamilyMember familyMember, boolean canPlaceInBusyActionSpot, GameController gameController){
		if(familyMember.isUsed()){
			gameController.sendMessage(familyMember.getPlayer(), "This family member is alreay used!");
			return false;
		}
		if(busy && !canPlaceInBusyActionSpot){
			gameController.sendMessage(familyMember.getPlayer(), "This action spot is busy!");
			return false;
		}
		if(diceValue > familyMember.getValue() + familyMember.getServantUsed().getQuantity()){
			gameController.sendMessage(familyMember.getPlayer(), "Your family member's value is too low!");
			return false;
		}
		return true;
	}

	public void placeFamilyMember(FamilyMember familyMember){
		familyMember.setUsed(true);
		occupiedBy.add(familyMember);
		if(!unrestricted) {
			busy = true;
		}
	}

	@Override
	public String toString(){
		StringBuilder bld = new StringBuilder();

		bld.append("  Busy: " + busy + "\n");
		bld.append("  Unrestricted: " + unrestricted + "\n");
		bld.append("  Dice value: " + diceValue + "\n");

		if(!occupiedBy.isEmpty()){
			bld.append("  Occupied by: ");
			for(FamilyMember fM : occupiedBy){
				bld.append(fM.toString1() + "\n");
			}
		}
		return bld.toString();
	}

	@Override
	public boolean equals (Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ActionSpot that = (ActionSpot) o;

		if (busy != that.busy) return false;
		if (active != that.active) return false;
		if (unrestricted != that.unrestricted) return false;
		if (diceValue != that.diceValue) return false;
		return occupiedBy != null ? occupiedBy.equals(that.occupiedBy) : that.occupiedBy == null;
	}
}
