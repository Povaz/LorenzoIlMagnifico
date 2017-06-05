package it.polimi.ingsw.pcXX;

/**
 * Created by trill on 24/05/2017.
 */
public class TerritorySpot extends CardSpot{
    public TerritorySpot(){
        super();
    }

    @Override
    public Reward estimateVictoryPoint(){
        switch(getCards().size()){
            case 3:
                return new Reward(RewardType.VICTORY_POINT, 1);
            case 4:
                return new Reward(RewardType.VICTORY_POINT, 4);
            case 5:
                return new Reward(RewardType.VICTORY_POINT, 10);
            case 6:
                return new Reward(RewardType.VICTORY_POINT, 20);
            default:
                return new Reward(RewardType.VICTORY_POINT, 0);
        }
    }

    @Override
    public boolean canPlaceCard(Counter copyForCosts){
        if(!super.canPlaceCard(copyForCosts)){
            return false;
        }
        switch(getCards().size()){
            case 0:
                return true;
            case 1:
                return true;
            case  2:
                if(copyForCosts.getMilitaryPoint().getQuantity() < 3){
                    return false;
                }
                return true;
            case  3:
                if(copyForCosts.getMilitaryPoint().getQuantity() < 7){
                    return false;
                }
                return true;
            case  4:
                if(copyForCosts.getMilitaryPoint().getQuantity() < 12){
                    return false;
                }
                return true;
            case  5:
                if(copyForCosts.getMilitaryPoint().getQuantity() < 18){
                    return false;
                }
                return true;
            default:
                return false;
        }
    }
}
