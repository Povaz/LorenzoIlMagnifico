package it.polimi.ingsw.pc34.Model;

import java.rmi.RemoteException;

public class ProductionArea extends ActionSpot{
	private final int diceModifier;
	private final Board board;

	public ProductionArea(boolean active, boolean unrestricted, int diceModifier, Board board){
	    super(active, unrestricted, 1);
	    this.diceModifier = diceModifier;
	    this.board = board;
    }

	@Override
	public boolean isPlaceable(FamilyMember familyMember, boolean canPlaceInBusyActionSpot, GameController gameController) throws RemoteException{
		if(!super.isPlaceable(familyMember, canPlaceInBusyActionSpot, gameController)){
			return false;
		}

		if(familyMember.isGhost()){
			if(familyMember.getAction() != null){
				if(familyMember.getAction() != ActionType.PRODUCE && familyMember.getAction() != ActionType.ALL){
					gameController.sendMessageCLI(familyMember.getPlayer(), "You cannot place in this type of action spot!");
					return false;
				}
			}
		}

		if(familyMember.getColor() != FamilyColor.NEUTRAL){
			for(ProductionArea pA : board.getProductionArea()){
				for(FamilyMember f : pA.occupiedBy){
					if(familyMember.samePlayer(f)){
						if(f.getColor() != FamilyColor.NEUTRAL){
							gameController.sendMessageCLI(familyMember.getPlayer(), "There is already one of yours colored family member in the production area!");
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
		}
	}

	@Override
	public String toString(){
		StringBuilder bld = new StringBuilder();
		bld.append(super.toString());
		bld.append("  Dice modifier: " + diceModifier + "\n");
		return bld.toString();
	}
}
