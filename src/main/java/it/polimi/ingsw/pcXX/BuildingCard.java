package it.polimi.ingsw.pcXX;

import java.util.Set;

public class BuildingCard extends DevelopmentCard{
	private int diceProductionAction;
	private Set<Reward> earnings;
	private Set<Trade> trades;
	private RewardForReward rewardForReward;
	private RewardForCard rewardForCard;

	public BuildingCard(String name, int period, Set<Reward> costs, Set<Reward> fastRewards, int diceProductionAction,
						Set<Reward> earnings, Set<Trade> trades, RewardForReward rewardForReward, RewardForCard rewardForCard){
		super(name, CardType.BUILDING, period, costs, fastRewards);
		this.diceProductionAction = diceProductionAction;
		this.earnings = earnings;
		this.trades = trades;
		this.rewardForReward = rewardForReward;
		this.rewardForCard = rewardForCard;
	}

	@Override
	public String toString(){
		String cardString = super.toString();
		cardString += "Dice harvest: " + diceProductionAction + "\n";
		if(earnings != null){
			cardString += "Earnings:\n";
			for(Reward r : earnings){
				cardString += "  " + r.toString() + "\n";
			}
		}
		if(trades != null){
			cardString += "Trades:\n";
			for(Trade t : trades){
				cardString += "  " + t.toString() + "\n";
			}
		}
		if(rewardForReward != null){
			cardString += "Reward for reward: " + rewardForReward.toString() + "\n";
		}
		if(rewardForCard != null){
			cardString += "Reward for card: " + rewardForCard.toString() + "\n";
		}
		return cardString;
	}
}
