package it.polimi.ingsw.pcXX;

import it.polimi.ingsw.pcXX.Exception.TooMuchTimeException;

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
		if(familyMember.getAction() != null) {
			if (familyMember.getAction() != ActionType.PRODUCE && familyMember.getAction() != ActionType.ALL) {
				return false;
			}
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
	public boolean place(FamilyMember familyMember) throws TooMuchTimeException {
		if(familyMember.getPlayer().getPlayerBoard().produce(familyMember.getValue() + diceModifier, familyMember.getServantUsed())){
			return super.place(familyMember);
		}
		return false;
	}

	@Override
	public String toString(){
		String string = super.toString();
		string += "\n  diceModifier: " + diceModifier;
		return string;
	}
}
