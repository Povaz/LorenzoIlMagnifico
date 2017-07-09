package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.JSONUtility;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by trill on 09/07/2017.
 */
public class CardSpotTest{

    // Territory spot
    @Test
    public void testEstimateVictoryPoint1(){
        TerritorySpot territorySpot = new TerritorySpot();

        try {
            for(int i = 0; i < 4; i++) {
                territorySpot.getCards().add(JSONUtility.getCard(1, i, CardType.TERRITORY));
            }
        } catch(Exception e){
            assertFalse(true);
        }

        Set<Reward> expected = Collections.singleton(new Reward(RewardType.VICTORY_POINT, 4));

        Set<Reward> calculated = territorySpot.estimateVictoryPoint();

        assertEquals(expected, calculated);
    }

    // Building spot
    @Test
    public void testEstimateVictoryPoint2(){
        BuildingSpot buildingSpot = new BuildingSpot();

        try {
            for(int i = 0; i < 4; i++) {
                buildingSpot.getCards().add(JSONUtility.getCard(1, i, CardType.BUILDING));
            }
        } catch(Exception e){
            assertFalse(true);
        }

        Set<Reward> expected = new HashSet<>();

        Set<Reward> calculated = buildingSpot.estimateVictoryPoint();

        assertEquals(expected, calculated);
    }

    // Character spot
    @Test
    public void testEstimateVictoryPoint3(){
        CharacterSpot characterCard = new CharacterSpot();

        try {
            for(int i = 0; i < 4; i++) {
                characterCard.getCards().add(JSONUtility.getCard(1, i, CardType.CHARACTER));
            }
        } catch(Exception e){
            assertFalse(true);
        }

        Set<Reward> expected = Collections.singleton(new Reward(RewardType.VICTORY_POINT, 10));

        Set<Reward> calculated = characterCard.estimateVictoryPoint();

        assertEquals(expected, calculated);
    }

    // Venture spot
    @Test
    public void testEstimateVictoryPoint4(){
        VentureSpot ventureSpot = new VentureSpot();

        try {
            for(int i = 0; i < 4; i++) {
                ventureSpot.getCards().add(JSONUtility.getCard(1, i, CardType.VENTURE));
            }
        } catch(Exception e){
            assertFalse(true);
        }

        Set<Reward> expected = new HashSet<>();

        Set<Reward> calculated = ventureSpot.estimateVictoryPoint();

        assertEquals(expected, calculated);
    }
}
