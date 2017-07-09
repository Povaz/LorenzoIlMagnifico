package it.polimi.ingsw.pc34.Controller.Action;

import it.polimi.ingsw.pc34.Controller.Game;
import it.polimi.ingsw.pc34.Model.*;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by trill on 12/06/2017.
 */
public class PlaceLeaderCard implements CommandPattern{
    private final Game game;
    private final Player player;
    @SuppressWarnings("unused")
	private final Board board;
    private final List<LeaderCard> leaderCardsInHand;
    private final List<ImmediateLeaderCard> immediateLeaderCardsPositionated;
    private final List<PermanentLeaderCard> permanentLeaderCardsPositionated;
    private final Counter newCounter;
    private final Modifier modifier;
    private LeaderCard leaderCard;
    private LeaderCard toCopy = null;

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

    public boolean canDoAction() throws IOException{
        if(leaderCardsInHand.isEmpty()){
            if (player.getClientType().equals(ClientType.GUI)) {
                game.getGameController().sendMessageChatGUI(player, "You don't have any leader card in your hand!", true);
            }
            else {
                game.getGameController().sendMessageCLI(player, "You don't have any leader card in your hand!");
            }
            return false;
        }

        leaderCard = game.getGameController().askWhichCardPlaceChangeCopyActivate(leaderCardsInHand, player, "P");

        if (leaderCard == null){
            return false;
        }

        if(!haveEnoughRewardToActive()){
            return false;
        }
        if(!haveEnoughCardToActive()){
            return false;
        }
        if(leaderCard instanceof PermanentLeaderCard){
            if(((PermanentLeaderCard) leaderCard).isCopyOtherCard()){
                if(!canCopyOtherCard()){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean haveEnoughRewardToActive() throws IOException{
        if(leaderCard.getActivationRewardCost() == null){
            return true;
        }
        newCounter.subtract(leaderCard.getActivationRewardCost());
        if(!newCounter.check()){
            if (player.getClientType().equals(ClientType.GUI)) {
                game.getGameController().sendMessageChatGUI(player, "You don't have enough resources to place the leader card!", true);
            }
            else {
                game.getGameController().sendMessageCLI(player, "You don't have enough resources to place the leader card!");
            }
            return false;
        }
        return true;
    }

    private boolean haveEnoughCardToActive() throws IOException{
        Map<CardType, Integer> cardNeeded = leaderCard.getActivationCardCost();
        if(cardNeeded == null){
            return true;
        }
        for(CardType cT : cardNeeded.keySet()){
            switch(cT){
                case TERRITORY:
                    if(player.getPlayerBoard().getTerritorySpot().getCards().size() < cardNeeded.get(cT)){
                        if(player.getClientType().equals(ClientType.GUI)) {
                            game.getGameController().sendMessageChatGUI(player, "You don't have enough territory card to place the leader card!", true);
                        }
                        else {
                            game.getGameController().sendMessageCLI(player, "You don't have enough territory card to place the leader card!");
                        }
                        return false;
                    }
                    break;
                case BUILDING:
                    if(player.getPlayerBoard().getBuildingSpot().getCards().size() < cardNeeded.get(cT)){
                        if (player.getClientType().equals(ClientType.GUI)) {
                            game.getGameController().sendMessageChatGUI(player, "You don't have enough building card to place the leader card!", true);
                        }
                        else {
                            game.getGameController().sendMessageCLI(player, "You don't have enough building card to place the leader card!");
                        }
                        return false;
                    }
                    break;
                case CHARACTER:
                    if(player.getPlayerBoard().getCharacterSpot().getCards().size() < cardNeeded.get(cT)){
                        if (player.getClientType().equals(ClientType.GUI)) {
                            game.getGameController().sendMessageChatGUI(player,"You don't have enough character card to place the leader card!", true );
                        }
                        else {
                            game.getGameController().sendMessageCLI(player, "You don't have enough character card to place the leader card!");

                        }
                        return false;
                    }
                    break;
                case VENTURE:
                    if(player.getPlayerBoard().getVentureSpot().getCards().size() < cardNeeded.get(cT)){
                        if (player.getClientType().equals(ClientType.GUI)) {
                            game.getGameController().sendMessageChatGUI(player, "You don't have enough venture card to place the leader card!", true);
                        }
                        else {
                            game.getGameController().sendMessageCLI(player, "You don't have enough venture card to place the leader card!");
                        }
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
                        if (player.getClientType().equals(ClientType.GUI)) {
                            game.getGameController().sendMessageChatGUI(player, "You don't have enough venture card to place the leader card!", true);
                        }
                        else {
                            game.getGameController().sendMessageCLI(player, "You don't have enough card to place the leader card!");
                        }
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    private boolean canCopyOtherCard() throws IOException {
        List<LeaderCard> canBeCopied = new ArrayList<>();
        for(Player p : game.getPlayers()){
            if(!player.equals(p)){
                for(PermanentLeaderCard pLC : p.getPlayerBoard().getPermanentLeaderCardsPositionated()){
                    canBeCopied.add(pLC);
                }
                for(ImmediateLeaderCard iLC : p.getPlayerBoard().getImmediateLeaderCardsPositionated()){
                    canBeCopied.add(iLC);
                }
            }
        }
        if(canBeCopied.isEmpty()){
            return false;
        }
        toCopy = game.getGameController().askWhichCardPlaceChangeCopyActivate(canBeCopied, player, "C");
        if(toCopy == null){
            return false;
        }
        return true;
    }

    public void doAction(){
        if(toCopy == null){
            if(leaderCard instanceof ImmediateLeaderCard){
                placeImmediateLeaderCard(leaderCard);
            }
            else if(leaderCard instanceof PermanentLeaderCard){
                placePermanentLeaderCard(leaderCard);
            }
        }
        else{
            if(toCopy instanceof ImmediateLeaderCard){
                placeImmediateLeaderCard(toCopy);
            }
            else if(toCopy instanceof PermanentLeaderCard){
                placePermanentLeaderCard(toCopy);
            }
        }
        removeLeaderCardFromHand();
    }

    private void placeImmediateLeaderCard(LeaderCard card){
        immediateLeaderCardsPositionated.add((ImmediateLeaderCard) card);
    }

    private void placePermanentLeaderCard(LeaderCard card){
        permanentLeaderCardsPositionated.add((PermanentLeaderCard) card);
        modifier.update((PermanentLeaderCard) card);
    }

    private void removeLeaderCardFromHand(){
        leaderCardsInHand.remove(leaderCard);
    }
}
