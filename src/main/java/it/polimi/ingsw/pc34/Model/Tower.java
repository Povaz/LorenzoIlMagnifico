package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.JSONUtility;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tower {
	Logger LOGGER = Logger.getLogger(Tower.class.getName());

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

		Set<Reward> reward0;
		Set<Reward> reward1;
		Set<Reward> reward2;
		Set<Reward> reward3;
		try{
			reward0 = JSONUtility.getSpotRewards(type.toActionType(), 0);
			reward1 = JSONUtility.getSpotRewards(type.toActionType(), 1);
			reward2 = JSONUtility.getSpotRewards(type.toActionType(), 2);
			reward3 = JSONUtility.getSpotRewards(type.toActionType(), 3);
		} catch(JSONException e){
			reward0 = new HashSet<>();
			reward1 = new HashSet<>();
			reward2 = new HashSet<>();
			reward3 = new HashSet<>();
			switch(type){
				case TERRITORY:
					reward2.add(new Reward(RewardType.WOOD, 1));
					reward3.add(new Reward(RewardType.WOOD, 2));
					break;
				case CHARACTER:
					reward2.add(new Reward(RewardType.STONE, 1));
					reward3.add(new Reward(RewardType.STONE, 2));
					break;
				case BUILDING:
					reward2.add(new Reward(RewardType.MILITARY_POINT, 1));
					reward3.add(new Reward(RewardType.MILITARY_POINT, 2));
					break;
				case VENTURE:
					reward2.add(new Reward(RewardType.COIN, 1));
					reward3.add(new Reward(RewardType.COIN, 2));
					break;
			}
			LOGGER.log(Level.WARNING, "Config.json: Wrong format", e);
		} catch(IOException e){
			reward0 = new HashSet<>();
			reward1 = new HashSet<>();
			reward2 = new HashSet<>();
			reward3 = new HashSet<>();
			switch(type){
				case TERRITORY:
					reward2.add(new Reward(RewardType.WOOD, 1));
					reward3.add(new Reward(RewardType.WOOD, 2));
					break;
				case CHARACTER:
					reward2.add(new Reward(RewardType.STONE, 1));
					reward3.add(new Reward(RewardType.STONE, 2));
					break;
				case BUILDING:
					reward2.add(new Reward(RewardType.MILITARY_POINT, 1));
					reward3.add(new Reward(RewardType.MILITARY_POINT, 2));
					break;
				case VENTURE:
					reward2.add(new Reward(RewardType.COIN, 1));
					reward3.add(new Reward(RewardType.COIN, 2));
					break;
			}
			LOGGER.log(Level.WARNING, "Config.json: Incorrect path", e);
		}

		floors.add(0, new Floor(1, reward0, this));
		floors.add(1, new Floor(3, reward1, this));
		floors.add(2, new Floor(5, reward2, this));
		floors.add(3, new Floor(7, reward3, this));
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
		StringBuilder bld = new StringBuilder();
		bld.append("Occupied: "+ occupied + "\n");
		for(Floor f : floors){
			bld.append(f.toString() + "\n");
		}
		return bld.toString();
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
