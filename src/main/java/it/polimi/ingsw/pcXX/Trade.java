package it.polimi.ingsw.pcXX;

import java.util.Set;

public class Trade {
	private final Set<Reward> give;
	private final Set<Reward> take;

	public Trade(Set<Reward> give, Set<Reward> take){
		this.give = give;
		this.take = take;
	}

	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Trade trade = (Trade) o;

		if (!give.equals(trade.give)) return false;
		return take.equals(trade.take);
	}

	@Override
	public int hashCode(){
		int result = give.hashCode();
		result = 31 * result + take.hashCode();
		return result;
	}

	@Override
	public String toString(){
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

	public Set<Reward> getGive() {
		return give;
	}

	public Set<Reward> getTake() {
		return take;
	}
}
