package it.polimi.ingsw.pcXX;

import java.util.Set;

/**
 * Created by trill on 24/05/2017.
 */
public class PersonalBonusTile {
    private Set<Reward> harvestRewards;
    private Set<Reward> productionRewards;

    public PersonalBonusTile(Set<Reward> harvestRewards, Set<Reward> productionRewards){
        this.harvestRewards = harvestRewards;
        this.productionRewards = productionRewards;
    }
}
