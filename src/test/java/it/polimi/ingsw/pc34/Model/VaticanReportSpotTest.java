package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.JSONUtility;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by Povaz on 09/07/2017.
 */
public class VaticanReportSpotTest extends TestCase {
    private VaticanReportSpot vaticanReportSpot1;
    @SuppressWarnings("unused")
	private VaticanReportSpot vaticanReportSpot2;
    @SuppressWarnings("unused")
	private VaticanReportSpot vaticanReportSpot3;
    private VaticanReportCard vaticanReportCard1;
    private VaticanReportCard vaticanReportCard2;
    @SuppressWarnings("unused")
	private VaticanReportCard vaticanReportCard3;


    public VaticanReportSpotTest(String name) {
        super(name);
    }

    public void calculateVictoryPointFromFaithPointTest () {
        HashSet<Reward> expected = new HashSet<>();
        assertEquals(new HashSet<Reward>(), vaticanReportSpot1.calculateVictoryPointFromFaithPoint(new Reward(RewardType.FAITH_POINT, 0)));
        expected.add(new Reward(RewardType.VICTORY_POINT, 1));
        assertEquals(expected, vaticanReportSpot1.calculateVictoryPointFromFaithPoint(new Reward(RewardType.FAITH_POINT, 1)));
        expected.clear();
        expected.add(new Reward(RewardType.VICTORY_POINT, 2));
        assertEquals(expected, vaticanReportSpot1.calculateVictoryPointFromFaithPoint(new Reward(RewardType.FAITH_POINT, 2)));
        expected.clear();
        expected.add(new Reward(RewardType.VICTORY_POINT, 3));
        assertEquals(expected, vaticanReportSpot1.calculateVictoryPointFromFaithPoint(new Reward(RewardType.FAITH_POINT, 3)));
        expected.clear();
        expected.add(new Reward(RewardType.VICTORY_POINT, 4));
        assertEquals(expected, vaticanReportSpot1.calculateVictoryPointFromFaithPoint(new Reward(RewardType.FAITH_POINT, 4)));
        expected.clear();
        expected.add(new Reward(RewardType.VICTORY_POINT, 5));
        assertEquals(expected, vaticanReportSpot1.calculateVictoryPointFromFaithPoint(new Reward(RewardType.FAITH_POINT, 5)));
        expected.clear();
        expected.add(new Reward(RewardType.VICTORY_POINT, 7));
        assertEquals(expected, vaticanReportSpot1.calculateVictoryPointFromFaithPoint(new Reward(RewardType.FAITH_POINT, 6)));
        expected.clear();
        expected.add(new Reward(RewardType.VICTORY_POINT, 9));
        assertEquals(expected, vaticanReportSpot1.calculateVictoryPointFromFaithPoint(new Reward(RewardType.FAITH_POINT, 7)));
        expected.clear();
        expected.add(new Reward(RewardType.VICTORY_POINT, 11));
        assertEquals(expected, vaticanReportSpot1.calculateVictoryPointFromFaithPoint(new Reward(RewardType.FAITH_POINT, 8)));
        expected.clear();
        expected.add(new Reward(RewardType.VICTORY_POINT, 13));
        assertEquals(expected, vaticanReportSpot1.calculateVictoryPointFromFaithPoint(new Reward(RewardType.FAITH_POINT, 9)));
        expected.clear();
        expected.add(new Reward(RewardType.VICTORY_POINT, 15));
        assertEquals(expected, vaticanReportSpot1.calculateVictoryPointFromFaithPoint(new Reward(RewardType.FAITH_POINT, 10)));
        expected.clear();
        expected.add(new Reward(RewardType.VICTORY_POINT, 17));
        assertEquals(expected, vaticanReportSpot1.calculateVictoryPointFromFaithPoint(new Reward(RewardType.FAITH_POINT, 11)));
        expected.clear();
        expected.add(new Reward(RewardType.VICTORY_POINT, 19));
        assertEquals(expected, vaticanReportSpot1.calculateVictoryPointFromFaithPoint(new Reward(RewardType.FAITH_POINT, 12)));
        expected.clear();
        expected.add(new Reward(RewardType.VICTORY_POINT, 22));
        assertEquals(expected, vaticanReportSpot1.calculateVictoryPointFromFaithPoint(new Reward(RewardType.FAITH_POINT, 13)));
        expected.clear();
        expected.add(new Reward(RewardType.VICTORY_POINT, 25));
        assertEquals(expected, vaticanReportSpot1.calculateVictoryPointFromFaithPoint(new Reward(RewardType.FAITH_POINT, 14)));
        expected.clear();
        expected.add(new Reward(RewardType.VICTORY_POINT, 30));
        assertEquals(expected, vaticanReportSpot1.calculateVictoryPointFromFaithPoint(new Reward(RewardType.FAITH_POINT, 15)));
        expected.clear();
    }


    public void standardRewardsFromFaithPoints() {
        assertEquals(new Reward(RewardType.VICTORY_POINT, 0), vaticanReportSpot1.standardRewardsFromFaithPoints(-1));
        assertEquals(new Reward(RewardType.VICTORY_POINT, 4), vaticanReportSpot1.standardRewardsFromFaithPoints(4));
        int faith = 11;
        assertEquals(new Reward(RewardType.VICTORY_POINT, 2 * faith - 5), vaticanReportSpot1.standardRewardsFromFaithPoints(faith));
        faith = 13;
        assertEquals(new Reward(RewardType.VICTORY_POINT, 3 * faith - 17), vaticanReportSpot1.standardRewardsFromFaithPoints(faith));
        faith = 14;
        assertEquals(new Reward(RewardType.VICTORY_POINT, 30), vaticanReportSpot1.standardRewardsFromFaithPoints(faith));
    }

    public void setUp () throws IOException, JSONException {
        vaticanReportCard1 = JSONUtility.getVaticanReportCard(1, 0);
        vaticanReportSpot1 = new VaticanReportSpot(vaticanReportCard1, new Reward(RewardType.FAITH_POINT, 3), false);

        vaticanReportCard2 = JSONUtility.getVaticanReportCard(2, 1);
        vaticanReportSpot2 = new VaticanReportSpot(vaticanReportCard2, new Reward(RewardType.FAITH_POINT, 4), false);

        vaticanReportCard3 = JSONUtility.getVaticanReportCard(3, 2);
        vaticanReportSpot3 = new VaticanReportSpot(vaticanReportCard2, new Reward(RewardType.FAITH_POINT, 4), true);
    }

    public static TestSuite suite() {
        TestSuite testSuite = new TestSuite();
        testSuite.addTest(new VaticanReportSpotTest("calculateVictoryPointFromFaithPointTest"));
        testSuite.addTest(new VaticanReportSpotTest("standardRewardsFromFaithPoints"));
        return testSuite;
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
