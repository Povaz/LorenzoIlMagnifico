package it.polimi.ingsw.pc34.Model;

import java.io.IOException;
import java.rmi.RemoteException;

public class HarvestArea extends ActionSpot{
    private final int diceModifier;
    private final Board board;

    public HarvestArea(boolean active, boolean unrestricted, int diceModifier, Board board){
        super(active, unrestricted, 1);
        this.diceModifier = diceModifier;
        this.board = board;
    }

    @Override
    public boolean isPlaceable(FamilyMember familyMember, boolean canPlaceInBusyActionSpot, GameController gameController) throws RemoteException, IOException {
        if(!super.isPlaceable(familyMember, canPlaceInBusyActionSpot, gameController)){
            return false;
        }

        if(familyMember.isGhost()){
            if (familyMember.getAction() != null){
                if (familyMember.getAction() != ActionType.HARVEST && familyMember.getAction() != ActionType.ALL){
                    try {
                        gameController.sendMessageCLI(familyMember.getPlayer(), "You cannot place in this type of action spot!");
                    }
                    catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            }
        }

        if(familyMember.getColor() != FamilyColor.NEUTRAL){
            for(HarvestArea hA : board.getHarvestArea()){
                for(FamilyMember f : hA.occupiedBy){
                    if(familyMember.samePlayer(f)){
                        if(f.getColor() != FamilyColor.NEUTRAL){
                            try {
                                gameController.sendMessageCLI(familyMember.getPlayer(), "There is already one of yours colored family member in the harvest area!");
                            }
                            catch (RemoteException e) {
                                e.printStackTrace();
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
