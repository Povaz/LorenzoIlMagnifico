package it.polimi.ingsw.pcXX;

import it.polimi.ingsw.pcXX.Exception.TooMuchTimeException;

import java.util.List;
import java.util.Scanner;
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
		String cardString = super.toString();
		if(militaryPointNeeded != null){
			cardString += "Military point needed: " + militaryPointNeeded + "\n";
		}
		if(militaryPointPrice != null){
			cardString += "Military point price: " + militaryPointPrice + "\n";
		}
		cardString += "Victory point earned: "+ victoryPointEarned + "\n";
		return cardString;
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

	public Reward getMilitaryPointPrice() {
		return militaryPointPrice;
	}

	public Reward getMilitaryPointNeeded() {
		return militaryPointNeeded;
	}

	public Reward getVictoryPointEarned() {
		return victoryPointEarned;
	}

	@Override
	public boolean isPlaceable(Counter newCounter, PlayerBoard playerBoard) throws TooMuchTimeException {
		if(!playerBoard.getVentureSpot().canPlaceCard(newCounter)){
			return false;
		}
		if(!canBuyCard(newCounter)){
			return false;
		}
		return true;
	}

	@Override
	public void place(PlayerBoard playerBoard){
		playerBoard.getVentureSpot().placeCard(this);
	}

	@Override
	public boolean canBuyCard(Counter newCounter){
		if(militaryPointNeeded != null && militaryPointPrice != null && getCosts() != null){
			boolean payWithMilitaryPoint = howWantPayVentureCard(getCosts(), militaryPointNeeded, militaryPointPrice);
			if(payWithMilitaryPoint){
				return canBuyCardMilitaryPoint(newCounter);
			}
			else{
				return super.canBuyCard(newCounter);
			}
		}
		else if(militaryPointNeeded != null && militaryPointPrice != null){
			return canBuyCardMilitaryPoint(newCounter);
		}
		else{
			return super.canBuyCard(newCounter);
		}
	}

	private boolean canBuyCardMilitaryPoint(Counter newCounter){
		if(newCounter.getMilitaryPoint().getQuantity() >= militaryPointNeeded.getQuantity()){
			newCounter.subtract(militaryPointPrice);
			return newCounter.check();
		}
		return false;
	}

	//TODO con client
	private boolean howWantPayVentureCard(Set<Reward> costs, Reward militaryPointNeeded, Reward militaryPointPrice){
		System.out.println("COSTS:\n" + costs);
		System.out.println("\nPOINT NEEDED:\n" + militaryPointNeeded);
		System.out.println("POINT PRICE:\n" + militaryPointPrice);
		System.out.println("\nWANT TO PAY WITH MILITARY POINTS?");
		Scanner input = new Scanner(System.in);
		return input.nextBoolean();
	}
}
