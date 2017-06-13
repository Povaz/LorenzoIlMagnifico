package it.polimi.ingsw.pc34.Model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CharacterCard extends DevelopmentCard{
	private final boolean noBonusTowerResource;
	private Map<CardType, List<List<Reward>>> discounts;
	private final Map<ActionType, Integer> actionModifiers;
	private final RewardForReward rewardForReward;
	private final RewardForCard rewardForCard;
	
	public CharacterCard(String name, int period, Set<Reward> costs, Set<Reward> fastRewards, List<FamilyMember> actions,
						 boolean noBonusTowerResource, Map<CardType, List<List<Reward>>> discounts,
						 Map<ActionType, Integer> actionModifiers, RewardForReward rewardForReward, RewardForCard rewardForCard){
		super(name, CardType.CHARACTER, period, costs, fastRewards, actions);
		this.noBonusTowerResource = noBonusTowerResource;
		this.discounts = discounts;
		this.actionModifiers = actionModifiers;
		this.rewardForReward = rewardForReward;
		this.rewardForCard = rewardForCard;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		CharacterCard that = (CharacterCard) o;

		if (noBonusTowerResource != that.noBonusTowerResource) return false;
		if (discounts != null ? !discounts.equals(that.discounts) : that.discounts != null) return false;
		if (actionModifiers != null ? !actionModifiers.equals(that.actionModifiers) : that.actionModifiers != null)
			return false;
		if (rewardForReward != null ? !rewardForReward.equals(that.rewardForReward) : that.rewardForReward != null)
			return false;
		return rewardForCard != null ? rewardForCard.equals(that.rewardForCard) : that.rewardForCard == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (noBonusTowerResource ? 1 : 0);
		result = 31 * result + (discounts != null ? discounts.hashCode() : 0);
		result = 31 * result + (actionModifiers != null ? actionModifiers.hashCode() : 0);
		result = 31 * result + (rewardForReward != null ? rewardForReward.hashCode() : 0);
		result = 31 * result + (rewardForCard != null ? rewardForCard.hashCode() : 0);
		return result;
	}

	public boolean isNoBonusTowerResource() {
		return noBonusTowerResource;
	}

	public Map<CardType, List<List<Reward>>> getDiscounts() {
		return discounts;
	}

	public Map<ActionType, Integer> getActionModifiers() {
		return actionModifiers;
	}

	public RewardForReward getRewardForReward() {
		return rewardForReward;
	}

	public RewardForCard getRewardForCard() {
		return rewardForCard;
	}

	@Override
	public String toString() {
		String cardString = super.toString();
		cardString += "No bonus tower resource: " + noBonusTowerResource + "\n";
		if(discounts != null){
			cardString += "Discounts:\n";
			for(CardType cT : discounts.keySet()){
				cardString += "  " + cT + ":\n";
				int i = 0;
				for(List<Reward> l : discounts.get(cT)){
					cardString += "    " + i + ".";
					for(Reward r : l){
						cardString += "  " + r.toString() + ";";
					}
					i++;
					cardString += "\n";
				}
			}
		}
		if(actionModifiers != null){
			cardString += "Dice modifiers:\n";
			for(ActionType d : actionModifiers.keySet()) {
				cardString += "  " + d.toString() + ": " + actionModifiers.get(d) + "\n";
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
