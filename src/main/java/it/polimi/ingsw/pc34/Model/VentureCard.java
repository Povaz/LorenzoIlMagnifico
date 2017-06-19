package it.polimi.ingsw.pc34.Model;

import java.util.List;
import java.util.Set;

public class VentureCard extends DevelopmentCard{
	private final Reward militaryPointPrice;
	private final Reward militaryPointNeeded;
	private final Reward victoryPointEarned;
	
	public VentureCard(String name, int period, Set<Reward> costs, Set<Reward> fastRewards, List<FamilyMember> actions,
					   Reward militaryPointNeeded, Reward militaryPointPrice, Reward victoryPointEarned){
		super(name, CardType.VENTURE, period, costs, fastRewards, actions);
		this.militaryPointNeeded = militaryPointNeeded;
		this.militaryPointPrice = militaryPointPrice;
		this.victoryPointEarned = victoryPointEarned;
	}

	@Override
	public String toString(){
		StringBuilder bld = new StringBuilder();
		bld.append(super.toString());
		if(militaryPointNeeded != null || militaryPointPrice != null){
			bld.append("    Military point needed: " + militaryPointNeeded + "\n");
			bld.append("    Military point price: " + militaryPointPrice + "\n");
		}
		bld.append("    Victory point earned: "+ victoryPointEarned + "\n");
		return bld.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		VentureCard that = (VentureCard) o;

		if (militaryPointPrice != null ? !militaryPointPrice.equals(that.militaryPointPrice) : that.militaryPointPrice != null)
			return false;
		if (militaryPointNeeded != null ? !militaryPointNeeded.equals(that.militaryPointNeeded) : that.militaryPointNeeded != null)
			return false;
		return victoryPointEarned.equals(that.victoryPointEarned);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (militaryPointPrice != null ? militaryPointPrice.hashCode() : 0);
		result = 31 * result + (militaryPointNeeded != null ? militaryPointNeeded.hashCode() : 0);
		result = 31 * result + victoryPointEarned.hashCode();
		return result;
	}

	public Reward getMilitaryPointPrice(){
		return militaryPointPrice;
	}

	public Reward getMilitaryPointNeeded(){
		return militaryPointNeeded;
	}

	public Reward getVictoryPointEarned(){
		return victoryPointEarned;
	}
}
