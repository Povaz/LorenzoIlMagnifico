package it.polimi.ingsw.pc34.Model;

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
}
