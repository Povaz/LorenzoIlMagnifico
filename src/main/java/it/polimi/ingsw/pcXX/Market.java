package it.polimi.ingsw.pcXX;

import java.util.Set;

public class Market extends ActionSpot{
	private final Set<Reward> rewards;

	public Market(boolean active, boolean unrestricted, int diceValue, Set<Reward> rewards){
		super(active, unrestricted, diceValue);
		this.rewards = rewards;
	}
}
