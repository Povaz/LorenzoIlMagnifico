package it.polimi.ingsw.pcXX;

public class FamilyMember {
	private boolean used;
	protected int value;
	private boolean changes;
	private Player player;
	private FamilyColor color;

	public FamilyMember(boolean used, int value, boolean changes){
		this.used = used;
		this.value = value;
		this.changes = changes;
	}

	public FamilyMember(boolean used, int value, boolean changes, Player player, FamilyColor color){
		this.used = used;
		this.value = value;
		this.changes = changes;
		this.player = player;
		this.color = color;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FamilyMember that = (FamilyMember) o;

		if (used != that.used) return false;
		if (value != that.value) return false;
		if (changes != that.changes) return false;
		if (!player.equals(that.player)) return false;
		return color == that.color;
	}

	@Override
	public int hashCode() {
		int result = (used ? 1 : 0);
		result = 31 * result + value;
		result = 31 * result + (changes ? 1 : 0);
		result = 31 * result + player.hashCode();
		result = 31 * result + color.hashCode();
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

	public boolean isChanges() {
		return changes;
	}

	public void setChanges(boolean changes) {
		this.changes = changes;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public FamilyColor getColor() {
		return color;
	}

	public void setColor(FamilyColor color) {
		this.color = color;
	}
}
