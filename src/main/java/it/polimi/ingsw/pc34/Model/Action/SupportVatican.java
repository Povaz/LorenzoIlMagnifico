package it.polimi.ingsw.pc34.Model.Action;

import it.polimi.ingsw.pc34.Controller.Game;
import it.polimi.ingsw.pc34.Controller.PlayerState;
import it.polimi.ingsw.pc34.Model.*;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Created by trill on 14/06/2017.
 */
public class SupportVatican implements CommandPattern{
    private final Game game;
    private final Player player;
    private final Board board;
    private final VaticanReportSpot vaticanReportSpot;
    private final Counter newCounter;
    private final Modifier modifier;

    public SupportVatican(Game game, Player player, VaticanReportSpot vaticanReportSpot){
        this.game = game;
        this.player = player;
        this.board = game.getBoard();
        this.vaticanReportSpot = vaticanReportSpot;
        this.newCounter = new Counter(player.getPlayerBoard().getCounter());
        this.modifier = player.getPlayerBoard().getModifier();
    }

    public boolean canDoAction() throws RemoteException, IOException{
        Reward playerFaithPoint = player.getPlayerBoard().getCounter().getFaithPoint();
        int faithPointNeeded = vaticanReportSpot.getFaithPointNeeded().getQuantity();
        if(vaticanReportSpot.isLast()){
            if(playerFaithPoint.getQuantity() < faithPointNeeded){
                earnVictoryPointSupport(player);
                newCounter.round();
                player.getPlayerBoard().setCounter(newCounter);
                return false;
            }
            earnVictoryPointSupport(player);
            if(playerFaithPoint.getQuantity() >= faithPointNeeded){
                earnRewardSupport(player);
            }
            return true;
        }
        else{
            if(playerFaithPoint.getQuantity() >= faithPointNeeded){
                player.putFirst_State(PlayerState.SUPPORT_VATICAN);
                if(game.getGameController().wantToSupportVatican(player)){
                	player.putFirst_State(PlayerState.WAITING);
                    return true;
                }
                else{
                	player.putFirst_State(PlayerState.WAITING);
                	return false;
                }
            }
            return false;
        }
    }

    public void doAction(){
        earnVictoryPointSupport(player);
        earnRewardSupport(player);

        newCounter.round();
        player.getPlayerBoard().setCounter(newCounter);
    }

    private void earnVictoryPointSupport(Player player){
        // guadagna i victoryPoint in base ai faithPoint che possiedi
        Reward victoryPoint = vaticanReportSpot.calculateVictoryPointFromFaithPoint(newCounter.getFaithPoint());
        newCounter.sum(victoryPoint);
        // azzera i faithPoint
        newCounter.subtract(newCounter.getFaithPoint());
    }

    private void earnRewardSupport(Player player){
        // guadagna le risorse aggiuntive
        for(Reward r : player.getPlayerBoard().getModifier().getBonusChurchSupport()){
            newCounter.sum(r);
        }
    }
}
