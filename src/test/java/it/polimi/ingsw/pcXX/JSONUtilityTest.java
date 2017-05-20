package it.polimi.ingsw.pcXX;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by trill on 20/05/2017.
 */
public class JSONUtilityTest extends TestCase{
    public JSONUtilityTest(String testName){
        super(testName);
    }

    public static Test suite(){
        return new TestSuite(JSONUtilityTest.class);
    }

    public void testJSONimportTerritoryCard1(){
        Set<Reward> fastRewards = new HashSet<Reward>();
        fastRewards.add(new Resource(ResourceType.WOOD, 1));
        Set<Reward> earnings = new HashSet<Reward>();
        earnings.add(new Resource(ResourceType.WOOD, 1));
        TerritoryCard expected = new TerritoryCard("Bosco", 1, fastRewards, 2, earnings);

        TerritoryCard calculated;
        try {
            calculated = (TerritoryCard) JSONUtility.getCard(1, 1, CardType.TERRITORY);
        } catch(Exception e){
            calculated = null;
        }

        assertEquals(expected, calculated);
    }

    public void testJSONimportTerritoryCard2(){
        Set<Reward> fastRewards = new HashSet<Reward>();
        fastRewards.add(new Point(PointType.MILITARY_POINT, 2));
        fastRewards.add(new Resource(ResourceType.SERVANT, 1));
        Set<Reward> earnings = new HashSet<Reward>();
        earnings.add(new Point(PointType.MILITARY_POINT, 1));
        earnings.add(new Resource(ResourceType.SERVANT, 2));
        TerritoryCard expected = new TerritoryCard("Città Fortificata", 3, fastRewards, 2, earnings);

        TerritoryCard calculated;
        try {
            calculated = (TerritoryCard) JSONUtility.getCard(3, 7, CardType.TERRITORY);
        } catch(Exception e){
            calculated = null;
        }

        assertEquals(expected, calculated);
    }
}
