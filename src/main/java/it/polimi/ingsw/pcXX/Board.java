package it.polimi.ingsw.pcXX;

import it.polimi.ingsw.pcXX.Exception.TooMuchTimeException;
import org.json.JSONException;

import java.io.IOException;
import java.util.*;

/**
 * Created by trill on 22/05/2017.
 */
public class Board {
    private final int playerNumber;
    private final List<HarvestArea> harvestArea;
    private final List<ProductionArea> productionArea;
    private final List<Market> market;
    private final Tower territoryTower;
    private final Tower buildingTower;
    private final Tower characterTower;
    private final Tower ventureTower;
    private final CouncilPalace councilPalace;
    private final Order order;
    private final List<VaticanReportSpot> vaticanReportSpot;
    private final List<Dice> dices;
    
    public Board(List<Player> players){
        this.playerNumber = players.size();
        this.harvestArea = new ArrayList<>();
        this.productionArea = new ArrayList<>();
        this.market = new ArrayList<>();
        this.territoryTower = new Tower(CardType.TERRITORY, this);
        this.buildingTower = new Tower(CardType.BUILDING, this);
        this.characterTower = new Tower(CardType.CHARACTER, this);
        this.ventureTower = new Tower(CardType.VENTURE, this);
        this.councilPalace = new CouncilPalace();
        this.order = new Order(players);
        this.vaticanReportSpot = new ArrayList<>();
        this.dices = new ArrayList<>();
        initialize(players.size());
    }

    private void initialize(int playerNumber){
        initializeHarvestArea(playerNumber);
        initializeProductionArea(playerNumber);
        initializeMarket(playerNumber);
        initializeVaticanReportSpot();
        initializeDices();
    }

    private void initializeHarvestArea(int playerNumber){
        harvestArea.add(new HarvestArea(true, false, 0, this));
        if(playerNumber >= 3)
            harvestArea.add(new HarvestArea(true, true, -3, this));
    }

    private void initializeProductionArea(int playerNumber){
        productionArea.add(new ProductionArea(true, false, 0, this));
        if(playerNumber >= 3)
            productionArea.add(new ProductionArea(true, true, -3, this));
    }

    private void initializeMarket(int playerNumber){
        Set<Reward> firstAreaReward = new HashSet<>();
        firstAreaReward.add(new Reward(RewardType.COIN, 5));
        market.add(new Market(true, false, 1, firstAreaReward));

        Set<Reward> secondAreaReward = new HashSet<>();
        secondAreaReward.add(new Reward(RewardType.SERVANT, 5));
        market.add(new Market(true, false, 1, secondAreaReward));

        if(playerNumber >= 4){
            Set<Reward> thirdAreaReward = new HashSet<>();
            thirdAreaReward.add(new Reward(RewardType.COIN, 2));
            thirdAreaReward.add(new Reward(RewardType.MILITARY_POINT, 3));
            market.add(new Market(true, false, 1, thirdAreaReward));

            Set<Reward> fourthAreaReward = new HashSet<>();
            fourthAreaReward.add(new Reward(RewardType.COUNCIL_PRIVILEGE, 2));
            market.add(new Market(true, false, 1, fourthAreaReward));
        }
    }

    private void initializeVaticanReportSpot(){
        try {
            for(int period = 1; period <= Game.PERIOD_NUMBER; period++) {
                int cardNumber = RandomUtility.randomInt(0, JSONUtility.getVaticanReportLength(period)) - 1;
                VaticanReportCard vaticanReportCard = JSONUtility.getVaticanReportCard(period, cardNumber);
                vaticanReportSpot.add(new VaticanReportSpot(vaticanReportCard));
            }
        } catch(JSONException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void initializeDices(){
        dices.add(new Dice(FamilyColor.WHITE));
        dices.add(new Dice(FamilyColor.ORANGE));
        dices.add(new Dice(FamilyColor.BLACK));
    }

    public ActionSpot getViewActionSpot() throws TooMuchTimeException {
        Action action = TerminalInput.chooseAction(playerNumber);
        switch(action.getActionType()){
            case TERRITORY_TOWER:
                return territoryTower.getFloors().get(action.getSpot());
            case BUILDING_TOWER:
                return buildingTower.getFloors().get(action.getSpot());
            case CHARACTER_TOWER:
                return characterTower.getFloors().get(action.getSpot());
            case VENTURE_TOWER:
                return ventureTower.getFloors().get(action.getSpot());
            case HARVEST:
                return harvestArea.get(action.getSpot());
            case PRODUCE:
                return productionArea.get(action.getSpot());
            case MARKET:
                return market.get(action.getSpot());
            case COUNCIL_PALACE:
                return councilPalace;
            default:
                return null;
        }
    }

    @Override
    public String toString(){
        String boardString = "";
        if(harvestArea != null) {
            boardString += "harvestArea:\n";
            for (HarvestArea hA : harvestArea) {
                boardString += hA.toString() + "\n\n";
            }
            boardString += "\n";
        }
        if(productionArea != null){
            boardString += "productionArea:\n";
            for(ProductionArea pA : productionArea){
                boardString += pA.toString() + "\n\n";
            }
            boardString += "\n";
        }
        if(market != null){
            boardString += "market:\n";
            for(Market m : market){
                boardString += m.toString() + "\n\n";
            }
            boardString += "\n";
        }
        if(councilPalace != null){
            boardString += "councilPalace:\n";
            boardString += councilPalace + "\n\n\n";
        }
        if(territoryTower != null){
            boardString += "territoryTower:\n";
            boardString += territoryTower + "\n\n";
        }
        if(buildingTower != null){
            boardString += "buildingTower:\n";
            boardString += buildingTower + "\n\n";
        }
        if(characterTower != null){
            boardString += "characterTower:\n";
            boardString += characterTower + "\n\n";
        }
        if(ventureTower != null){
            boardString += "ventureTower:\n";
            boardString += ventureTower + "\n\n";
        }
        if(order != null){
            boardString += "order: \n";
            boardString += order.toString() + "\n";
        }
        if(dices != null){
            boardString += "dices:\n";
            for(Dice d : dices){
                boardString += d.toString() + "\n";
            }
            boardString += "\n";
        }
        if(vaticanReportSpot != null){
            boardString += "vaticanReportSpot:\n";
            for(VaticanReportSpot vRS : vaticanReportSpot){
                boardString += "  " + vRS.toString() + "\n";
            }
            boardString += "\n";
        }
        return boardString;
    }

    public List<HarvestArea> getHarvestArea() {
        return harvestArea;
    }

    public List<ProductionArea> getProductionArea() {
        return productionArea;
    }

    public List<Market> getMarket() {
        return market;
    }

    public Tower getTerritoryTower() {
        return territoryTower;
    }

    public Tower getBuildingTower() {
        return buildingTower;
    }

    public Tower getCharacterTower() {
        return characterTower;
    }

    public Tower getVentureTower() {
        return ventureTower;
    }

    public CouncilPalace getCouncilPalace() {
        return councilPalace;
    }

    public Order getOrder() {
        return order;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public List<VaticanReportSpot> getVaticanReportSpot() {
        return vaticanReportSpot;
    }

    public List<Dice> getDices() {
        return dices;
    }
}
