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

	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		TerritoryCard that = (TerritoryCard) o;

		if (diceHarvestAction != that.diceHarvestAction) return false;
		return earnings.equals(that.earnings);
	}

	@Override
	public int hashCode(){
		int result = super.hashCode();
		result = 31 * result + diceHarvestAction;
		result = 31 * result + earnings.hashCode();
		return result;
	}

	@Override
	public String toString(){
		String cardString = super.toString();
		cardString += "Dice harvest: " + diceHarvestAction + "\n";
		if(earnings != null){
			cardString += "Earnings:\n";
			for(Reward r : earnings){
				cardString += "  " + r.toString() + "\n";
			}
		}
		return cardString;
	}
}
