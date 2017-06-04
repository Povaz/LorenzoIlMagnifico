package it.polimi.ingsw.pcXX;

public class Floor extends ActionSpot{
	private DevelopmentCard card;
	private final Reward reward;
	private final Tower tower;

	public Floor(int value, Reward reward, Tower tower){
		super(true, false, value);
		this.card = null;
		this.reward = reward;
		this.tower = tower;
	}

	public DevelopmentCard getCard() {
		return card;
	}

	public void setCard(DevelopmentCard card) {
		this.card = card;
	}

	public Reward getReward() {
		return reward;
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
		if(familyMember.getPlayer().getPlayerBoard().buyCard(familyMember.getValue())){
			super.place(familyMember);
			return true;
		}
		return false;
	}
}
