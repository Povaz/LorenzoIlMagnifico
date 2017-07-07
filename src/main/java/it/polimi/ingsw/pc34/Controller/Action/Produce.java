package it.polimi.ingsw.pc34.Controller.Action;

import it.polimi.ingsw.pc34.Controller.Game;
import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;
import it.polimi.ingsw.pc34.Model.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by trill on 10/06/2017.
 */
public class Produce {
    private final Game game;
    private final Player player;
    private final Board board;
    private final ProductionArea productionArea;
    private final FamilyMember familyMember;
    private final Counter newCounter;
    private final Counter copyForCosts;
    private final Modifier modifier;
    private final int actionValue;

    public Produce(Game game, ActionSpot actionSpot, FamilyMember familyMember){
        this.game = game;
        this.player = familyMember.getPlayer();
        this.board = game.getBoard();
        this.productionArea = (ProductionArea) actionSpot;
        this.familyMember = familyMember;
        this.newCounter = new Counter(player.getPlayerBoard().getCounter());
        this.copyForCosts = new Counter(player.getPlayerBoard().getCounter());
        this.modifier = player.getPlayerBoard().getModifier();
        this.actionValue = familyMember.getValue() + familyMember.getServantUsed().getQuantity();
        updateFamilyMemberRealValue();
    }

    private void updateFamilyMemberRealValue(){
        int realValue = familyMember.getRealValue();
        realValue += modifier.getActionModifiers().get(ActionType.PRODUCE);
        familyMember.setRealValue(realValue);
    }

    public boolean canDoAction() throws TooMuchTimeException, RemoteException, IOException{
        if(!productionArea.isPlaceable(familyMember, modifier.isPlaceInBusyActionSpot(), game.getGameController())){
            return false;
        }

        if(!haveEnoughServant()){
            return false;
        }

        earnTileReward();

        if(!earnProductionReward()){
            return false;
        }

        // correggi i limiti di risorse
        newCounter.round();

        return true;
    }

    // controlla se ha più servant di quelli che ha usato per fare l'azione
    private boolean haveEnoughServant() throws RemoteException, IOException{
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
            if(tile.getProductionRewards() != null){
                if(actionValue >= tile.getDiceProduction()){
                    Set<Reward> rewards = game.getGameController().exchangeCouncilPrivilege(tile.getProductionRewards(), player);
                    newCounter.sum(rewards);
                }
            }
        }
    }

    // guadagna i reward delle BuildingCard
    private boolean earnProductionReward() throws TooMuchTimeException, RemoteException, IOException{
        for(DevelopmentCard card : player.getPlayerBoard().getBuildingSpot().getCards()){
            BuildingCard bCard = (BuildingCard) card;
            if(actionValue >= bCard.getDiceProductionAction()){
                if(bCard.getEarnings() != null){
                    Set<Reward> rewards = game.getGameController().exchangeCouncilPrivilege(bCard.getEarnings(), player);
                    newCounter.sumWithLose(rewards, modifier.getLoseRewards());
                }
                if(bCard.getRewardForCard() != null){
                    Set<Reward> rewards = game.getGameController().exchangeCouncilPrivilege(convertRewardForReward(bCard.getRewardForReward()), player);
                    newCounter.sumWithLose(rewards, modifier.getLoseRewards());
                }
                if(bCard.getRewardForReward() != null){
                    Set<Reward> rewards = game.getGameController().exchangeCouncilPrivilege(covertRewardForCard(bCard.getRewardForCard()), player);
                    newCounter.sumWithLose(rewards, modifier.getLoseRewards());
                }
                if(bCard.getTrades() != null){
                    Trade trade = game.getGameController().chooseTrade(bCard, player);
                    // se ho scelto di fare un trade lo faccio
                    if(trade != null){
                        copyForCosts.subtract(trade.getGive());
                        newCounter.subtract(trade.getGive());
                        Set<Reward> rewards = game.getGameController().exchangeCouncilPrivilege(trade.getTake(), player);
                        newCounter.sumWithLose(rewards, modifier.getLoseRewards());
                    }
                }
            }
        }
        if(!copyForCosts.check()){
            game.getGameController().sendMessageCLI(player, "You don't have enough resources to do all the trades!");
            return false;
        }
        if(!newCounter.check()){
            game.getGameController().sendMessageCLI(player, "You don't have enough resources to do all the trades!");
            return false;
        }
        return true;
    }

    private Set<Reward> convertRewardForReward(RewardForReward rewardForReward){
        Reward owned = rewardForReward.getOwned();
        Reward currentReward = newCounter.giveSameReward(owned);
        int multiplier = currentReward.getQuantity() / owned.getQuantity();
        Set<Reward> earned = new HashSet<>();
        for(Reward r : earned){
            earned.add(r.multiplyQuantity(multiplier));
        }
        return earned;
    }

    private Set<Reward> covertRewardForCard(RewardForCard rewardForCard){
        CardType cardType = rewardForCard.getCardTypeOwned();
        int multiplier = player.getPlayerBoard().getCardSpot(cardType).getCards().size();
        Set<Reward> earned = new HashSet<>();
        for(Reward r : earned){
            earned.add(r.multiplyQuantity(multiplier));
        }
        return earned;
    }

    public void doAction(){
        // aggiorna risorse giocatore
        player.getPlayerBoard().setCounter(newCounter);

        // posiziona il familiare nell'ActionSpot
        productionArea.placeFamilyMember(familyMember);
    }
}
