package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.Controller.ActionInput;
import it.polimi.ingsw.pc34.Controller.PlayerState;
import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;
import it.polimi.ingsw.pc34.RMI.*;
import it.polimi.ingsw.pc34.Socket.ServerHandler;
import it.polimi.ingsw.pc34.Socket.ServerSOC;
import it.polimi.ingsw.pc34.View.TerminalInput;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Created by trill on 14/06/2017.
 */
public class GameController{
    private final Board board;
    private final List<Player> players;
    private ServerSOC serverSoc;
    private String currentPlayer;
    private ArrayList<ServerHandler> usersSoc;
    

    private ServerLoginImpl serverLoginImpl;
    private ActionInputCreated actionInputCreated;
    private IntegerCreated integerCreated;
    private FamilyColorCreated familyColorCreated;
    private BooleanCreated booleanCreated;
    private ArrayIntegerCreated arrayIntegerCreated;
    private TradeCreated tradeCreated;

    public GameController(Game game, ServerLoginImpl serverLoginImpl, ServerSOC serverSoc) {
        Thread threadGame = new Thread (game);
        threadGame.start();
        this.board = game.getBoard();
        this.players = game.getPlayers();
        this.serverSoc = serverSoc;
        this.usersSoc = serverSoc.getUsers();
        this.serverLoginImpl = serverLoginImpl;
    }
    
    public int getNumberPlayers(){
    	return board.getPlayerNumber();
    }
    
    public String getCurrentPlayer(){
    	return currentPlayer;
    }

    public void setActionInputCreated (ActionInputCreated actionInputCreated) {
        this.actionInputCreated = actionInputCreated;
    }

    public void setIntegerCreated (IntegerCreated integerCreated) {
        this.integerCreated = integerCreated;
    }

    public void setFamilyColorCreated (FamilyColorCreated familyColorCreated) {
    	this.familyColorCreated = familyColorCreated;
    }

    public void sendMessageCLI(Player player, String message) throws RemoteException {
        switch(player.getConnectionType()){
            case RMI:
                serverLoginImpl.sendMessage(player, message);
                System.out.println(message);
                break;
            case SOCKET:
                // TODO tom :P
                System.out.println(message);
                break;
        }
    }

    /*public void sendMessageGUI(Player player, String message) throws RemoteException {
        switch(player.getConnectionType()){
            case RMI:
                serverLoginImpl.sendMessage(player, message);
                System.out.println(message);
                break;
            case SOCKET:
                // TODO tom :P
                System.out.println(message);
                break;
        }
    }*/

    public int getWhatToDo(Player player) throws TooMuchTimeException, RemoteException{
        int whatToDo;
        whatToDo = integerCreated.get();
        return whatToDo;
    }

    public ActionSpot getViewActionSpot(Player player) throws TooMuchTimeException, RemoteException {
        ActionInput actionInput = actionInputCreated.get();
        if(actionInput == null) {
            return null;
        }
        switch(actionInput.getActionType()){
            case TERRITORY_TOWER:
                return board.getTerritoryTower().getFloors().get(actionInput.getSpot());
            case BUILDING_TOWER:
                return board.getBuildingTower().getFloors().get(actionInput.getSpot());
            case CHARACTER_TOWER:
                return board.getCharacterTower().getFloors().get(actionInput.getSpot());
            case VENTURE_TOWER:
                return board.getVentureTower().getFloors().get(actionInput.getSpot());
            case HARVEST:
                return board.getHarvestArea().get(actionInput.getSpot());
            case PRODUCE:
                return board.getProductionArea().get(actionInput.getSpot());
            case MARKET:
                return board.getMarket().get(actionInput.getSpot());
            case COUNCIL_PALACE:
                return board.getCouncilPalace();
            default:
                return null;
        }
    }

    public FamilyMember getViewFamilyMember(Player player) throws TooMuchTimeException, RemoteException{
        FamilyColor familyColor = familyColorCreated.get();
        int servant = 0;
        for(FamilyMember fM : player.getPlayerBoard().getFamilyMembers()){
            if(fM.getColor() == familyColor) {
                servant = getHowManyServants(player);
            }
            fM.setServantUsed(new Reward(RewardType.SERVANT, servant));
            return fM;
        }
        return null;
    }

    public int getHowManyServants (Player player) {
        player.putThird_State(PlayerState.SERVANTS);
        int index = integerCreated.get();
        return index;
    }

    public Set<Reward> exchangeCouncilPrivilege(Set<Reward> rewards, Player player) throws TooMuchTimeException, RemoteException{
        if(rewards == null){
            return null;
        }
        Set<Reward> newRewards = new HashSet<>();
        for(Reward r : rewards){
            if(r.getType() != RewardType.COUNCIL_PRIVILEGE){
                newRewards.add(r);
            }
            else{
                player.putThird_State(PlayerState.EXCHANGE_COUNCIL_PRIVILEGE);
                int[] rewardArray = rewardArray = arrayIntegerCreated.get();
                for(int i = 0; i < rewardArray.length; i++) {
                    switch(rewardArray[i]){
                        case 1:
                            newRewards.add(new Reward(RewardType.WOOD, 1));
                            newRewards.add(new Reward(RewardType.STONE, 1));
                            break;
                        case 2:
                            newRewards.add(new Reward(RewardType.SERVANT, 2));
                            break;
                        case 3:
                            newRewards.add(new Reward(RewardType.COIN, 2));
                            break;
                        case 4:
                            newRewards.add(new Reward(RewardType.MILITARY_POINT, 2));
                            break;
                        case 5:
                            newRewards.add(new Reward(RewardType.FAITH_POINT, 1));
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        return newRewards;
    }

    public FamilyColor chooseFamilyMemberColorNotNeutral(Player player){
        player.putThird_State(PlayerState.FAMILY_MEMBER);
        return familyColorCreated.get();
    }

    public LeaderCard askWhichCardPlaceChangeCopyActivate(List<LeaderCard> leaderCardsInHand, Player player) throws RemoteException {
        String message = "";
        for (int i = 0; i < leaderCardsInHand.size(); i++) {
            message += i + ".\n" + leaderCardsInHand.get(i).toString() + "\n";
        }
        this.sendMessageCLI(player, message);
        int index = integerCreated.get();
        return leaderCardsInHand.get(index);
    }

    public ImmediateLeaderCard askWhichImmediateCardActivate(List<ImmediateLeaderCard> leaderCardsInHand, Player player) throws RemoteException {
        String message = "";
        for (int i = 0; i < leaderCardsInHand.size(); i++) {
            message += i + ".\n" + leaderCardsInHand.get(i).toString() + "\n";
        }
        this.sendMessageCLI(player, message);
        int index = integerCreated.get();
        return leaderCardsInHand.get(index);
    }

    public boolean wantToSupportVatican(Player player) throws RemoteException{
        String message = "Do you support Vatican?";
        this.sendMessageCLI(player, message);
        boolean choose = booleanCreated.get();
        return  choose;
    }

    public Trade chooseTrade(BuildingCard buildingCard, Player player) throws RemoteException{
        String message = "";
        for (int i = 0; i < buildingCard.getTrades().size(); i++) {
            message += i + ". " + buildingCard.getTrades().get(i).toString() + "\n";
        }
        this.sendMessageCLI(player, message);
        player.putThird_State(PlayerState.CHOOSE_TRADE);
        Trade trade = tradeCreated.get();
        return trade;
    }

    public List<Reward> askWhichDiscount(List<List<Reward>> discounts, Player player){
        return discounts.get(TerminalInput.askWhichDiscount(discounts));
    }

    public boolean wantToPayWithMilitaryPoint(Set<Reward> costs, Reward militaryPointNeeded, Reward militaryPointPrice, Player player){
        return TerminalInput.wantToPayWithMilitaryPoint(costs, militaryPointNeeded, militaryPointPrice);
    }
}
