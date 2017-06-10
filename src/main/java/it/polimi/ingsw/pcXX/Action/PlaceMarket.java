package it.polimi.ingsw.pcXX.Action;

import it.polimi.ingsw.pcXX.*;
import it.polimi.ingsw.pcXX.Exception.TooMuchTimeException;

/**
 * Created by trill on 10/06/2017.
 */
public class PlaceMarket implements CommandPattern{
    private final Player player;
    private final Board board;
    private final Market market;
    private final FamilyMember familyMember;
    private final Counter newCounter;

    public PlaceMarket(Player player, Board board, Market market, FamilyMember familyMember){
        this.player = player;
        this.board = board;
        this.market = market;
        this.familyMember = familyMember;
        this.newCounter = new Counter(player.getPlayerBoard().getCounter());
    }

    public boolean canDoAction() throws TooMuchTimeException{
        if(!market.isPlaceable(familyMember)){
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
        newCounter.sum(market.getRewards());
    }

    public void doAction(){

    }
}
