package it.polimi.ingsw.pcXX;

import java.util.List;
import java.util.Set;

public class BuildingCard extends DevelopmentCard{
	private final int diceProductionAction;
	private final Set<Reward> earnings;
	private final List<Trade> trades;
	private final RewardForReward rewardForReward;
	private final RewardForCard rewardForCard;

	public BuildingCard(String name, int period, Set<Reward> costs, Set<Reward> fastRewards, int diceProductionAction,
						Set<Reward> earnings, List<Trade> trades, RewardForReward rewardForReward, RewardForCard rewardForCard){
		super(name, CardType.BUILDING, period, costs, fastRewards, null);
		this.diceProductionAction = diceProductionAction;
		this.earnings = earnings;
		this.trades = trades;
		this.rewardForReward = rewardForReward;
		this.rewardForCard = rewardForCard;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		BuildingCard that = (BuildingCard) o;

		if (diceProductionAction != that.diceProductionAction) return false;
		if (earnings != null ? !earnings.equals(that.earnings) : that.earnings != null) return false;
		if (trades != null ? !trades.equals(that.trades) : that.trades != null) return false;
		if (rewardForReward != null ? !rewardForReward.equals(that.rewardForReward) : that.rewardForReward != null)
			return false;
		return rewardForCard != null ? rewardForCard.equals(that.rewardForCard) : that.rewardForCard == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + diceProductionAction;
		result = 31 * result + (earnings != null ? earnings.hashCode() : 0);
		result = 31 * result + (trades != null ? trades.hashCode() : 0);
		result = 31 * result + (rewardForReward != null ? rewardForReward.hashCode() : 0);
		result = 31 * result + (rewardForCard != null ? rewardForCard.hashCode() : 0);
		return result;
	}

	@Override
	public String toString(){
		String cardString = super.toString();
		cardString += "Dice production: " + diceProductionAction + "\n";
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

	public int getDiceProductionAction() {
		return diceProductionAction;
	}

	public Set<Reward> getEarnings() {
		return earnings;
	}

	public List<Trade> getTrades() {
		return trades;
	}

	public RewardForReward getRewardForReward() {
		return rewardForReward;
	}

	public RewardForCard getRewardForCard() {
		return rewardForCard;
	}
}
