package it.polimi.ingsw.pcXX;

import java.util.LinkedList;
import java.util.Set;

public class CharacterCard extends DevelopmentCard{
	private LinkedList<GhostFamilyMember> actions;
	private boolean noBonusTowerResource;
	private LinkedList<CostDiscount> discounts;
	private LinkedList<ActionModifier> actionModifiers;
	private RewardForReward rewardForReward;
	private RewardForCard rewardForCard;
	
	public CharacterCard(String name, int period, Set<Reward> costs, Set<Reward> fastRewards, LinkedList<GhostFamilyMember> actions,
						 boolean noBonusTowerResource, LinkedList<CostDiscount> discounts, LinkedList<ActionModifier> actionModifiers,
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
}
