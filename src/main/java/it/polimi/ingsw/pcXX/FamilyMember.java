package it.polimi.ingsw.pcXX;

import java.util.Iterator;
import java.util.Set;

public class FamilyMember{
	private boolean used;
	private int value;
	private final boolean ghost;
	private final Player player;
	private final FamilyColor color;
	private final ActionType action;
	private final Set<Reward> discounts;

	public String toString(){
		String familyMemberString = null;
		familyMemberString+="color:" + color.toString() + "\n";
		familyMemberString+="used : " + used + "\n";
		familyMemberString+="value : " + value + "\n";
		familyMemberString+="ghost : " + ghost + "\n";
		familyMemberString+="ActionType : " + action.toString() + "\n";
		int contatore = 1;
		Reward element;
		Iterator<Reward> iteratorDiscounts = discounts.iterator();
		while(iteratorDiscounts.hasNext()){
    	  element = (Reward) iteratorDiscounts.next();
    	  familyMemberString += "Discount n° " + contatore + " : " + element.toString() + "\n";
    	  contatore++;
    	}
		return familyMemberString;
	}
	
	public FamilyMember(Player player, FamilyColor color){
		this.used = false;
		this.value = 0;
		this.ghost = false;
		this.player = player;
		this.color = color;
		this.action = null;
		this.discounts = null;
	}

	public FamilyMember(ActionType action, int value, Set<Reward> discounts){
		this.used = false;
		this.value = value;
		this.ghost = true;
		this.player = null;
		this.color = FamilyColor.NEUTRAL;
		this.action = action;
		this.discounts = discounts;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FamilyMember that = (FamilyMember) o;

		if (used != that.used) return false;
		if (value != that.value) return false;
		if (player != null ? !player.equals(that.player) : that.player != null) return false;
		if (color != that.color) return false;
		if (action != that.action) return false;
		return discounts != null ? discounts.equals(that.discounts) : that.discounts == null;
	}

	@Override
	public int hashCode() {
		int result = (used ? 1 : 0);
		result = 31 * result + value;
		result = 31 * result + (player != null ? player.hashCode() : 0);
		result = 31 * result + color.hashCode();
		result = 31 * result + (action != null ? action.hashCode() : 0);
		result = 31 * result + (discounts != null ? discounts.hashCode() : 0);
		return result;
	}

	public boolean samePlayer(FamilyMember other){
		return player.equals(other.player);
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Player getPlayer() {
		return player;
	}

	public FamilyColor getColor() {
		return color;
	}

	public ActionType getAction() {
		return action;
	}

	public Set<Reward> getDiscounts() {
		return discounts;
	}
}
