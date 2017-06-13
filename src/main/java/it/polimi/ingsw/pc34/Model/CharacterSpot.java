package it.polimi.ingsw.pc34.Model;

/**
 * Created by trill on 24/05/2017.
 */
public class CharacterSpot extends CardSpot{
    public CharacterSpot(){
        super();
    }

    @Override
    public Reward estimateVictoryPoint(){
        switch(getCards().size()){
            case 1:
                return new Reward(RewardType.VICTORY_POINT, 1);
            case 2:
                return new Reward(RewardType.VICTORY_POINT, 3);
            case 3:
                return new Reward(RewardType.VICTORY_POINT, 6);
            case 4:
                return new Reward(RewardType.VICTORY_POINT, 10);
            case 5:
                return new Reward(RewardType.VICTORY_POINT, 15);
            case 6:
                return new Reward(RewardType.VICTORY_POINT, 21);
            default:
                return new Reward(RewardType.VICTORY_POINT, 0);
        }
    }
}
