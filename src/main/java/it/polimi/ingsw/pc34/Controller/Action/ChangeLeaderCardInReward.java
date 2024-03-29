package it.polimi.ingsw.pc34.Controller.Action;

import it.polimi.ingsw.pc34.Controller.Game;
import it.polimi.ingsw.pc34.Model.*;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientType;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by trill on 14/06/2017.
 */
public class ChangeLeaderCardInReward implements CommandPattern{
    private final Game game;
    private final Player player;
    @SuppressWarnings("unused")
	private final Board board;
    private final List<LeaderCard> leaderCardsInHand;
    private final Counter newCounter;
    @SuppressWarnings("unused")
	private final Modifier modifier;
    private LeaderCard leaderCard;

    public ChangeLeaderCardInReward(Game game, Player player){
        this.game = game;
        this.player = player;
        this.board = game.getBoard();
        this.leaderCardsInHand = player.getPlayerBoard().getLeaderCardsInHand();
        this.newCounter = new Counter(player.getPlayerBoard().getCounter());
        this.modifier = player.getPlayerBoard().getModifier();
    }

    public boolean canDoAction() throws IOException{
        if(leaderCardsInHand.isEmpty()){
            if (player.getClientType().equals(ClientType.GUI)) {
                game.getGameController().sendMessageChatGUI(player, "You don't have any leader card in your hand!", true);
            }
            else {
                game.getGameController().sendMessageCLI(player, "You don't have any leader card in your hand!\nWhat action you want to do? 1-action 2-place Leader Card 3-activate Leader Card 4-exchange Leader Card 5-skip");
            }
            return false;
        }

        leaderCard = game.getGameController().askWhichCardPlaceChangeCopyActivate(leaderCardsInHand, player, "E");

        if (leaderCard == null) {
            return false;
        }

        Set<Reward> rewards = game.getGameController().exchangeCouncilPrivilege(leaderCard.getChangedRewards(), player);
        newCounter.sum(rewards);
        newCounter.round();

        return true;
    }

    public void doAction(){
        player.getPlayerBoard().setCounter(newCounter);
        removeLeaderCardFromHand();
    }

    private void removeLeaderCardFromHand(){
        leaderCardsInHand.remove(leaderCard);
    }
}
