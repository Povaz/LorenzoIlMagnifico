package it.polimi.ingsw.pc34.Controller;

import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;
import it.polimi.ingsw.pc34.Model.*;
import it.polimi.ingsw.pc34.RMI.*;
import it.polimi.ingsw.pc34.Socket.ServerHandler;
import it.polimi.ingsw.pc34.Socket.ServerSOC;

import java.io.IOException;
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

    private ServerRMIImpl serverRMI;
    private ActionInputCreated actionInputCreated = new ActionInputCreated();
    private IntegerCreated whatToDoCreated = new IntegerCreated();
    private IntegerCreated integerCreated = new IntegerCreated();
    private FamilyColorCreated familyColorCreated = new FamilyColorCreated();
    private BooleanCreated booleanCreated = new BooleanCreated();
    private ArrayIntegerCreated arrayIntegerCreated = new ArrayIntegerCreated();
    private int councilRewardsSize;
    private int tradesSize;
    
    private String actionSpot;
    private ActionInput actionInput = new ActionInput();
    private boolean inFlow = false;
    private String afkVar;
    

    private Timer timerTillTheEnd;

    public GameController(Game game, ServerRMIImpl serverRMI, ServerSOC serverSoc) {
        this.board = game.getBoard();
        this.players = game.getPlayers();
        this.serverSoc = serverSoc;
        this.usersSoc = serverSoc.getUsers();
        this.serverRMI = serverRMI;
    }

    public void addServerHandler(ServerHandler newHandler) throws IOException{
    	usersSoc.add(newHandler);
    }
    
    public void deleteServerHandler(String username) throws IOException{
    	for(ServerHandler userSoc : usersSoc){
    		if(userSoc.getName().equals(username)){
    			usersSoc.remove(userSoc);
    			return;
    		}
    	}
    }
    
    public void startTimer(String username) {
    	System.out.println("Starting a new Timer for " + username);
    	this.timerTillTheEnd = new Timer();
    	this.timerTillTheEnd.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					System.out.println("Timer expired for " + username);
					final String flow = flow("/afk", username);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 200000);
	}

	public void stopTimer() {
    	System.out.println("I've stopped the Timer");
    	this.timerTillTheEnd.purge();
    	this.timerTillTheEnd.cancel();
		System.out.println("I've stopped the Timer");
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
    
    public void sendMessageCLI(Player player, String message) throws RemoteException, IOException {
        switch(player.getConnectionType()){
            case RMI:
                serverRMI.sendMessage(player, message);
                System.out.println(message);
                break;
            case SOCKET:
				ServerHandler serverHandler = serverSoc.getServerHandler(player.getUsername());
				if(serverHandler == null){
					break;
				}
				serverHandler.sendToClient(message);
                System.out.println(message);
                break;
        }
    }

    public void sendMessageChat(String message, String username) throws IOException {
		message = username + ": " + message;
    	for (int i = 0; i < players.size(); i++) {
			sendMessageCLI(players.get(i), message);
		}
	}

    /*public void sendMessageGUI(Player player, String message) throws IOException {
        switch(player.getConnectionType()){
            case RMI:
                serverRMI.sendMessage(player, message);
                System.out.println(message);
                break;
            case SOCKET:
                ServerHandler serverHandler = serverSoc.getServerHandler(player.getUsername());
                serverHandler.sendToClient(message);
                System.out.println(message);
                break;
        }
    }*/

    public Integer getWhatToDo(Player player) throws TooMuchTimeException, RemoteException{
        int whatToDo;
        afkVar = "whatToDo";
        whatToDo = whatToDoCreated.get();
        System.out.println("WhatToDo taken: " + whatToDo);
        setInFlow();
        return whatToDo;
    }

    public ActionSpot getViewActionSpot(Player player) throws TooMuchTimeException, RemoteException {
        System.out.println("Aspetto un actioninput");
        afkVar = "actionInput";
    	try {
			ActionInput actionInput = actionInputCreated.get();
			if (actionInput == null) {
				setInFlow();
				return null;
			}
			setInFlow();
			switch (actionInput.getActionType()) {
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
		} catch (NullPointerException e) {
    		return null;
		}
    }

    public FamilyMember getViewFamilyMember(Player player) throws TooMuchTimeException, RemoteException{
    	afkVar= "familyColor";
        FamilyColor familyColor = familyColorCreated.get();
        if (familyColor == null) {
        	return null;
		}
        setInFlow();
        Integer servant = 0;
        for(FamilyMember fM : player.getPlayerBoard().getFamilyMembers()){
            if(fM.getColor() == familyColor) {
                servant = getHowManyServants(player);
                if (servant == null){
                	return null;
                }
                fM.setServantUsed(new Reward(RewardType.SERVANT, servant));
				return fM;
            }
        }
        return null;
    }

    public Integer getHowManyServants (Player player) {
        player.putSecond_State(PlayerState.SERVANTS);
        afkVar = "integer";
        int index = integerCreated.get(); //TODO Deve poter tornare -1
        setInFlow();
        return index;
    }

    public Set<Reward> exchangeCouncilPrivilege(Set<Reward> rewards, Player player) throws TooMuchTimeException, IOException{
    	this.councilRewardsSize=0;
		for(Reward reward : rewards) {
			if (reward.getType().equals(RewardType.COUNCIL_PRIVILEGE)) {
				this.councilRewardsSize++;
			}
		}
        Set<Reward> newRewards = new HashSet<>();
        for(Reward r : rewards){
            if(r.getType() != RewardType.COUNCIL_PRIVILEGE){
                newRewards.add(r);
            }
            else{
				player.putSecond_State(PlayerState.EXCHANGE_COUNCIL_PRIVILEGE);
				this.sendMessageCLI(player, "choose + " + councilRewardsSize + " different rewards! 1. 1 WOOD 1 Stone   2. 2 SERVANT   3. 2 COIN   4. 2 MILITARY_POINT   5. 1 FAITH_POINT");
				afkVar = "intArray";
                int[] rewardArray = arrayIntegerCreated.get(); //TODO Deve poter essere null
                setInFlow();
                if (rewardArray == null) {
                	return null;
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
    	afkVar = "familyColor";
        player.putSecond_State(PlayerState.FAMILY_MEMBER);
        FamilyColor familyColor = familyColorCreated.get(); //TODO Deve poter essere Null
        setInFlow();
        return familyColor;
    }

    public LeaderCard askWhichCardPlaceChangeCopyActivate(List<LeaderCard> leaderCardsInHand, Player player) throws RemoteException, IOException{
        String message = "";
        for (int i = 0; i < leaderCardsInHand.size(); i++) {
            message += i + ".\n" + leaderCardsInHand.get(i).toString() + "\n";
        }
        this.sendMessageCLI(player, message);
        afkVar = "integer";
        int index = integerCreated.get(); //TODO deve poter essere -1
        setInFlow();
        return leaderCardsInHand.get(index);
    }

    public ImmediateLeaderCard askWhichImmediateCardActivate(List<ImmediateLeaderCard> leaderCardsInHand, Player player) throws RemoteException, IOException {
        String message = "";
        for (int i = 0; i < leaderCardsInHand.size(); i++) {
            message += i + ".\n" + leaderCardsInHand.get(i).toString() + "\n";
        }
        this.sendMessageCLI(player, message);
        afkVar = "integer";  
        int index = integerCreated.get(); //TODO deve poter essere -1
        return leaderCardsInHand.get(index);
    }

    public boolean wantToSupportVatican(Player player) throws IOException{
    	String message = "Do you support Vatican? (yes or no)";
    	ServerHandler currPlayer = null;
    	switch (player.getConnectionType()) {
			case SOCKET:
				currPlayer = getServerHandler(player.getUsername());
				currPlayer.setStateGame("/vaticansupport");
				break;
			case RMI:
				serverRMI.setStateGame(player, "/vaticansupport");
				break;
		}
        this.sendMessageCLI(player, message);
        afkVar = "booleanVat";
        boolean choose = booleanCreated.get();
        switch (player.getConnectionType()) {
			case SOCKET:
				currPlayer.setStateGame(null);
				break;
			case RMI:
				serverRMI.setStateGame(player, null);
				break;
        }
        setInFlow();
        return  choose;
    }
    
    public ServerHandler getServerHandler (String username){
    	for(ServerHandler handler : usersSoc){
    		if(handler.getName().equals(username)){
    			return handler;
    		}
    	}
		return null;
    }
    
    public Trade chooseTrade (BuildingCard buildingCard, Player player) throws RemoteException, IOException{ //TODO Need check
        String message = "";
        for (int i = 0; i < buildingCard.getTrades().size(); i++) {
            message += i + ". " + buildingCard.getTrades().get(i).toString() + "\n";
        }
        this.sendMessageCLI(player, message);
        this.tradesSize = buildingCard.getTrades().size();
        player.putSecond_State(PlayerState.CHOOSE_TRADE);
        afkVar = "integer";
        int choose = integerCreated.get();  //TODO Deve poter essere -1
        Trade trade = buildingCard.getTrades().get(choose);
        setInFlow();
        return trade;
    }

    public List<Reward> askWhichDiscount(List<List<Reward>> discounts, Player player) throws RemoteException, IOException{
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
        afkVar = "integer";
        int index = integerCreated.get();
        List<Reward> discount = discounts.get(index); //TODO index deve poter essere -1
        setInFlow();
        return discount;
    }

    public int wantToPayWithMilitaryPoint(Set<Reward> costs, Reward militaryPointNeeded, Reward militaryPointPrice, Player player) throws RemoteException, IOException{ //TODO WITH PAOLO: null
        String message = "Do you want to pay with militaryPoint? You need " + militaryPointNeeded + "military Point and it costs + " + militaryPointPrice + "militaryPoint";
        this.sendMessageCLI(player, message);
        player.putSecond_State(PlayerState.PAY_WITH_MILITARY_POINT);
        afkVar = "boolean";
        int choose = integerCreated.get(); //TODO Deve poter essere -1
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
    	actionSpot = null;
    	actionInput = new ActionInput();
    }
    
	public String flow (String asked, String username) throws IOException{
    	//ENTER HERE IF IT'S YOUR TURN
    	if(inFlow == false) {
    		inFlow = true;
    		PlayerState state1 = getState(1 , username);
    		if(checkCurrentPlayer(username)){
    			//AFK PER PLAYER DI CUI E' IL TURNO
    			if(asked.equals("/afk")){
    				Player player = this.searchPlayerWithUsername(username);
    	    		switch(afkVar){
    	    			case("whatToDo"):
    	    				skip();
    	    				disconnectPlayer(player);
    	    				whatToDoCreated.put(4);
    	    				setInFlow();
    	    				return "You're being disconnected";
    	    			case("actionInput"):
    	    				skip();
    						disconnectPlayer(player);
    	    				actionInputCreated.put(null);	
    						setInFlow();
    	    				return "You're being disconnected";
    	    			case("familyColor"):
    	    				skip();
    	    				disconnectPlayer(player);
    	    				familyColorCreated.put(null);
    	    				setInFlow();
    	    				return "You're being disconnected";
    	    			case("integer"):
    	    				skip();
    	    				disconnectPlayer(player);
    	    				integerCreated.put(-1);
    	    				setInFlow();	
    	    				return "You're being disconnected";
    	    			case("intArray"):
    	    				skip();
    	    				disconnectPlayer(player);
    	    				arrayIntegerCreated.put(null);
    	    				setInFlow();
    	    				return "You're being disconnected";
    	    			case("booleanVat"):
    	    				skip();
    	    				disconnectPlayer(player);
    	    				booleanCreated.put(true);
    	    				setInFlow();
    	    				return "You're being disconnected";
    	    		}
    	    		return "not handled case";
    	    	}
	    		//ENTER HERE IF STATE1 STILL NOT DEFINED
	    		if(state1.equals(PlayerState.WAITING)){
	    			switch (asked) {
	    				case "/playturn" :
	    					setInFlow();
	    					return "What action you want to do? 1-action 2-place Leader Card 3-activate Leader Card 4-exchange Leader Card 5-skip";
	    				case "1" :
	    					whatToDoCreated.put(0);
	    			        return "Which ActionSpot do you choose? Choose a number : 1. TERRITORY TOWER 2. BUILDING TOWER 3. CHARACTER TOWER 4. VENTURE TOWER 5. HARVEST 6. PRODUCE 7. MARKET 8. COUNCILPALACE";
	    				case "2" :
	    					whatToDoCreated.put(1);
	    					return "Which Leader Card to place? From 0 to 3";
	    				case "3" :
	    					whatToDoCreated.put(2); 
	    					return "Which Leader Card to activate? From 0 to 3";
	    				case "4" :
	    					whatToDoCreated.put(3);
	    					return "Which Leader Card to exchange? From 0 to 3";
	    				case "5" :
	    					whatToDoCreated.put(4);
	    					skip();
	    					return "You skipped your turn!"; 
	    				default :
	    					setInFlow();
	    					return "Input error, Retry!";
	    			}
	        	}
	    		//ENTER HERE IF STATE1 IS DEFINED
	    		else{
	    			PlayerState state2 = getState(2 , username);
					if(state2.equals(PlayerState.WAITING)){
	        			switch (state1) {
		    				case PLACE_LEADER_CARD :
		    					if(checkNumber(0, 3, asked)){
		    						integerCreated.put(Integer.parseInt(asked));
		    						return "Request to place " + asked + " leader card";
		    					}
		    					else{
		    						setInFlow();
		    						return "Input error, Retry!";
		    					}
		    				case ACTIVATE_LEADER_CARD :
		    					if(checkNumber(0, 3, asked)){
		    						integerCreated.put(Integer.parseInt(asked));
		    						return "Requested to activate " + asked + " leader card";
		    					}
		    					else{
		    						setInFlow();
		    						return "Input error, Retry!";
		    					}
		    				case EXCHANGE_LEADER_CARD :
		    					if(checkNumber(0, 3, asked)){
		    						integerCreated.put(Integer.parseInt(asked));
		    						setInFlow();
		    						return "You choose to exchange " + asked + " leader card";
		    					}
		    					else{
		    						setInFlow();
		    						return "Input error, Retry!";
		    					}
		    				default:
		    					setInFlow();
		    					return "State not handled";
	        			}
	        		}
	        		else {
	        			switch (state1){ 
		    				case ACTION :
		    					System.out.println("ACTION confirmed");
		    					switch (state2) {
		    						case ACTION_INPUT :
		    							System.out.println("ACTION_INPUT confirmed");
		    							System.out.println("entra" + actionSpot);
		    							if(actionSpot==null && checkNumber(1, 8, asked)){
		    								actionSpot = asked;
		    								System.out.println("Ora setto l'ActionType");
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
			    									System.out.println("si sono entrato proprio qua");
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
		    							else if(actionSpot!=null){
		    								System.out.println("entra");
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
			    				                    	return "Retry";
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
			    				                    	return "Input error, Retry!";
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
			    				                    	return "Input error, Retry!";
			    				                    }
			    							}
		    							}
		    							else {
		    								setInFlow();
		    								return "Input error, Retry!";
		    							}
		    						case FAMILY_MEMBER :
		    							if (checkNumber(1, 4, asked)){
		    								switch (asked){
		    									case "1" :
		    										familyColorCreated.put(FamilyColor.WHITE);
		    										setInFlow();
		    										return "How many Servants do you want to use?";
		    									case "2" :
		    										familyColorCreated.put(FamilyColor.BLACK);
													setInFlow();
													return "How many Servants do you want to use?";
		    									case "3" :
		    										familyColorCreated.put(FamilyColor.ORANGE);
													setInFlow();
													return "How many Servants do you want to use?";
		    									case "4" :
		    										familyColorCreated.put(FamilyColor.NEUTRAL);
													setInFlow();
													return "How many Servants do you want to use?";
		    								}
		    							}
		    							else{
		    								setInFlow();
		    								return "Input error, Retry!";
		    							}
		    						case SERVANTS :
		    							if(checkNumber(0, 1000, asked)){
		    								integerCreated.put(Integer.parseInt(asked));
		    								setInFlow();
			    						    return "You choose to use " + Integer.parseInt(asked) + " servants";
		    							}
		    							else{
		    								setInFlow();
			    						    return "Input error, Retry!";
		    							}
		    							
									case EXCHANGE_COUNCIL_PRIVILEGE : //TODO Check con Tom: Compie l'azione ma gli input successivi continuano ad entrare qui, lo stato non è cambiato.
				    					System.out.println(PlayerState.EXCHANGE_COUNCIL_PRIVILEGE + " confirmed");
				    					String message = "";
				    					if(asked.length()==councilRewardsSize){
				    						int [] integerProduced = new int [councilRewardsSize]; 
				    						int value ;
				    						for(int i = 0; i < councilRewardsSize; i++){
				    							value = Character.getNumericValue(asked.charAt(i));
				    							integerProduced[i] = value;
				    							message += value;
				    						}
				    						arrayIntegerCreated.put(integerProduced);
											setInFlow();
				    						return "You request for " + message;
				    					}
				    					setInFlow();
				    					return "Input error, Retry!";
				    				case CHOOSE_TRADE :
				    					integerCreated.put(Integer.parseInt(asked));
				    					return "You choose the " + Integer.parseInt(asked) + " trade";
				    				case ASK_WHICH_DISCOUNT :			
				    					integerCreated.put(Integer.parseInt(asked));
				    					//TODO GESTIRE ERRORE PARSE INT
				    					return "You choose the " + Integer.parseInt(asked) + " discount";
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
				    		    		return "Input error, Retry!";
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
												return "You choose " + FamilyColor.WHITE + " color";
											case "1" :
												familyColorCreated.put(FamilyColor.BLACK);
												return "You choose " + FamilyColor.BLACK + " color";
											case "2" :
												familyColorCreated.put(FamilyColor.ORANGE);
												return "You choose " + FamilyColor.ORANGE + " color";
											default : 
												setInFlow();
												return "Error input, Retry!";
				    					}
				    				case ASK_WHICH_CARD_COPY :
				    					integerCreated.put(Integer.parseInt(asked));
				    					//TODO GESTIRE TUTTI GLI ERRORI DI PARSEINT
				    					return null;
				    				default:
				    					setInFlow();
				    					return "State not handled";
		    					}
		    				case EXCHANGE_LEADER_CARD :
		    					switch (state2){ 
				    				case EXCHANGE_COUNCIL_PRIVILEGE :
				    					if(asked.length()==1){
				    						int [] integerProduced = new int [1]; 
				    						int value ;
				    						value = Character.getNumericValue(asked.charAt(0));
				    						integerProduced[0] = value;
				    						arrayIntegerCreated.put(integerProduced);
				    						return "You choose the " + value + " reward";
				    					}
				    					setInFlow();
				    					return "Input error, Retry!";
				    				case WAITING:
				    					integerCreated.put(Integer.parseInt(asked));
				    					return "You choose " + Integer.parseInt(asked);
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
        			setInFlow();
        			return "You choose to support vatican";
        		}
        		else if(asked.equals("no")){
        			booleanCreated.put(false);
        			setInFlow();
        			return "You choose not to support vatican";
        		}
        		setInFlow();
        		return "Input error, Retry!";
        	}
        	
        	//ENTER HERE IF IT ISN'T YOUR TURN
        	else{
        		//AFK PER PLAYER DI CUI NON E' IL TURNO
        		if(asked.equals("/afk")){
    	    		Player player = this.searchPlayerWithUsername(username);
    	    		skip(); //TODO WHY?
    	    		disconnectPlayer(player);
					setInFlow();
					return "You're being disconnected";
    	    	}
        		setInFlow();
        		return "It isn't your turn";
        	}
    	}
    	else{
	    	return "I am still processing a request";
	   	}
    }

    public Player searchPlayerWithUsername (String username) throws IOException {
		for (Player player : players) {
			if (player.getUsername().equals(username)) {
				return player;
			}
		}
		return null;
	}

	public void disconnectPlayer (Player player) throws IOException {
		player.setDisconnected(true);
		player.setYourTurn(false);
		this.sendMessageCLI(player, "This Client has been disconnected");
		this.sendMessageChat("has disconnected.", player.getUsername());
	}
}
