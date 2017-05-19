package it.polimi.ingsw.pcXX;

public class Point extends Reward{
	private PointType type;
	private int quantity;

	public Point(PointType type, int quantity){
		this.type = type;
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "" + type.toString() + " = " + quantity;
	}
}
