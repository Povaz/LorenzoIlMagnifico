package it.polimi.ingsw.pcXX;

import java.util.*;

/**
 * Created by trill on 22/05/2017.
 */
public class Board {
    private final List<HarvestArea> harvestArea;
    private final List<ProductionArea> productionArea;
    private final List<Market> market;
    private final Tower territoryTower;
    private final Tower buildingTower;
    private final Tower characterTower;
    private final Tower ventureTower;
    private final CouncilPalace councilPalace;
    private final Order order;
    private final VaticanReportSpot vaticanReportSpot;
    private final List<Dice> dices;

    public Board(List<Player> players){
        this.harvestArea = new ArrayList<>();
        this.productionArea = new ArrayList<>();
        this.market = new ArrayList<>();
        this.territoryTower = new Tower(CardType.TERRITORY);
        this.buildingTower = new Tower(CardType.BUILDING);
        this.characterTower = new Tower(CardType.CHARACTER);
        this.ventureTower = new Tower(CardType.VENTURE);
        this.councilPalace = new CouncilPalace();
        this.order = new Order(players);
        this.vaticanReportSpot = new VaticanReportSpot();
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
        firstAreaReward.add(new Resource(ResourceType.COIN, 5));
        market.add(new Market(true, false, 1, firstAreaReward));

        Set<Reward> secondAreaReward = new HashSet<>();
        secondAreaReward.add(new Resource(ResourceType.SERVANT, 5));
        market.add(new Market(true, false, 1, secondAreaReward));

        if(playerNumber >= 4){
            Set<Reward> thirdAreaReward = new HashSet<>();
            thirdAreaReward.add(new Resource(ResourceType.COIN, 2));
            thirdAreaReward.add(new Point(PointType.MILITARY_POINT, 3));
            market.add(new Market(true, false, 1, thirdAreaReward));

            Set<Reward> fourthAreaReward = new HashSet<>();
            fourthAreaReward.add(new CouncilPrivilege(2));
            market.add(new Market(true, false, 1, fourthAreaReward));
        }
    }

    private void initializeVaticanReportSpot(){
        /*
        scegli 3 numeri casuali,
        da ogni numero estrai una tessera dal file JSON,
        creala e collegala allo spot,
        inizializza le altre variabili
         */
    }

    private void initializeDices(){
        dices.add(new Dice(FamilyColor.WHITE));
        dices.add(new Dice(FamilyColor.ORANGE));
        dices.add(new Dice(FamilyColor.BLACK));
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

    public VaticanReportSpot getVaticanReportSpot() {
        return vaticanReportSpot;
    }

    public List<Dice> getDices() {
        return dices;
    }
}
