package it.polimi.ingsw.pcXX.Action;

import it.polimi.ingsw.pcXX.*;
import it.polimi.ingsw.pcXX.Exception.TooMuchTimeException;

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

    public boolean canDoAction() throws TooMuchTimeException{
        if(!productionArea.isPlaceable(familyMember, modifier.isPlaceInBusyActionSpot())){
            return false;
        }

        if(!haveEnoughServant()){
            System.out.println("Hai usato più servant di quelli che possiedi!");
            return false;
        }

        earnTileReward();

        earnProductionReward();

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
            if(tile.getProductionRewards() != null){
                if(actionValue >= tile.getDiceProduction()){
                    newCounter.sum(tile.getProductionRewards());
                }
            }
        }
    }

    // guadagna i reward delle BuildingCard
    private boolean earnProductionReward() throws TooMuchTimeException{
        for(DevelopmentCard card : player.getPlayerBoard().getBuildingSpot().getCards()){
            BuildingCard bCard = (BuildingCard) card;
            if(actionValue >= bCard.getDiceProductionAction()){
                if(bCard.getEarnings() != null){
                    newCounter.sum(bCard.getEarnings());
                }
                if(bCard.getRewardForCard() != null){
                    newCounter.sum(convertRewardForReward(bCard.getRewardForReward()));
                }
                if(bCard.getRewardForReward() != null){
                    newCounter.sum(covertRewardForCard(bCard.getRewardForCard()));
                }
                if(bCard.getTrades() != null){
                    Trade trade = TerminalInput.chooseTrade(bCard);
                    copyForCosts.subtract(trade.getGive());
                    newCounter.subtract(trade.getGive());
                    newCounter.sum(trade.getTake());
                }
            }
        }
        if(!copyForCosts.check()){
            return false;
        }
        if(!newCounter.check()){
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
