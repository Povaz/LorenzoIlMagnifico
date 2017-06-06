package it.polimi.ingsw.pcXX;

public class HarvestArea extends ActionSpot{
    private final int diceModifier;
    private final Board board;

    public HarvestArea(boolean active, boolean unrestricted, int diceModifier, Board board){
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
            if (familyMember.getAction() != ActionType.HARVEST && familyMember.getAction() != ActionType.ALL) {
                return false;
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
    public boolean place(FamilyMember familyMember){
        if(familyMember.getPlayer().getPlayerBoard().harvest(familyMember.getValue() + diceModifier)){
            return super.place(familyMember);
        }
        return false;
    }
}
