package it.polimi.ingsw.pcXX.Action;

import it.polimi.ingsw.pcXX.*;
import it.polimi.ingsw.pcXX.Exception.TooMuchTimeException;

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

    public boolean canDoAction() throws TooMuchTimeException{
        if(modifier.isCannotPlaceInMarket()){
            return false;
        }

        if(!market.isPlaceable(familyMember, modifier.isPlaceInBusyActionSpot())){
            return false;
        }

        if(!haveEnoughServant()){
            System.out.println("Hai usato più servant di quelli che possiedi!");
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
        return newCounter.check();
    }

    // guadagna i reward del CouncilPalace
    private void earnReward() throws TooMuchTimeException{
        newCounter.sumWithLose(market.getRewards(), modifier.getLoseRewards());
    }

    public void doAction(){

    }
}
