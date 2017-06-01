package it.polimi.ingsw.pcXX;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Order{
	private final int playerNumber;
	private List<PlayerColor> shown;
	private List<PlayerColor> real;
	private PlayerColor current;

	public Order(int playerNumber){
		this.playerNumber = playerNumber;
		this.shown = initializeOrder();
		this.real = calculateRealOrder(shown);
		this.current = real.get(0);
	}

	private List<PlayerColor> initializeOrder(){
		int[] randArray = RandomUtility.randomIntArray(1, playerNumber);
		List<PlayerColor> shown = new ArrayList<>();
		for(int i = 0; i < randArray.length; i++){
			shown.add(i, PlayerColor.fromInt(randArray[i]));
		}
		return shown;
	}

	public static List<PlayerColor> calculateRealOrder(List<PlayerColor> shown){
		List<PlayerColor> real = new ArrayList<>();
		int familyMemberNumber = 4;
		for(int familyMember = 0; familyMember < familyMemberNumber; familyMember++){
			for(int player = 0; player < shown.size(); player++){
				real.add((familyMember * shown.size()) + player, shown.get(player));
			}
		}
		return real;
	}

	private List<PlayerColor> calculateShownOrder(List<FamilyMember> councilPalaceOrder){
		List<PlayerColor> newOrder = new ArrayList<>(shown);
		for(int i = 0; i < councilPalaceOrder.size(); i++){
			newOrder.add(i, councilPalaceOrder.get(i).getPlayerColor());
		}
		removeBottomDuplicates(newOrder);
		return newOrder;
	}

	public static void removeBottomDuplicates(List<PlayerColor> list){
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
}
