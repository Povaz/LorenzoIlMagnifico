package it.polimi.ingsw.pc34.Model;

public class HarvestArea extends ActionSpot{
    private final int diceModifier;
    private final Board board;

    public HarvestArea(boolean active, boolean unrestricted, int diceModifier, Board board){
        super(active, unrestricted, 1);
        this.diceModifier = diceModifier;
        this.board = board;
    }

    @Override
    public boolean isPlaceable(FamilyMember familyMember, boolean canPlaceInBusyActionSpot){
        if(!super.isPlaceable(familyMember, canPlaceInBusyActionSpot)){
            return false;
        }

        if(familyMember.isGhost()){
            if (familyMember.getAction() != null){
                if (familyMember.getAction() != ActionType.HARVEST && familyMember.getAction() != ActionType.ALL){
                    return false;
                }
            }
        }

        if(familyMember.getColor() != FamilyColor.NEUTRAL){
            for(HarvestArea hA : board.getHarvestArea()){
                for(FamilyMember f : hA.occupiedBy){
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
    public void placeFamilyMember(FamilyMember familyMember){
        if(!familyMember.isGhost()){
            super.placeFamilyMember(familyMember);
        }
    }

    @Override
    public String toString(){
        String string = super.toString();
        string += "\n  diceModifier: " + diceModifier;
        return string;
    }
}
