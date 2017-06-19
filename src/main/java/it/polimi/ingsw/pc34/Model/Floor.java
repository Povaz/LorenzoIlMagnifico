package it.polimi.ingsw.pc34.Model;

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
	public boolean isPlaceable(FamilyMember familyMember, boolean canPlaceInBusyActionSpot, GameController gameController){
		if(!super.isPlaceable(familyMember, canPlaceInBusyActionSpot, gameController)){
			return false;
		}

		if(familyMember.isGhost()){
			if(familyMember.getAction() != null){
				if(familyMember.getAction() != ActionType.ALL){
					if(!tower.getType().sameType(familyMember.getAction())){
						gameController.sendMessage(familyMember.getPlayer(), "You cannot place in this type of action spot!");
						return false;
					}
				}
			}
		}

		if(familyMember.getColor() != FamilyColor.NEUTRAL){
			for(Floor f : tower.getFloors()){
				for(FamilyMember fM : f.occupiedBy){
					if(familyMember.samePlayer(fM)){
						if(fM.getColor() != FamilyColor.NEUTRAL){
							gameController.sendMessage(familyMember.getPlayer(), "There is already one of yours colored family member in this tower!");
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
		String string = super.toString();
		if(rewards != null){
			string += "\n  Reward if placeFamilyMember: ";
			for(Reward r : rewards){
				string += r.toString() + "   ";
			}
		}
		if(card != null){
			string += "\n  CARD:" + card.toString();
		}
		return string;
	}
}
