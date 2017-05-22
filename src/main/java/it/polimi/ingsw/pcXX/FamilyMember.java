package it.polimi.ingsw.pcXX;

public class FamilyMember {
	private boolean used;
	protected int value;
	private boolean changes;
	private PlayerColor playerColor;
	private FamilyColor color;

	public FamilyMember(boolean used, int value, boolean changes){
		this.used = used;
		this.value = value;
		this.changes = changes;
	}

	public FamilyMember(boolean used, int value, boolean changes, PlayerColor playerColor, FamilyColor color){
		this.used = used;
		this.value = value;
		this.changes = changes;
		this.playerColor = playerColor;
		this.color = color;
	}

	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FamilyMember that = (FamilyMember) o;

		if (used != that.used) return false;
		if (value != that.value) return false;
		return changes == that.changes;
	}

	@Override
	public int hashCode(){
		int result = (used ? 1 : 0);
		result = 31 * result + value;
		result = 31 * result + (changes ? 1 : 0);
		return result;
	}

	public PlayerColor getPlayerColor(){
		return playerColor;
	}
}
