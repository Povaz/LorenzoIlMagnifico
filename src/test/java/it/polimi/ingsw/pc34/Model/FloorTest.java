package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.Controller.Game;
import it.polimi.ingsw.pc34.Controller.GameController;
import it.polimi.ingsw.pc34.RMI.ServerRMIImpl;
import it.polimi.ingsw.pc34.Socket.ServerSOC;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Povaz on 02/07/2017.
 */
public class FloorTest extends TestCase {
    private Floor floor;
    private Tower tower;
    private List<Player> players;
    private Board board;

    private FamilyMember familyMember1;
    private FamilyMember familyMember2;
    private FamilyMember familyMember3;
    private FamilyMember familyMember4;
    private FamilyMember familyMember5;

    private Game game;
    private GameController gameController;
    private ServerRMIImpl serverRMI;
    private ServerSOC serverSOC;
    private Player player;
    private ActionType actionType;
    private int value;
    private Reward reward1;
    private Reward reward2;
    private Set<Reward> discount;
    private Set<Reward> rewards;
    private FamilyColor familyColor1;
    private FamilyColor familyColor2;
    private FamilyColor familyColor3;
    private Lobby lobby;


    public FloorTest(String name) {
        super(name);
    }

    public void isPlaceableTest() throws IOException {
        assertFalse(floor.isPlaceable(familyMember1, false, gameController));

        assertFalse(floor.isPlaceable(familyMember2, true,  gameController));

        familyMember5.setValue(100);
        assertTrue(floor.isPlaceable(familyMember5, true, gameController));

        /*familyMember4.setValue(100); TODO NEED CHECK
        familyMember3.setValue(100);
        floor.placeFamilyMember(familyMember4);
        assertFalse(floor.isPlaceable(familyMember3, false, gameController));*/
    }

    public void setUp () throws RemoteException {
        lobby = new Lobby();
        lobby.setUser("Erick", ConnectionType.RMI);
        lobby.setUser("Paolo", ConnectionType.SOCKET);
        lobby.setUser("Tom", ConnectionType.SOCKET);
        serverRMI = new ServerRMIImpl(lobby);
        serverSOC = new ServerSOC(3000, lobby);

        player = new Player("Erick", ConnectionType.RMI, PlayerColor.RED);
        actionType = ActionType.BUILDING_TOWER;
        familyColor1 = FamilyColor.BLACK;
        familyColor2 = FamilyColor.NEUTRAL;
        familyColor3 = FamilyColor.ORANGE;
        value = 7;
        reward1 = new Reward(RewardType.SERVANT, 4);
        discount = new HashSet<>();
        discount.add(reward1);
        game = new Game(lobby.getUsers(), serverRMI, serverSOC);
        gameController = new GameController(game, serverRMI, serverSOC);

        familyMember1 = new FamilyMember(player, familyColor1);
        familyMember2 = new FamilyMember(actionType, value, discount);
        familyMember2.setPlayer(player);
        familyMember3 = new FamilyMember(player, familyColor1);
        familyMember4 = new FamilyMember(player, familyColor3);
        familyMember5 = new FamilyMember(player, familyColor2);

        reward2 = new Reward(RewardType.WOOD, 2);
        rewards = new HashSet<>();
        rewards.add(reward2);
        players = new ArrayList<>();
        players.add(new Player("Erick", ConnectionType.RMI, PlayerColor.RED));
        players.add(new Player("Erick", ConnectionType.RMI, PlayerColor.RED));
        players.add(new Player("Erick", ConnectionType.RMI, PlayerColor.RED));
        board = new Board(players);
        tower = new Tower(CardType.CHARACTER, board);
        floor = new Floor(5, rewards, tower);
    }

    public static TestSuite suite() {
        TestSuite testSuite = new TestSuite();
        testSuite.addTest(new FloorTest("isPlaceableTest"));
        return testSuite;
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}