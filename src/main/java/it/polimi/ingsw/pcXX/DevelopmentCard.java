package it.polimi.ingsw.pcXX;

import java.util.Set;

public abstract class DevelopmentCard {
	private String name;
	private CardType type;
	private int period;
	private Set<Resource> costs;
	private Set<Reward> fastRewards;

	public DevelopmentCard(String name, CardType type, int period, Set<Resource> costs, Set<Reward> fastRewards){
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

	public Set<Resource> getCosts(){
		return costs;
	}

	public Set<Reward> getFastRewards(){
		return fastRewards;
	}

	@Override
	public String toString(){
		String cardString = "";

		cardString += type.toString() + "\n";
		cardString += "Name: " + name + "\n";
		cardString += "Period: " + period + "\n";

		if(costs != null){
			cardString += "Costs: \n";
			for(Resource r: costs){
				cardString += "  " + r.toString() + "\n";
			}
		}
		if(fastRewards != null){
			cardString += "Fast rewards: \n";
			for(Reward r: fastRewards){
				cardString +=  "  " + r.toString() + "\n";
			}
		}

		return cardString;
	}
}
