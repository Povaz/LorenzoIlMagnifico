package it.polimi.ingsw.pc34.Model.Action;

import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;
import it.polimi.ingsw.pc34.Model.*;

/**
 * Created by trill on 10/06/2017.
 */
public class Harvest implements CommandPattern{
    private final Game game;
    private final Player player;
    private final Board board;
    private final HarvestArea harvestArea;
    private final FamilyMember familyMember;
    private final Counter newCounter;
    private final Modifier modifier;
    private final int actionValue;

    public Harvest(Game game, ActionSpot actionSpot, FamilyMember familyMember){
        this.game = game;
        this.player = familyMember.getPlayer();
        this.board = game.getBoard();
        this.harvestArea = (HarvestArea) actionSpot;
        this.familyMember = familyMember;
        this.newCounter = new Counter(player.getPlayerBoard().getCounter());
        this.modifier = player.getPlayerBoard().getModifier();
        this.actionValue = familyMember.getValue() + familyMember.getServantUsed().getQuantity();
        updateFamilyMemberRealValue();
    }

    private void updateFamilyMemberRealValue(){
        int realValue = familyMember.getRealValue();
        realValue += modifier.getActionModifiers().get(ActionType.HARVEST);
        familyMember.setRealValue(realValue);
    }

    public boolean canDoAction() throws TooMuchTimeException{
        if(!harvestArea.isPlaceable(familyMember, modifier.isPlaceInBusyActionSpot())){
            return false;
        }

        if(!haveEnoughServant()){
            System.out.println("Hai usato più servant di quelli che possiedi!");
            return false;
        }

        earnTileReward();

        earnHarvestReward();

        // correggi i limiti di risorse
        newCounter.round();

        return true;
    }

    // controlla se ha più servant di quelli che ha usato per fare l'azione
    private boolean haveEnoughServant(){
        newCounter.subtract(familyMember.getServantUsed());
        return newCounter.check();
    }

    // guadagna i reward del PersonalBonusTile
    private void earnTileReward() throws TooMuchTimeException{
        PersonalBonusTile tile = player.getPlayerBoard().getPersonalBonusTile();
        if(tile != null) {
            if(tile.getHarvestRewards() != null){
                if(actionValue >= tile.getDiceHarvest()){
                    newCounter.sum(tile.getHarvestRewards());
                }
            }
        }
    }

    // guadagna i reward delle TerritoryCard
    private void earnHarvestReward() throws TooMuchTimeException{
        for(DevelopmentCard card : player.getPlayerBoard().getTerritorySpot().getCards()){
            TerritoryCard tCard = (TerritoryCard) card;
            if(actionValue >= tCard.getDiceHarvestAction()){
                if(tCard.getEarnings() != null){
                    newCounter.sumWithLose(tCard.getEarnings(), modifier.getLoseRewards());
                }
            }
        }
    }

    public void doAction(){
        // aggiorna risorse giocatore
        player.getPlayerBoard().setCounter(newCounter);

        // posiziona il familiare nell'ActionSpot
        harvestArea.placeFamilyMember(familyMember);
    }
}
