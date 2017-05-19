package it.polimi.ingsw.pcXX;

import java.util.Set;

public class TerritoryCard extends DevelopmentCard{
	private int diceHarvestAction;
	private Set<Reward> earnings;

	public TerritoryCard(String name, int period, Set<Reward> fastRewards, int diceHarvestAction, Set<Reward> earnings){
		super(name, CardType.TERRITORY, period, null, fastRewards);
		this.diceHarvestAction = diceHarvestAction;
		this.earnings = earnings;
	}


}
