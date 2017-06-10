package it.polimi.ingsw.pcXX;

import it.polimi.ingsw.pcXX.Exception.TooMuchTimeException;

/**
 * Created by trill on 10/06/2017.
 */
public class BuyCard{
    private final Player player;
    private final Board board;
    private final Floor floor;
    private final FamilyMember familyMember;
    private final Counter newCounter;
    private DevelopmentCard card;
    private CardSpot cardSpot;

    public BuyCard(Player player, Board board, ActionSpot actionSpot, FamilyMember familyMember){
        this.player = player;
        this.board = board;
        this.floor = (Floor) actionSpot;
        this.familyMember = familyMember;
        this.newCounter = new Counter(player.getPlayerBoard().getCounter());
    }

    public boolean canDoAction() throws TooMuchTimeException{
        /*
            deve esserci la carta nell'actionSpot (se c'è inizializza card e cardSpot)
        */
        if(!floorHaveCard()){
            return false;
        }

        /*
            familyMember può essere piazzato nell'actionSpot:
                - familiare non usato, spazio azione libero, valore dado sufficiente;
                - se è un'azione aggiuntiva deve essere del tipo giusto;
                - non ci possono essere due familiari dello stesso giocatore non neutrali nella stessa torre
        */
        if(!floor.isPlaceable(familyMember)){
            return false;
        }

        /*
            controlla se ha più servant di quelli che ha usato per fare l'azione
        */
        if(!haveEnoughServant()){
            System.out.println("Hai usato più servant di quelli che possiedi!");
            return false;
        }

        /*
            controlla se ha abbastanza coin per pagare la tassa sulla torre nel caso sia già occupata
        */
        if(!canPayTowerTax()){
            System.out.println("Non hai abbastanza coin per pagare la tassa della torre!");
            return false;
        }

        /*
            guadagna i reward della torre
        */
        earnReward();

        /*
            controlla se ha abbastanza risorse per pagare la carta
        */
        if(!canPayCardCost()){
            System.out.println("Non hai abbastanza risorse per pagare i costi della carta!");
            return false;
        }

        /*
            guadagna i fastReward della carta
        */
        earnCardFastReward();

        /*
            carta può essere piazzata nel cardSpot:
                - c'è abbastanza spazio nella plancia del giocatore;
                - se carta territorio ha abbastanza
        */
        if(!canBePlacedInCardSpot()){
            System.out.println("Non hai abbastanza spazio nel CardSpot per poter piazzare la carta");
            return false;
        }

        return true;
    }

    private boolean floorHaveCard(){
        if(floor.getCard() != null){
            card = floor.getCard();
            cardSpot = player.getPlayerBoard().getCardSpot(card.getType());
            return true;
        }
        return false;
    }

    private boolean haveEnoughServant(){
        newCounter.subtract(familyMember.getServantUsed());
        return newCounter.check();
    }

    private boolean canPayTowerTax(){
        if(floor.getTower().isOccupied()){
            newCounter.subtract(floor.getTower().getOccupiedTax());
        }
        return newCounter.check();
    }

    private void earnReward() throws TooMuchTimeException{
        newCounter.sum(floor.getRewards());
    }

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
        return newCounter.check();
    }

    private boolean canPayMilitaryPoint(VentureCard vCard){
        Reward militaryPoint = newCounter.giveSameReward(vCard.getMilitaryPointNeeded());
        if(militaryPoint.getQuantity() >= vCard.getMilitaryPointNeeded().getQuantity()){
            newCounter.subtract(vCard.getMilitaryPointPrice());
            return newCounter.check();
        }
        return false;
    }

    private void earnCardFastReward() throws TooMuchTimeException{
        newCounter.sum(card.getFastRewards());
    }

    private boolean canBePlacedInCardSpot(){
        if(!cardSpot.canPlaceCard()){
            return false;
        }
        if(cardSpot instanceof CardSpot){
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
                    return false;
                }
                return true;
            case  3:
                if(newCounter.getMilitaryPoint().getQuantity() < 7){
                    return false;
                }
                return true;
            case  4:
                if(newCounter.getMilitaryPoint().getQuantity() < 12){
                    return false;
                }
                return true;
            case  5:
                if(newCounter.getMilitaryPoint().getQuantity() < 18){
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    public void doAction(){
        return;
    }
}
