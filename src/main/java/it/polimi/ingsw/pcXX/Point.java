package it.polimi.ingsw.pcXX;

public class Point extends Reward{
	private PointType type;
	private int quantity;

	public Point(PointType type, int quantity){
		this.type = type;
		this.quantity = quantity;
	}

	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Point point = (Point) o;

		if (quantity != point.quantity) return false;
		return type == point.type;
	}

	@Override
	public int hashCode(){
		int result = type.hashCode();
		result = 31 * result + quantity;
		return result;
	}

	@Override
	public String toString() {
		return "" + quantity + " " + type.toString();
	}
}
