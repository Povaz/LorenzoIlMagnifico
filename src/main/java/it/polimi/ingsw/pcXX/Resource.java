package it.polimi.ingsw.pcXX;

public class Resource extends Reward{
	private ResourceType type;
	private int quantity;

	public Resource(ResourceType type, int quantity){
		this.type = type;
		this.quantity = quantity;
	}

	@Override
	public String toString(){
		return "" + type.toString() + " = " + quantity;
	}
}
