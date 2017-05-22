package it.polimi.ingsw.pcXX;

import java.util.*;

/**
 * Created by trill on 22/05/2017.
 */
public class Board {
    private final ArrayList<HarvestArea> harvestArea;
    private final ArrayList<ProductionArea> productionArea;
    private final ArrayList<Market> market;
    private final Tower territoryTower;
    private final Tower buildingTower;
    private final Tower characterTower;
    private final Tower ventureTower;
    private final CouncilPalace councilPalace;
    private final Order order;
    private final VaticanReportSpot vaticanReportSpot;
    private final ArrayList<Dice> dices;

    public Board(int playerNumber, ArrayList<PlayerColor> order){
        this.harvestArea = new ArrayList<HarvestArea>();
        this.productionArea = new ArrayList<ProductionArea>();
        this.market = new ArrayList<Market>();
        this.territoryTower = new Tower(CardType.TERRITORY);
        this.buildingTower = new Tower(CardType.BUILDING);
        this.characterTower = new Tower(CardType.CHARACTER);
        this.ventureTower = new Tower(CardType.VENTURE);
        this.councilPalace = new CouncilPalace();
        this.order = new Order(order);
        this.vaticanReportSpot = new VaticanReportSpot();
        this.dices = new ArrayList<Dice>();
        initialize(playerNumber);
    }

    private void initialize(int playerNumber){
        initializeHarvestArea(playerNumber);
        initializeProductionArea(playerNumber);
        initializeMarket(playerNumber);
        initializeVaticanReportSpot();
        initializeDices();
    }

    private void initializeHarvestArea(int playerNumber){
        harvestArea.add(new HarvestArea(true, false, 0));
        if(playerNumber >= 3)
            harvestArea.add(new HarvestArea(true, true, -3));
    }

    private void initializeProductionArea(int playerNumber){
        productionArea.add(new ProductionArea(true, false, 0));
        if(playerNumber >= 3)
            productionArea.add(new ProductionArea(true, true, -3));
    }

    private void initializeMarket(int playerNumber){
        Set<Reward> firstAreaReward = new HashSet<Reward>();
        firstAreaReward.add(new Resource(ResourceType.COIN, 5));
        market.add(new Market(true, false, 1, firstAreaReward));

        Set<Reward> secondAreaReward = new HashSet<Reward>();
        secondAreaReward.add(new Resource(ResourceType.SERVANT, 5));
        market.add(new Market(true, false, 1, secondAreaReward));

        if(playerNumber >= 4){
            Set<Reward> thirdAreaReward = new HashSet<Reward>();
            thirdAreaReward.add(new Resource(ResourceType.COIN, 2));
            thirdAreaReward.add(new Point(PointType.MILITARY_POINT, 3));
            market.add(new Market(true, false, 1, thirdAreaReward));

            Set<Reward> fourthAreaReward = new HashSet<Reward>();
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
}
