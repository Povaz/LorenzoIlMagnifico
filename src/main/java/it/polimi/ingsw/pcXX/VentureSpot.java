package it.polimi.ingsw.pcXX;

/**
 * Created by trill on 24/05/2017.
 */
public class VentureSpot extends CardSpot{
    public VentureSpot(){
        super();
    }

    @Override
    public Reward estimateVictoryPoint(){
        Reward victoryPoint = new Reward(RewardType.VICTORY_POINT, 0);
        for(DevelopmentCard dC : getCards()){
            VentureCard vC = (VentureCard) dC;
            victoryPoint.sumQuantity(vC.getVictoryPointEarned());
        }
        return victoryPoint;
    }
}
