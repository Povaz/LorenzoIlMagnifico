package it.polimi.ingsw.pc34.Model;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Test;

/**
 * Created by Povaz on 14/06/2017.
 */
public class BuildingSpotTest extends TestCase {
    private BuildingSpot buildingSpot;
    private Reward reward;


    public BuildingSpotTest(String name) {
        super(name);
    }

    @Test
    public void testEstimateVictoryPoint () {
        assertEquals(reward, buildingSpot.estimateVictoryPoint());

        reward.setQuantity(1);

        assertFalse("Non sono uguali", reward.equals(buildingSpot.estimateVictoryPoint()));
    }

    public void setUp () {
        buildingSpot = new BuildingSpot();
        reward = new Reward(RewardType.VICTORY_POINT, 0);
    }

    public static TestSuite suite() {
        TestSuite testSuite = new TestSuite();
        testSuite.addTest(new BuildingSpotTest("testEstimateVictoryPoint"));
        return testSuite;
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
