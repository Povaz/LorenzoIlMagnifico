package it.polimi.ingsw.pcXX;

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
		if(familyMember.getAction() != null){
			if(familyMember.getAction() != ActionType.ALL && !tower.getType().same(familyMember.getAction())){
				return false;
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
	public boolean place(FamilyMember familyMember){
		if(familyMember.getPlayer().getPlayerBoard().buyCard(this)){
			super.place(familyMember);
			this.card = null;
			return true;
		}
		return false;
	}
}
