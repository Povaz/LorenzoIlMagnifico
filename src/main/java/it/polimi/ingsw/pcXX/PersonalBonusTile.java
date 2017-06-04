package it.polimi.ingsw.pcXX;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by trill on 24/05/2017.
 */
public class PersonalBonusTile {
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
    
    
    public int getDiceHarvest(){
        return diceHarvest;
    }

    public int getDiceProduction(){
        return diceProduction;
    }
    
}
