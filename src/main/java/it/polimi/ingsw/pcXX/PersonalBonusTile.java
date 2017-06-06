package it.polimi.ingsw.pcXX;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by trill on 24/05/2017.
 */
public class PersonalBonusTile {
    private final int diceHarvest;
    private final Set<Reward> harvestRewards;
    private final int diceProduction;
    private final Set<Reward> productionRewards;

    public PersonalBonusTile(int diceProduction, int diceHarvest, Set<Reward> productionRewards, Set<Reward> harvestRewards){
        this.diceProduction = diceProduction;
        this.diceHarvest = diceHarvest;
        this.harvestRewards = harvestRewards;
        this.productionRewards = productionRewards;
    }

    public String toString (){
    	String tileString = super.toString();
    	tileString+="\nDice Harvest: "+ diceHarvest + "\n";
    	Iterator<Reward> iteratorHar = harvestRewards.iterator();
    	int contatore=1;
    	Reward element;
    	while(iteratorHar.hasNext()){
    	  element = (Reward) iteratorHar.next();
    	  tileString += "Harvest Reward n° " + contatore + " : " + element.toString() + "\n";
    	}
    	tileString+="\nDice Production: "+ diceProduction + "\n";
    	contatore=1;
    	Iterator<Reward> iteratorProd = productionRewards.iterator();
    	while(iteratorProd.hasNext()){
    	  element = (Reward) iteratorProd.next();
    	  tileString += "Production Reward n° " + contatore + " : " + element.toString() + "\n";
    	}
    	return tileString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonalBonusTile that = (PersonalBonusTile) o;

        if (diceHarvest != that.diceHarvest) return false;
        if (diceProduction != that.diceProduction) return false;
        if (harvestRewards != null ? !harvestRewards.equals(that.harvestRewards) : that.harvestRewards != null)
            return false;
        return productionRewards != null ? productionRewards.equals(that.productionRewards) : that.productionRewards == null;
    }

    @Override
    public int hashCode() {
        int result = diceHarvest;
        result = 31 * result + (harvestRewards != null ? harvestRewards.hashCode() : 0);
        result = 31 * result + diceProduction;
        result = 31 * result + (productionRewards != null ? productionRewards.hashCode() : 0);
        return result;
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
