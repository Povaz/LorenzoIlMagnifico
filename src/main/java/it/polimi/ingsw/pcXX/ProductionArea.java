package it.polimi.ingsw.pcXX;

public class ProductionArea extends ActionSpot{
	private final int diceModifier;
	private final Board board;

	public ProductionArea(boolean active, boolean unrestricted, int diceModifier, Board board){
	    super(active, unrestricted, 1);
	    this.diceModifier = diceModifier;
	    this.board = board;
    }

	@Override
	public boolean isPlaceable(FamilyMember familyMember){
		if(!super.isPlaceable(familyMember)){
			return false;
		}
		if(familyMember.getColor() != FamilyColor.NEUTRAL){
			for(ProductionArea pA : board.getProductionArea()){
				for(FamilyMember f : pA.occupiedBy){
					if(familyMember.samePlayer(f)){
						if(f.getColor() != FamilyColor.NEUTRAL){
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
		if(familyMember.getPlayer().getPlayerBoard().produce(familyMember.getValue() + diceModifier)){
			return super.place(familyMember);
		}
		return false;
	}
}
