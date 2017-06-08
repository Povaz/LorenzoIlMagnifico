package it.polimi.ingsw.pcXX;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Order{

	// TODO playercolor --> player
	private final int playerNumber;
	private List<Player> shown;
	private List<Player> real;
	private int current;
	
	public String toString() {
		String orderString = null;
		orderString +="Player number : " + playerNumber + "\n";
		orderString +="current : " + current + "\n";
		
		int contatore = 1;
		Player element;
		Iterator<Player> iteratorShown = shown.iterator();
		while(iteratorShown.hasNext()){
    	  element = (Player) iteratorShown.next();
    	  orderString += "Shown n° " + contatore + " : " + element.toString() + "\n";
    	  contatore++;
    	}
		contatore = 1;
		Iterator<Player> iteratorReal = real.iterator();
		while(iteratorReal.hasNext()){
    	  element = (Player) iteratorReal.next();
    	  orderString += "Real n° " + contatore + " : " + element.toString() + "\n";
    	  contatore++;
    	}
		return orderString;
	}
	
	public Order(List<Player> players){
		this.playerNumber = players.size();
		initializeOrder(players);
		calculateRealOrder();
		this.current = 0;
	}

	private void initializeOrder(List<Player> players){
		List<Player> shown = new ArrayList<>(players);
		Collections.shuffle(shown);
		this.shown = shown;
	}

	public void recalculate(List<FamilyMember> councilPalaceOrder){
		calculateShownOrder(councilPalaceOrder);
		calculateRealOrder();
		this.current = 0;
	}

	private void calculateShownOrder(List<FamilyMember> councilPalaceOrder){
		List<Player> newOrder = new ArrayList<>(shown);
		for(int i = 0; i < councilPalaceOrder.size(); i++){
			newOrder.add(i, councilPalaceOrder.get(i).getPlayer());
		}
		removeBottomDuplicates(newOrder);
		this.shown = newOrder;
	}

	private void calculateRealOrder(){
		List<Player> real = new ArrayList<>();
		int familyMemberNumber = 4;
		for(int familyMember = 0; familyMember < familyMemberNumber; familyMember++){
			for(int player = 0; player < shown.size(); player++){
				real.add((familyMember * shown.size()) + player, shown.get(player));
			}
		}
		this.real = real;
	}


	private void removeBottomDuplicates(List<Player> list){
		for(int i = 0; i < list.size() - 1; i++){
			for(int j = i + 1; j < list.size(); j++){
				if(list.get(i).sameColor(list.get(j))){
					list.remove(j);
				}
			}
		}
	}

	public List<Player> getShown() {
		return shown;
	}

	public int getPositionOrder(PlayerColor playerColor){
		return shown.indexOf(playerColor);
	}

	public Player getCurrent() {
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
