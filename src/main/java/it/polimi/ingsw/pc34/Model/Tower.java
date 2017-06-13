package it.polimi.ingsw.pc34.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tower {
	private final List<Floor> floors;
	private boolean occupied;
	private final CardType type;
	private final Set<Reward> occupiedTax;
	private final Board board;

	public Tower(CardType type, Board board){
		this.board = board;
		this.occupied = false;
		this.type = type;
		this.floors = initializeFloors(type);
		this.occupiedTax = new HashSet<>();
		occupiedTax.add(new Reward(RewardType.COIN, 3));
	}

	private List<Floor> initializeFloors(CardType type){
		List<Floor> floors = new ArrayList<>();

		Set<Reward> reward5 = new HashSet<>();
		Set<Reward> reward7 = new HashSet<>();
		switch(type){
			case TERRITORY:
				reward5.add(new Reward(RewardType.WOOD, 1));
				reward7.add(new Reward(RewardType.WOOD, 2));
				break;
			case CHARACTER:
				reward5.add(new Reward(RewardType.STONE, 1));
				reward7.add(new Reward(RewardType.STONE, 2));
				break;
			case BUILDING:
				reward5.add(new Reward(RewardType.MILITARY_POINT, 1));
				reward7.add(new Reward(RewardType.MILITARY_POINT, 2));
				break;
			case VENTURE:
				reward5.add(new Reward(RewardType.COIN, 1));
				reward7.add(new Reward(RewardType.COIN, 2));
		}

		floors.add(0, new Floor(1, null, this));
		floors.add(1, new Floor(3, null, this));
		floors.add(2, new Floor(5, reward5, this));
		floors.add(3, new Floor(7, reward7, this));
		return floors;
	}

	public void reinitialize(){
		this.occupied = false;
		for(Floor f : floors){
			f.reinitialize();
		}
	}

	@Override
	public String toString(){
		String string = "";
		for(Floor f : floors){
			string += f.toString() + "\n";
		}
		return string;
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

	public Set<Reward> getOccupiedTax() {
		return occupiedTax;
	}

	public Board getBoard(){
		return board;
	}
}
