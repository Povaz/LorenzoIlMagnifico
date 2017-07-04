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

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by Povaz on 02/07/2017.
 */
public class CouncilPalaceTest extends TestCase {
    private CouncilPalace councilPalace;
    private ServerRMIImpl serverRMI;
    private ServerSOC serverSOC;
    private Game game;
    private GameController gameController;
    private FamilyMember familyMember;
    private Player player;

    public CouncilPalaceTest(String name) {
        super(name);
    }

    public void isPlaceableTest () throws IOException {
        assertFalse(councilPalace.isPlaceable(familyMember, false, gameController));

        familyMember.setValue(1);

        assertTrue(councilPalace.isPlaceable(familyMember, true, gameController));
    }

    public void setUp () throws RemoteException{
        councilPalace = new CouncilPalace();

        Lobby lobby = new Lobby();
        lobby.setUser("Erick", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        lobby.setUser("Paolo", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        lobby.setUser("Tom", new ClientInfo (ConnectionType.SOCKET, ClientType.CLI));

        serverRMI = new ServerRMIImpl(lobby);
        serverSOC = new ServerSOC(3000, lobby);

        player = new Player("Erick", new ClientInfo(ConnectionType.RMI, ClientType.CLI), PlayerColor.BLUE);

        familyMember = new FamilyMember(player, FamilyColor.WHITE);

        game = new Game(lobby.getUsers(), serverRMI, serverSOC);

        gameController = new GameController(game, serverRMI, serverSOC);
    }

    public static TestSuite suite() {
        TestSuite testSuite = new TestSuite();
        testSuite.addTest(new CouncilPalaceTest("isPlaceableTest"));
        return testSuite;
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
