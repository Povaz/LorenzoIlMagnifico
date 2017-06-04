package it.polimi.ingsw.pcXX;

import java.util.HashSet;
import java.util.Set;

public class CouncilPalace extends ActionSpot{
	private final Set<Reward> rewards;

	public CouncilPalace(){
		super(true, true, 1);
		this.rewards = new HashSet<>();
		rewards.add(new Reward(RewardType.COIN, 1));
		rewards.add(new Reward(RewardType.COUNCIL_PRIVILEGE, 1));
	}

	public Set<Reward> getRewards() {
		return rewards;
	}

	@Override
	public boolean isPlaceable(FamilyMember familyMember){
		return super.isPlaceable(familyMember);
	}

	@Override
	public boolean place(FamilyMember familyMember){
		if(super.place(familyMember)){
			familyMember.getPlayer().getPlayerBoard().getCounter().add(rewards);
			return true;
		}
		return false;
	}
}
