package it.polimi.ingsw.pcXX;

import java.util.LinkedList;
import java.util.Set;

public class VentureCard extends DevelopmentCard{
	private final LinkedList<GhostFamilyMember> actions;
	private final Point militaryPointPrice;
	private final Point militaryPointNeeded;
	private final Point victoryPointEarned;
	
	public VentureCard(String name, int period, Set<Reward> costs, Set<Reward> fastRewards, LinkedList<GhostFamilyMember> actions,
					   Point militaryPointNeeded, Point militaryPointPrice, Point victoryPointEarned){
		super(name, CardType.VENTURE, period, costs, fastRewards);
		this.actions = actions;
		this.militaryPointNeeded = militaryPointNeeded;
		this.militaryPointPrice = militaryPointPrice;
		this.victoryPointEarned = victoryPointEarned;
	}

	@Override
	public String toString(){
		String cardString = super.toString();
		if(militaryPointNeeded != null){
			cardString += "Military point needed: " + militaryPointNeeded + "\n";
		}
		if(militaryPointPrice != null){
			cardString += "Military point price: " + militaryPointPrice + "\n";
		}
		cardString += "Victory point earned: "+ victoryPointEarned + "\n";
		if(actions != null){
			cardString += "Actions:\n";
			for (GhostFamilyMember g : actions){
				cardString += "  " + g.toString() + "\n";
			}
		}
		return cardString;
	}
}
