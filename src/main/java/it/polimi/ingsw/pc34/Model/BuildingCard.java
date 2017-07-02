package it.polimi.ingsw.pc34.Model;

import java.util.List;
import java.util.Set;

public class BuildingCard extends DevelopmentCard{
	private final int diceProductionAction;
	private final Set<Reward> earnings;
	private final List<Trade> trades;
	private final RewardForReward rewardForReward;
	private final RewardForCard rewardForCard;

	public BuildingCard(String name, String path, int period, Set<Reward> costs, Set<Reward> fastRewards, int diceProductionAction,
						Set<Reward> earnings, List<Trade> trades, RewardForReward rewardForReward, RewardForCard rewardForCard){
		super(name, path, CardType.BUILDING, period, costs, fastRewards, null);
		this.diceProductionAction = diceProductionAction;
		this.earnings = earnings;
		this.trades = trades;
		this.rewardForReward = rewardForReward;
		this.rewardForCard = rewardForCard;
	}

	@Override
	public boolean equals(Object o){
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
	public int hashCode(){
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
		StringBuilder bld = new StringBuilder();
		bld.append(super.toString());

		bld.append("    Dice production: " + diceProductionAction + "\n");
		if(earnings != null){
			bld.append("    Earnings: ");
			for(Reward r : earnings){
				bld.append(r.toString() + ";  ");
			}
			bld.append("\n");
		}
		if(trades != null){
			bld.append("    Trades:\n");
			for(Trade t : trades){
				bld.append("      " + t.toString() + "\n");
			}
		}
		if(rewardForReward != null){
			bld.append("    Reward for reward: " + rewardForReward.toString() + "\n");
		}
		if(rewardForCard != null){
			bld.append("    Reward for card: " + rewardForCard.toString() + "\n");
		}
		return bld.toString();
	}

	public int getDiceProductionAction(){
		return diceProductionAction;
	}

	public Set<Reward> getEarnings(){
		return earnings;
	}

	public List<Trade> getTrades(){
		return trades;
	}

	public RewardForReward getRewardForReward(){
		return rewardForReward;
	}

	public RewardForCard getRewardForCard(){
		return rewardForCard;
	}
}
