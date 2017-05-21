package it.polimi.ingsw.pcXX;

import sun.security.util.Resources_de;

import java.util.HashSet;

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

    public HashSet<Reward> exchange (HashSet <Reward> rewards) {
        for (Reward reward: rewards) {
            if (reward instanceof Resource) {
                if (((Resource) reward).getType() == ResourceType.WOOD) {
                    ((Resource) reward).setQuantity(1);
                }
                if (((Resource)reward).getType() == ResourceType.STONE) {
                    ((Resource) reward).setQuantity(1);
                }
                if (((Resource) reward).getType() == ResourceType.SERVANT) {
                    ((Resource) reward).setQuantity(2);
                }
                if (((Resource) reward).getType() == ResourceType.COIN) {
                    ((Resource) reward).setQuantity(2);
                }
            }

            if (reward instanceof Point) {
                if (((Point) reward).getType() == PointType.FAITH_POINT) {
                    ((Point) reward).setQuantity(1);
                }
                if (((Point) reward).getType() == PointType.MILITARY_POINT) {
                    ((Point) reward).setQuantity(2);
                }
            }
        }

        return rewards;
    }
}
