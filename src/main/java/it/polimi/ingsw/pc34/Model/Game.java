package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;
import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.Model.Action.*;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by trill on 30/05/2017.
 */
public class Game implements Runnable{
    public static final int PERIOD_NUMBER = 3;
    public static final int TURNS_FOR_PERIOD = 2;
    public static final int CARD_FOR_TOWER = 4;
    private int turn;
    private int period;
    private final List<String> usernames;
    private final int playerNumber;
    private final Board board;
    private final List<Player> players;
    private final GameController gameController;
    private int[] territoryCard;
    private int[] buildingCard;
    private int[] characterCard;
    private int[] ventureCard;

    public static void main(String[] args){
        Thread game = new Thread(new Game(Arrays.asList("Affetti", "Cugola")));
        game.start();
    }

    public void run(){
        while(this.period <= this.PERIOD_NUMBER){
            this.startPeriod();
            while(this.turn <= this.TURNS_FOR_PERIOD){
                this.startTurn();
                this.playTurn();
                this.endTurn();
            }
            this.endPeriod();
        }
        Player winner = this.decreeWinner();
        System.out.println("\n\nTHE WINNER IS: " + winner.getUsername());
    }

    public Game(List<String> usernames){
        this.turn = 1;
        this.period = 1;
        this.usernames = usernames;
        this.playerNumber = usernames.size();
        this.players = initializePlayers();
        this.board = new Board(players);
        initializePlayersRewards();
        this.gameController = new GameController(this);
    }

    // TODO aggiungi carte leader e personalBonusTile

    private List<Player> initializePlayers(){
        List<Player> players = new ArrayList<>();
        int[] tiles = RandomUtility.randomIntArray(0, playerNumber - 1);
        for(int i = 0; i < playerNumber; i++){
            PersonalBonusTile personalBonusTile = null;
            try {
                personalBonusTile = JSONUtility.getPersonalBonusTile(tiles[i]);
            } catch(JSONException e){
                personalBonusTile = null;
            } catch(IOException e){
                personalBonusTile = null;
            }
            PlayerColor playerColor = PlayerColor.fromInt(i + 1);
            players.add(new Player(usernames.get(i), playerColor, personalBonusTile, null));
        }
        return players;
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
        try {
            territoryCard = RandomUtility.randomIntArray(0, JSONUtility.getCardLength(period, CardType.TERRITORY) - 1,
                    CARD_FOR_TOWER * 2);
            buildingCard = RandomUtility.randomIntArray(0, JSONUtility.getCardLength(period, CardType.BUILDING) - 1,
                    CARD_FOR_TOWER * 2);
            characterCard = RandomUtility.randomIntArray(0, JSONUtility.getCardLength(period, CardType.CHARACTER) - 1,
                    CARD_FOR_TOWER * 2);
            ventureCard = RandomUtility.randomIntArray(0, JSONUtility.getCardLength(period, CardType.VENTURE) - 1,
                    CARD_FOR_TOWER * 2);
        } catch(JSONException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void endPeriod(){
        churchSupport();
        period++;
        turn = 1;
    }

    private void churchSupport(){
        VaticanReportSpot vaticanReportSpot = board.getVaticanReportSpot().get(period - 1);
        for(Player p : board.getOrder().getShown()){
            SupportVatican supportVatican = new SupportVatican(this, p, vaticanReportSpot);
            if(supportVatican.canDoAction()){
                supportVatican.canDoAction();
            }
            else{
                vaticanReportSpot.report(p);
            }
        }
    }

    private void startTurn(){
        throwDices();
        reinitializeFamilyMembers();
        reinitializeLeaderCards();
        placeDevelopmentCard();
    }

    private void playTurn(){
        System.out.println("\n\nBOARD:");
        System.out.println(board);
        Order order = board.getOrder();
        do{
            try{
                ActionSpot actionSpot;
                FamilyMember familyMember = null;
                do{
                    System.out.println("\n\nPLAYERBOARD:");
                    System.out.println(order.getCurrent().getPlayerBoard());
                    System.out.println("\n\nIS YOUR TURN " + order.getCurrent().getUsername() + "!!!   " + order.getCurrent().getColor() + "\n\n");
                    actionSpot = gameController.getViewActionSpot(order.getCurrent());
                    if(actionSpot != null){
                        familyMember = gameController.getViewFamilyMember(order.getCurrent());
                    }
                } while(!(placeFamilyMember(familyMember, actionSpot)));
            } catch(TooMuchTimeException e){
                // TODO addTimer
                e.printStackTrace();
            }
            System.out.println("\n\nPLAYERBOARD:");
            System.out.println(order.getCurrent().getPlayerBoard());
        } while(board.getOrder().nextOrder());
    }

    public boolean placeFamilyMember(FamilyMember familyMember, ActionSpot actionSpot) throws TooMuchTimeException{
        if(actionSpot == null || familyMember == null){
            return true;
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
            if(buyCard.canDoAction()){
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
            else if(current < firstMP && current > secondMP){
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
        try {
            List<Floor> territoryFloors = board.getTerritoryTower().getFloors();
            List<Floor> buildingFloors = board.getBuildingTower().getFloors();
            List<Floor> characterFloors = board.getCharacterTower().getFloors();
            List<Floor> ventureFloors = board.getVentureTower().getFloors();

            for (int i = 0; i < CARD_FOR_TOWER; i++){
                int index = i + CARD_FOR_TOWER*(turn - 1);
                territoryFloors.get(i).setCard(JSONUtility.getCard(period, territoryCard[index], CardType.TERRITORY));
                buildingFloors.get(i).setCard(JSONUtility.getCard(period, buildingCard[index], CardType.BUILDING));
                characterFloors.get(i).setCard(JSONUtility.getCard(period, characterCard[index], CardType.CHARACTER));
                ventureFloors.get(i).setCard(JSONUtility.getCard(period, ventureCard[index], CardType.VENTURE));
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
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
}
