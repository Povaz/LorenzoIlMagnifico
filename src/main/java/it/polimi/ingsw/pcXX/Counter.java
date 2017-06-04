package it.polimi.ingsw.pcXX;

import java.util.Set;

/**
 * Created by trill on 02/06/2017.
 */
public class Counter{
    private final Resource coin;
    private final Resource wood;
    private final Resource stone;
    private final Resource servant;
    private final Point militaryPoint;
    private final Point faithPoint;
    private final Point victoryPoint;

    public Counter(){
        this.coin = new Resource(ResourceType.COIN, 0);
        this.wood = new Resource(ResourceType.WOOD, 0);
        this.stone = new Resource(ResourceType.STONE, 0);
        this.servant = new Resource(ResourceType.SERVANT, 0);
        this.militaryPoint = new Point(PointType.MILITARY_POINT, 0);
        this.faithPoint = new Point(PointType.FAITH_POINT, 0);
        this.victoryPoint = new Point(PointType.VICTORY_POINT, 0);
    }

    public Counter(int playerOrder){
        this.coin = new Resource(ResourceType.COIN, 5);
        this.wood = new Resource(ResourceType.WOOD, 2);
        this.stone = new Resource(ResourceType.STONE, 2);
        this.servant = new Resource(ResourceType.SERVANT, 3);
        initializeResources(playerOrder);
        this.militaryPoint = new Point(PointType.MILITARY_POINT, 0);
        this.faithPoint = new Point(PointType.FAITH_POINT, 0);
        this.victoryPoint = new Point(PointType.VICTORY_POINT, 0);
    }

    public Counter(Counter copied){
        this.coin = new Resource(ResourceType.COIN, copied.coin.getQuantity());
        this.wood = new Resource(ResourceType.WOOD, copied.wood.getQuantity());
        this.stone = new Resource(ResourceType.STONE, copied.stone.getQuantity());
        this.servant = new Resource(ResourceType.SERVANT, copied.servant.getQuantity());
        this.militaryPoint = new Point(PointType.MILITARY_POINT, copied.militaryPoint.getQuantity());
        this.faithPoint = new Point(PointType.FAITH_POINT, copied.faithPoint.getQuantity());
        this.victoryPoint = new Point(PointType.VICTORY_POINT, copied.victoryPoint.getQuantity());
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
        for(Reward r : rewards){
            if(r instanceof Resource){
                Resource resource = (Resource) r;
                switch(resource.getType()){
                    case WOOD:
                        temp.wood.addQuantity(resource);
                        if(temp.wood.getQuantity() < 0){
                            return false;
                        }
                        break;
                    case STONE:
                        temp.stone.addQuantity(resource);
                        if(temp.stone.getQuantity() < 0){
                            return false;
                        }
                        break;
                    case SERVANT:
                        temp.servant.addQuantity(resource);
                        if(temp.servant.getQuantity() < 0){
                            return false;
                        }
                        break;
                    case COIN:
                        temp.coin.addQuantity(resource);
                        if(temp.coin.getQuantity() < 0){
                            return false;
                        }
                        break;
                }
            }
            else if(r instanceof Point){
                Point point = (Point) r;
                switch(point.getType()){
                    case MILITARY_POINT:
                        temp.militaryPoint.addQuantity(point);
                        if(temp.militaryPoint.getQuantity() < 0){
                            return false;
                        }
                        break;
                    case FAITH_POINT:
                        temp.faithPoint.addQuantity(point);
                        if(temp.faithPoint.getQuantity() < 0){
                            return false;
                        }
                        break;
                    case VICTORY_POINT:
                        temp.victoryPoint.addQuantity(point);
                        if(temp.victoryPoint.getQuantity() < 0){
                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }

    public void add(Set<Reward> rewards){
        for(Reward r : rewards){
            if(r instanceof Resource){
                Resource resource = (Resource) r;
                switch(resource.getType()){
                    case WOOD:
                        wood.addQuantity(resource);
                        break;
                    case STONE:
                        stone.addQuantity(resource);
                        break;
                    case SERVANT:
                        servant.addQuantity(resource);
                        break;
                    case COIN:
                        coin.addQuantity(resource);
                        break;
                }
            }
            else if(r instanceof Point){
                Point point = (Point) r;
                switch(point.getType()){
                    case MILITARY_POINT:
                        militaryPoint.addQuantity(point);
                        break;
                    case FAITH_POINT:
                        faithPoint.addQuantity(point);
                        break;
                    case VICTORY_POINT:
                        victoryPoint.addQuantity(point);
                        break;
                }
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
        if(reward instanceof Resource){
            Resource resource = (Resource) reward;
            switch(resource.getType()){
                case WOOD:
                    return wood;
                case STONE:
                    return stone;
                case SERVANT:
                    return servant;
                case COIN:
                    return coin;
            }
        }
        else if(reward instanceof Point){
            Point point = (Point) reward;
            switch(point.getType()) {
                case MILITARY_POINT:
                    return militaryPoint;
                case FAITH_POINT:
                    return faithPoint;
                case VICTORY_POINT:
                    return victoryPoint;
            }
        }
        return null;
    }
}
