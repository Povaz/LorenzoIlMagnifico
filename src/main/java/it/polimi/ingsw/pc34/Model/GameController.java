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
    
    public PlayerState getState (int number, String username){
    	for(Player player : players){
    		if(player.getUsername().equals(username)){
    			switch (number) {
    				case 1 :
    					return player.getFirst_state();	
    				case 2 :
    					return player.getSecond_state();
    				case 3 :
    					return player.getThird_state();
    			}
    		}
    	}
		return null;
    }
    
    public boolean checkCurrentPlayer(String username){
    	for(Player player : players){
    		if(player.getUsername().equals(username)){
    			return player.isYourTurn();
    		}
    	}
		return false;
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

    public List<Reward> askWhichDiscount(List<List<Reward>> discounts, Player player) throws RemoteException{
        String message = "";
        for (int j = 0; j < discounts.size(); j++) {
            message += j + ". ";
            for (int i = 0; i < discounts.get(j).size(); i++) {
                message += discounts.get(j).get(i).toString();
            }
            message += "\n";
        }
        this.sendMessageCLI(player, message);
        int index = integerCreated.get();
        return discounts.get(index);
    }

    public boolean wantToPayWithMilitaryPoint(Set<Reward> costs, Reward militaryPointNeeded, Reward militaryPointPrice, Player player){
        return TerminalInput.wantToPayWithMilitaryPoint(costs, militaryPointNeeded, militaryPointPrice);
    }

    public String flow (String asked, String username){
    	PlayerState state1 = getState(1 , username);
    	//ENTER HERE IF IT'S YOUR TURN
    	if(checkCurrentPlayer(username)){
    		//ENTER HERE IF STATE1 STILL NOT DEFINED
    		if(state1.equals(PlayerState.WAITING)){
    			switch (asked){ 
    				case "/playturn" :
    					return "What action you want to do? 1-action 2-place Leader Card 3-activate Leader Card 4-exchange Leader Card 5-skip";
    				case "1" :
    					integerCreated.put(0);
    			        return "Which ActionSpot do you choose? Choose a number : 1. TERRITORY TOWER 2. BUILDING TOWER 3. CHARACTER TOWER 4. VENTURE TOWER 5. HARVEST 6. PRODUCE 7. MARKET 8. COUNCILPALACE";
    				case "2" :
    					integerCreated.put(1);
    					return "Which Leader Card to place? From 0 to 3";
    				case "3" :
    					integerCreated.put(2); 
    					return "Which Leader Card to activate? From 0 to 3";
    				case "4" :
    					integerCreated.put(3);
    					return "Which Leader Card to exchange? From 0 to 3";
    				case "5" :
    					integerCreated.put(4);
    					// PULISCE TUTTI  DATI
    					// skip();
    					return "You skipped your turn!";   	
    				default :
    					return "Input error";
    			}
        	}
    		//ENTER HERE IF STATE1 IS DEFINED
    		else{
    			PlayerState state2 = getState(2 , username);
        		if(state2.equals(PlayerState.WAITING)){
        			switch (state1){ 
	    				case ACTION :
	    					return null;
	    				case PLACE_LEADER_CARD :
	    					integerCreated.put(Integer.parseInt(asked));
	    					return null;
	    				case ACTIVATE_LEADER_CARD :
	    					integerCreated.put(Integer.parseInt(asked));
	    					return null;
	    				case EXCHANGE_LEADER_CARD :
	    					integerCreated.put(Integer.parseInt(asked));
	    					return null;
	    					//COUNCIL PRIVILEGE DA' IN INGRESSO TUTTI GLI INTERI INSIEME
	    				default:
	    					return "State not handled";
        			}
        		}
        		else {
        			switch (state1){ 
    				case ACTION :
    					switch (state2) {
		    				case ACTION_INPUT :
		    					//CHECK SE SEI A TYPE O A SPOT
		    				case FAMILY_MEMBER :
		    				case EXCHANGE_COUNCIL_PRIVILEGE :
		    				case CHOOSE_TRADE :
		    				case ASK_WHICH_DISCOUNT :
		    				case PAY_WITH_MILITARY_POINT :
		    				default:
		    					return "State not handled";
	        			}
    				case ACTIVATE_LEADER_CARD :
    					switch (state2){ 
		    				case FAMILY_MEMBER_NOT_NEUTRAL :
		    					
		    				case ASK_WHICH_CARD_COPY :
		    				default:
		    					return "State not handled";
    					}
    				case EXCHANGE_LEADER_CARD :
    					switch (state2){ 
		    				case EXCHANGE_COUNCIL_PRIVILEGE :
		    				default:
		    					return "State not handled";
						}
    				default:
    					return "State not handled";
        			}
        		}
        	}
    	}
    	//ENTER HERE IF YOU ARE ASKED TO SUPPORT VATICAN
    	else if (state1.equals(PlayerState.SUPPORT_VATICAN)){
    		if(asked.equals("yes") || asked.equals("no")){
    			//CREA INTEGER CHE SI ASPETTA
    		}
    		return "Input error";
    	}
    	
    	//ENTER HERE IF IT ISN'T YOUR TURN
    	else{
    		return "It isn't your turn";
    	}
    	
    	
    	
    	
    	
    	
    }
    
}
