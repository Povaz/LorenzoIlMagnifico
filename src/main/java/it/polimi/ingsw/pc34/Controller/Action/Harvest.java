package it.polimi.ingsw.pc34.Controller.Action;

import it.polimi.ingsw.pc34.Controller.Game;
import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;
import it.polimi.ingsw.pc34.Model.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Set;

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

    public boolean canDoAction() throws TooMuchTimeException, RemoteException, IOException{
        if(!harvestArea.isPlaceable(familyMember, modifier.isPlaceInBusyActionSpot(), game.getGameController())){
            return false;
        }

        if(!haveEnoughServant()){
            return false;
        }

        earnTileReward();

        earnHarvestReward();

        // correggi i limiti di risorse
        newCounter.round();

        return true;
    }

    // controlla se ha più servant di quelli che ha usato per fare l'azione
    private boolean haveEnoughServant() throws RemoteException, IOException {
        newCounter.subtract(familyMember.getServantUsed());
        if(!newCounter.check()){
            game.getGameController().sendMessageCLI(player, "You don't have enough servant!");
            return false;
        }
        return true;
    }

    // guadagna i reward del PersonalBonusTile
    private void earnTileReward() throws TooMuchTimeException, IOException{
        PersonalBonusTile tile = player.getPlayerBoard().getPersonalBonusTile();
        if(tile != null) {
            if(tile.getHarvestRewards() != null){
                if(actionValue >= tile.getDiceHarvest()){
                    Set<Reward> rewards = game.getGameController().exchangeCouncilPrivilege(tile.getHarvestRewards(), player);
                    newCounter.sum(rewards);
                }
            }
        }
    }

    // guadagna i reward delle TerritoryCard
    private void earnHarvestReward() throws TooMuchTimeException, IOException{
        for(DevelopmentCard card : player.getPlayerBoard().getTerritorySpot().getCards()){
            TerritoryCard tCard = (TerritoryCard) card;
            if(actionValue >= tCard.getDiceHarvestAction()){
                if(tCard.getEarnings() != null){
                    Set<Reward> rewards = game.getGameController().exchangeCouncilPrivilege(tCard.getEarnings(), player);
                    newCounter.sumWithLose(rewards, modifier.getLoseRewards());
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
