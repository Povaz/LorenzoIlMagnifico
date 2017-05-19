package it.polimi.ingsw.pcXX;

public class BuildingCard extends DevelopmentCard{
	private int diceProductionAction;
	private Reward[] retrieved;
	private Trade[] trades;
	private RewardForReward rewardForReward;
	private RewardForCard rewardForCard;
	
	public BuildingCard(){
		super(null,null,0,null,null);
	}
}
