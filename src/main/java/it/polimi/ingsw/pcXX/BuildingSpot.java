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
        return null;
    }

    @Override
    public boolean placeCard(DevelopmentCard card){
        return false;
    }
}
