package it.polimi.ingsw.pc34.Controller.Action;

import it.polimi.ingsw.pc34.Controller.Game;
import it.polimi.ingsw.pc34.Model.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Set;

/**
 * Created by trill on 10/06/2017.
 */
public class PlaceMarket implements CommandPattern{
    private final Game game;
    private final Player player;
    private final Board board;
    private final Market market;
    private final FamilyMember familyMember;
    private final Counter newCounter;
    private final Modifier modifier;

    public PlaceMarket(Game game, ActionSpot actionSpot, FamilyMember familyMember){
        this.game = game;
        this.player = familyMember.getPlayer();
        this.board = game.getBoard();
        this.market = (Market) actionSpot;
        this.familyMember = familyMember;
        this.newCounter = new Counter(player.getPlayerBoard().getCounter());
        this.modifier = player.getPlayerBoard().getModifier();
        updateFamilyMemberRealValue();
    }

    private void updateFamilyMemberRealValue(){
        int realValue = familyMember.getRealValue();
        realValue += modifier.getActionModifiers().get(ActionType.MARKET);
        familyMember.setRealValue(realValue);
    }

    public boolean canDoAction() throws IOException{
        if(modifier.isCannotPlaceInMarket()){
            game.getGameController().sendMessageCLI(player, "You cannot place in the market!");
            return false;
        }

        if(!market.isPlaceable(familyMember, modifier.isPlaceInBusyActionSpot(), game.getGameController())){
            return false;
        }

        if(!haveEnoughServant()){
            return false;
        }

        earnReward();

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

    // guadagna i reward del CouncilPalace
    private void earnReward() throws IOException{
        Set<Reward> rewards = game.getGameController().exchangeCouncilPrivilege(market.getRewards(), player);
        newCounter.sumWithLose(rewards, modifier.getLoseRewards());
    }

    public void doAction(){
        // aggiorna risorse giocatore
        player.getPlayerBoard().setCounter(newCounter);

        // posiziona il familiare nell'ActionSpot
        market.placeFamilyMember(familyMember);
    }
}
