package it.polimi.ingsw.pc34.Model.Action;

import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;
import it.polimi.ingsw.pc34.Model.*;

import java.util.Set;

/**
 * Created by trill on 10/06/2017.
 */
public class PlaceCouncilPalace implements CommandPattern{
    private final Game game;
    private final Player player;
    private final Board board;
    private final CouncilPalace councilPalace;
    private final FamilyMember familyMember;
    private final Counter newCounter;
    private final Modifier modifier;

    public PlaceCouncilPalace(Game game, ActionSpot actionSpot, FamilyMember familyMember){
        this.game = game;
        this.player = familyMember.getPlayer();
        this.board = game.getBoard();
        this.councilPalace = (CouncilPalace) actionSpot;
        this.familyMember = familyMember;
        this.newCounter = new Counter(player.getPlayerBoard().getCounter());
        this.modifier = player.getPlayerBoard().getModifier();
        updateFamilyMemberRealValue();
    }

    private void updateFamilyMemberRealValue(){
        int realValue = familyMember.getRealValue();
        realValue += modifier.getActionModifiers().get(ActionType.COUNCIL_PALACE);
        familyMember.setRealValue(realValue);
    }

    public boolean canDoAction() throws TooMuchTimeException{
        if(!councilPalace.isPlaceable(familyMember, modifier.isPlaceInBusyActionSpot(), game.getGameController())){
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
    private boolean haveEnoughServant(){
        newCounter.subtract(familyMember.getServantUsed());
        if(!newCounter.check()){
            game.getGameController().sendMessage(player, "You don't have enough servant!");
            return false;
        }
        return true;
    }

    // guadagna i reward del CouncilPalace
    private void earnReward() throws TooMuchTimeException{
        Set<Reward> rewards = game.getGameController().exchangeCouncilPrivilege(councilPalace.getRewards(), player);
        newCounter.sumWithLose(rewards, modifier.getLoseRewards());
    }

    public void doAction(){
        // aggiorna risorse giocatore
        player.getPlayerBoard().setCounter(newCounter);

        // posiziona il familiare nell'ActionSpot
        councilPalace.placeFamilyMember(familyMember);
    }
}
