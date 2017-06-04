package it.polimi.ingsw.pcXX;

public class Reward{
    private final RewardType type;
    private int quantity;

    public Reward(RewardType type, int quantity){
        this.type = type;
        this.quantity = quantity;
    }

    public RewardType getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reward reward = (Reward) o;

        if (quantity != reward.quantity) return false;
        return type == reward.type;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + quantity;
        return result;
    }

    @Override
    public String toString(){
        return "" + quantity + " " + type.toString();
    }

    public void addQuantity(Reward other){
        if(type != other.type){
            throw new IllegalArgumentException();
        }
        quantity += other.quantity;
    }

    /* TODO: fix exchange method!
    public Set<Reward> exchange (Set<Reward> rewards) {
        if(type == COUNCIL_PRIVILEGE) {
            for (Reward reward : rewards) {
                if (reward instanceof Resource) {
                    if (((Resource) reward).getType() == ResourceType.WOOD) {
                        ((Resource) reward).setQuantity(1);
                    }
                    if (((Resource) reward).getType() == ResourceType.STONE) {
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
        else{
            throw new IllegalArgumentException();
        }
    }*/
}
