package it.polimi.ingsw.pcXX;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by trill on 30/05/2017.
 */
public class Game{
    private final int PERIOD_NUMBER = 3;
    private final int TURNS_FOR_PERIOD = 2;
    private final int CARD_FOR_TOWER = 4;
    private int turn;
    private int period;
    private final List<String> usernames;
    private final int playerNumber;
    private final Board board;
    private final List<Player> players;
    private int[] territoryCard;
    private int[] buildingCard;
    private int[] characterCard;
    private int[] ventureCard;

    public Game(List<String> usernames){
        this.turn = 1;
        this.period = 1;
        this.usernames = usernames;
        this.playerNumber = usernames.size();
        this.board = new Board(null/*players*/);    //TODO inizializza (per inizializzare board c'è bisogno di player e viceversa)
        this.players = initializePlayers();
    }

    public static void main(String[] args) {
        Game game = new Game(Arrays.asList("Paolo", "Erick", "Tommaso"));
        while(game.period <= game.PERIOD_NUMBER){
            game.startPeriod();
            while(game.turn <= game.TURNS_FOR_PERIOD){
                game.startTurn();
                game.playTurn();
                game.endTurn();
            }
            game.endPeriod();
        }
        game.decreeWinner();
    }

    // TODO aggiungi carte leader e personalBonusTile

    private List<Player> initializePlayers(){
        List<Player> players = new ArrayList<>();
        for(int i = 0; i < playerNumber; i++){
            PlayerColor playerColor = PlayerColor.fromInt(i + 1);
            int position = board.getOrder().getPositionOrder(playerColor);
            players.add(new Player(usernames.get(i), playerColor, position, null, null));
        }

        return players;
    }

    public void startPeriod(){
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

    public void endPeriod(){
        //TODO
    }

    public void startTurn(){
        throwDices();
        placeDevelopmentCard();
    }

    public void playTurn(){
        FamilyMember familyMember = new FamilyMember(null, FamilyColor.WHITE);
        familyMember.setValue(5);
        ActionSpot actionSpot = new Market(true, true, 1, null);
        do{
            try {
                board.getOrder().getCurrent().placeFamilyMember(familyMember, actionSpot);
            } catch (TooMuchTimeException e){}
        } while(board.getOrder().nextOrder());
    }

    public void endTurn(){
        calculateNewOrder();
        reinitializeBoard();
    }

    public void decreeWinner(){
        //TODO
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

            int start = (CARD_FOR_TOWER) * (turn - 1);
            int end = CARD_FOR_TOWER * turn;
            for (int i = start; i < end; i++) {
                territoryFloors.get(i).setCard(JSONUtility.getCard(period, territoryCard[i], CardType.TERRITORY));
                buildingFloors.get(i).setCard(JSONUtility.getCard(period, buildingCard[i], CardType.BUILDING));
                characterFloors.get(i).setCard(JSONUtility.getCard(period, characterCard[i], CardType.CHARACTER));
                ventureFloors.get(i).setCard(JSONUtility.getCard(period, ventureCard[i], CardType.VENTURE));
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

    private void reinitializeCouncilPalace(){
        board.getCouncilPalace().reinitialize();
    }

    public int getPERIOD_NUMBER() {
        return PERIOD_NUMBER;
    }

    public int getCARD_FOR_TOWER() {
        return CARD_FOR_TOWER;
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
}
