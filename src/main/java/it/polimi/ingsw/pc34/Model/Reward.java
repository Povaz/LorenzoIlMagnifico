package it.polimi.ingsw.pc34.Model;

public class Reward{
    private final RewardType type;
    private int quantity;

    public Reward(RewardType type, int quantity){
        this.type = type;
        this.quantity = quantity;
    }

    public Reward(Reward other){
        this.type = other.type;
        this.quantity = other.quantity;
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
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reward reward = (Reward) o;

        if (quantity != reward.quantity) return false;
        return type == reward.type;
    }

    @Override
    public int hashCode(){
        return type.hashCode();
    }

    @Override
    public String toString(){
        StringBuilder bld = new StringBuilder();
        bld.append(quantity + " " + type.toString());
        return bld.toString();
    }

    public void sumQuantity(Reward other){
        if(type != other.type){
            return;
        }
        quantity += other.quantity;
    }

    public void subtractQuantity(Reward other){
        if(type != other.type){
            throw new IllegalArgumentException();
        }
        quantity -= other.quantity;
    }

    public void subtractQuantityLimitedZero(Reward other){
        if(type != other.type){
            throw new IllegalArgumentException();
        }
        quantity -= other.quantity;
        if(quantity < 0){
            quantity = 0;
        }
    }

    public Reward multiplyQuantity(int multiplier){
        return new Reward(type, quantity * multiplier);
    }

    public boolean isResource(){
        if(type == RewardType.WOOD || type == RewardType.STONE || type == RewardType.COIN || type == RewardType.SERVANT){
            return true;
        }
        return false;
    }
}
