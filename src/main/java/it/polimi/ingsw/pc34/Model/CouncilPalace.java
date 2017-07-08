package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.Controller.GameController;
import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientType;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CouncilPalace extends ActionSpot{
	Logger LOGGER = Logger.getLogger(CouncilPalace.class.getName());

	private final Set<Reward> rewards;

	public CouncilPalace(){
		super(true, true, 1);

		Set<Reward> rewardSet;
		try{
			rewardSet = JSONUtility.getSpotRewards(ActionType.COUNCIL_PALACE, 0);
		} catch(JSONException e){
			rewardSet = new HashSet<>();
			rewardSet.add(new Reward(RewardType.COIN, 1));
			rewardSet.add(new Reward(RewardType.COUNCIL_PRIVILEGE, 1));
			LOGGER.log(Level.WARNING, "Config.json: Wrong format", e);
		} catch(IOException e){
			rewardSet = new HashSet<>();
			rewardSet.add(new Reward(RewardType.COIN, 1));
			rewardSet.add(new Reward(RewardType.COUNCIL_PRIVILEGE, 1));
			LOGGER.log(Level.WARNING, "Config.json: Incorrect path", e);
		}
		rewards = rewardSet;
	}

	public Set<Reward> getRewards() {
		return rewards;
	}

	@Override
	public boolean isPlaceable(FamilyMember familyMember, boolean canPlaceInBusyActionSpot, GameController gameController) throws IOException{
		if(!super.isPlaceable(familyMember, canPlaceInBusyActionSpot, gameController)){
			return false;
		}

		if(familyMember.isGhost()){
			if(familyMember.getAction() != null){
				if(familyMember.getAction() != ActionType.ALL && familyMember.getAction() != ActionType.COUNCIL_PALACE){
					if (familyMember.getPlayer().getClientType().equals(ClientType.GUI)) {
						gameController.sendMessageChatGUI(familyMember.getPlayer(), "You cannot place in this type of action spot!", true);
					}
					else {
						gameController.sendMessageCLI(familyMember.getPlayer(), "You cannot place in this type of action spot!");
					}
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
