package it.polimi.ingsw.pcXX;

public class CouncilPrivilege extends Reward{
    private int quantity;

    public CouncilPrivilege(int quantity){
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CouncilPrivilege that = (CouncilPrivilege) o;

        return quantity == that.quantity;
    }

    @Override
    public int hashCode(){
        return quantity;
    }

    @Override
    public String toString(){
        return "" + quantity + " COUNCIL_PRIVILEGE";
    }
}
