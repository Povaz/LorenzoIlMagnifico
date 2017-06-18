package it.polimi.ingsw.pc34.Model.Action;

import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;
import it.polimi.ingsw.pc34.Model.*;

import java.util.List;
import java.util.Map;

/**
 * Created by trill on 12/06/2017.
 */
public class PlaceLeaderCard implements CommandPattern{
    private final Game game;
    private final Player player;
    private final Board board;
    private final List<LeaderCard> leaderCardsInHand;
    private final List<ImmediateLeaderCard> immediateLeaderCardsPositionated;
    private final List<PermanentLeaderCard> permanentLeaderCardsPositionated;
    private final Counter newCounter;
    private final Modifier modifier;
    private LeaderCard leaderCard;

    public PlaceLeaderCard(Game game, Player player){
        this.game = game;
        this.player = player;
        this.board = game.getBoard();
        this.leaderCardsInHand = player.getPlayerBoard().getLeaderCardsInHand();
        this.immediateLeaderCardsPositionated = player.getPlayerBoard().getImmediateLeaderCardsPositionated();
        this.permanentLeaderCardsPositionated = player.getPlayerBoard().getPermanentLeaderCardsPositionated();
        this.newCounter = new Counter(player.getPlayerBoard().getCounter());
        this.modifier = player.getPlayerBoard().getModifier();
    }

    public boolean canDoAction() throws TooMuchTimeException{
        if(leaderCardsInHand.size() <= 0){
            game.getGameController().sendMessage(player, "You don't have any leader card in your hand!");
            return false;
        }

        leaderCard = game.getGameController().askWhichCardPlace(leaderCardsInHand, player);

        if(!haveEnoughRewardToActive()){
            return false;
        }
        if(!haveEnoughCardToActive()){
            return false;
        }
        return true;
    }

    private boolean haveEnoughRewardToActive(){
        newCounter.subtract(leaderCard.getActivationRewardCost());
        if(!newCounter.check()){
            game.getGameController().sendMessage(player, "You don't have enough resources to place the leader card!");
            return false;
        }
        return true;
    }

    private boolean haveEnoughCardToActive(){
        Map<CardType, Integer> cardNeeded = leaderCard.getActivationCardCost();
        for(CardType cT : cardNeeded.keySet()){
            switch(cT){
                case TERRITORY:
                    if(player.getPlayerBoard().getTerritorySpot().getCards().size() < cardNeeded.get(cT)){
                        game.getGameController().sendMessage(player, "You don't have enough territory card to place the leader card!");
                        return false;
                    }
                    break;
                case BUILDING:
                    if(player.getPlayerBoard().getBuildingSpot().getCards().size() < cardNeeded.get(cT)){
                        game.getGameController().sendMessage(player, "You don't have enough building card to place the leader card!");
                        return false;
                    }
                    break;
                case CHARACTER:
                    if(player.getPlayerBoard().getCharacterSpot().getCards().size() < cardNeeded.get(cT)){
                        game.getGameController().sendMessage(player, "You don't have enough character card to place the leader card!");
                        return false;
                    }
                    break;
                case VENTURE:
                    if(player.getPlayerBoard().getVentureSpot().getCards().size() < cardNeeded.get(cT)){
                        game.getGameController().sendMessage(player, "You don't have enough venture card to place the leader card!");
                        return false;
                    }
                    break;
                case ANY:
                    boolean enoughCard = false;
                    if(player.getPlayerBoard().getTerritorySpot().getCards().size() >= cardNeeded.get(cT)){
                        enoughCard = true;
                    }
                    if(player.getPlayerBoard().getBuildingSpot().getCards().size() >= cardNeeded.get(cT)){
                        enoughCard = true;
                    }
                    if(player.getPlayerBoard().getCharacterSpot().getCards().size() >= cardNeeded.get(cT)){
                        enoughCard = true;
                    }
                    if(player.getPlayerBoard().getVentureSpot().getCards().size() >= cardNeeded.get(cT)){
                        enoughCard = true;
                    }
                    if(!enoughCard){
                        game.getGameController().sendMessage(player, "You don't have enough card to place the leader card!");
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    public void doAction(){
        if(leaderCard instanceof ImmediateLeaderCard){
            placeImmediateLeaderCard();
        }
        else if(leaderCard instanceof PermanentLeaderCard){
            placePermanentLeaderCard();
        }
        removeLeaderCardFromHand();
    }

    private void placeImmediateLeaderCard(){
        immediateLeaderCardsPositionated.add((ImmediateLeaderCard) leaderCard);
    }

    private void placePermanentLeaderCard(){
        permanentLeaderCardsPositionated.add((PermanentLeaderCard) leaderCard);
        modifier.update((PermanentLeaderCard) leaderCard);
    }

    private void removeLeaderCardFromHand(){
        leaderCardsInHand.remove(leaderCard);
    }
}
