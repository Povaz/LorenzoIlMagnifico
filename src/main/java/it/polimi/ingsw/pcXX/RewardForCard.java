package it.polimi.ingsw.pcXX;

import java.util.Set;

public class RewardForCard {
	private Set<Reward> earned;
	private CardType cardTypeOwned;

	public RewardForCard(Set<Reward> earned, CardType cardTypeOwned){
		this.earned = earned;
		this.cardTypeOwned = cardTypeOwned;
	}

	@Override
	public String toString(){
		String rfrString = "";
		for(Reward r : earned){
			rfrString += r.toString() + "; ";
		}
		rfrString += "X " + cardTypeOwned.toString();
		return rfrString;
	}
}
