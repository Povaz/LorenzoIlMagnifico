package it.polimi.ingsw.pc34.Model;

import java.rmi.RemoteException;
import java.util.Set;

public class Market extends ActionSpot{
	private final Set<Reward> rewards;

	public Market(boolean active, boolean unrestricted, int diceValue, Set<Reward> rewards){
		super(active, unrestricted, diceValue);
		this.rewards = rewards;
	}

	@Override
	public boolean isPlaceable(FamilyMember familyMember, boolean canPlaceInBusyActionSpot, GameController gameController) throws RemoteException{
		if(!super.isPlaceable(familyMember, canPlaceInBusyActionSpot, gameController)){
			return false;
		}

		if(familyMember.isGhost()){
			if(familyMember.getAction() != null){
				if(familyMember.getAction() != ActionType.ALL && familyMember.getAction() != ActionType.MARKET){
					gameController.sendMessage(familyMember.getPlayer(), "You cannot place in this type of action spot!");
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

		if(rewards!= null) {
			bld.append("  Rewards: ");
			for(Reward r : rewards){
				bld.append(r.toString() + "; ");
			}
			bld.append("\n");
		}
		return bld.toString();
	}

	public Set<Reward> getRewards() {
		return rewards;
	}
}
