package it.polimi.ingsw.pcXX;

import java.util.ArrayList;

public class Order{
	private final int playerNumber;
	private ArrayList<PlayerColor> shown;
	private ArrayList<PlayerColor> real;
	private PlayerColor current;

	public Order(ArrayList<PlayerColor> initialOrder){
		this.playerNumber = initialOrder.size();
		this.shown = initialOrder;
		this.current = initialOrder.get(0);
		this.real = initializeOrder(initialOrder);
	}

	private ArrayList<PlayerColor> initializeOrder(ArrayList<PlayerColor> initialOrder){
		ArrayList<PlayerColor> real = new ArrayList<PlayerColor>();
		for(int familyMember = 0; familyMember < 4; familyMember++){
			for(int player = 0; player < initialOrder.size(); player++){
				real.add((player * familyMember) + player, initialOrder.get(player));
			}
		}
		return real;
	}
}
