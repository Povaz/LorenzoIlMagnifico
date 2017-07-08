package it.polimi.ingsw.pc34.Controller.Action;

import it.polimi.ingsw.pc34.Controller.Game;
import it.polimi.ingsw.pc34.Controller.PlayerState;
import it.polimi.ingsw.pc34.Model.*;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Client;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientType;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private final Modifier modifier;
    private DevelopmentCard card;
    private CardSpot cardSpot;

    public BuyCard(Game game, ActionSpot actionSpot, FamilyMember familyMember){
        this.game = game;
        this.player = familyMember.getPlayer();
        this.board = game.getBoard();
        this.floor = (Floor) actionSpot;
        this.familyMember = familyMember;
        this.newCounter = new Counter(player.getPlayerBoard().getCounter());
        this.modifier = player.getPlayerBoard().getModifier();
        updateFamilyMemberRealValue();
    }

    private void updateFamilyMemberRealValue(){
        int realValue = familyMember.getRealValue();
        switch(floor.getTower().getType()){
            case TERRITORY:
                realValue += modifier.getActionModifiers().get(ActionType.TERRITORY_TOWER);
                break;
            case BUILDING:
                realValue += modifier.getActionModifiers().get(ActionType.BUILDING_TOWER);
                break;
            case CHARACTER:
                realValue += modifier.getActionModifiers().get(ActionType.CHARACTER_TOWER);
                break;
            case VENTURE:
                realValue += modifier.getActionModifiers().get(ActionType.VENTURE_TOWER);
                break;
        }
        familyMember.setRealValue(realValue);
    }

    public boolean canDoAction() throws IOException {

        if(!floorHaveCard()){
            return false;
        }

        if(!floor.isPlaceable(familyMember, modifier.isPlaceInBusyActionSpot(), game.getGameController())){
            return false;
        }

        if(!haveEnoughServant()){
            return false;
        }

        if(!modifier.isNotPayTollBusyTower()){
            if(!canPayTowerTax()){
                return false;
            }
        }

        if(!modifier.isNoBonusTowerResource()){
            earnReward();
        }

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
    private boolean floorHaveCard() throws IOException{
        if(floor.getCard() != null){
            card = floor.getCard();
            cardSpot = player.getPlayerBoard().getCardSpot(card.getType());
            return true;
        }
        if (player.getClientType().equals(ClientType.GUI)) {
            game.getGameController().sendMessageChatGUI(player, "There isn't any Card in this Floor!", true);
        }
        else {
            game.getGameController().sendMessageCLI(player, "There isn't any Card in this Floor!");
        }
        return false;
    }

    // controlla se ha più servant di quelli che ha usato per fare l'azione
    private boolean haveEnoughServant() throws IOException{
        newCounter.subtract(familyMember.getServantUsed());
        if(!newCounter.check()){
            if (player.getClientType().equals(ClientType.GUI)) {
                game.getGameController().sendMessageChatGUI(player, "You don't have enough servant!", true);
            }
            else {
                game.getGameController().sendMessageCLI(player, "You don't have enough servant!");
            }
            return false;
        }
        return true;
    }

    // controlla se ha abbastanza coin per pagare la tassa sulla torre nel caso sia già occupata
    private boolean canPayTowerTax() throws IOException{
        if(floor.getTower().isOccupied()){
            newCounter.subtract(floor.getTower().getOccupiedTax());
        }
        if(!newCounter.check()){
            if (player.getClientType().equals(ClientType.GUI)) {
                game.getGameController().sendMessageChatGUI(player, "You cannot pay the tower tax!", true);
            }
            else {
                game.getGameController().sendMessageCLI(player, "You cannot pay the tower tax!");
            }
            return false;
        }
        return true;
    }

    // guadagna i reward della torre
    private void earnReward() throws IOException{
        if(floor.getRewards() != null){
            Set<Reward> rewards = game.getGameController().exchangeCouncilPrivilege(floor.getRewards(), player);
            newCounter.sumWithLose(rewards, modifier.getLoseRewards());
        }
    }

    // controlla se ha abbastanza risorse per pagare la carta
    private boolean canPayCardCost() throws IOException{
        if(card instanceof VentureCard){
            VentureCard vCard = (VentureCard) card;
            if(vCard.getCosts() != null && vCard.getMilitaryPointNeeded() != null && vCard.getMilitaryPointPrice() != null){
                int payWithMilitaryPoint = game.getGameController().wantToPayWithMilitaryPoint(vCard.getCosts(), vCard.getMilitaryPointNeeded(), vCard.getMilitaryPointPrice(), player);
                if(payWithMilitaryPoint == 1){
                    return canPayMilitaryPoint(vCard);
                }
                else if(payWithMilitaryPoint == 0){
                    return canPayNormalCost();
                }
                else{
                    return false;
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

    private boolean canPayNormalCost() throws IOException{
        List<List<Reward>> discountsSelectables = modifier.getDiscounts().get(floor.getTower().getType());

        // choose which permanent discount use
        List<Reward> permanentDiscount;
        if(discountsSelectables.isEmpty()){
            permanentDiscount = new ArrayList<>();
        }
        else if(discountsSelectables.size() == 1){
            permanentDiscount = discountsSelectables.get(0);
        }
        else{
            permanentDiscount = game.getGameController().askWhichDiscount(discountsSelectables, player);
        }

        List<Reward> discount = addRewardFromSet(permanentDiscount, familyMember.getDiscounts());
        newCounter.subtractWithDiscount(card.getCosts(), discount);
        if(!newCounter.check()){
            if (player.getClientType().equals(ClientType.GUI)) {
                game.getGameController().sendMessageChatGUI(player, "You cannot pay the card cost!", true);
            }
            else {
                game.getGameController().sendMessageCLI(player, "You cannot pay the card cost!");
            }
            return false;
        }
        return true;
    }

    private List<Reward> addRewardFromSet(List<Reward> rewards, Set<Reward> toSum){
        if(toSum == null){
            return rewards;
        }
        List<Reward> copyOfReward = new ArrayList<>();
        for(Reward r : rewards){
            Reward toBeAdded = new Reward(r);
            for(Reward toAdd : toSum){
                if(toAdd.getType() == toBeAdded.getType()) {
                    toBeAdded.sumQuantity(toAdd);
                }
            }
            copyOfReward.add(new Reward(toBeAdded));
        }
        return copyOfReward;
    }

    private boolean canPayMilitaryPoint(VentureCard vCard) throws RemoteException, IOException{
        Reward militaryPoint = newCounter.giveSameReward(vCard.getMilitaryPointNeeded());
        if(militaryPoint.getQuantity() >= vCard.getMilitaryPointNeeded().getQuantity()){
            newCounter.subtract(vCard.getMilitaryPointPrice());
            if(newCounter.check()){
                return true;
            }
        }
        if (player.getClientType().equals(ClientType.GUI)) {
            game.getGameController().sendMessageChatGUI(player, "You don't have enough military point to buy the card!", true);
        }
        else {
            game.getGameController().sendMessageCLI(player, "You don't have enough military point to buy the card!");
        }
        return false;
    }

    // guadagna i fastReward della carta
    private void earnCardFastReward() throws IOException{
        if(card.getFastRewards() != null){
            if(modifier.isDoubleFastRewardDevelopmentCard()){
                Set<Reward> doubleReward = new HashSet<>();
                for(Reward r : card.getFastRewards()){
                    if(r.isResource()){
                        doubleReward.add(r.multiplyQuantity(2));
                    }
                }
                Set<Reward> rewards = game.getGameController().exchangeCouncilPrivilege(doubleReward, player);
                newCounter.sumWithLose(rewards, modifier.getLoseRewards());
            }
            else{
                Set<Reward> rewards = game.getGameController().exchangeCouncilPrivilege(card.getFastRewards(), player);
                newCounter.sumWithLose(rewards, modifier.getLoseRewards());
            }
        }
    }

    /* carta può essere piazzata nel cardSpot:
        - c'è abbastanza spazio nella plancia del giocatore;
        - se è una carta territorio ha abbastanza militaryPoint
    */
    private boolean canBePlacedInCardSpot() throws RemoteException, IOException{
        if(!cardSpot.canPlaceCard()){
            if (player.getClientType().equals(ClientType.GUI)) {
                game.getGameController().sendMessageChatGUI(player, "You don't have enough space in the card spot!", true); //TODO TESTA
            }
            else {
                game.getGameController().sendMessageCLI(player, "You don't have enough space in the card spot!");
            }
            return false;
        }
        if(!modifier.isNotSatisfyMilitaryPointForTerritory()) {
            if(cardSpot instanceof TerritorySpot){
                TerritorySpot tSpot = (TerritorySpot) cardSpot;
                return haveEnoughMilitaryPoint(tSpot);
            }
        }
        return true;
    }

    private boolean haveEnoughMilitaryPoint(TerritorySpot tSpot) throws RemoteException, IOException{
        switch(tSpot.getCards().size()){
            case 0:
            case 1:
                return true;
            case  2:
                if(newCounter.getMilitaryPoint().getQuantity() < 3){
                    if (player.getClientType().equals(ClientType.GUI)) {
                        game.getGameController().sendMessageChatGUI(player, "You don't have enough military point to place in the card spot!", true);
                    }
                    else {
                        game.getGameController().sendMessageCLI(player, "You don't have enough military point to place in the card spot!");
                    }
                    return false;
                }
                return true;
            case  3:
                if(newCounter.getMilitaryPoint().getQuantity() < 7){
                    if (player.getClientType().equals(ClientType.GUI)) {
                        game.getGameController().sendMessageChatGUI(player, "You don't have enough military point to place in the card spot!", true);
                    }
                    else {
                        game.getGameController().sendMessageCLI(player, "You don't have enough military point to place in the card spot!");
                    }
                    return false;
                }
                return true;
            case  4:
                if(newCounter.getMilitaryPoint().getQuantity() < 12){
                    if (player.getClientType().equals(ClientType.GUI)) {
                        game.getGameController().sendMessageChatGUI(player, "You don't have enough military point to place in the card spot!", true);
                    }
                    else {
                        game.getGameController().sendMessageCLI(player, "You don't have enough military point to place in the card spot!");
                    }
                    return false;
                }
                return true;
            case  5:
                if(newCounter.getMilitaryPoint().getQuantity() < 18){
                    if (player.getClientType().equals(ClientType.GUI)) {
                        game.getGameController().sendMessageChatGUI(player, "You don't have enough military point to place in the card spot!", true);
                    }
                    else {
                        game.getGameController().sendMessageCLI(player, "You don't have enough military point to place in the card spot!");
                    }
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    public void doAction() throws IOException{
        // aggiorna risorse giocatore
        player.getPlayerBoard().setCounter(newCounter);

        // aggiorna modificatore giocatore
        modifier.update(card);

        // posiziona il familiare nell'ActionSpot
        floor.placeFamilyMember(familyMember);

        // posiziona la carta nel CardSpot del giusto tipo
        cardSpot.placeCard(card);

        // esegui azioni aggiuntive della carta
        doBonusActions();
    }

    private void doBonusActions() throws IOException{
        // TODO uguale a Game
        List<FamilyMember> actions = card.getActions();
        if(actions != null){
            for(FamilyMember fM : actions){
                if (player.getClientType().equals(ClientType.GUI)) {
                    switch (player.getConnectionType()) {
                        case RMI:
                            game.getGameController().updateRequested(player.getUsername());
                            break;
                        case SOCKET:
                            //Fill
                            break;
                    }
                }
                else {
                    game.getGameController().sendMessageCLI(player, board.toString());
                    game.getGameController().sendMessageCLI(player, player.getPlayerBoard().toString());
                }
                fM.setPlayer(player);
                doBonusAction(fM);
            }
        }
    }

    private void doBonusAction(FamilyMember fM) throws IOException{
        ActionSpot actionSpot;
        do{
            String info = familyMember.getAction() + ": " + familyMember.getValue();
            System.out.println("AZIONE AGGIUNTIVA!!!");
            System.out.println(fM.getAction() + ":  " + fM.getValue());
            game.getGameController().ghost.set(true);
            player.putFirst_State(PlayerState.ACTION);
            player.putSecond_State(PlayerState.ACTION_INPUT);
            if (player.getClientType().equals(ClientType.GUI)) {
                switch (player.getConnectionType()) {
                    case RMI:
                        game.getGameController().getServerRMI().openNewWindow(player, "/bonusaction", info);
                        break;
                    case SOCKET:
                        break;
                }
            }
            actionSpot = game.getGameController().getViewActionSpot(player);
            if(actionSpot != null){
                fM.setServantUsed(new Reward(RewardType.SERVANT, game.getGameController().getHowManyServants(player)));
            }
        } while(actionSpot != null && !(game.placeFamilyMember(fM, actionSpot)));
    }
}
