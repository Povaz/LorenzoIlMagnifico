package it.polimi.ingsw.pcXX;

import java.util.ArrayList;
import java.util.List;

public class Order{

	// TODO playercolor --> player
	private final int playerNumber;
	private List<PlayerColor> shown;
	private List<PlayerColor> real;
	private int current;

	public Order(int playerNumber){
		this.playerNumber = playerNumber;
		initializeOrder();
		calculateRealOrder();
		this.current = 0;
	}

	private void initializeOrder(){
		int[] randArray = RandomUtility.randomIntArray(1, playerNumber);
		List<PlayerColor> shown = new ArrayList<>();
		for(int i = 0; i < randArray.length; i++){
			shown.add(i, PlayerColor.fromInt(randArray[i]));
		}
		this.shown = shown;
	}

	public void recalculate(List<FamilyMember> councilPalaceOrder){
		calculateShownOrder(councilPalaceOrder);
		calculateRealOrder();
		current = 0;
	}

	private void calculateShownOrder(List<FamilyMember> councilPalaceOrder){
		List<PlayerColor> newOrder = new ArrayList<>(shown);
		for(int i = 0; i < councilPalaceOrder.size(); i++){
			newOrder.add(i, councilPalaceOrder.get(i).getPlayerColor());
		}
		removeBottomDuplicates(newOrder);
		this.shown = newOrder;
	}

	private void calculateRealOrder(){
		List<PlayerColor> real = new ArrayList<>();
		int familyMemberNumber = 4;
		for(int familyMember = 0; familyMember < familyMemberNumber; familyMember++){
			for(int player = 0; player < shown.size(); player++){
				real.add((familyMember * shown.size()) + player, shown.get(player));
			}
		}
		this.real = real;
	}


	private void removeBottomDuplicates(List<PlayerColor> list){
		for(int i = 0; i < list.size() - 1; i++){
			for(int j = i + 1; j < list.size(); j++){
				if(list.get(i).equals(list.get(j)))
					list.remove(j);
			}
		}
	}

	public int getPositionOrder(PlayerColor playerColor){
		return shown.indexOf(playerColor);
	}

	public PlayerColor getCurrent() {
		return real.get(current);
	}

	public boolean nextOrder(){
		if(real.size() > current + 1){
			current++;
			return true;
		}
		return false;
	}
}
