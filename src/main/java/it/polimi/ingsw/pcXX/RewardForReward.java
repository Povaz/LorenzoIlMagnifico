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
	public String toString(){
		String rfcString = "";
		for(Reward r : earned){
			rfcString += r.toString() + "; ";
		}
		rfcString += "X " + owned.toString();
		return rfcString;
	}
}
