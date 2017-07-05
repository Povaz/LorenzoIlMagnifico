package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.Controller.Game;
import it.polimi.ingsw.pc34.Controller.GameController;
import it.polimi.ingsw.pc34.RMI.ServerRMIImpl;
import it.polimi.ingsw.pc34.Socket.ServerSOC;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientInfo;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Povaz on 14/06/2017.
 */
public class ActionMarketSpotTest extends TestCase {
    private ActionSpot actionSpot;
    private ActionSpot actionSpot1;
    private ActionSpot actionSpot2;
    private FamilyMember familyMember;
    private FamilyMember familyMemberGhost;
    private Player player;
    private Game game;
    private GameController gameController;
    private HashMap<String, ClientInfo> users;
    private ServerRMIImpl serverRMI;
    private ServerSOC serverSOC;
    private Lobby lobby;


    public ActionMarketSpotTest(String name) {
        super(name);
    }

    @Test
    public void testIsPlaceable() throws RemoteException, IOException{
        //TEST di ACTIONSPOT
        familyMember.setUsed(true);
        assertFalse("Family member already used", actionSpot.isPlaceable(familyMember, false, gameController));

        familyMember.setUsed(false);
        actionSpot.setBusy(true);
        assertFalse("Action Spot busy", actionSpot.isPlaceable(familyMember, false, gameController));

        assertFalse("Family Member value too low", actionSpot.isPlaceable(familyMember, true, gameController));

        //TEST di MARKET
        actionSpot.setBusy(false);
        familyMemberGhost.setPlayer(player);
        assertFalse("FamilyMember Ghost not appropriate", actionSpot.isPlaceable(familyMemberGhost, true, gameController));

        familyMember.setValue(7);
        assertTrue("FamilyMember can be placed", actionSpot.isPlaceable(familyMember, true, gameController));
    }

    @Test
    public void testPlaceFamilyMemberReinitialize() {
        //TEST di ACTION SPOT
        actionSpot.placeFamilyMember(familyMember);
        LinkedList<FamilyMember> occupiedByExpected = new LinkedList<>();
        occupiedByExpected.add(familyMember);
        assertTrue("Used == true, FamilyMember", familyMember.isUsed());
        assertEquals("Check on OccupiedBy: familyMember should be added", occupiedByExpected, actionSpot.getOccupiedBy());

        actionSpot.reinitialize();
        assertFalse("Reinitialize set Busy = false", actionSpot.isBusy());
        assertEquals("OccupiedBy need to be cleaned", 0, actionSpot.getOccupiedBy().size());

        actionSpot.placeFamilyMember(familyMember);
        assertTrue("(Unrestricted == False) ==> Busy = True", actionSpot.isBusy());

        //TEST di MARKET
        ActionSpot actionSpotExpected = actionSpot1;
        actionSpot1.placeFamilyMember(familyMemberGhost);
        assertTrue("Nothing should be changed", actionSpotExpected.equals(actionSpot1));
    }


    @Test
    public void testEquals() {
        assertTrue("Sono lo stesso oggetto", actionSpot.equals(actionSpot1));
        assertFalse("Sono lo stesso oggetto", actionSpot1.equals(actionSpot2));
    }

    public void setUp() throws RemoteException {
        Set<Reward> rewards = new HashSet<>();
        rewards.add(new Reward(RewardType.COIN, 5));
        player = new Player("Erick", new ClientInfo(ConnectionType.RMI, ClientType.CLI), PlayerColor.BLUE);
        familyMember = new FamilyMember(player, FamilyColor.BLACK); //Value = 0, used = false;
        familyMemberGhost = new FamilyMember(ActionType.BUILDING_TOWER, 7, null);


        users = new HashMap<>();
        users.put("Erick", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        users.put("Paolo", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        lobby = new Lobby();
        serverRMI = new ServerRMIImpl(lobby);
        serverSOC = new ServerSOC(3000, lobby);
        game = new Game(users, serverRMI, serverSOC);
        gameController = new GameController(game, serverRMI, serverSOC);



        boolean active = true;
        boolean unrestricted = false;
        int diceValue = 6;

        actionSpot = new Market(active, unrestricted, diceValue, rewards);
        actionSpot1 = new Market(active, unrestricted, diceValue, rewards);
        actionSpot2 = new Market(active, !unrestricted, diceValue, rewards);

    }

    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new ActionMarketSpotTest("testIsPlaceable"));
        suite.addTest(new ActionMarketSpotTest("testPlaceFamilyMemberReinitialize"));
        suite.addTest(new ActionMarketSpotTest("testEquals"));
        return suite;
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
