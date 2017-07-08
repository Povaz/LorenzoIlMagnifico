package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.Controller.GameController;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientType;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Set;

public class HarvestArea extends ActionSpot{
    private final int diceModifier;
    private final Board board;
    private final Set<Reward> rewards;

    public HarvestArea(boolean active, boolean unrestricted, Set<Reward> rewards, int diceModifier, Board board){
        super(active, unrestricted, 1);
        this.rewards = rewards;
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
                        if (familyMember.getPlayer().getClientType().equals(ClientType.GUI)) {
                            gameController.sendMessageChatGUI(familyMember.getPlayer(), "You cannot place in this type of action spot!", true);
                        }
                        else {
                            gameController.sendMessageCLI(familyMember.getPlayer(), "You cannot place in this type of action spot!");
                        }
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
                                if (familyMember.getPlayer().getClientType().equals(ClientType.GUI)) {
                                    gameController.sendMessageChatGUI(familyMember.getPlayer(), "There is already one of yours colored family member in the harvest area!", true);
                                }
                                else {
                                    gameController.sendMessageCLI(familyMember.getPlayer(), "There is already one of yours colored family member in the harvest area!");
                                }
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

    public Set<Reward> getRewards(){
        return rewards;
    }

    public int getDiceModifier(){
        return diceModifier;
    }
}
