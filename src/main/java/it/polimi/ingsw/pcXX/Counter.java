package it.polimi.ingsw.pcXX;

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
        initializeResources(playerOrder);
        this.militaryPoint = new Reward(RewardType.MILITARY_POINT, 0);
        this.faithPoint = new Reward(RewardType.FAITH_POINT, 0);
        this.victoryPoint = new Reward(RewardType.VICTORY_POINT, 0);
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


    public boolean canAdd(Set<Reward> rewards){
        Counter temp = new Counter(this);
        for(Reward r : rewards) {
            switch (r.getType()) {
                case WOOD:
                    temp.wood.addQuantity(r);
                    if (temp.wood.getQuantity() < 0) {
                        return false;
                    }
                    break;
                case STONE:
                    temp.stone.addQuantity(r);
                    if (temp.stone.getQuantity() < 0) {
                        return false;
                    }
                    break;
                case SERVANT:
                    temp.servant.addQuantity(r);
                    if (temp.servant.getQuantity() < 0) {
                        return false;
                    }
                    break;
                case COIN:
                    temp.coin.addQuantity(r);
                    if (temp.coin.getQuantity() < 0) {
                        return false;
                    }
                    break;
                case MILITARY_POINT:
                    temp.militaryPoint.addQuantity(r);
                    if (temp.militaryPoint.getQuantity() < 0) {
                        return false;
                    }
                    break;
                case FAITH_POINT:
                    temp.faithPoint.addQuantity(r);
                    if (temp.faithPoint.getQuantity() < 0) {
                        return false;
                    }
                    break;
                case VICTORY_POINT:
                    temp.victoryPoint.addQuantity(r);
                    if (temp.victoryPoint.getQuantity() < 0) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    public void add(Set<Reward> rewards){
        for(Reward r : rewards) {
            switch (r.getType()) {
                case WOOD:
                    wood.addQuantity(r);
                    break;
                case STONE:
                    stone.addQuantity(r);
                    break;
                case SERVANT:
                    servant.addQuantity(r);
                    break;
                case COIN:
                    coin.addQuantity(r);
                    break;
                case MILITARY_POINT:
                    militaryPoint.addQuantity(r);
                    break;
                case FAITH_POINT:
                    faithPoint.addQuantity(r);
                    break;
                case VICTORY_POINT:
                    victoryPoint.addQuantity(r);
                    break;
            }
        }
    }

    public void add(Counter other){
        coin.addQuantity(other.coin);
        wood.addQuantity(other.wood);
        stone.addQuantity(other.stone);
        servant.addQuantity(other.servant);
        militaryPoint.addQuantity(other.militaryPoint);
        faithPoint.addQuantity(other.faithPoint);
        victoryPoint.addQuantity(other.victoryPoint);
    }

    public Reward giveSameReward(Reward reward){
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
        }
        return null;
    }
}
