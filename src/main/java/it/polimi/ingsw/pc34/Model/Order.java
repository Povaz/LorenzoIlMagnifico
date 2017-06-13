package it.polimi.ingsw.pc34.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order{
	private final int playerNumber;
	private List<Player> shown;
	private List<Player> real;
	private int current;
	
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
		// se hanno la scomunica saltano il primo round
		for(int i = 0; i < shown.size(); i++){
			if(real.get(i).getPlayerBoard().getModifier().isJumpFirstRound()){
				real.add(real.get(i));
				real.remove(i);
				i--;
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

	public String toString() {
		String string = "";
		for(Player p : shown){
			string += "  " + p.getUsername() + "\n";
		}
		string += "\n";
		return string;
	}
}
