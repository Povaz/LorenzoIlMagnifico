package it.polimi.ingsw.pcXX;

/**
 * Created by trill on 24/05/2017.
 */
public class CharacterSpot extends CardSpot{
    public CharacterSpot(){
        super();
    }

    @Override
    public Point estimateVictoryPoint(){
        return null;
    }

    @Override
    public boolean placeCard(DevelopmentCard card){
        return false;
    }
}
