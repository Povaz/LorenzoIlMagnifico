package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.Controller.ActionInput;
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
    private ArrayIntegerCreated arrayIntegerCreated;

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

    public void sendMessage(Player player, String message) throws RemoteException {
        switch(player.getConnectionType()){
            case RMI:
                //serverGameRMI.sendMessage(player, message); //TODO
                System.out.println(message);
                break;
            case SOCKET:
                // TODO tom :P
                System.out.println(message);
                break;
        }
    }

    public int getWhatToDo(Player player) throws TooMuchTimeException, RemoteException{
        int whatToDo = 0;
        //serverGameRMI.setCurrentPlayer(player);
        switch(player.getConnectionType()) {
            case RMI:
                //serverGameRMI.askNumberMinMax(0,4);
                whatToDo = integerCreated.get();
                System.out.println("Action chosen: " + whatToDo);
                break;
            case SOCKET:
            	currentPlayer = player.getUsername();
            	for (ServerHandler user : usersSoc){
            		if(user.getName().equals(currentPlayer)){
            			user.getGameFlow().askNumber(0,3);
            		}
            	}
            	whatToDo = integerCreated.get();
            	System.out.println("Action chosen: " + whatToDo);
            	break;
        }
        return whatToDo;
    }

    public ActionSpot getViewActionSpot(Player player) throws TooMuchTimeException, RemoteException {
        ActionInput actionInput = new ActionInput();
        switch(player.getConnectionType()) {
            case RMI:
                //serverGameRMI.askAction(board.getPlayerNumber());
                actionInput = actionInputCreated.get();
                System.out.println("Action Input chosen: " + actionInput.toString());
                break;
            case SOCKET:
               for (ServerHandler user : usersSoc){
            		if(user.getName().equals(currentPlayer)){
            			user.getGameFlow().askAction();
            		}
               }
               actionInput = actionInputCreated.get();
               System.out.println("Action Input chosen: " + actionInput.toString());
               break;
        }
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
        FamilyColor familyColor = FamilyColor.NEUTRAL;
        switch(player.getConnectionType()) {
            case RMI:
                //serverGameRMI.askFamilyColor();
                familyColor = familyColorCreated.get();
                break;
            case SOCKET:
            	for (ServerHandler user : usersSoc){
            		if(user.getName().equals(currentPlayer)){
            			user.getGameFlow().askFamilyColor();
            		}
            	}
            	familyColor = familyColorCreated.get();
                break;
        }
        int servant = 0;
        for(FamilyMember fM : player.getPlayerBoard().getFamilyMembers()){
            if(fM.getColor() == familyColor) {
                switch(player.getConnectionType()) {
                    case RMI:
                        //serverGameRMI.askNumberMinMax(0,10);
                        servant = integerCreated.get();
                        break;
                    case SOCKET:
                    	for (ServerHandler user : usersSoc){
                    		if(user.getName().equals(currentPlayer)){
                    			user.getGameFlow().askNumberMinMax(0,10);
                    		}
                    	}
                    	servant = integerCreated.get();
                        break;
                }
                fM.setServantUsed(new Reward(RewardType.SERVANT, servant));
                return fM;
            }
        }
        return null;
    }

    public Reward askNumberOfServant(Player player){
        return new Reward(RewardType.SERVANT, TerminalInput.askNumberOfServant());
    }

    public void setArrayIntegerCreated (ArrayIntegerCreated arrayIntegerCreated) {
        this.arrayIntegerCreated = arrayIntegerCreated;
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
                int[] rewardArray = TerminalInput.exchangeCouncilPrivilege(r);
                switch (player.getConnectionType()) {
                    case RMI:
                        //serverGameRMI.askArrayInt();
                        rewardArray = arrayIntegerCreated.get();
                        break;
                    case SOCKET:
                        break;
                }
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
        return TerminalInput.chooseFamilyMemberColorNotNeutral();
    }

    public LeaderCard askWhichCardChange(List<LeaderCard> leaderCardsInHand, Player player){
        int index =  TerminalInput.askWhichCardChange(leaderCardsInHand);
        return leaderCardsInHand.get(index);
    }

    public LeaderCard askWhichCardPlace(List<LeaderCard> leaderCardsInHand, Player player){
        int index = TerminalInput.askWhichCardPlace(leaderCardsInHand);
        return leaderCardsInHand.get(index);
    }

    public LeaderCard askWhichCardCopy(List<LeaderCard> leaderCards, Player player){
        int index = TerminalInput.askWhichCardCopy(leaderCards);
        return leaderCards.get(index);
    }

    public ImmediateLeaderCard askWhichCardActivate(List<ImmediateLeaderCard> immediateLeaderCardsPositionated, Player player){
        int index = TerminalInput.askWhichCardActivate(immediateLeaderCardsPositionated);
        return immediateLeaderCardsPositionated.get(index);
    }

    public boolean wantToSupportVatican(Player player){
        return  TerminalInput.wantToSupportVatican();
    }

    public Trade chooseTrade(BuildingCard buildingCard, Player player){
        return TerminalInput.chooseTrade(buildingCard);
    }

    public List<Reward> askWhichDiscount(List<List<Reward>> discounts, Player player){
        return discounts.get(TerminalInput.askWhichDiscount(discounts));
    }

    public boolean wantToPayWithMilitaryPoint(Set<Reward> costs, Reward militaryPointNeeded, Reward militaryPointPrice, Player player){
        return TerminalInput.wantToPayWithMilitaryPoint(costs, militaryPointNeeded, militaryPointPrice);
    }
}
