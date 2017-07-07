package it.polimi.ingsw.pc34.Controller;

import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;
import it.polimi.ingsw.pc34.Model.*;
import it.polimi.ingsw.pc34.RMI.*;
import it.polimi.ingsw.pc34.Socket.ServerHandler;
import it.polimi.ingsw.pc34.Socket.ServerSOC;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pc34.View.GUI.BoardView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Created by trill on 14/06/2017.
 */

public class GameController {
	private final Board board;
	private final List<Player> players;
	private ServerSOC serverSoc;
	private ArrayList<ServerHandler> usersSoc;
	private ServerRMIImpl serverRMI;

	//Synchronized variables (Producer/Consumer pattern) to manage the dialogue between GameController.Flow() and
	// the other GameController methods
	private ActionInputCreated actionInputCreated = new ActionInputCreated();
	private IntegerCreated whatToDoCreated = new IntegerCreated();
	private IntegerCreated integerCreated = new IntegerCreated();
	private FamilyColorCreated familyColorCreated = new FamilyColorCreated();
	private BooleanCreated booleanCreated = new BooleanCreated();
	private ArrayIntegerCreated arrayIntegerCreated = new ArrayIntegerCreated();
	private int councilRewardsSize;
	private int tradesSize;

	//Support variables
	private String actionSpot;
	private ActionInput actionInput = new ActionInput();
	private boolean inFlow = false; //It tells if someone is already in the action flow in GameController.flow()
	private String afkVar; //State of a PlayerInput: if the Player crashes, GameController.flow() will finish his action


	private Timer timerTillTheEnd; //Timer till the end of the turn of a player

	public GameController(Game game, ServerRMIImpl serverRMI, ServerSOC serverSoc) {
		this.board = game.getBoard();
		this.players = game.getPlayers();
		this.serverSoc = serverSoc;
		this.usersSoc = serverSoc.getUsers();
		this.serverRMI = serverRMI;
	}

	public ServerHandler getServerHandler(Player player){
		String username = player.getUsername();
		for(ServerHandler userSearched : usersSoc){
			if(userSearched.getName().equals(username)){
				return userSearched;
			}
		}
		return null;
	}
	
	private ServerHandler getServerHandler (String username){ //TODO Commenta Tommaso
    	for(ServerHandler handler : usersSoc){
    		if(handler.getName().equals(username)){
    			return handler;
    		}
    	}
		return null;
    }
	
	public void addServerHandler(ServerHandler newHandler) throws IOException {
		usersSoc.add(newHandler);
	}

	public void deleteServerHandler(String username) throws IOException {
		for (ServerHandler userSoc : usersSoc) {
			if (userSoc.getName().equals(username)) {
				usersSoc.remove(userSoc);
				return;
			}
		}
	}

	public void startTimer(String username) { //Starts the timer for "Username" player's turn
		System.out.println("Starting a new Timer for " + username);
		this.timerTillTheEnd = new Timer();
		this.timerTillTheEnd.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					System.out.println("Timer expired for " + username);
					final String flow = flow("/afk", username);
					System.out.println(flow);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 300000);
	}

	public void stopTimer(String username) { //Cancels all scheduled task on timerTillTheEnd and cancels it
		System.out.println("Stopping Timer for " + username);
		this.timerTillTheEnd.purge();
		this.timerTillTheEnd.cancel();
	}

	public void setInFlow() {
		this.inFlow = false;
	} //It unlocks the principal flow in GameController.flow()

	public PlayerState getState(int number, String username) { //Get the "Username" player's first/second state
		for (Player player : players) {
			if (player.getUsername().equals(username)) {
				switch (number) {
					case 1:
						return player.getFirst_state();
					case 2:
						return player.getSecond_state();
				}
			}
		}
		return null;
	}

	public boolean checkCurrentPlayer(String username) { //It tells if "username" is the current player
		for (Player player : players) {
			if (player.getUsername().equals(username)) {
				return player.isYourTurn();
			}
		}
		return false;
	}

	public void sendMessageCLI(Player player, String message) throws IOException { //It sends message to "player"
		switch (player.getConnectionType()) {
			case RMI:
				serverRMI.sendMessage(player, message); //Some messages are evaluated also for GUI Users
				break;
			case SOCKET:
				ServerHandler serverHandler = serverSoc.getServerHandler(player.getUsername());
				if (serverHandler == null) {
					break;
				}
				serverHandler.sendToClient(message);
				break;
		}
	}

	public void sendMessageAll (String message) throws IOException { //Sends messages for all CLI Users, TODO used also to represent Board and PlayerBoard
		for (Player player : players) {
			sendMessageCLI(player, message);
		}
	}

	public void sendMessageChat(String message, String username) throws IOException { //Sends messages in the Chat
		message = username + " : " + message;
		for (Player player : players) {
			
			
			//TODO TOMMASO, MODIFICARE SE NO NON ARRIVANO MESSAGGI DELLA CHAT
			if(player.getClientType().equals(ClientType.GUI) && player.getConnectionType().equals(ConnectionType.SOCKET)){
				continue;
			}
			sendMessageCLI(player, message);
		}
	}

	public void updatePlayersView(BoardView boardView) throws RemoteException { //Sends the new "BoardView" object, to update GUI
		for (int i = 0; i < players.size(); i++) {
			switch (players.get(i).getConnectionType()) {
				case RMI:
					serverRMI.updateUserRMIView(boardView, players.get(i).getUsername());
					break;
				case SOCKET:
					for(ServerHandler gui : usersSoc){
						if(gui.getGraphicType().equals("2")){
							gui.sendToClientGUI(boardView);
						}
					}
			}
		}
	}

	public void updateRequested (String currentUsername) throws RemoteException { //Sends a requested update to "currentUsername" player
		List<PlayerBoard> playerBoards = new ArrayList<>();
		Player playerReconnected = null;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getUsername().equals(currentUsername)) {
				playerReconnected = players.get(i);
			}
			playerBoards.add(players.get(i).getPlayerBoard());
		}
		if (playerReconnected == null) {
			System.out.println("Error");
			return;
		}
		BoardView boardView = new BoardView(board, playerBoards, board.getOrder().getCurrent().getUsername());
		updatePlayerReconnectedView(boardView, playerReconnected);
	}

	//method for socket that return an updated board, especially for reconnected people
	public BoardView getBoardView(String username) {
		List<PlayerBoard> playerBoards = new ArrayList<>();
		for (int i = 0; i < players.size(); i++) {
			playerBoards.add(players.get(i).getPlayerBoard());
		}
		BoardView boardView = new BoardView(board, playerBoards, board.getOrder().getCurrent().getUsername());
		return boardView;
	}
	
	public void updatePlayerReconnectedView (BoardView boardView, Player player) throws RemoteException { //Update GUI for a Reconnected player
		switch (player.getConnectionType()) {
			case RMI:
				serverRMI.updateUserRMIView(boardView, player.getUsername());
				break;
			case SOCKET:
				//TODO Fill socket
				break;
		}
	}

	public Integer getWhatToDo(Player player) throws TooMuchTimeException, RemoteException { //Wait for a WhatToDo variabile
		int whatToDo;
		afkVar = "whatToDo";
		whatToDo = whatToDoCreated.get();
		setInFlow();
		return whatToDo;
	}

	public ActionSpot getViewActionSpot(Player player) throws TooMuchTimeException, RemoteException { //Wait for an ActionSpot from Client
		afkVar = "actionInput";
		try {
			ActionInput actionInput = actionInputCreated.get();
			resetActions();
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

	public FamilyMember getViewFamilyMember(Player player) throws TooMuchTimeException, RemoteException { //Waits for a FamilyColor, in order to create a FamilyMember
		afkVar = "familyColor";
		FamilyColor familyColor = familyColorCreated.get();
		if (familyColor == null) {
			return null;
		}
		setInFlow();
		Integer servant;
		for (FamilyMember fM : player.getPlayerBoard().getFamilyMembers()) {
			if (fM.getColor() == familyColor) {
				servant = getHowManyServants(player); //Here it waits for the number of Servants used by the player in this action
				if (servant == null) {
					return null;
				}
				fM.setServantUsed(new Reward(RewardType.SERVANT, servant));
				return fM;
			}
		}
		return null;
	}

	public Integer getHowManyServants(Player player) throws RemoteException { //Waits for the number of Servants used by the player in this action
		player.putSecond_State(PlayerState.SERVANTS);
		afkVar = "servant";
		if (player.getClientType().equals(ClientType.GUI)) { //If this Player is a GUI Player, it sends the right command in order to open the Servant Window
			switch (player.getConnectionType()) {
				case RMI:
					serverRMI.openNewWindow(player, "/numberservant");
					break;
				case SOCKET:
					getServerHandler(player).openNewWindow("/numberservant");
					break;
			}
		}
		int index = integerCreated.get();
		setInFlow();
		return index;
	}

	public Set<Reward> exchangeCouncilPrivilege(Set<Reward> rewards, Player player) throws TooMuchTimeException, IOException { //Waits for the array that represents the Council privilege rewards chosen by this Player
		this.councilRewardsSize = 0;
		for (Reward reward : rewards) {
			if (reward.getType().equals(RewardType.COUNCIL_PRIVILEGE)) {
				this.councilRewardsSize++;
			}
		}
		Set<Reward> newRewards = new HashSet<>();
		for (Reward r : rewards) {
			if (r.getType() != RewardType.COUNCIL_PRIVILEGE) {
				newRewards.add(r);
			} else {
				player.putSecond_State(PlayerState.EXCHANGE_COUNCIL_PRIVILEGE);
				String message = "choose + " + councilRewardsSize + " different rewards! 1. 1 WOOD 1 Stone   2. 2 SERVANT   3. 2 COIN   4. 2 MILITARY_POINT   5. 1 FAITH_POINT";
				this.sendMessageCLI(player, message);
				afkVar = "intArray";
				if (player.getClientType().equals(ClientType.GUI)) {//If this Player is a GUI Player, it sends the right command in order to open the Exchange Council Privilege
					switch (player.getConnectionType()) {
						case RMI:
							serverRMI.openNewWindow(player, "/exchangeprivilege", councilRewardsSize);
							break;
						case SOCKET:
							getServerHandler(player).openNewWindow("/exchangeprivilege", councilRewardsSize);
							break;
					}
				}
				int[] rewardArray = arrayIntegerCreated.get(); //Here it waits
				if (rewardArray == null) { //If the players crashes, GameController.flow will set rewardArray = null;
					rewardArray = new int[councilRewardsSize];
					for (int i = 0; i < councilRewardsSize; i++) { //If that happens, this array will be filled in order
						rewardArray[i] = i + 1; //And the player will get this rewards instead.
					}
				}
				setInFlow();
				for (int i = 0; i < rewardArray.length; i++) { //It fills rewards with the right Reward.
					switch (rewardArray[i]) {
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



    public FamilyColor chooseFamilyMemberColorNotNeutral(Player player){ //Waits for a FamilyColor (Not Neutral) chosen by this Player
    	afkVar = "familyColorNotNeutral";
        player.putSecond_State(PlayerState.FAMILY_MEMBER);
        FamilyColor familyColor = familyColorCreated.get();
        setInFlow();
        return familyColor;
    }

    public LeaderCard askWhichCardPlaceChangeCopyActivate(List<LeaderCard> leaderCardsInHand, Player player, String type) throws IOException{ //Waits for the leaderCard chosen by the player
		if (player.getClientType().equals(ClientType.GUI)) { //If the player is a GUI Players, it sends this command in order to open the "pay with military points" window

			String info = type; //Builds a string with the LeaderCard informations of the player and sends it to him
			for (int i = 0; i < leaderCardsInHand.size(); i++) {
				info += leaderCardsInHand.get(i).getName() + "/";
			}

			switch (player.getConnectionType()) {
				case RMI:
					serverRMI.openNewWindow(player, "/leadercard", info);
					break;
				case SOCKET:
					getServerHandler(player).openNewWindow("/leadercard", info);
					break;
			}
		}
		else {
			String message = ""; //Builds a string with the LeaderCard information of the player and sends it to him
			for (int i = 0; i < leaderCardsInHand.size(); i++) {
				message += i + ".\n" + leaderCardsInHand.get(i).toString() + "\n";
			}
			this.sendMessageCLI(player, message);
		}
		afkVar = "integer";
		int index = integerCreated.get(); //Here it waits
        setInFlow();
		if (index == -1) {
			return null;
		}
        return leaderCardsInHand.get(index);
    }

    public ImmediateLeaderCard askWhichImmediateCardActivate(List<ImmediateLeaderCard> immediateLeaderCardsInHand, Player player, String type) throws IOException { //As above, it waits for an ImmediateLeaderCard
		if (player.getClientType().equals(ClientType.GUI)) { //If the player is a GUI Players, it sends this command in order to open the "pay with military points" window

			String info = type; //Builds a string with the LeaderCard informations of the player and sends it to him
			for (int i = 0; i < immediateLeaderCardsInHand.size(); i++) {
				info += immediateLeaderCardsInHand.get(i).getName() + "/";
			}

			switch (player.getConnectionType()) {
				case RMI:
					serverRMI.openNewWindow(player, "/leadercard", info);
					break;
				case SOCKET:
					getServerHandler(player).openNewWindow("/leadercard", info);
					break;
			}
		}
		else {
			String message = ""; //Builds a string with the LeaderCard information of the player and sends it to him
			for (int i = 0; i < immediateLeaderCardsInHand.size(); i++) {
				message += i + ".\n" + immediateLeaderCardsInHand.get(i).toString() + "\n";
			}
			this.sendMessageCLI(player, message);
		}
		afkVar = "integer";
        int index = integerCreated.get();
		setInFlow();
		if (index == -1) {
			return null;
		}
        return immediateLeaderCardsInHand.get(index);
    }

    public boolean wantToSupportVatican(Player player) throws IOException{ //It manages the Vatican Support request
		if(player.isDisconnected()){ //If a Player is not present in the game, it will be set "false"
    		return false;
    	}
    	ServerHandler currPlayer = null;
    	switch (player.getConnectionType()) { //It sets this Player gameState in Server in order to evaluated next input as an answer to this question
			case SOCKET:
				currPlayer = getServerHandler(player.getUsername());
				currPlayer.setStateGame("/vaticansupport");
				break;
			case RMI:
				serverRMI.setStateGame(player, "/vaticansupport");
				break;
		}

		if (player.getClientType().equals(ClientType.GUI)) {
			switch (player.getConnectionType()) {
				case RMI:
					serverRMI.openNewWindow(player, "/supportvatican", "toSynchro");
					break;
				case SOCKET:
					getServerHandler(player).openNewWindow("/supportvatican", "toSynchro");
					break;
			}
		}
		else {
			String message = "Do you support Vatican? (yes or no)";
			this.sendMessageCLI(player, message);
		}

    	afkVar = "booleanVat";
    	System.out.println("prima get vatican AFK");
        boolean choose = booleanCreated.get(); //Here it waits
		System.out.println("dopo get vatican AFK");
        switch (player.getConnectionType()) { //Resets this player gameState in Server, so that new inputs are evaluated accordingly
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
    
    public Trade chooseTrade (BuildingCard buildingCard, Player player) throws IOException{ //Waits for the Trade chosen by the player
        String message = "";
		if (player.getClientType().equals(ClientType.GUI)) {
			String info = "";
			switch (player.getConnectionType()) {
				case RMI:
					serverRMI.openNewWindow(player, "/choosetrade", info);
					break;
				case SOCKET:
					getServerHandler(player).openNewWindow("/choosetrade", "toSynchro");
					break;
			}
		}
        else {
			for (int i = 0; i < buildingCard.getTrades().size(); i++) {
				message += i + ". " + buildingCard.getTrades().get(i).toString() + "\n";
			}
			this.sendMessageCLI(player, message);
		}
        this.tradesSize = buildingCard.getTrades().size();
        player.putSecond_State(PlayerState.CHOOSE_TRADE);
        afkVar = "integer";
        int choose = integerCreated.get(); //Here it waits
        Trade trade;
		if (choose == -1) { //If this player crashes, it will be automatically chosen the first trade
			trade = buildingCard.getTrades().get(0);
		}
		else {
			trade = buildingCard.getTrades().get(choose);
		}
        setInFlow();
        return trade;
    }

    public List<Reward> askWhichDiscount(List<List<Reward>> discounts, Player player) throws IOException{ //Waits for the Discount chosen by the player
        player.putSecond_State(PlayerState.ASK_WHICH_DISCOUNT);
		if (player.getClientType().equals(ClientType.GUI)) {
			String info = "";
			for (int i = 0; i < discounts.size(); i++) {
				info += discounts.get(i).toString() + "/";
			}
			switch (player.getConnectionType()) {
				case RMI:
					serverRMI.openNewWindow(player, "/choosediscount", info);
					break;
				case SOCKET:
					getServerHandler(player).openNewWindow("/choosediscount", "toSynchro");
					break;
			}
		}
		else {
			String message = "";
			for (int j = 0; j < discounts.size(); j++) {
				message += j + ". ";
				for (int i = 0; i < discounts.get(j).size(); i++) {
					message += discounts.get(j).get(i).toString();
				}
				message += "\n";
			}
			this.sendMessageCLI(player, message);
		}
		afkVar = "integer";
        int index = integerCreated.get(); //Here it waits
        List<Reward> discount;
        if (index == -1) { //If this player crashes, it will be automatically chosen the first trade
			discount = discounts.get(0);
		}
		else {
        	discount = discounts.get(index);
		}
		setInFlow();
        return discount;
    }

    public int wantToPayWithMilitaryPoint(Set<Reward> costs, Reward militaryPointNeeded, Reward militaryPointPrice, Player player) throws IOException{ //Waits for the decision of the player to pay a Venture Card with MilitaryPoint or normal resource
        String message = "Do you want to pay with militaryPoint? You need " + militaryPointNeeded + "military Point and it costs + " + militaryPointPrice + "militaryPoint";
        this.sendMessageCLI(player, message);
        player.putSecond_State(PlayerState.PAY_WITH_MILITARY_POINT);
        afkVar = "boolean";

        if (player.getClientType().equals(ClientType.GUI)) { //If the player is a GUI Players, it sends this command in order to open the "pay with military points" window
        	switch (player.getConnectionType()) {
				case RMI:
					serverRMI.openNewWindow(player, "/paymilitarypoint", "1");
					break;
				case SOCKET:
					getServerHandler(player).openNewWindow("/paymilitarypoint", "toSynchro");
					break;
			}
		}

        int choose = integerCreated.get(); //Here it waits
        if (choose == -1) {//If the player crashes, it will automatically choose to pay with normal resources
        	choose = 0;
		}
        setInFlow();
        return choose;
    }
    
    public boolean checkNumber (int min, int max, String decision){ //It checkes if the number inserted is correct
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
    
    private void resetActions (){
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
    	    				disconnectPlayer(player);
    	    				whatToDoCreated.put(4);
    	    				setInFlow();
    	    				return "You're being disconnected";
    	    			case("actionInput"):
    	    				resetActions();
    						disconnectPlayer(player);
    	    				actionInputCreated.put(null);	
    						setInFlow();
    	    				return "You're being disconnected";
						case("familyColorNotNeutral"):
							disconnectPlayer(player);
							familyColorCreated.put(FamilyColor.BLACK);
							setInFlow();
							return "You're being disconnected";
    	    			case("familyColor"):
    	    				disconnectPlayer(player);
    	    				familyColorCreated.put(null);
    	    				setInFlow();
    	    				return "You're being disconnected";
						case("servant"):
							disconnectPlayer(player);
							integerCreated.put(0);
							setInFlow();
							return "You're being disconnected";
    	    			case("integer"):
    	    				disconnectPlayer(player);
    	    				integerCreated.put(-1);
    	    				setInFlow();	
    	    				return "You're being disconnected";
    	    			case("intArray"):
    	    				disconnectPlayer(player);
    	    				arrayIntegerCreated.put(null);
    	    				setInFlow();
    	    				return "You're being disconnected";
    	    			case("booleanVat"):
    	    				disconnectPlayer(player);
    	    				booleanCreated.put(false);
    	    				setInFlow();
    	    				return "You're being disconnected";
    	    		}
    	    		System.out.println("booleanVatCase jumped");
    	    		setInFlow();
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
		    						String message = "Request to place " + asked + " leader card";
		    						return message;
		    					}
		    					else{
		    						setInFlow();
		    						return "Input error, Retry!";
		    					}
		    				case ACTIVATE_LEADER_CARD :
		    					if(checkNumber(0, 3, asked)){
		    						integerCreated.put(Integer.parseInt(asked));
		    						String message = "Requested to activate " + asked + " leader card";
		    						return message;
		    					}
		    					else{
		    						setInFlow();
		    						return "Input error, Retry!";
		    					}
		    				case EXCHANGE_LEADER_CARD :
		    					if(checkNumber(0, 3, asked)){
		    						integerCreated.put(Integer.parseInt(asked));
		    						setInFlow();
		    						String message = "You choose to exchange " + asked + " leader card";
		    						return message;
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
		    					switch (state2) {
		    						case ACTION_INPUT :
		    							if(actionSpot==null && checkNumber(1, 8, asked)){
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
			    									return "Which FamilyMember do you choose? 1. " + FamilyColor.BLACK + "  " + "2. " + FamilyColor.WHITE + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    							}
		    							}
		    							else if(actionSpot!=null){
		    								switch(actionSpot){ 
			    								case "1":
			    									if(checkNumber(0, 3, asked)){
			    										actionInput.setSpot(Integer.parseInt(asked));
			    										actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.BLACK + "  " + "2. " + FamilyColor.WHITE + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    									}
			    								case "2":
			    									if(checkNumber(0, 3, asked)){
			    										actionInput.setSpot(Integer.parseInt(asked));
			    										actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.BLACK + "  " + "2. " + FamilyColor.WHITE + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    									}
			    								case "3":
			    									if(checkNumber(0, 3, asked)){
			    										actionInput.setSpot(Integer.parseInt(asked));
			    										actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.BLACK + "  " + "2. " + FamilyColor.WHITE + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    									}
			    								case "4":
			    									if(checkNumber(0, 3, asked)){
			    										actionInput.setSpot(Integer.parseInt(asked));
			    										actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.BLACK + "  " + "2. " + FamilyColor.WHITE + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    									}
			    								case "5":
			    									if (players.size() > 2 && checkNumber(0, 1, asked)) {
			    										actionInput.setSpot(Integer.parseInt(asked));
			    										actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.BLACK + "  " + "2. " + FamilyColor.WHITE + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    				                    } else if(players.size() == 2 && checkNumber(0, 0, asked)){
			    				                        actionInput.setSpot(0);
			    				                        actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.BLACK + "  " + "2. " + FamilyColor.WHITE + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    				                    }
			    				                    else{
			    				                    	setInFlow();
			    				                    	return "Retry";
			    				                    }
			    								case "6":
			    									if (players.size() > 2 && checkNumber(0, 1, asked)) {
			    										actionInput.setSpot(Integer.parseInt(asked));
			    										actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.BLACK + "  " + "2. " + FamilyColor.WHITE + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    				                    } else if(players.size() == 2 && checkNumber(0, 0, asked)){
			    				                        actionInput.setSpot(0);
			    				                        actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.BLACK + "  " + "2. " + FamilyColor.WHITE + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    				                    }
			    				                    else{
			    				                    	setInFlow();
			    				                    	return "Input error, Retry!";
			    				                    }
			    								case "7":
			    									if (players.size() > 3 && checkNumber(0, 3, asked)) {
			    										actionInput.setSpot(Integer.parseInt(asked));
			    										actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.BLACK + "  " + "2. " + FamilyColor.WHITE + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
			    				                    } else if(players.size() <= 3 && checkNumber(0, 1, asked)){
			    				                        actionInput.setSpot(0);
			    				                        actionInputCreated.put(actionInput);
			    										return "Which FamilyMember do you choose? 1. " + FamilyColor.BLACK + "  " + "2. " + FamilyColor.WHITE + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
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
		    										familyColorCreated.put(FamilyColor.BLACK);
		    										setInFlow();
		    										return "How many Servants do you want to use?";
		    									case "2" :
		    										familyColorCreated.put(FamilyColor.WHITE);
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
		    							
									case EXCHANGE_COUNCIL_PRIVILEGE :
				    					String message = "";
				    					if(asked.length()==councilRewardsSize){
				    						int [] integerProduced = new int [councilRewardsSize]; 
				    						int value ;
				    						for(int i = 0; i < councilRewardsSize; i++){
				    							value = Character.getNumericValue(asked.charAt(i));
				    							if(!checkNumber(1 , 5 , Character.toString(asked.charAt(i)))){
				    								setInFlow();
					    							return "Input error, Retry!";
					    						}
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
				    					message = "You choose the " + Integer.parseInt(asked) + " trade";
				    					return message;
				    				case ASK_WHICH_DISCOUNT :			
				    					integerCreated.put(Integer.parseInt(asked));
				    					//TODO GESTIRE ERRORE PARSE INT
				    					message = "You choose the " + Integer.parseInt(asked) + " discount";
				    					return message;
				    				case PAY_WITH_MILITARY_POINT :
				    					if(asked.equals("yes")){
				    		    			integerCreated.put(1);
				    		    			return "You choose to pay with military points";
				    		    		}
				    		    		else if(asked.equals("no")){
				    		    			integerCreated.put(0);
				    		    			return "You choose to pay with standard reward";
				    		    		}
				    					setInFlow();
				    		    		return "Input error, Retry!";
				    				default:
				    					setInFlow();
				    					return "State not handled";
			        			}
		    				case ACTIVATE_LEADER_CARD :
		    					switch (state2){ 
				    				case FAMILY_MEMBER_NOT_NEUTRAL : //TODO CAMBIA NUMERAZIONE FAMILY MEMBER NOT NEUTRAL
				    					switch (asked){
											case "1" :
												familyColorCreated.put(FamilyColor.BLACK);
												return "You choose " + FamilyColor.BLACK + " color";
											case "2" :
												familyColorCreated.put(FamilyColor.WHITE);
												return "You choose " + FamilyColor.WHITE + " color";
											case "3" :
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
				    						if(!checkNumber(1 , 5 , Character.toString(asked.charAt(0)))){
				    							setInFlow();
				    							return "Input error, Retry!";
				    						}
				    						integerProduced[0] = value;
				    						arrayIntegerCreated.put(integerProduced);
				    						String message = "You choose the " + value + " reward";
				    						return message;
				    					}
				    					setInFlow();
				    					return "Input error, Retry!";
				    				case WAITING:
				    					integerCreated.put(Integer.parseInt(asked));
				    					String message = "You choose " + Integer.parseInt(asked);
				    					return message;
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
        		else if(asked.equals("/afk")) {this.searchPlayerWithUsername(username);
					booleanCreated.put(false);
					disconnectPlayer(this.searchPlayerWithUsername(username));
					setInFlow();
					return "You choose not to support vatican - disconnected";
				}
        		setInFlow();
        		return "Input error, Retry!";
        	}
        	
        	//ENTER HERE IF IT ISN'T YOUR TURN
        	else{
        		//AFK PER PLAYER DI CUI NON E' IL TURNO
        		if(asked.equals("/afk")){
    	    		Player player = this.searchPlayerWithUsername(username);
    	    		disconnectPlayer(player);
					setInFlow();
					return "You're being disconnected";
    	    	}
        		setInFlow();
        		return "It isn't your turn";
        	}
    	}
    	else{
    		System.out.println("I am still processing a request");
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