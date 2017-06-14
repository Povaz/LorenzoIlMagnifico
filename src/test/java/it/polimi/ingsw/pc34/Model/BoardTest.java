package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.Model.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.json.JSONException;
import org.junit.Test;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Povaz on 14/06/2017.
 */
public class BoardTest extends TestCase {
    private Board board;
    private List<Player> players;

    public BoardTest(String name) {
        super(name);
    }

    @Test
    public void testInitializeHarvestArea() {
        board.initializeHarvestArea(1);
        assertEquals(1, board.getHarvestArea().size());

        board.initializeHarvestArea(3);
        assertEquals(2, board.getHarvestArea().size());
    }

    @Test
    public void testInitializeProductionArea() {
        board.initializeProductionArea(1);
        assertEquals(1, board.getProductionArea().size());

        board.initializeProductionArea(3);
        assertEquals(2, board.getProductionArea().size());
    }

    @Test
    public void testInitializeMarket() {
        board.initializeMarket(1);
        assertEquals(1, board.getMarket().size());

        board.initializeMarket(3);
        assertEquals(4, board.getMarket().size());
    }

    @Test
    public void testInitilizeVatican() {
        board.initializeVaticanReportSpot();
        assertEquals(3, board.getVaticanReportSpot().size());
    }

    @Test
    public void testInitializeDice() {
        board.initializeDices();
        assertEquals(3, board.getDices().size());
    }

    public void setUp () throws JSONException, IOException {
        Set<Reward> personalBonusTileRewards =  new HashSet<>();
        personalBonusTileRewards.add(new Reward(RewardType.COIN, 1));
        PersonalBonusTile personalBonusTile = new PersonalBonusTile(1, 1, personalBonusTileRewards, personalBonusTileRewards);

        List<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(JSONUtility.getPermanentLeaderCard(0));

        players = new ArrayList<>();
        players.add(new Player("Erick", PlayerColor.RED, personalBonusTile, leaderCards));

        board = new Board(players);
    }

    public static TestSuite suite () {
        TestSuite testSuite = new TestSuite();
        testSuite.addTest(new BoardTest("testInitializeHarvestArea"));
        testSuite.addTest(new BoardTest("testInitializeProductionArea"));
        testSuite.addTest(new BoardTest("testInitializeMarket"));
        testSuite.addTest(new BoardTest("testInitializeVatican"));
        testSuite.addTest(new BoardTest("testInitializeDice"));
        return testSuite;
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    //ASPETTARE CHE ABBIANO SISTEMATO LE VATICAN REPORT CARD
}
