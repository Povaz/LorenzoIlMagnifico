package it.polimi.ingsw.pc34.Model.Action;

import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;
import it.polimi.ingsw.pc34.Model.*;
import it.polimi.ingsw.pc34.View.TerminalInput;

import java.util.List;
import java.util.Set;

/**
 * Created by trill on 14/06/2017.
 */
public class ChangeLeaderCardInReward implements CommandPattern{
    private final Game game;
    private final Player player;
    private final Board board;
    private final List<LeaderCard> leaderCardsInHand;
    private final Counter newCounter;
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

    public boolean canDoAction() throws TooMuchTimeException{
        if(leaderCardsInHand.size() <= 0){
            return false;
        }

        leaderCard = game.getGameController().askWhichCardChange(leaderCardsInHand, player);
        Set<Reward> rewards = game.getGameController().exchangeCouncilPrivilege(leaderCard.getChangedRewards(), player);
        newCounter.sum(rewards);
        newCounter.round();

        return true;
    }

    public void doAction() throws TooMuchTimeException{
        player.getPlayerBoard().setCounter(newCounter);

        removeLeaderCardFromHand();
    }

    private void removeLeaderCardFromHand(){
        leaderCardsInHand.remove(leaderCard);
    }
}
