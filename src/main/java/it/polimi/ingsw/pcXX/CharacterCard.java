package it.polimi.ingsw.pcXX;

public class CharacterCard extends DevelopmentCard{
	private GhostFamilyMember[] actions;
	private boolean noBonusTowerResource;
	private Resource[] characterDiscount;
	private Resource[] buildingDiscount;
	private int harvestModifier;
	private int productionModifier;
	private int territoryTowerModifier;
	private int buildingTowerModifier;
	private int characterTowerModifier;
	private int ventureTowerModifier;
	private Trade[] trades;
	private RewardForReward rewardForReward;
	private RewardForCard rewardForCard;
	
	public CharacterCard(){
		super(null,null,0,null,null);
	}
}
