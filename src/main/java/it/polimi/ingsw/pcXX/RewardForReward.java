package it.polimi.ingsw.pcXX;

import java.util.Set;

public class RewardForReward {
	private Set<Reward> earned;
	private Reward owned;

	public RewardForReward(Set<Reward> earned, Reward owned){
		this.earned = earned;
		this.owned = owned;
	}

	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RewardForReward that = (RewardForReward) o;

		if (!earned.equals(that.earned)) return false;
		return owned.equals(that.owned);
	}

	@Override
	public int hashCode(){
		int result = earned.hashCode();
		result = 31 * result + owned.hashCode();
		return result;
	}

	@Override
	public String toString(){
		String rfcString = "";
		for(Reward r : earned){
			rfcString += r.toString() + "; ";
		}
		rfcString += "X " + owned.toString();
		return rfcString;
	}
}
