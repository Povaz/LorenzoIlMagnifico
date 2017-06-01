package it.polimi.ingsw.pcXX;

import java.util.ArrayList;
import java.util.List;

public class Tower {
	private final List<Floor> floors;
	private boolean occupied;
	private final CardType type;

	public Tower(CardType type){
		this.occupied = false;
		this.type = type;
		this.floors = initializeFloors(type);
	}

	private List<Floor> initializeFloors(CardType type){
		ArrayList<Floor> floors = new ArrayList<Floor>();

		Reward reward5 = null;
		Reward reward7 = null;
		switch(type){
			case TERRITORY:
				reward5 = new Resource(ResourceType.WOOD, 1);
				reward7 = new Resource(ResourceType.WOOD, 2);
				break;
			case CHARACTER:
				reward5 = new Resource(ResourceType.STONE, 1);
				reward7 = new Resource(ResourceType.STONE, 2);
				break;
			case BUILDING:
				reward5 = new Point(PointType.MILITARY_POINT, 1);
				reward7 = new Point(PointType.MILITARY_POINT, 2);
				break;
			case VENTURE:
				reward5 = new Resource(ResourceType.COIN, 1);
				reward7 = new Resource(ResourceType.COIN, 2);
		}

		floors.add(0, new Floor(1, null));
		floors.add(1, new Floor(3, null));
		floors.add(2, new Floor(5, reward5));
		floors.add(3, new Floor(7, reward7));
		return floors;
	}

	public List<Floor> getFloors() {
		return floors;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	public CardType getType() {
		return type;
	}

	public void reinitialize(){
		this.occupied = false;
		for(Floor f : floors){
			f.reinitialize();
		}
	}
}
