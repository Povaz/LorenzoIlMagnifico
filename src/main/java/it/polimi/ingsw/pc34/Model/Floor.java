package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.Controller.GameController;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientType;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Set;

public class Floor extends ActionSpot{
	private DevelopmentCard card;
	private final Set<Reward> rewards;
	private final Tower tower;

	public Floor(int value, Set<Reward> rewards, Tower tower){
		super(true, false, value);
		this.card = null;
		this.rewards = rewards;
		this.tower = tower;
	}

	public DevelopmentCard getCard() {
		return card;
	}

	public void setCard(DevelopmentCard card) {
		this.card = card;
	}

	public Set<Reward> getRewards() {
		return rewards;
	}

	public Tower getTower() {
		return tower;
	}

	public void reinitialize(){
		super.reinitialize();
		card = null;
	}

	/* familyMember può essere piazzato nell'actionSpot:
    	- familiare non usato, spazio azione libero, valore dado sufficiente;
  	  	- se è un'azione aggiuntiva deve essere del tipo giusto;
  		- non ci possono essere due familiari dello stesso giocatore non neutrali nella stessa torre
    */
	@Override
	public boolean isPlaceable(FamilyMember familyMember, boolean canPlaceInBusyActionSpot, GameController gameController) throws RemoteException, IOException{
		if(!super.isPlaceable(familyMember, canPlaceInBusyActionSpot, gameController)){
			return false;
		}

		if(familyMember.isGhost()){
			if(familyMember.getAction() != null){
				if(familyMember.getAction() != ActionType.ALL){
					if(!tower.getType().sameType(familyMember.getAction())){
						if (familyMember.getPlayer().getClientType().equals(ClientType.GUI)) {
							gameController.sendMessageChatGUI(familyMember.getPlayer(), "You cannot place in this type of action spot!", true);
						}
						else {
							gameController.sendMessageCLI(familyMember.getPlayer(), "You cannot place in this type of action spot!");
						}
						return false;
					}
				}
			}
		}

		if(familyMember.getColor() != FamilyColor.NEUTRAL){
			for(Floor f : tower.getFloors()) {
				for(FamilyMember fM : f.occupiedBy){
					if(familyMember.samePlayer(fM)){
						if(fM.getColor() != FamilyColor.NEUTRAL){
							if (familyMember.getPlayer().getClientType().equals(ClientType.GUI)) {
								gameController.sendMessageChatGUI(familyMember.getPlayer(), "There is already one of yours colored family member in this tower!", true);
							}
							else {
								gameController.sendMessageCLI(familyMember.getPlayer(), "There is already one of yours colored family member in this tower!");
							}
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public void placeFamilyMember(FamilyMember familyMember){
		if(!familyMember.isGhost()){
			super.placeFamilyMember(familyMember);
			tower.setOccupied(true);
		}
		card = null;
	}

	@Override
	public String toString(){
		StringBuilder bld = new StringBuilder();
		bld.append(super.toString());
		if(rewards != null){
			bld.append("  Rewards: ");
			for(Reward r : rewards){
				bld.append(r.toString() + "; ");
			}
			bld.append("\n");
		}
		if(card != null){
			bld.append("  Card:\n" + card.toString());
		}
		return bld.toString();
	}
}
