package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.Controller.ActionInput;
import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;
import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.View.TerminalInput;

import org.json.JSONException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by trill on 22/05/2017.
 */
public class Board {
    private Logger LOGGER = Logger.getLogger(Game.class.getName());

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

    void initializeHarvestArea(int playerNumber){
        harvestArea.add(new HarvestArea(true, false, 0, this));
        if(playerNumber >= 3)
            harvestArea.add(new HarvestArea(true, true, -3, this));
    }

    void initializeProductionArea(int playerNumber){
        productionArea.add(new ProductionArea(true, false, 0, this));
        if(playerNumber >= 3)
            productionArea.add(new ProductionArea(true, true, -3, this));
    }

    void initializeMarket(int playerNumber){
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

    void initializeVaticanReportSpot(){
        try {
            for(int period = 1; period <= Game.PERIOD_NUMBER; period++) {
                int cardNumber = RandomUtility.randomInt(0, JSONUtility.getVaticanReportLength(period) - 1);
                VaticanReportCard vaticanReportCard = JSONUtility.getVaticanReportCard(period, cardNumber);
                if(period == Game.PERIOD_NUMBER){
                    vaticanReportSpot.add(new VaticanReportSpot(vaticanReportCard, new Reward(RewardType.FAITH_POINT, 5), true));
                }
                else{
                    vaticanReportSpot.add(new VaticanReportSpot(vaticanReportCard, new Reward(RewardType.FAITH_POINT, period + 2), false));
                }
            }
        } catch(JSONException e){
            LOGGER.log(Level.WARNING, "VaticanReportCard.json: Wrong format", e);
        } catch(IOException e){
            LOGGER.log(Level.WARNING, "VaticanReportCard.json: Incorrect path", e);
        }
    }

    void initializeDices(){
        dices.add(new Dice(FamilyColor.WHITE));
        dices.add(new Dice(FamilyColor.ORANGE));
        dices.add(new Dice(FamilyColor.BLACK));
    }

    @Override
    public String toString(){
        StringBuilder bld = new StringBuilder();
        if(harvestArea != null){
            bld.append("Harvest area:\n");
            for(HarvestArea hA : harvestArea){
                bld.append(hA.toString() + "\n");
            }
            bld.append("\n");
        }
        if(productionArea != null){
            bld.append("Production area:\n");
            for(ProductionArea pA : productionArea){
                bld.append(pA.toString() + "\n");
            }
            bld.append("\n");
        }
        if(market != null){
            bld.append("Market:\n");
            for(Market m : market){
                bld.append(m.toString() + "\n");
            }
            bld.append("\n");
        }
        if(councilPalace != null){
            bld.append("Council palace:\n");
            bld.append(councilPalace + "\n\n\n");
        }
        if(territoryTower != null){
            bld.append("Territory tower:\n");
            bld.append(territoryTower + "\n");
        }
        if(buildingTower != null){
            bld.append("Building tower:\n");
            bld.append(buildingTower + "\n");
        }
        if(characterTower != null){
            bld.append("Character tower:\n");
            bld.append(characterTower + "\n");
        }
        if(ventureTower != null){
            bld.append("Venture tower:\n");
            bld.append(ventureTower + "\n");
        }
        if(vaticanReportSpot != null){
            bld.append("Vatican report spot:\n");
            for(VaticanReportSpot vRS : vaticanReportSpot){
                bld.append(vRS.toString() + "\n");
            }
            bld.append("\n");
        }
        if(order != null){
            bld.append("Order: \n");
            bld.append(order.toString() + "\n");
        }
        if(dices != null){
            bld.append("Dices:\n");
            for(Dice d : dices){
                bld.append(d.toString() + "\n");
            }
            bld.append("\n");
        }
        return bld.toString();
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
