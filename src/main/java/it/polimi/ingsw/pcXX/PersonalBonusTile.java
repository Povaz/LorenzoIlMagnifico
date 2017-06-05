package it.polimi.ingsw.pcXX;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by trill on 24/05/2017.
 */
public class PersonalBonusTile {
<<<<<<< HEAD
    private final int diceHarvest = 1;
    private final Reward [] harvestRewards = new Reward [3];
    private final int diceProduction = 1;
    private final Reward [] productionRewards = new Reward [2];

    public PersonalBonusTile(Reward [] productionRewards, Reward [] harvestRewards){
        for(int i=0; i<3; i++){
        	this.harvestRewards[i] = harvestRewards [i];
        }
        for(int j=0; j<2; j++){
        	this.productionRewards[j] = productionRewards [j];
        }
    }
    
    
    //SERVE IL TOSTRING DI REWARD
    /*public String toString (){
    	String tileString = super.toString();
    	tileString+="\nDice Harvest: "+ diceHarvest + "\n";
    	for(int i=0; i<3; i++){
        	tileString += "Harvest Reward n° " + i + " : " harvestRewards[i].toString + "\n";
        }
    	tileString+="\nDice Production: "+ diceProduction + "\n";
    	for(int j=0; j<2; j++){
        	tileString += "Production Reward n° " + j + " : " productionRewards[j].toString + "\n";
        }
    	return tileString;
    }*/
    
    
=======
    private final int diceHarvest;
    private final Set<Reward> harvestRewards;
    private final int diceProduction;
    private final Set<Reward> productionRewards;

    public PersonalBonusTile(int number, int diceHarvest, int diceProduction){
        this.harvestRewards = new HashSet<>();
        this.productionRewards = new HashSet<>();
        this.diceHarvest = diceHarvest;
        this.diceProduction = diceProduction;
        //initialize(number);
    }

    /*private void initialize(int number){
        switch(number){
            case 0:
                productionRewards.add(new Reward(RewardType.MILITARY_POINT, 1));
                productionRewards.add(new Reward(RewardType.COIN, 2));
                harvestRewards.add(new Reward(RewardType.WOOD, 1));
                harvestRewards.add(new Reward(RewardType.STONE, 1));
                harvestRewards.add(new Reward(RewardType.SERVANT, 1));
                break;
            case 1:
                productionRewards.add(new Reward(RewardType.SERVANT, 2));
                productionRewards.add(new Reward(RewardType.COIN, 1));
                harvestRewards.add(new Reward(RewardType.WOOD, 1));
                harvestRewards.add(new Reward(RewardType.STONE, 1));
                harvestRewards.add(new Reward(RewardType.MILITARY_POINT, 1));
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
    }*/

>>>>>>> master
    public int getDiceHarvest(){
        return diceHarvest;
    }

    public int getDiceProduction(){
        return diceProduction;
    }
    
}
