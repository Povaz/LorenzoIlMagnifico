package it.polimi.ingsw.pcXX;

/**
 * Created by trill on 24/05/2017.
 */
public class TerritorySpot extends CardSpot{
    public TerritorySpot(){
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
