package it.polimi.ingsw.pcXX.Action;

import it.polimi.ingsw.pcXX.*;
import it.polimi.ingsw.pcXX.Exception.TooMuchTimeException;

import java.util.List;

/**
 * Created by trill on 10/06/2017.
 */
public class BuyCard implements CommandPattern {
    private final Game game;
    private final Player player;
    private final Board board;
    private final Floor floor;
    private final FamilyMember familyMember;
    private final Counter newCounter;
    private DevelopmentCard card;
    private CardSpot cardSpot;

    public BuyCard(Game game, ActionSpot actionSpot, FamilyMember familyMember){
        this.game = game;
        this.player = familyMember.getPlayer();
        this.board = game.getBoard();
        this.floor = (Floor) actionSpot;
        this.familyMember = familyMember;
        this.newCounter = new Counter(player.getPlayerBoard().getCounter());
    }

    public boolean canDoAction() throws TooMuchTimeException {

        if(!floorHaveCard()){
            return false;
        }

        if(!floor.isPlaceable(familyMember)){
            return false;
        }

        if(!haveEnoughServant()){
            return false;
        }

        if(!canPayTowerTax()){
            return false;
        }

        earnReward();

        if(!canPayCardCost()){
            return false;
        }

        earnCardFastReward();

        if(!canBePlacedInCardSpot()){
            return false;
        }

        // correggi i limiti di risorse
        newCounter.round();

        return true;
    }

    // deve esserci la carta nell'actionSpot (se c'è inizializza card e cardSpot)
    private boolean floorHaveCard(){
        if(floor.getCard() != null){
            card = floor.getCard();
            cardSpot = player.getPlayerBoard().getCardSpot(card.getType());
            return true;
        }
        return false;
    }

    // controlla se ha più servant di quelli che ha usato per fare l'azione
    private boolean haveEnoughServant(){
        newCounter.subtract(familyMember.getServantUsed());
        if(!newCounter.check()){
            System.out.println("Hai usato più servant di quelli che possiedi!");
            return false;
        }
        return true;
    }

    // controlla se ha abbastanza coin per pagare la tassa sulla torre nel caso sia già occupata
    private boolean canPayTowerTax(){
        if(floor.getTower().isOccupied()){
            newCounter.subtract(floor.getTower().getOccupiedTax());
        }
        if(!newCounter.check()){
            System.out.println("Non hai abbastanza coin per pagare la tassa della torre!");
            return false;
        }
        return true;
    }

    // guadagna i reward della torre
    private void earnReward() throws TooMuchTimeException{
        newCounter.sum(floor.getRewards());
    }

    // controlla se ha abbastanza risorse per pagare la carta
    private boolean canPayCardCost(){
        if(card instanceof VentureCard){
            VentureCard vCard = (VentureCard) card;
            if(vCard.getCosts() != null && vCard.getMilitaryPointNeeded() != null && vCard.getMilitaryPointPrice() != null){
                if(TerminalInput.wantToPayWithMilitaryPoint(vCard.getCosts(), vCard.getMilitaryPointNeeded(), vCard.getMilitaryPointPrice())){
                    return canPayMilitaryPoint(vCard);
                }
                else{
                    return canPayNormalCost();
                }
            }
            else if(vCard.getCosts() != null){
                return canPayNormalCost();
            }
            else if(vCard.getMilitaryPointNeeded() != null && vCard.getMilitaryPointPrice() != null){
                return canPayMilitaryPoint(vCard);
            }
        }
        else{
            if(card.getCosts() != null){
                return canPayNormalCost();
            }
        }
        return true;
    }

    private boolean canPayNormalCost(){
        newCounter.subtract(card.getCosts());
        if(!newCounter.check()){
            System.out.println("Non hai abbastanza risorse per pagare i costi della carta!");
            return false;
        }
        return true;
    }

    private boolean canPayMilitaryPoint(VentureCard vCard){
        Reward militaryPoint = newCounter.giveSameReward(vCard.getMilitaryPointNeeded());
        if(militaryPoint.getQuantity() >= vCard.getMilitaryPointNeeded().getQuantity()){
            newCounter.subtract(vCard.getMilitaryPointPrice());
            if(newCounter.check()){
                return true;
            }
        }
        System.out.println("Non hai abbastanza militaryPoint per pagare i costi della carta!");
        return false;
    }

    // guadagna i fastReward della carta
    private void earnCardFastReward() throws TooMuchTimeException{
        newCounter.sum(card.getFastRewards());
    }

    /* carta può essere piazzata nel cardSpot:
        - c'è abbastanza spazio nella plancia del giocatore;
        - se è una carta territorio ha abbastanza militaryPoint
    */
    private boolean canBePlacedInCardSpot(){
        if(!cardSpot.canPlaceCard()){
            System.out.println("Non hai abbastanza spazio nel CardSpot per poter piazzare la carta");
            return false;
        }
        if(cardSpot instanceof TerritorySpot){
            TerritorySpot tSpot = (TerritorySpot) cardSpot;
            return haveEnoughtMilitaryPoint(tSpot);
        }
        return true;
    }

    private boolean haveEnoughtMilitaryPoint(TerritorySpot tSpot){
        switch(tSpot.getCards().size()){
            case 0:
            case 1:
                return true;
            case  2:
                if(newCounter.getMilitaryPoint().getQuantity() < 3){
                    System.out.println("Non hai abbastanza militaryPoint per poter piazzare la carta");
                    return false;
                }
                return true;
            case  3:
                if(newCounter.getMilitaryPoint().getQuantity() < 7){
                    System.out.println("Non hai abbastanza militaryPoint per poter piazzare la carta");
                    return false;
                }
                return true;
            case  4:
                if(newCounter.getMilitaryPoint().getQuantity() < 12){
                    System.out.println("Non hai abbastanza militaryPoint per poter piazzare la carta");
                    return false;
                }
                return true;
            case  5:
                if(newCounter.getMilitaryPoint().getQuantity() < 18){
                    System.out.println("Non hai abbastanza militaryPoint per poter piazzare la carta");
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    public void doAction() throws TooMuchTimeException{
        // aggiorna risorse giocatore
        player.getPlayerBoard().setCounter(newCounter);

        // posiziona il familiare nell'ActionSpot
        floor.placeFamilyMember(familyMember);

        // posiziona la carta nel CardSpot del giusto tipo
        cardSpot.placeCard(card);

        // esegui azioni aggiuntive della carta
        doBonusActions();
    }

    private void doBonusActions() throws TooMuchTimeException{
        // TODO uguale a Game
        List<FamilyMember> actions = card.getActions();
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
