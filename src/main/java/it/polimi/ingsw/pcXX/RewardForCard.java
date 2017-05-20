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
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RewardForCard that = (RewardForCard) o;

		if (!earned.equals(that.earned)) return false;
		return cardTypeOwned == that.cardTypeOwned;
	}

	@Override
	public int hashCode(){
		int result = earned.hashCode();
		result = 31 * result + cardTypeOwned.hashCode();
		return result;
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
