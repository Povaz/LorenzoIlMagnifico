package it.polimi.ingsw.pc34.Model;

import java.io.IOException;
import java.rmi.RemoteException;
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
	public boolean isPlaceable(FamilyMember familyMember, boolean canPlaceInBusyActionSpot, GameController gameController) throws RemoteException, IOException{
		if(!super.isPlaceable(familyMember, canPlaceInBusyActionSpot, gameController)){
			return false;
		}

		if(familyMember.isGhost()){
			if(familyMember.getAction() != null){
				if(familyMember.getAction() != ActionType.ALL && familyMember.getAction() != ActionType.COUNCIL_PALACE){
					gameController.sendMessageCLI(familyMember.getPlayer(), "You cannot place in this type of action spot!");
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
		StringBuilder bld = new StringBuilder();
		bld.append(super.toString());

		if(rewards != null){
			bld.append("  Rewards: ");
			for (Reward r : rewards) {
				bld.append(r.toString() + "; ");
			}
		}
		return bld.toString();
	}
}
