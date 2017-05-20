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
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GhostFamilyMember that = (GhostFamilyMember) o;

		if (action != that.action) return false;
		return discounts != null ? discounts.equals(that.discounts) : that.discounts == null;
	}

	@Override
	public int hashCode(){
		int result = action.hashCode();
		result = 31 * result + (discounts != null ? discounts.hashCode() : 0);
		return result;
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
