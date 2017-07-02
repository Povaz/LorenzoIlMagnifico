package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Povaz on 14/06/2017.
 */
public class BoardTest extends TestCase {
    private Board board1player;
    private Board board3player;
    private Board board4player;
    private List<Player> players;

    public BoardTest(String name) {
        super(name);
    }
    
    //ERRORI
    public void testInitializeHarvestArea() {
        assertEquals(1, board1player.getHarvestArea().size());
        assertEquals(2, board3player.getHarvestArea().size());
    }


    public void testInitializeProductionArea() {
        assertEquals(1, board1player.getProductionArea().size());
        assertEquals(2, board3player.getProductionArea().size());
    }


    public void testInitializeMarket() {
        assertEquals(2, board1player.getMarket().size());
        assertEquals(2, board3player.getMarket().size());
        assertEquals(4, board4player.getMarket().size());
    }


    public void testInitializeVatican() {
        assertEquals(3, board1player.getVaticanReportSpot().size());
    }


    public void testInitializeDice() {
        assertEquals(3, board1player.getDices().size());
    }

	
    public void setUp () throws JSONException, IOException {
        Set<Reward> personalBonusTileRewards =  new HashSet<>();
        personalBonusTileRewards.add(new Reward(RewardType.COIN, 1));
        PersonalBonusTile personalBonusTile = new PersonalBonusTile(null, 1, 1, personalBonusTileRewards, personalBonusTileRewards);

        List<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(JSONUtility.getPermanentLeaderCard(0));

        players = new ArrayList<>();
        players.add(new Player("Erick", ConnectionType.RMI, PlayerColor.RED));

        board1player = new Board(players);

        players.add(new Player("Erick", ConnectionType.RMI, PlayerColor.BLUE));
        players.add(new Player("Erick", ConnectionType.RMI, PlayerColor.YELLOW));

        board3player = new Board(players);

        players.add(new Player("Erick", ConnectionType.RMI, PlayerColor.GREEN));

        board4player = new Board(players);
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

}
