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
                    temp.wood.sumQuantity(r);
                    if (temp.wood.getQuantity() < 0) {
                        return false;
                    }
                    break;
                case STONE:
                    temp.stone.sumQuantity(r);
                    if (temp.stone.getQuantity() < 0) {
                        return false;
                    }
                    break;
                case SERVANT:
                    temp.servant.sumQuantity(r);
                    if (temp.servant.getQuantity() < 0) {
                        return false;
                    }
                    break;
                case COIN:
                    temp.coin.sumQuantity(r);
                    if (temp.coin.getQuantity() < 0) {
                        return false;
                    }
                    break;
                case MILITARY_POINT:
                    temp.militaryPoint.sumQuantity(r);
                    if (temp.militaryPoint.getQuantity() < 0) {
                        return false;
                    }
                    break;
                case FAITH_POINT:
                    temp.faithPoint.sumQuantity(r);
                    if (temp.faithPoint.getQuantity() < 0) {
                        return false;
                    }
                    break;
                case VICTORY_POINT:
                    temp.victoryPoint.sumQuantity(r);
                    if (temp.victoryPoint.getQuantity() < 0) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    public void sum(Set<Reward> rewards){
        for(Reward r : rewards) {
            switch (r.getType()) {
                case WOOD:
                    wood.sumQuantity(r);
                    break;
                case STONE:
                    stone.sumQuantity(r);
                    break;
                case SERVANT:
                    servant.sumQuantity(r);
                    break;
                case COIN:
                    coin.sumQuantity(r);
                    break;
                case MILITARY_POINT:
                    militaryPoint.sumQuantity(r);
                    break;
                case FAITH_POINT:
                    faithPoint.sumQuantity(r);
                    break;
                case VICTORY_POINT:
                    victoryPoint.sumQuantity(r);
                    break;
            }
        }
    }

    public void subtract(Set<Reward> rewards){
        for(Reward r : rewards) {
            switch (r.getType()) {
                case WOOD:
                    wood.subtractQuantity(r);
                    break;
                case STONE:
                    stone.subtractQuantity(r);
                    break;
                case SERVANT:
                    servant.subtractQuantity(r);
                    break;
                case COIN:
                    coin.subtractQuantity(r);
                    break;
                case MILITARY_POINT:
                    militaryPoint.subtractQuantity(r);
                    break;
                case FAITH_POINT:
                    faithPoint.subtractQuantity(r);
                    break;
                case VICTORY_POINT:
                    victoryPoint.subtractQuantity(r);
                    break;
            }
        }
    }

    public void sum(Counter other){
        coin.sumQuantity(other.coin);
        wood.sumQuantity(other.wood);
        stone.sumQuantity(other.stone);
        servant.sumQuantity(other.servant);
        militaryPoint.sumQuantity(other.militaryPoint);
        faithPoint.sumQuantity(other.faithPoint);
        victoryPoint.sumQuantity(other.victoryPoint);
    }

    public void subtract(Counter other){
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
}
