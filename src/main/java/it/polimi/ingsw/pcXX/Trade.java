package it.polimi.ingsw.pcXX;

import java.util.Set;

public class Trade {
	private Set<Reward> give;
	private Set<Reward> take;

	public Trade(Set<Reward> give, Set<Reward> take){
		this.give = give;
		this.take = take;
	}

	@Override
	public String toString() {
		String tradeString = "";
		for(Reward r : give){
			tradeString += r.toString() + "; ";
		}
		tradeString += "--> ";
		for(Reward r : take){
			tradeString += r.toString() + "; ";
		}
		return tradeString;
	}
}
