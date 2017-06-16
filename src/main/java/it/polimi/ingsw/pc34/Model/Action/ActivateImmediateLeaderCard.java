package it.polimi.ingsw.pc34.Model.Action;

import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;
import it.polimi.ingsw.pc34.Model.*;
import it.polimi.ingsw.pc34.View.TerminalInput;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by trill on 12/06/2017.
 */
public class ActivateImmediateLeaderCard implements CommandPattern{
    private final Game game;
    private final Player player;
    private final Board board;
    private final List<ImmediateLeaderCard> immediateLeaderCardsPositionated;
    private final Counter newCounter;
    private final Modifier modifier;
    private ImmediateLeaderCard leaderCard;
    private FamilyMember toChange;
    private int newValueFamilyMember;

    public ActivateImmediateLeaderCard(Game game, Player player){
        this.game = game;
        this.player = player;
        this.board = game.getBoard();
        this.immediateLeaderCardsPositionated = player.getPlayerBoard().getImmediateLeaderCardsPositionated();
        this.newCounter = new Counter(player.getPlayerBoard().getCounter());
        this.modifier = player.getPlayerBoard().getModifier();
        this.toChange = null;
        this.newValueFamilyMember = 0;
    }

    public boolean canDoAction() throws TooMuchTimeException{
        if(immediateLeaderCardsPositionated.size() <= 0){
            return false;
        }

        leaderCard = game.getGameController().askWhichCardActivate(immediateLeaderCardsPositionated, player);

        if(leaderCard.isActivated()){
            return false;
        }

        earnReward();

        if(!canChangeFamilyMemberValue()){
            return false;
        }

        return true;
    }

    private void earnReward() throws TooMuchTimeException{
        if(leaderCard.getReward() != null){
            newCounter.sum(leaderCard.getReward());
        }
    }

    private boolean canChangeFamilyMemberValue() throws TooMuchTimeException{
        if(!leaderCard.isChangeColoredFamilyMamberValue()){
            return true;
        }
        newValueFamilyMember = leaderCard.getNewValueColoredFamilyMember();
        FamilyColor familyColor = game.getGameController().chooseFamilyMemberColorNotNeutral(player);
        for(FamilyMember fM : player.getPlayerBoard().getFamilyMembers()){
            if(fM.getColor() == familyColor){
                toChange = fM;
            }
        }
        if(toChange == null){
            return false;
        }
        return true;
    }

    public void doAction() throws TooMuchTimeException, RemoteException{
        // aggiorna risorse giocatore
        player.getPlayerBoard().setCounter(newCounter);

        // segna la carta come attivata
        leaderCard.setActivated(true);

        // fai gli altri effetti
        doOtherEffect();

        // esegui azioni aggiuntive della carta
        doBonusActions();
    }

    private void doOtherEffect(){
        if(leaderCard.isChangeColoredFamilyMamberValue()){
            toChange.setValue(newValueFamilyMember);
        }
    }

    private void doBonusActions() throws TooMuchTimeException, RemoteException{
        // TODO uguale a Game
        List<FamilyMember> actions = leaderCard.getActions();
        if(actions != null){
            for(FamilyMember fM : actions){
                fM.setPlayer(player);
                doBonusAction(fM);
            }
        }
    }

    private void doBonusAction(FamilyMember fM) throws TooMuchTimeException, RemoteException{
        ActionSpot actionSpot;
        do{
            System.out.println("AZIONE AGGIUNTIVA!!!");
            System.out.println(fM.getAction() + ":  " + fM.getValue());
            actionSpot = game.getGameController().getViewActionSpot(player);
            if(actionSpot != null){
                fM.setServantUsed(game.getGameController().askNumberOfServant(player));
            }
        } while(!(game.placeFamilyMember(fM, actionSpot)));
    }
}
