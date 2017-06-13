package it.polimi.ingsw.pcXX.Action;

import it.polimi.ingsw.pcXX.*;
import it.polimi.ingsw.pcXX.Exception.TooMuchTimeException;

import java.util.List;

/**
 * Created by trill on 12/06/2017.
 */
public class ActivateImmediateLeaderCard implements CommandPattern{
    private final Game game;
    private final Player player;
    private final Board board;
    private final ImmediateLeaderCard leaderCard;
    private final List<ImmediateLeaderCard> immediateLeaderCardsPositionated;
    private final Counter newCounter;
    private final Modifier modifier;
    private FamilyMember toChange;
    private int newValueFamilyMember;

    public ActivateImmediateLeaderCard(Game game, Player player, ImmediateLeaderCard leaderCard){
        this.game = game;
        this.player = player;
        this.board = game.getBoard();
        this.leaderCard = leaderCard;
        this.immediateLeaderCardsPositionated = player.getPlayerBoard().getImmediateLeaderCardsPositionated();
        this.newCounter = new Counter(player.getPlayerBoard().getCounter());
        this.modifier = player.getPlayerBoard().getModifier();
        this.toChange = null;
        this.newValueFamilyMember = 0;
    }

    public boolean canDoAction() throws TooMuchTimeException{
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
        newCounter.sum(leaderCard.getReward());
    }

    private boolean canChangeFamilyMemberValue() throws TooMuchTimeException{
        if(!leaderCard.isChangeColoredFamilyMamberValue()){
            return false;
        }
        newValueFamilyMember = leaderCard.getNewValueColoredFamilyMember();
        FamilyColor familyColor = TerminalInput.chooseFamilyMemberColorNotNeutral();
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

    public void doAction() throws TooMuchTimeException{
        // aggiorna risorse giocatore
        player.getPlayerBoard().setCounter(newCounter);

        // fai gli altri effetti
        doOtherEffect();

        // esegui azioni aggiuntive della carta
        doBonusActions();
    }

    private void doOtherEffect(){
        toChange.setValue(newValueFamilyMember);
    }

    private void doBonusActions() throws TooMuchTimeException{
        // TODO uguale a Game
        List<FamilyMember> actions = leaderCard.getActions();
        if(actions != null){
            for(FamilyMember fM : actions){
                fM.setPlayer(player);
                doBonusAction(fM);
            }
        }
    }

    private void doBonusAction(FamilyMember fM) throws TooMuchTimeException{
        ActionSpot actionSpot;
        do{
            System.out.println("AZIONE AGGIUNTIVA!!!");
            System.out.println(fM.getAction() + ":  " + fM.getValue());
            actionSpot = board.getViewActionSpot();
            if(actionSpot != null){
                fM.setServantUsed(TerminalInput.askNumberOfServant());
            }
        } while(!(game.placeFamilyMember(fM, actionSpot)));
    }
}
