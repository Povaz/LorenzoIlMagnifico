package it.polimi.ingsw.pcXX;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trill on 23/05/2017.
 */
public abstract class CardSpot{
    private final List<DevelopmentCard> cards;

    public CardSpot(){
        this.cards = new ArrayList<>();
    }

    public abstract Reward estimateVictoryPoint();

    public void placeCard(DevelopmentCard card){
        getCards().add(card);
    }

    public boolean canPlaceCard(Counter copyForCosts){
        if(cards.size() >= 6){
            return false;
        }
        return true;
    }

    public List<DevelopmentCard> getCards(){
        return cards;
    }

    @Override
    public String toString(){
        String string = "";
        for(DevelopmentCard dC : cards){
            string += dC.toString();
        }
        return string;
    }
}
