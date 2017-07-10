package it.polimi.ingsw.pc34.Model;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Test;

import java.util.HashSet;

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
        assertEquals(new HashSet<Reward>(), buildingSpot.estimateVictoryPoint());

        System.out.println(reward.toString());
        System.out.println(buildingSpot.estimateVictoryPoint());
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
