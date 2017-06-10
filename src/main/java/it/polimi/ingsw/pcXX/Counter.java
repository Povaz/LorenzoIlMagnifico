package it.polimi.ingsw.pcXX;

import it.polimi.ingsw.pcXX.Exception.TooMuchTimeException;

import java.util.Set;

/**
 * Created by trill on 02/06/2017.
 */
public class Counter{
    private final Reward coin;
    private final Reward wood;
    private final Reward stone;
    private final Reward servant;
    private final Reward militaryPoint;
    private final Reward faithPoint;
    private final Reward victoryPoint;
    
    public Counter(){
        this.coin = new Reward(RewardType.COIN, 0);
        this.wood = new Reward(RewardType.WOOD, 0);
        this.stone = new Reward(RewardType.STONE, 0);
        this.servant = new Reward(RewardType.SERVANT, 0);
        this.militaryPoint = new Reward(RewardType.MILITARY_POINT, 0);
        this.faithPoint = new Reward(RewardType.FAITH_POINT, 0);
        this.victoryPoint = new Reward(RewardType.VICTORY_POINT, 0);
    }

    public Counter(int playerOrder){
        this.coin = new Reward(RewardType.COIN, 5);
        this.wood = new Reward(RewardType.WOOD, 2);
        this.stone = new Reward(RewardType.STONE, 2);
        this.servant = new Reward(RewardType.SERVANT, 3);
        this.militaryPoint = new Reward(RewardType.MILITARY_POINT, 0);
        this.faithPoint = new Reward(RewardType.FAITH_POINT, 0);
        this.victoryPoint = new Reward(RewardType.VICTORY_POINT, 0);
        initializeResources(playerOrder);
    }

    public Counter(Counter copied){
        this.coin = new Reward(RewardType.COIN, copied.coin.getQuantity());
        this.wood = new Reward(RewardType.WOOD, copied.wood.getQuantity());
        this.stone = new Reward(RewardType.STONE, copied.stone.getQuantity());
        this.servant = new Reward(RewardType.SERVANT, copied.servant.getQuantity());
        this.militaryPoint = new Reward(RewardType.MILITARY_POINT, copied.militaryPoint.getQuantity());
        this.faithPoint = new Reward(RewardType.FAITH_POINT, copied.faithPoint.getQuantity());
        this.victoryPoint = new Reward(RewardType.VICTORY_POINT, copied.victoryPoint.getQuantity());
    }

    private void initializeResources(int playerOrder){
        if(playerOrder == 1)
            coin.setQuantity(5);
        if(playerOrder == 2)
            coin.setQuantity(6);
        if(playerOrder == 3)
            coin.setQuantity(7);
        if(playerOrder == 4)
            coin.setQuantity(8);
        if(playerOrder == 5)
            coin.setQuantity(9);
    }

    public void sum(Set<Reward> rewards) throws TooMuchTimeException {
        if(rewards == null){
            return;
        }
        for(Reward r : rewards){
            this.sum(r);
        }
    }

    public void subtract(Set<Reward> rewards){
        if(rewards == null){
            return;
        }
        for(Reward r : rewards){
            this.subtract(r);
        }
    }

    public void sum(Reward reward){
        if(reward == null){
            return;
        }
        giveSameReward(reward).sumQuantity(reward);
    }

    public void subtract(Reward reward){
        if(reward == null){
            return;
        }
        giveSameReward(reward).subtractQuantity(reward);
    }

    public boolean canSubtract(Reward reward){
        if(reward == null){
            return true;
        }
        return giveSameReward(reward).getQuantity() - reward.getQuantity() >= 0;
    }

    public void sum(Counter other){
        if(other == null){
            return;
        }
        coin.sumQuantity(other.coin);
        wood.sumQuantity(other.wood);
        stone.sumQuantity(other.stone);
        servant.sumQuantity(other.servant);
        militaryPoint.sumQuantity(other.militaryPoint);
        faithPoint.sumQuantity(other.faithPoint);
        victoryPoint.sumQuantity(other.victoryPoint);
    }

    public void subtract(Counter other){
        if(other == null){
            return;
        }
        coin.subtractQuantity(other.coin);
        wood.subtractQuantity(other.wood);
        stone.subtractQuantity(other.stone);
        servant.subtractQuantity(other.servant);
        militaryPoint.subtractQuantity(other.militaryPoint);
        faithPoint.subtractQuantity(other.faithPoint);
        victoryPoint.subtractQuantity(other.victoryPoint);
    }

    public boolean check(){
        if(coin.getQuantity() < 0){
            return false;
        }
        if(wood.getQuantity() < 0){
            return false;
        }
        if(stone.getQuantity() < 0){
            return false;
        }
        if(servant.getQuantity() < 0){
            return false;
        }
        if(militaryPoint.getQuantity() < 0){
            return false;
        }
        if(faithPoint.getQuantity() < 0){
            return false;
        }
        if(victoryPoint.getQuantity() < 0){
            return false;
        }
        return true;
    }

    public void round(){
        if(coin.getQuantity() < 0){
            coin.setQuantity(0);
        }
        if(wood.getQuantity() < 0){
            wood.setQuantity(0);
        }
        if(stone.getQuantity() < 0){
            stone.setQuantity(0);
        }
        if(servant.getQuantity() < 0){
            servant.setQuantity(0);
        }
        if(militaryPoint.getQuantity() < 0){
            militaryPoint.setQuantity(0);
        }
        if(faithPoint.getQuantity() < 0){
            faithPoint.setQuantity(0);
        }
        if(victoryPoint.getQuantity() < 0){
            victoryPoint.setQuantity(0);
        }
        if(militaryPoint.getQuantity() > 30){
            militaryPoint.setQuantity(30);
        }
        if(faithPoint.getQuantity() > 30){
            faithPoint.setQuantity(30);
        }
    }

    public Reward giveSameReward(Reward reward){
        if(reward == null){
            return null;
        }
        switch(reward.getType()) {
            case WOOD:
                return wood;
            case STONE:
                return stone;
            case SERVANT:
                return servant;
            case COIN:
                return coin;
            case MILITARY_POINT:
                return militaryPoint;
            case FAITH_POINT:
                return faithPoint;
            case VICTORY_POINT:
                return victoryPoint;
            default:
                return null;
        }
    }

    public Reward getCoin() {
        return coin;
    }

    public Reward getWood() {
        return wood;
    }

    public Reward getStone() {
        return stone;
    }

    public Reward getServant() {
        return servant;
    }

    public Reward getMilitaryPoint() {
        return militaryPoint;
    }

    public Reward getFaithPoint() {
        return faithPoint;
    }

    public Reward getVictoryPoint() {
        return victoryPoint;
    }

    @Override
    public String toString(){
    	String counterString = "counter:\n";
    	counterString += "  " + coin.toString() + "\n";
    	counterString += "  " + wood.toString () + "\n";
    	counterString += "  " + stone.toString() + "\n";
    	counterString += "  " + servant.toString() + "\n";
    	counterString += "  " + militaryPoint.toString() + "\n";
    	counterString += "  " + faithPoint.toString() + "\n";
    	counterString += "  " + victoryPoint.toString() + "\n";
    	return counterString;
    }
}
