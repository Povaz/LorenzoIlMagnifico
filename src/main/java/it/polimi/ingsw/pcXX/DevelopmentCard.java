package it.polimi.ingsw.pcXX;

import java.util.Set;

public abstract class DevelopmentCard {
	private final String name;
	private final CardType type;
	private final int period;
	private final Set<Reward> costs;
	private final Set<Reward> fastRewards;

	public DevelopmentCard(String name, CardType type, int period, Set<Reward> costs, Set<Reward> fastRewards){
		this.name = name;
		this.type = type;
		this.period = period;
		this.costs = costs;
		this.fastRewards = fastRewards;
	}

	public String getName(){
		return name;
	}

	public CardType getType(){
		return type;
	}

	public int getPeriod(){
		return period;
	}

	public Set<Reward> getCosts(){
		return costs;
	}

	public Set<Reward> getFastRewards(){
		return fastRewards;
	}

	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DevelopmentCard that = (DevelopmentCard) o;

		if (period != that.period) return false;
		if (!name.equals(that.name)) return false;
		if (type != that.type) return false;
		if (costs != null ? !costs.equals(that.costs) : that.costs != null) return false;
		return fastRewards != null ? fastRewards.equals(that.fastRewards) : that.fastRewards == null;
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + type.hashCode();
		result = 31 * result + period;
		result = 31 * result + (costs != null ? costs.hashCode() : 0);
		result = 31 * result + (fastRewards != null ? fastRewards.hashCode() : 0);
		return result;
	}

	@Override
	public String toString(){
		String cardString = "";

		cardString += type.toString() + "\n";
		cardString += "Name: " + name + "\n";
		cardString += "Period: " + period + "\n";

		if(costs != null){
			cardString += "Costs:\n";
			for(Reward r: costs){
				cardString += "  " + r.toString() + "\n";
			}
		}
		if(fastRewards != null){
			cardString += "Fast rewards:\n";
			for(Reward r: fastRewards){
				cardString +=  "  " + r.toString() + "\n";
			}
		}

		return cardString;
	}
}
