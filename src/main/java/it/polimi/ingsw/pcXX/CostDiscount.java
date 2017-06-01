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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CostDiscount that = (CostDiscount) o;

        if (type != that.type) return false;
        return discount != null ? discount.equals(that.discount) : that.discount == null;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (discount != null ? discount.hashCode() : 0);
        return result;
    }
}
