package it.polimi.ingsw.pcXX;

public class FamilyMember {
	private boolean used;
	protected int value;
	private boolean changes;

	public FamilyMember(boolean used, int value, boolean changes){
		this.used = used;
		this.value = value;
		this.changes = changes;
	}
}
