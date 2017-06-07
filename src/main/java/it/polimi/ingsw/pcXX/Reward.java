package it.polimi.ingsw.pcXX;

import it.polimi.ingsw.pcXX.Exception.TooMuchTimeException;

import java.util.HashSet;
import java.util.Set;

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
        return type.hashCode();
    }

    @Override
    public String toString(){
        return "" + quantity + " " + type.toString();
    }

    public void sumQuantity(Reward other){
        if(type != other.type){
            throw new IllegalArgumentException();
        }
        quantity += other.quantity;
    }

    public void subtractQuantity(Reward other){
        if(type != other.type){
            throw new IllegalArgumentException();
        }
        quantity -= other.quantity;
    }

    public void multiplyQuantity(int multiplier){
        quantity *= multiplier;
    }

    public Set<Reward> exchange() throws TooMuchTimeException {
        Set<Reward> rewards = new HashSet<>();
<<<<<<< HEAD
        int i = 0;
        switch (i) {
            case 0:
                rewards.add(new Reward(RewardType.WOOD, 1));
                rewards.add(new Reward(RewardType.STONE, 1));
            case 1:
                rewards.add(new Reward(RewardType.SERVANT, 2));
            case 2:
                rewards.add(new Reward(RewardType.COIN, 2));
            case 3:
                rewards.add(new Reward(RewardType.MILITARY_POINT, 2));
            case 4:
                rewards.add(new Reward(RewardType.FAITH_POINT, 1));
=======
        int[] rewardArray = TerminalInput.exchangeCouncilPrivilege(this);
        for(int i = 0; i < rewardArray.length; i++) {
            switch(rewardArray[i]){
                case 1:
                    rewards.add(new Reward(RewardType.WOOD, 1));
                    rewards.add(new Reward(RewardType.STONE, 1));
                case 2:
                    rewards.add(new Reward(RewardType.SERVANT, 2));
                case 3:
                    rewards.add(new Reward(RewardType.COIN, 2));
                case 4:
                    rewards.add(new Reward(RewardType.MILITARY_POINT, 2));
                case 5:
                    rewards.add(new Reward(RewardType.FAITH_POINT, 1));
            }
>>>>>>> master
        }
        return rewards;
    }
}
