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
		String rfcString = "";
		for(Reward r : earned){
			rfcString += r.toString() + "; ";
		}
		rfcString += "X " + cardTypeOwned.toString();
		return rfcString;
	}
}
