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
		if(super.isPlaceable(familyMember)){
			/*if(familyMember instanceof GhostFamilyMember){
				GhostFamilyMember ghostFamilyMember = (GhostFamilyMember) familyMember;
			}*/
		}
		return false;
	}

	@Override
	public boolean place(FamilyMember familyMember){
		familyMember.getPlayer().getPlayerBoard().getCounter().sum(rewards);
		return super.place(familyMember);
	}
}
