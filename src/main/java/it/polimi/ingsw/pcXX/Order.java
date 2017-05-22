package it.polimi.ingsw.pcXX;

import java.util.ArrayList;
import java.util.LinkedList;

public class Order{
	private final int playerNumber;
	private ArrayList<PlayerColor> shown;
	private ArrayList<PlayerColor> real;
	private PlayerColor current;

	public Order(ArrayList<PlayerColor> initialOrder){
		this.playerNumber = initialOrder.size();
		this.shown = initialOrder;
		this.current = initialOrder.get(0);
		this.real = calculateRealOrder(initialOrder);
	}

	private ArrayList<PlayerColor> calculateRealOrder(ArrayList<PlayerColor> shown){
		ArrayList<PlayerColor> real = new ArrayList<PlayerColor>();
		int familyMemberNumber = 4;
		for(int familyMember = 0; familyMember < familyMemberNumber; familyMember++){
			for(int player = 0; player < shown.size(); player++){
				real.add((familyMember * shown.size()) + player, shown.get(player));
			}
		}
		return real;
	}

	private ArrayList<PlayerColor> calculateShownOrder(LinkedList<FamilyMember> councilPalaceOrder){
		ArrayList<PlayerColor> newOrder = new ArrayList<PlayerColor>(shown);
		for(int i = 0; i < councilPalaceOrder.size(); i++){
			newOrder.add(i, councilPalaceOrder.get(i).getPlayerColor());
		}
		removeBottomDuplicates(newOrder);
		return newOrder;
	}

	private void removeBottomDuplicates(ArrayList<PlayerColor> arrayList){
		for(int i = 0; i < arrayList.size() - 1; i++){
			for(int j = i + 1; j < arrayList.size(); j++){
				if(arrayList.get(i).equals(arrayList.get(j)))
					arrayList.remove(j);
			}
		}
	}
}
