package it.polimi.ingsw.pcXX;

import it.polimi.ingsw.pcXX.Exception.TooMuchTimeException;

import java.util.List;
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

	@Override
	public boolean isPlaceable(FamilyMember familyMember){
		if(!super.isPlaceable(familyMember)){
			return false;
		}

		if(familyMember.isGhost()){
			if(familyMember.getAction() != null){
				if (familyMember.getAction() != ActionType.ALL){
					if (!tower.getType().same(familyMember.getAction())){
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
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean place(FamilyMember familyMember) throws TooMuchTimeException{
		if(familyMember.getPlayer().getPlayerBoard().buyCard(this, familyMember.getServantUsed())){
			super.place(familyMember);
			tower.setOccupied(true);
			DevelopmentCard developmentCard = this.card;
			this.card = null;
			doActions(developmentCard, familyMember.getPlayer());
			return true;
		}
		return false;
	}

	private void doActions(DevelopmentCard developmentCard, Player player) throws TooMuchTimeException{
		List<FamilyMember> actions = developmentCard.getActions();
		if(actions != null){
			for(FamilyMember fM : actions){
				fM.setPlayer(player);
				ActionSpot actionSpot = null;
				boolean skipTurn = false;
				do{
					skipTurn = TerminalInput.doYouWantToSkip();
					if(!skipTurn){
						actionSpot = tower.getBoard().getViewActionSpot();
						fM.setServantUsed(TerminalInput.askNumberOfServant());
					}
				}
				while(!skipTurn && !fM.getPlayer().placeFamilyMember(fM, actionSpot));
			}
		}
	}

	@Override
	public String toString(){
		String string = super.toString();
		if(rewards != null){
			string += "\n  Reward if place: ";
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
