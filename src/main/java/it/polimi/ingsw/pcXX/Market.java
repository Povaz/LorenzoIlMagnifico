package it.polimi.ingsw.pcXX;

import java.util.Set;

public class Market extends ActionSpot{
	private final Set<Reward> rewards;

	public Market(boolean active, boolean unrestricted, int diceValue, Set<Reward> rewards){
		super(active, unrestricted, diceValue);
		this.rewards = rewards;
	}

	@Override
	public boolean isPlaceable(FamilyMember familyMember){
		if(!super.isPlaceable(familyMember)){
			return false;
		}

		if(familyMember.isGhost()){
			if(familyMember.getAction() != null){
				if(familyMember.getAction() != ActionType.ALL && familyMember.getAction() != ActionType.MARKET){
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public void placeFamilyMember(FamilyMember familyMember){
		if(!familyMember.isGhost()){
			super.placeFamilyMember(familyMember);
		}
	}

	@Override
	public String toString(){
		String string = super.toString();
		if(rewards!= null) {
			string += "\n  rewards: ";
			for (Reward r : rewards) {
				string += r.toString() + "   ";
			}
		}
		return string;
	}

	public Set<Reward> getRewards() {
		return rewards;
	}
}
