package it.polimi.ingsw.pcXX.Action;

import it.polimi.ingsw.pcXX.*;
import it.polimi.ingsw.pcXX.Exception.TooMuchTimeException;

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

    public PlaceCouncilPalace(Game game, ActionSpot actionSpot, FamilyMember familyMember){
        this.game = game;
        this.player = familyMember.getPlayer();
        this.board = game.getBoard();
        this.councilPalace = (CouncilPalace) actionSpot;
        this.familyMember = familyMember;
        this.newCounter = new Counter(player.getPlayerBoard().getCounter());
    }

    public boolean canDoAction() throws TooMuchTimeException{
        if(!councilPalace.isPlaceable(familyMember)){
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
        newCounter.sum(councilPalace.getRewards());
    }

    public void doAction(){
        // aggiorna risorse giocatore
        player.getPlayerBoard().setCounter(newCounter);

        // posiziona il familiare nell'ActionSpot
        councilPalace.placeFamilyMember(familyMember);
    }
}
