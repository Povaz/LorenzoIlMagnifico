package it.polimi.ingsw.pcXX;

import java.util.Set;

public class GhostFamilyMember extends FamilyMember{
	private ActionType action;
	private Set<Reward> discounts;

	public GhostFamilyMember(ActionType action, int value, Set<Reward> discounts){
		super(false, value, false);
		this.action = action;
		this.discounts = discounts;
	}

	@Override
	public String toString(){
		String actionString = "" + action.toString() + "\n";
		actionString += "  Value: " + super.value + "\n";
		if(discounts != null){
			actionString += "  Discounts:\n";
			for (Reward r : discounts) {
				actionString += "    " + r.toString() + "\n";
			}
		}
		return actionString;
	}
}
