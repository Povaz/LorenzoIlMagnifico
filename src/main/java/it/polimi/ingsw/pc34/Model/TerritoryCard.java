package it.polimi.ingsw.pc34.Model;

import java.util.Set;

public class TerritoryCard extends DevelopmentCard{
	private final int diceHarvestAction;
	private final Set<Reward> earnings;

	public TerritoryCard(String name, String path, int period, Set<Reward> fastRewards, int diceHarvestAction, Set<Reward> earnings){
		super(name, path, CardType.TERRITORY, period, null, fastRewards, null);
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
		StringBuilder bld = new StringBuilder();
		bld.append(super.toString());

		bld.append("    Dice harvest: " + diceHarvestAction + "\n");
		if(earnings != null){
			bld.append("    Earnings: ");
			for(Reward r : earnings){
				bld.append(r.toString() + ";  ");
			}
			bld.append("\n");
		}
		return bld.toString();
	}

	public int getDiceHarvestAction(){
		return diceHarvestAction;
	}

	public Set<Reward> getEarnings(){
		return earnings;
	}
}
