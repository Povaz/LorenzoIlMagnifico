package it.polimi.ingsw.pcXX;

import java.util.Set;

/**
 * Created by trill on 20/05/2017.
 */
public class CostDiscount {
    private CardType type;
    private Set<Reward> discount;

    public CostDiscount(CardType type, Set<Reward> discount){
        this.type = type;
        this.discount = discount;
    }

    @Override
    public String toString(){
        String discountString = "" + type.toString() + ":\n";
        for(Reward r : discount){
            discountString += "  " + r.toString() + "\n";
        }
        return discountString;
    }
}
