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
    public abstract boolean placeCard(DevelopmentCard card);

    public List<DevelopmentCard> getCards(){
        return cards;
    }
}
