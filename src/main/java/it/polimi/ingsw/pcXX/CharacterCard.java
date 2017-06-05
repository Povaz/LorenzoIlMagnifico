package it.polimi.ingsw.pcXX;

import java.util.List;
import java.util.Set;

public class CharacterCard extends DevelopmentCard{
	private final List<GhostFamilyMember> actions;
	private final boolean noBonusTowerResource;
	private final List<CostDiscount> discounts;
	private final List<ActionModifier> actionModifiers;
	private final RewardForReward rewardForReward;
	private final RewardForCard rewardForCard;
	
	public CharacterCard(String name, int period, Set<Reward> costs, Set<Reward> fastRewards, List<GhostFamilyMember> actions,
						 boolean noBonusTowerResource, List<CostDiscount> discounts, List<ActionModifier> actionModifiers,
						 RewardForReward rewardForReward, RewardForCard rewardForCard){
		super(name, CardType.CHARACTER, period, costs, fastRewards);
		this.actions = actions;
		this.noBonusTowerResource = noBonusTowerResource;
		this.discounts = discounts;
		this.actionModifiers = actionModifiers;
		this.rewardForReward = rewardForReward;
		this.rewardForCard = rewardForCard;
	}

	@Override
	public String toString() {
		String cardString = super.toString();
		cardString += "No bonus tower resource: " + noBonusTowerResource + "\n";
		if(actions != null){
			cardString += "Actions:\n";
			for (GhostFamilyMember g : actions){
				cardString += "  " + g.toString() + "\n";
			}
		}
		if(discounts != null){
			cardString += "Discounts:\n";
			for (CostDiscount d : discounts) {
				cardString += "  " + d.toString() + "\n";
			}
		}
		if(actionModifiers != null){
			cardString += "Dice modifiers:\n";
			for (ActionModifier d : actionModifiers) {
				cardString += "  " + d.toString() + "\n";
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		CharacterCard that = (CharacterCard) o;
        
		if (noBonusTowerResource != that.noBonusTowerResource) return false;
		if (actions != null ? !actions.equals(that.actions) : that.actions != null) return false;
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
		result = 31 * result + (actions != null ? actions.hashCode() : 0);
		result = 31 * result + (noBonusTowerResource ? 1 : 0);
		result = 31 * result + (discounts != null ? discounts.hashCode() : 0);
		result = 31 * result + (actionModifiers != null ? actionModifiers.hashCode() : 0);
		result = 31 * result + (rewardForReward != null ? rewardForReward.hashCode() : 0);
		result = 31 * result + (rewardForCard != null ? rewardForCard.hashCode() : 0);
		return result;
	}

	@Override
	public boolean isPlaceable(Counter copyForCosts, Counter counterMod, PlayerBoard playerBoard){
		if(!playerBoard.getCharacterSpot().canPlaceCard(copyForCosts)){
			return false;
		}
		if(!canBuyCard(copyForCosts, counterMod)){
			return false;
		}
		return true;
	}

	@Override
	public void place(PlayerBoard playerBoard){
		playerBoard.getCharacterSpot().placeCard(this);
	}
}
