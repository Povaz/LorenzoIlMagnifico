package it.polimi.ingsw.pcXX;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by trill on 24/05/2017.
 */
public class PersonalBonusTile {
    private final int diceHarvest;
    private final Set<Reward> harvestRewards;
    private final int diceProduction;
    private final Set<Reward> productionRewards;

    public PersonalBonusTile(int number, int diceHarvest, int diceProduction){
        this.harvestRewards = new HashSet<>();
        this.productionRewards = new HashSet<>();
        this.diceHarvest = diceHarvest;
        this.diceProduction = diceProduction;
        initialize(number);
    }

    private void initialize(int number){
        switch(number){
            case 0:
                productionRewards.add(new Point(PointType.MILITARY_POINT, 1));
                productionRewards.add(new Resource(ResourceType.COIN, 2));
                harvestRewards.add(new Resource(ResourceType.WOOD, 1));
                harvestRewards.add(new Resource(ResourceType.STONE, 1));
                harvestRewards.add(new Resource(ResourceType.SERVANT, 1));
                break;
            case 1:
                productionRewards.add(new Resource(ResourceType.SERVANT, 2));
                productionRewards.add(new Resource(ResourceType.COIN, 1));
                harvestRewards.add(new Resource(ResourceType.WOOD, 1));
                harvestRewards.add(new Resource(ResourceType.STONE, 1));
                harvestRewards.add(new Point(PointType.MILITARY_POINT, 1));
                break;
            case 2:
                productionRewards.add(new Resource(ResourceType.SERVANT, 1));
                productionRewards.add(new Point(PointType.MILITARY_POINT, 2));
                harvestRewards.add(new Resource(ResourceType.WOOD, 1));
                harvestRewards.add(new Resource(ResourceType.STONE, 1));
                harvestRewards.add(new Resource(ResourceType.COIN, 1));
                break;
            case 3:
                productionRewards.add(new Point(PointType.MILITARY_POINT, 2));
                productionRewards.add(new Resource(ResourceType.COIN, 1));
                harvestRewards.add(new Resource(ResourceType.WOOD, 1));
                harvestRewards.add(new Resource(ResourceType.STONE, 1));
                harvestRewards.add(new Resource(ResourceType.SERVANT, 1));
                break;
            case 4:
                productionRewards.add(new Resource(ResourceType.SERVANT, 1));
                productionRewards.add(new Resource(ResourceType.COIN, 2));
                harvestRewards.add(new Resource(ResourceType.WOOD, 1));
                harvestRewards.add(new Resource(ResourceType.STONE, 1));
                harvestRewards.add(new Point(PointType.MILITARY_POINT, 1));
                break;
        }
    }

    public int getDiceHarvest(){
        return diceHarvest;
    }

    public int getDiceProduction(){
        return diceProduction;
    }

    public Set<Reward> getHarvestRewards() {
        return harvestRewards;
    }

    public Set<Reward> getProductionRewards() {
        return productionRewards;
    }
}
