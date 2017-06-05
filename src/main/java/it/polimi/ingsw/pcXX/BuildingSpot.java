package it.polimi.ingsw.pcXX;

/**
 * Created by trill on 24/05/2017.
 */
public class BuildingSpot extends CardSpot{
    public BuildingSpot(){
        super();
    }

    @Override
    public Reward estimateVictoryPoint(){
        return new Reward(RewardType.VICTORY_POINT, 0);
    }
}
