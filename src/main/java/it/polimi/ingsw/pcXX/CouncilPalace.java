package it.polimi.ingsw.pcXX;

import it.polimi.ingsw.pcXX.Exception.TooMuchTimeException;

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
		if(super.isPlaceable(familyMember)){
			if(familyMember.getAction() != null){
				if(familyMember.getAction() == ActionType.COUNCIL_PALACE || familyMember.getAction() == ActionType.ALL){
					return true;
				}
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean place(FamilyMember familyMember) throws TooMuchTimeException {
		familyMember.getPlayer().getPlayerBoard().getCounter().sum(rewards);
		return super.place(familyMember);
	}
}
