package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.Controller.ActionInput;
import it.polimi.ingsw.pc34.Controller.PlayerState;
import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;
import it.polimi.ingsw.pc34.RMI.*;
import it.polimi.ingsw.pc34.Socket.ServerHandler;
import it.polimi.ingsw.pc34.Socket.ServerSOC;
import it.polimi.ingsw.pc34.View.TerminalInput;

import java.io.IOException;
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
    private ActionInputCreated actionInputCreated = new ActionInputCreated();
    private IntegerCreated integerCreated = new IntegerCreated();
    private FamilyColorCreated familyColorCreated = new FamilyColorCreated();
    private BooleanCreated booleanCreated = new BooleanCreated();
    private ArrayIntegerCreated arrayIntegerCreated = new ArrayIntegerCreated();
    private int councilRewardsSize;
    private int tradesSize;
    
    private String actionSpot;
    private ActionInput actionInput;
    private boolean inFlow = false;
    
    

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
    
    public void setInFlow(){
    	this.inFlow = false;
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

    public void sendMessageGUI(Player player, String message) throws IOException {
        switch(player.getConnectionType()){
            case RMI:
                serverLoginImpl.sendMessage(player, message);
                System.out.println(message);
                break;
            case SOCKET:
                ServerHandler serverHandler = serverSoc.getServerHandler(player.getUsername());
                serverHandler.sendToClient(message);
                System.out.println(message);
                break;
        }
    }

    public int getWhatToDo(Player player) throws TooMuchTimeException, RemoteException{
        int whatToDo;
        whatToDo = integerCreated.get();
        setInFlow();
        return whatToDo;
    }

    public ActionSpot getViewActionSpot(Player player) throws TooMuchTimeException, RemoteException {
        ActionInput actionInput = actionInputCreated.get();
        setInFlow();
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
		setInFlow();
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
        player.putSecond_State(PlayerState.SERVANTS);
        setInFlow();
        int index = integerCreated.get();
        return index;
    }

    public Set<Reward> exchangeCouncilPrivilege(Set<Reward> rewards, Player player) throws TooMuchTimeException, RemoteException{
        if(rewards == null) {
            return null;
        }
        Set<Reward> newRewards = new HashSet<>();
        for(Reward r : rewards){
            if(r.getType() != RewardType.COUNCIL_PRIVILEGE){
                newRewards.add(r);
            }
            else{
                this.councilRewardsSize = rewards.size();
                player.putSecond_State(PlayerState.EXCHANGE_COUNCIL_PRIVILEGE);
                int[] rewardArray = arrayIntegerCreated.get();
                setInFlow();
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
        player.putSecond_State(PlayerState.FAMILY_MEMBER);
        FamilyColor familyColor = familyColorCreated.get();
        setInFlow();
        return familyColor;
    }

    public LeaderCard askWhichCardPlaceChangeCopyActivate(List<LeaderCard> leaderCardsInHand, Player player) throws RemoteException {
        String message = "";
        for (int i = 0; i < leaderCardsInHand.size(); i++) {
            message += i + ".\n" + leaderCardsInHand.get(i).toString() + "\n";
        }
        this.sendMessageCLI(player, message);
        int index = integerCreated.get();
        setInFlow();
        return leaderCardsInHand.get(index);
    }

    public ImmediateLeaderCard askWhichImmediateCardActivate(List<ImmediateLeaderCard> leaderCardsInHand, Player player) throws RemoteException {
        String message = "";
        for (int i = 0; i < leaderCardsInHand.size(); i++) {
            message += i + ".\n" + leaderCardsInHand.get(i).toString() + "\n";
        }
        this.sendMessageCLI(player, message);
        int index = integerCreated.get();
        setInFlow();
        return leaderCardsInHand.get(index);
    }

    public boolean wantToSupportVatican(Player player) throws RemoteException{
        String message = "Do you support Vatican?";
        this.sendMessageCLI(player, message);
        boolean choose = booleanCreated.get();
        setInFlow();
        return  choose;
    }

    public Trade chooseTrade(BuildingCard buildingCard, Player player) throws RemoteException{
        String message = "";
        for (int i = 0; i < buildingCard.getTrades().size(); i++) {
            message += i + ". " + buildingCard.getTrades().get(i).toString() + "\n";
        }
        this.sendMessageCLI(player, message);
        this.tradesSize = buildingCard.getTrades().size();
        player.putSecond_State(PlayerState.CHOOSE_TRADE);
        int choose = integerCreated.get();
        setInFlow();
        return buildingCard.getTrades().get(choose);
    }

    public List<Reward> askWhichDiscount(List<List<Reward>> discounts, Player player) throws RemoteException{
        player.putSecond_State(PlayerState.ASK_WHICH_DISCOUNT);
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
        setInFlow();
        return discounts.get(index);
    }

    public boolean wantToPayWithMilitaryPoint(Set<Reward> costs, Reward militaryPointNeeded, Reward militaryPointPrice, Player player) throws RemoteException{
        String message = "Do you want to pay with militaryPoint? You need " + militaryPointNeeded + "military Point and it costs + " + militaryPointPrice + "militaryPoint";
        this.sendMessageCLI(player, message);
        player.putSecond_State(PlayerState.PAY_WITH_MILITARY_POINT);
        boolean choose = booleanCreated.get();
        setInFlow();
        return choose;
    }
    
    public boolean checkNumber (int min, int max, String decision){
        int dec;
        try{
        	dec = Integer.parseInt(decision);
        }
		catch(NumberFormatException nfe){
			return false;
		}
        
		if (dec >= min && dec <= max) {
            return true;
        }
        else {
            return false;
        }
	}
    
    private void skip (){
    	String actionSpot = null;
    	ActionInput actionInput = null;
    }
    
    public String flow (String asked, String username){
    	//ENTER HERE IF IT'S YOUR TURN
    	if(inFlow == false){
    		inFlow = true;
    		PlayerState state1 = getState(1 , username);
    		if(checkCurrentPlayer(username)){
	    		//ENTER HERE IF STATE1 STILL NOT DEFINED
	    		if(state1.equals(PlayerState.WAITING)){
	    			switch (asked) {
	    				case "/playturn" :
	    					setInFlow();
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
	    					skip();
	    					return "You skipped your turn!"; 
	    				default :
	    					setInFlow();
	    					return "Input error";
	    			}
	        	}
	    		//ENTER HERE IF STATE1 IS DEFINED
	    		else{
	    			PlayerState state2 = getState(2 , username);
	        		if(state2.equals(PlayerState.WAITING)){
	        			switch (state1){ 
		    				case PLACE_LEADER_CARD :
		    					integerCreated.put(Integer.parseInt(asked));
		    					return null;
		    				case ACTIVATE_LEADER_CARD :
		    					integerCreated.put(Integer.parseInt(asked));
		    					return null;
		    				case EXCHANGE_LEADER_CARD :
		    					integerCreated.put(Integer.parseInt(asked));
		    					return null;
		    				default:
		    					setInFlow();
		    					return "State not handled";
	        			}
	        		}
	        		else {
	        			switch (state1){ 
		    				case ACTION :
		    					actionInput = new ActionInput();
		    					switch (state2) {
		    						case ACTION_INPUT : 
		    							if(actionSpot==null){
		    								actionSpot = asked;
			    							switch(actionSpot) {
			    								case "1":
			    									actionInput.setActionType(ActionType.TERRITORY_TOWER);
			    									setInFlow();
			    									return "Which card? From 0 to 3";
			    								case "2":
			    									actionInput.setActionType(ActionType.BUILDING_TOWER);
			    									setInFlow();
			    									return "Which card? From 0 to 3";
			    								case "3":
			    									actionInput.setActionType(ActionType.CHARACTER_TOWER);
			    									setInFlow();
			    									return "Which card? From 0 to 3";
			    								case "4":
			    									actionInput.setActionType(ActionType.VENTURE_TOWER);
			    									setInFlow();
			    									return "Which card? From 0 to 3";
			    								case "5":
			    									actionInput.setActionType(ActionType.HARVEST);
			    									setInFlow();
			    									return "Which spot? 0 or 1";
			    								case "6":
			    									actionInput.setActionType(ActionType.PRODUCE);
			    									setInFlow();
			    									return "Which spot? 0 or 1";
			    								case "7":
			    									actionInput.setActionType(ActionType.MARKET);
			    									setInFlow();
			    									return "Which Spot? 0.COIN(5)  1.SERVANT(5)   2.COIN(2) & MILITARY_POINT(3) 3.COUNCILPRIVILEGE(2)";
			    								case "8":
			    									actionInput.setActionType(ActionType.COUNCIL_PALACE);
			    									actionInput.setSpot(0);
			    									actionInputCreated.put(actionInput);
			    									return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    							}
		    							}
		    							else{
		    								switch(actionSpot){ 
			    								case "1":
			    									if(checkNumber(0, 3, asked)){
			    										actionInput.setSpot(Integer.parseInt(asked));
			    										actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    									}
			    								case "2":
			    									if(checkNumber(0, 3, asked)){
			    										actionInput.setSpot(Integer.parseInt(asked));
			    										actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    									}
			    								case "3":
			    									if(checkNumber(0, 3, asked)){
			    										actionInput.setSpot(Integer.parseInt(asked));
			    										actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    									}
			    								case "4":
			    									if(checkNumber(0, 3, asked)){
			    										actionInput.setSpot(Integer.parseInt(asked));
			    										actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    									}
			    								case "5":
			    									if (players.size() > 2 && checkNumber(0, 1, asked)) {
			    										actionInput.setSpot(Integer.parseInt(asked));
			    										actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    				                    } else if(players.size() == 2 && checkNumber(0, 0, asked)){
			    				                        actionInput.setSpot(0);
			    				                        actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    				                    }
			    				                    else{
			    				                    	setInFlow();
			    				                    	return "retry";
			    				                    }
			    								case "6":
			    									if (players.size() > 2 && checkNumber(0, 1, asked)) {
			    										actionInput.setSpot(Integer.parseInt(asked));
			    										actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    				                    } else if(players.size() == 2 && checkNumber(0, 0, asked)){
			    				                        actionInput.setSpot(0);
			    				                        actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    				                    }
			    				                    else{
			    				                    	setInFlow();
			    				                    	return "retry";
			    				                    }
			    								case "7":
			    									if (players.size() > 3 && checkNumber(0, 3, asked)) {
			    										actionInput.setSpot(Integer.parseInt(asked));
			    										actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    				                    } else if(players.size() <= 3 && checkNumber(0, 1, asked)){
			    				                        actionInput.setSpot(0);
			    				                        actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    				                    }
			    				                    else{
			    				                    	setInFlow();
			    				                    	return "retry";
			    				                    }
			    							}
		    							}
		    						case FAMILY_MEMBER :
		    							if (checkNumber(0, 3, asked)){
		    								switch (asked){
		    									case "0" :
		    										familyColorCreated.put(FamilyColor.WHITE);
		    										return "How many Servants do you want to use?";
		    									case "1" :
		    										familyColorCreated.put(FamilyColor.BLACK);
		    										return "How many Servants do you want to use?";
		    									case "2" :
		    										familyColorCreated.put(FamilyColor.ORANGE);
		    										return "How many Servants do you want to use?";
		    									case "3" :
		    										familyColorCreated.put(FamilyColor.NEUTRAL);
		    										return "How many Servants do you want to use?";
		    								}
		    							}
		    						case SERVANTS :
		    							integerCreated.put(Integer.parseInt(asked));
		    						    return null;
				    				case EXCHANGE_COUNCIL_PRIVILEGE :
				    					if(asked.length()==councilRewardsSize){
				    						int [] integerProduced = new int [councilRewardsSize]; 
				    						int value ;
				    						for(int i = 0; i < councilRewardsSize; i++){
				    							value = Character.getNumericValue(asked.charAt(i));
				    							integerProduced[i] = value;
				    						}
				    						arrayIntegerCreated.put(integerProduced);
				    						return null;
				    					}
				    					return "Input error";
				    				case CHOOSE_TRADE :
				    					integerCreated.put(Integer.parseInt(asked));
				    					return null;
				    				case ASK_WHICH_DISCOUNT :			
				    					integerCreated.put(Integer.parseInt(asked));
				    					//GESTIRE ERRORE PARSE INT
				    					return null;
				    				case PAY_WITH_MILITARY_POINT :
				    					if(asked.equals("yes")){
				    		    			booleanCreated.put(true);
				    		    			return null;
				    		    		}
				    		    		else if(asked.equals("no")){
				    		    			booleanCreated.put(false);
				    		    			return null;
				    		    		}
				    					setInFlow();
				    		    		return "Input error";
				    				default:
				    					setInFlow();
				    					return "State not handled";
			        			}
		    				case ACTIVATE_LEADER_CARD :
		    					switch (state2){ 
				    				case FAMILY_MEMBER_NOT_NEUTRAL :
				    					switch (asked){
											case "0" :
												familyColorCreated.put(FamilyColor.WHITE);
												return null;
											case "1" :
												familyColorCreated.put(FamilyColor.BLACK);
												return null;
											case "2" :
												familyColorCreated.put(FamilyColor.ORANGE);
												return null;
											default : 
												setInFlow();
												return "Error input";
				    					}
				    				case ASK_WHICH_CARD_COPY :
				    					integerCreated.put(Integer.parseInt(asked));
				    					//GESTIRE TUTTI GLI ERRORI DI PARSEINT
				    					return null;
				    				default:
				    					setInFlow();
				    					return "State not handled";
		    					}
		    				case EXCHANGE_LEADER_CARD :
		    					switch (state2){ 
				    				case EXCHANGE_COUNCIL_PRIVILEGE :
				    					integerCreated.put(Integer.parseInt(asked));
				    					return null;
				    				default:
				    					setInFlow();
				    					return "State not handled";
								}
		    				default:
		    					setInFlow();
		    					return "State not handled";
	        			}
	        		}
	        	}
    		}
    		//ENTER HERE IF YOU ARE ASKED TO SUPPORT VATICAN
    		else if (state1.equals(PlayerState.SUPPORT_VATICAN)){
        		if(asked.equals("yes")){
        			booleanCreated.put(true);
        			return null;
        		}
        		else if(asked.equals("no")){
        			booleanCreated.put(false);
        			return null;
        		}
        		setInFlow();
        		return "Input error";
        	}
        	
        	//ENTER HERE IF IT ISN'T YOUR TURN
        	else{
        		setInFlow();
        		return "It isn't your turn";
        	}
    	}
    	else{
	    	return "I am still processing a request";
	   	}
    }
    
}
