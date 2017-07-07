package it.polimi.ingsw.pc34.Controller;

import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;
import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.Model.*;
import it.polimi.ingsw.pc34.Controller.Action.*;
import it.polimi.ingsw.pc34.RMI.ServerRMIImpl;
import it.polimi.ingsw.pc34.RMI.SynchronizedBoardView;
import it.polimi.ingsw.pc34.Socket.ServerSOC;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientInfo;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pc34.View.GUI.BoardView;
import org.json.JSONException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by trill on 30/05/2017.
 */
public class Game implements Runnable{
    private Logger LOGGER = Logger.getLogger(Game.class.getName());

    public static final int PERIOD_NUMBER = 3;
    public static final int TURNS_FOR_PERIOD = 2;
    public static final int CARD_FOR_TOWER = 4;
    public static final int LEADER_CARD_FOR_PLAYER = 4;
    private int turn;
    private int period;
    private final List<String> usernames;
    private final int playerNumber;
    private final Board board;
    private final List<Player> players;
    private final List<PlayerBoard> playerBoards;
    private final GameController gameController;
    private int[] territoryCard;
    private int[] buildingCard;
    private int[] characterCard;
    private int[] ventureCard;

    private ServerRMIImpl serverLoginImpl;
    private ServerSOC serverSoc;

    private Timer timer;
    private TimerTask timerTask;

    public static void main(String[] args) {
        Map<String, ClientInfo> users = new HashMap<>();
        users.put("Cugola", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        users.put("Affetti", new ClientInfo(ConnectionType.SOCKET, ClientType.GUI));
        users.put("Erik", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        users.put("TommLezzo", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        Thread thread = new Thread(new Game(users, null, null));
        thread.start();
    }

    public void run(){
        while (this.period <= this.PERIOD_NUMBER) {
            this.startPeriod();
            try {
				gameController.sendMessageChat("inizio periodo " + this.period, "Game");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            while (this.turn <= this.TURNS_FOR_PERIOD) {
                this.startTurn();
                try {
					gameController.sendMessageChat("inizio turno " + this.turn, "Game");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
                try {
                    this.playTurn();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                this.endTurn();
            }
            try {
                this.endPeriod();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        Player winner = this.decreeWinner();
        System.out.println("\n\nTHE WINNER IS: " + winner.getUsername());
        try {
			gameController.sendMessageChat("THE WINNER IS : " + winner.getUsername(), "Game");
			gameController.sendMessageChat("This game is finished", "Game");
		} catch (IOException e) {
			e.printStackTrace();
		}
        serverSoc.getServer().removeGame(this);
        
    }

    public Game(Map<String, ClientInfo> usersOfThisGame, ServerRMIImpl serverLoginImpl, ServerSOC serverSoc) {
        this.turn = 1;
        this.period = 2;
        this.usernames = new ArrayList<>();
        usernames.addAll(usersOfThisGame.keySet());
        this.playerNumber = usernames.size();
        this.players = initializePlayers(usersOfThisGame);
        this.board = new Board(players);
        initializePlayersRewards();
        initializePersonalBonusTile();
        initializeLeaderCards();
        this.playerBoards = initializePlayerBoards();
        this.serverSoc = serverSoc;
        this.serverLoginImpl = serverLoginImpl;
        this.gameController = new GameController(this, serverLoginImpl, serverSoc);
        ServerSOC.setGameControllerSoc(this.gameController);
    }

    private List<Player> initializePlayers(Map<String, ClientInfo> usersOfThisGame){
        List<Player> players = new ArrayList<>();
        for(int i = 0; i < playerNumber; i++){
            PlayerColor playerColor = PlayerColor.fromInt(i + 1);
            players.add(new Player(usernames.get(i), usersOfThisGame.get(usernames.get(i)), playerColor));
        }
        return players;
    }
    
    private List<PlayerBoard> initializePlayerBoards(){
        List<PlayerBoard> playerBoards = new ArrayList<>();
        for(int i = 0; i < players.size(); i++){
            playerBoards.add(players.get(i).getPlayerBoard());
        }
        return playerBoards;
    }

    private void initializePersonalBonusTile(){
        int tileNum;
        try{
            tileNum = JSONUtility.getPersonalBonusTileLength();
        } catch(JSONException e){
            LOGGER.log(Level.WARNING, "PersonalBonusTile.json: Wrong format", e);
            return;
        } catch(IOException e){
            LOGGER.log(Level.WARNING, "PersonalBonusTile.json: Incorrect path", e);
            return;
        }

        // Se non ci sono abbastanza bonus tile non darle;
        if(playerNumber > tileNum){
            return;
        }
        int[] tiles = RandomUtility.randomIntArray(0, tileNum - 1, playerNumber);

        for(int i = 0; i < tiles.length; i++){
            try{
                PersonalBonusTile tile = JSONUtility.getPersonalBonusTile(tiles[i]);
                players.get(i).getPlayerBoard().setPersonalBonusTile(tile);
            } catch(JSONException e){
                LOGGER.log(Level.WARNING, "PersonalBonusTile.json: Wrong format", e);
            } catch(IOException e){
                LOGGER.log(Level.WARNING, "PersonalBonusTile.json: Incorrect path", e);
            }
        }
    }

    private void initializeLeaderCards(){
        int permanentNum;
        int immediateNum;
        try{
            permanentNum = JSONUtility.getPermanentLeaderCardLength();
            immediateNum = JSONUtility.getImmediateLeaderCardLength();
        } catch(JSONException e){
            LOGGER.log(Level.WARNING, "ImmediateLeaderCard.json or PermanentLeaderCard.json: Wrong format", e);
            return;
        } catch(IOException e){
            LOGGER.log(Level.WARNING, "ImmediateLeaderCard.json or PermanentLeaderCard.json: Incorrect path", e);
            return;
        }

        // Se non ci sono abbastanza carte non darle;
        if(playerNumber * LEADER_CARD_FOR_PLAYER > permanentNum + immediateNum){
            return;
        }

        int[] leaderCards = RandomUtility.randomIntArray(0, permanentNum + immediateNum - 1, playerNumber * 4);
        int playerNum = 0;
        for(int i = 0; i < leaderCards.length; i++){
            try{
                LeaderCard card;
                // sono carte leader permanenti
                if(leaderCards[i] < permanentNum){
                    card = JSONUtility.getPermanentLeaderCard(leaderCards[i]);
                }
                // sono carte leader immediate
                else{
                    card = JSONUtility.getImmediateLeaderCard(leaderCards[i] - permanentNum);
                }
                players.get(playerNum).getPlayerBoard().getLeaderCardsInHand().add(card);
            }catch(JSONException e){
                LOGGER.log(Level.WARNING, "ImmediateLeaderCard.json or PermanentLeaderCard.json: Wrong format", e);
            } catch(IOException e){
                LOGGER.log(Level.WARNING, "ImmediateLeaderCard.json or PermanentLeaderCard.json: Incorrect path", e);
            }

            if((i + 1) % LEADER_CARD_FOR_PLAYER == 0){
                playerNum++;
            }
        }

    }

    private void initializePlayersRewards(){
        int i = 1;
        List<Player> order = board.getOrder().getShown();
        for(Player p : order){
            p.getPlayerBoard().setCounter(new Counter(i));
            i++;
        }
    }

    private void startPeriod(){
        try{
            territoryCard = RandomUtility.randomIntArray(0, JSONUtility.getCardLength(period, CardType.TERRITORY) - 1,
                    CARD_FOR_TOWER * 2);
        } catch(JSONException e){
            territoryCard = null;
            LOGGER.log(Level.WARNING, "TerritoryCard.json: Wrong format", e);
        } catch(IOException e){
            territoryCard = null;
            LOGGER.log(Level.WARNING, "TerritoryCard.json: Incorrect path", e);
        }

        try{
            buildingCard = RandomUtility.randomIntArray(0, JSONUtility.getCardLength(period, CardType.BUILDING) - 1,
                    CARD_FOR_TOWER * 2);
        } catch(JSONException e){
            buildingCard = null;
            LOGGER.log(Level.WARNING, "BuildingCard.json: Wrong format", e);
        } catch(IOException e){
            buildingCard = null;
            LOGGER.log(Level.WARNING, "BuildingCard.json: Incorrect path", e);
        }

        try{
            characterCard = RandomUtility.randomIntArray(0, JSONUtility.getCardLength(period, CardType.CHARACTER) - 1,
                    CARD_FOR_TOWER * 2);
        } catch(JSONException e){
            characterCard = null;
            LOGGER.log(Level.WARNING, "CharacterCard.json: Wrong format", e);
        } catch(IOException e){
            characterCard = null;
            LOGGER.log(Level.WARNING, "CharacterCard.json: Incorrect path", e);
        }

        try{
            ventureCard = RandomUtility.randomIntArray(0, JSONUtility.getCardLength(period, CardType.VENTURE) - 1,
                    CARD_FOR_TOWER * 2);
        } catch(JSONException e){
            ventureCard = null;
            LOGGER.log(Level.WARNING, "VentureCard.json: Wrong format", e);
        } catch(IOException e){
            ventureCard = null;
            LOGGER.log(Level.WARNING, "VentureCard.json: Incorrect path", e);
        }
    }

    private void endPeriod() throws RemoteException, IOException{
        churchSupport();
        period++;
        turn = 1;
        board.setPeriod(period);
        board.setTurn(turn);
    }

    private void churchSupport() throws RemoteException, IOException{
        VaticanReportSpot vaticanReportSpot = board.getVaticanReportSpot().get(period - 1);
        for(Player p : board.getOrder().getShown()){
            SupportVatican supportVatican = new SupportVatican(this, p, vaticanReportSpot);
            if(supportVatican.canDoAction()){
                supportVatican.doAction();
            }
            else{
                vaticanReportSpot.report(p);
            }
        }
    }

    private void startTurn() {
        throwDices();
        reinitializeFamilyMembers();
        reinitializeLeaderCards();
        placeDevelopmentCard();
    }

    private void playTurn() throws IOException {
        Order order = board.getOrder();
        do{
            Player current = order.getCurrent();
            gameController.sendMessageChat(current.getUsername() + " is your turn!", "Game");
            if (!current.isDisconnected()) {
                try {
                    ActionSpot actionSpot;
                    FamilyMember familyMember;
                    current.setYourTurn(true);
                    BoardView boardView = new BoardView(board, playerBoards, current.getUsername());
                    gameController.updatePlayersView(boardView);
                    gameController.startTimer(current.getUsername());
                    do {
                        System.out.println("\n\nPLAYERBOARD:"); //TODO GameController.sendMessageAll
                        System.out.println(board.toString());
                        System.out.println(current.getPlayerBoard());
                        System.out.println("\n\nIS YOUR TURN " + current.getUsername() + "!!!   " + current.getColor() + "\n\n");
                        Integer whatToDo = gameController.getWhatToDo(current);
                        switch (whatToDo) {
                        	case 0:
                                current.putFirst_State(PlayerState.ACTION);

                                if (!current.isPlacedFamilyMember()) {

                                    current.putSecond_State(PlayerState.ACTION_INPUT);
                                    actionSpot = gameController.getViewActionSpot(current);
                                    if (actionSpot == null) {
                                        break;
                                    }

                                    current.putSecond_State(PlayerState.FAMILY_MEMBER);
                                    familyMember = gameController.getViewFamilyMember(current);
                                    if (familyMember == null) {
                                        break;
                                    }

                                    if (placeFamilyMember(familyMember, actionSpot)) {
                                        current.setPlacedFamilyMember(true);
                                    }
                                    gameController.sendMessageCLI(current, "Action has been executed");
                                    boardView = new BoardView(board, playerBoards, current.getUsername());
                                    gameController.updatePlayersView(boardView);

                                } else {
                                    gameController.sendMessageCLI(current, "You have already placed a family member!");
                                    current.putFirst_State(PlayerState.ACTION);
                                }
                                break;
                            case 1:
                                current.putFirst_State(PlayerState.PLACE_LEADER_CARD);
                                PlaceLeaderCard placeLeaderCard = new PlaceLeaderCard(this, current);
                                if (placeLeaderCard.canDoAction()) {
                                    placeLeaderCard.doAction();
                                    gameController.sendMessageCLI(current, "Action has been executed");
                                    boardView = new BoardView(board, playerBoards, current.getUsername());
                                    gameController.updatePlayersView(boardView);
                                }
                                break;
                            case 2:
                                current.putFirst_State(PlayerState.ACTIVATE_LEADER_CARD);
                                ActivateImmediateLeaderCard activateImmediateLeaderCard = new ActivateImmediateLeaderCard(this, current);
                                if (activateImmediateLeaderCard.canDoAction()) {
                                    activateImmediateLeaderCard.doAction();
                                    gameController.sendMessageCLI(current, "Action has been executed");
                                    boardView = new BoardView(board, playerBoards, current.getUsername());
                                    gameController.updatePlayersView(boardView);

                                }
                                break;
                            case 3:
                                current.putFirst_State(PlayerState.EXCHANGE_LEADER_CARD);
                                ChangeLeaderCardInReward changeLeaderCardInReward = new ChangeLeaderCardInReward(this, current);
                                if (changeLeaderCardInReward.canDoAction()) {
                                    changeLeaderCardInReward.doAction();
                                    gameController.sendMessageCLI(current, "Action has been executed");
                                    boardView = new BoardView(board, playerBoards, current.getUsername());
                                    gameController.updatePlayersView(boardView);
                                }
                                break;
                            case 4:
                                current.setYourTurn(false);
                                gameController.stopTimer(current.getUsername());
                                gameController.setInFlow();
                                gameController.sendMessageCLI(current, "Action has been executed");
                                break;
                            default:
                                System.out.println("WhatToDo Error"); // No user should get here
                        }
                        current.putFirst_State(PlayerState.WAITING);
                        current.putSecond_State(PlayerState.WAITING);
                    } while (current.isYourTurn());
                } catch (TooMuchTimeException e) {
                    gameController.sendMessageCLI(current, "Timeout expired");
                    current.putFirst_State(PlayerState.WAITING);
                    current.putSecond_State(PlayerState.WAITING);
                }
            }
            current.putFirst_State(PlayerState.WAITING);
            current.putSecond_State(PlayerState.WAITING);
            current.setYourTurn(false);
            current.setPlacedFamilyMember(false);
            System.out.println("\n\nPLAYERBOARD:"); //TODO MANDARE LA STAMPA AGLI USER CLI
            System.out.println(order.getCurrent().getPlayerBoard());
        } while(board.getOrder().nextOrder());
    }

    public boolean placeFamilyMember(FamilyMember familyMember, ActionSpot actionSpot) throws TooMuchTimeException, RemoteException, IOException{
        if(actionSpot == null || familyMember == null){
            return false;
        }
        calculateRealValueFamilyMember(familyMember);
        if(actionSpot instanceof Market){
            PlaceMarket placeMarket = new PlaceMarket(this, actionSpot, familyMember);
            if(placeMarket.canDoAction()){
                placeMarket.doAction();
                return true;
            }
            return false;
        }
        if(actionSpot instanceof CouncilPalace){
            PlaceCouncilPalace placeCouncilPalace = new PlaceCouncilPalace(this, actionSpot, familyMember);
            if(placeCouncilPalace.canDoAction()){
                placeCouncilPalace.doAction();
                return true;
            }
            return false;
        }
        if(actionSpot instanceof HarvestArea){
            Harvest harvest = new Harvest(this, actionSpot, familyMember);
            if(harvest.canDoAction()){
                harvest.doAction();
                return true;
            }
            return false;
        }
        if(actionSpot instanceof ProductionArea){
            Produce produce = new Produce(this, actionSpot, familyMember);
            if(produce.canDoAction()){
                produce.doAction();
                return true;
            }
            return false;
        }
        if(actionSpot instanceof Floor){
            BuyCard buyCard = new BuyCard(this, actionSpot, familyMember);
            if(buyCard.canDoAction()) {
                buyCard.doAction();
                return true;
            }
            return false;
        }
        return false;
    }

    private void calculateRealValueFamilyMember(FamilyMember familyMember){
        Modifier modifier = familyMember.getPlayer().getPlayerBoard().getModifier();
        int value = familyMember.getValue();
        int servantUsed = familyMember.getServantUsed().getQuantity();
        if(modifier.isServantValueHalved()){
            servantUsed = servantUsed / 2;
        }
        int diceModifier = 0;
        if(!familyMember.isGhost()){
            if(familyMember.getColor() == FamilyColor.NEUTRAL){
                diceModifier = modifier.getNeutralFamilyMemberModifier();
            }
            else{
                diceModifier = modifier.getColoredFamilyMemberModifier();
            }
        }
        int realValue = value + servantUsed + diceModifier;
        familyMember.setRealValue(realValue);
    }

    private void endTurn(){
        calculateNewOrder();
        reinitializeBoard();
        turn++;
        board.setTurn(turn);
    }

    private Player decreeWinner(){
        List<Player> order = board.getOrder().getShown();
        earnVictoryPointFromMilitaryPoint(order);
        for(Player p : order){
            p.getPlayerBoard().earnFinalVictoryPoint();
        }
        return calculateWinner(order);
    }

    private void earnVictoryPointFromMilitaryPoint(List<Player> order){
        List<Player> first = new ArrayList<>();
        List<Player> second = new ArrayList<>();
        int firstMP = -1;
        int secondMP = -2;

        for(Player p : order){
            int current = p.getPlayerBoard().getCounter().getMilitaryPoint().getQuantity();
            if(current > firstMP){
                secondMP = firstMP;
                firstMP = current;
                second = first;
                first = new ArrayList<>();
                first.add(p);
            }
            else if(current == firstMP){
                first.add(p);
            }
            else if(current < firstMP && current > secondMP) {
                secondMP = current;
                second = new ArrayList<>();
                second.add(p);
            }
            else if(current == secondMP){
                second.add(p);
            }
        }

        for(Player p : first){
            p.getPlayerBoard().getCounter().sum(new Reward(RewardType.VICTORY_POINT, 5));
        }
        if(first.size() < 2){
            for(Player p : second){
                p.getPlayerBoard().getCounter().sum(new Reward(RewardType.VICTORY_POINT, 2));
            }
        }
    }

    private Player calculateWinner(List<Player> order){
        List<Player> winner = new ArrayList<>();
        int victoryPoint = -1;
        for(Player p : order){
            int current = p.getPlayerBoard().getCounter().getVictoryPoint().getQuantity();
            if(current > victoryPoint){
                victoryPoint = current;
                winner = new ArrayList<>();
                winner.add(p);
            }
            else if(current == victoryPoint){
                winner.add(p);
            }
        }
        for(Player p : order){
            for(Player w : winner){
                if(p == w){
                    return p;
                }
            }
        }
        return winner.get(0);
    }

    private void throwDices(){
        List<Dice> dices = board.getDices();
        for(Dice d : dices){
            d.throwDice();
        }
    }

    private void placeDevelopmentCard(){
        List<Floor> territoryFloors = board.getTerritoryTower().getFloors();
        List<Floor> buildingFloors = board.getBuildingTower().getFloors();
        List<Floor> characterFloors = board.getCharacterTower().getFloors();
        List<Floor> ventureFloors = board.getVentureTower().getFloors();

        for (int i = 0; i < CARD_FOR_TOWER; i++){
            int index = i + CARD_FOR_TOWER*(turn - 1);
            if(territoryCard != null) {
                try{
                    territoryFloors.get(i).setCard(JSONUtility.getCard(period, territoryCard[index], CardType.TERRITORY));
                } catch (JSONException e){
                    LOGGER.log(Level.WARNING, "TerritoryCard.json: Wrong format", e);
                } catch(IOException e){
                    LOGGER.log(Level.WARNING, "TerritoryCard.json: Incorrect path", e);
                }
            }

            if(buildingCard != null){
                try{
                    buildingFloors.get(i).setCard(JSONUtility.getCard(period, buildingCard[index], CardType.BUILDING));
                } catch (JSONException e){
                    LOGGER.log(Level.WARNING, "BuildingCard.json: Wrong format", e);
                } catch(IOException e){
                    LOGGER.log(Level.WARNING, "BuildingCard.json: Incorrect path", e);
                }
            }

            if(characterCard != null){
                try{
                    characterFloors.get(i).setCard(JSONUtility.getCard(period, characterCard[index], CardType.CHARACTER));
                } catch (JSONException e){
                    LOGGER.log(Level.WARNING, "CharacterCard.json: Wrong format", e);
                } catch(IOException e){
                    LOGGER.log(Level.WARNING, "CharacterCard.json: Incorrect path", e);
                }
            }

            if(ventureCard != null){
                try{
                    ventureFloors.get(i).setCard(JSONUtility.getCard(period, ventureCard[index], CardType.VENTURE));
                } catch (JSONException e){
                    LOGGER.log(Level.WARNING, "VentureCard.json: Wrong format", e);
                } catch(IOException e){
                    LOGGER.log(Level.WARNING, "VentureCard.json: Incorrect path", e);
                }
            }
        }
    }

    private void calculateNewOrder(){
        board.getOrder().recalculate(board.getCouncilPalace().getOccupiedBy());
    }

    private void reinitializeBoard() {
        reinitializeTowers();
        reinitializeHarvestArea();
        reinitializeProductionArea();
        reinitializeMarket();
        reinitializeCouncilPalace();
    }

    private void reinitializeTowers(){
        board.getTerritoryTower().reinitialize();
        board.getBuildingTower().reinitialize();
        board.getCharacterTower().reinitialize();
        board.getVentureTower().reinitialize();
    }

    private void reinitializeHarvestArea(){
        for(HarvestArea hA : board.getHarvestArea()){
            hA.reinitialize();
        }
    }

    private void reinitializeProductionArea(){
        for(ProductionArea pA : board.getProductionArea()){
            pA.reinitialize();
        }
    }

    private void reinitializeMarket(){
        for(Market m : board.getMarket()){
            m.reinitialize();
        }
    }

    private void reinitializeFamilyMembers(){
        for(Player p : players){
            p.getPlayerBoard().reinitializeFamilyMembers(board.getDices());
        }
    }

    private void reinitializeLeaderCards(){
        for(Player p : players){
            p.getPlayerBoard().reinitializeLeaderCards();
        }
    }

    private void reinitializeCouncilPalace(){
        board.getCouncilPalace().reinitialize();
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isDisconnected(String username){
    	for(Player player : players){
    		if(player.getUsername().equals(username)){
    			return player.isDisconnected();
    		}
    	}
		return false;
    }
    
    public List<Player> getPlayers() {
        return players;
    }

    public int[] getTerritoryCard() {
        return territoryCard;
    }

    public void setTerritoryCard(int[] territoryCard) {
        this.territoryCard = territoryCard;
    }

    public int[] getBuildingCard() {
        return buildingCard;
    }

    public void setBuildingCard(int[] buildingCard) {
        this.buildingCard = buildingCard;
    }

    public int[] getCharacterCard() {
        return characterCard;
    }

    public void setCharacterCard(int[] characterCard) {
        this.characterCard = characterCard;
    }

    public int[] getVentureCard() {
        return ventureCard;
    }

    public void setVentureCard(int[] ventureCard) {
        this.ventureCard = ventureCard;
    }

    public GameController getGameController(){
        return gameController;
    }

    public List<PlayerBoard> getPlayerBoards() {
        return playerBoards;
    }
}
