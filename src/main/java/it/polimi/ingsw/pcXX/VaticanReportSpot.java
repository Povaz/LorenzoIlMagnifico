package it.polimi.ingsw.pcXX;

import java.util.ArrayList;
import java.util.List;

public class VaticanReportSpot {
	private final List<Player> reported;
	private final VaticanReportCard vaticanReportCard;
	private final Reward faithPointNeeded;
	private final boolean last;

	public VaticanReportSpot(VaticanReportCard vaticanReportCard, Reward faithPointNeeded, boolean last){
		this.reported = new ArrayList<>();
		this.vaticanReportCard = vaticanReportCard;
		this.faithPointNeeded = faithPointNeeded;
		this.last = last;
	}

	public void support(Player player){
		Reward playerFaithPoint = player.getPlayerBoard().getCounter().getFaithPoint();
		if(last){

		}
		else{
			if(playerFaithPoint.getQuantity() >= faithPointNeeded.getQuantity()){
				if(TerminalInput.wantToSupportVatican()){
					earnRewardSupport(player);
				}
			}
		}
	}

	private void earnRewardSupport(Player player){
		Reward victoryPoint = calculateVictoryPointFromFaithPoint(faithPoint);
	}

	private Reward calculateVictoryPointFromFaithPoint(Reward faithPoint){
		int faith = faithPoint.getQuantity();
		if(faith < 0){
			return new Reward(RewardType.VICTORY_POINT, 0);
		}
		else if(faith <= 5){
			return new Reward(RewardType.VICTORY_POINT, faith);
		}
		else if(faith <= 12){
			return new Reward(RewardType.VICTORY_POINT, 2 * faith - 5);
		}
		else if(faith <= 13){
			return new Reward(RewardType.VICTORY_POINT, 3 * faith - 17);
		}
		else{
			return new Reward(RewardType.VICTORY_POINT, 30);
		}
	}

	private void getExcommunicated(Player player){

	}

	@Override
	public String toString(){
		return "ANCORA DA IMPLEMENTARE!!!\n";
	}
}
