package it.polimi.ingsw.pcXX;

import java.util.ArrayList;

/**
 * Created by trill on 23/05/2017.
 */
public abstract class CardSpot{
    private final ArrayList<DevelopmentCard> cards;

    public CardSpot(){
        this.cards = new ArrayList<DevelopmentCard>();
    }

    public abstract Point estimateVictoryPoint();
    public abstract boolean placeCard(DevelopmentCard card);
}
