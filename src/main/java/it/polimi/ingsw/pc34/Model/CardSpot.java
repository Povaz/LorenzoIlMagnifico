package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.JSONUtility;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by trill on 23/05/2017.
 */
public abstract class CardSpot{
    private Logger LOGGER = java.util.logging.Logger.getLogger(CardSpot.class.getName());

    private final List<DevelopmentCard> cards;
    private final CardType cardType;
    final int CARD_SPOT_CAPACITY = 6;

    public CardSpot(CardType cardType){
        this.cards = new ArrayList<>();
        this.cardType = cardType;
    }

    public Set<Reward> estimateVictoryPoint(){
        try{
            return JSONUtility.getEndGameCardRewards(cardType, cards.size());
        } catch (JSONException e){
            LOGGER.log(Level.WARNING, "Config.json: Wrong format", e);
        } catch(IOException e){
            LOGGER.log(Level.WARNING, "Config.json: Incorrect path", e);
        }

        return new HashSet<>();
    }

    public void placeCard(DevelopmentCard card){
        getCards().add(card);
    }

    public boolean canPlaceCard(){
        if(cards.size() >= CARD_SPOT_CAPACITY){
            return false;
        }
        return true;
    }

    public List<DevelopmentCard> getCards(){
        return cards;
    }

    @Override
    public String toString(){
        StringBuilder bld = new StringBuilder();
        for(DevelopmentCard dC : cards){
            bld.append(dC.toString());
        }
        return bld.toString();
    }

    public CardType getCardType(){
        return cardType;
    }
}
