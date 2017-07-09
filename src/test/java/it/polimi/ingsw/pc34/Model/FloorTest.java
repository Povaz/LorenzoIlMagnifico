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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Povaz on 02/07/2017.
 */
public class FloorTest extends TestCase {
    @SuppressWarnings("unused")
	private Floor floor;
    private Tower tower;
    private List<Player> players;
    private Board board;

    private FamilyMember familyMember0;
    private FamilyMember familyMember1;
    private FamilyMember familyMember2;
    private FamilyMember familyMember3;
    private FamilyMember familyMember4;
    private FamilyMember familyMember5;
    private FamilyMember familyMember6;
    private FamilyMember familyMember7;

    private Game game;
    private GameController gameController;
    private ServerRMIImpl serverRMI;
    private ServerSOC serverSOC;
    private Player player;
    @SuppressWarnings("unused")
	private ActionType actionType;
    private int value;
    private Reward reward1;
    private Reward reward2;
    private Set<Reward> discount;
    private Set<Reward> rewards;
    @SuppressWarnings("unused")
	private FamilyColor familyColor1;
    @SuppressWarnings("unused")
	private FamilyColor familyColor2;
    @SuppressWarnings("unused")
	private FamilyColor familyColor3;
    private Lobby lobby;


    public FloorTest(String name) {
        super(name);
    }

    @Test
    public void isPlaceableTest() throws IOException {
        //FamilyMember.Ghost == true
            //FamilyMember.getAction() == null
        assertTrue(tower.getFloors().get(0).isPlaceable(familyMember0, false, gameController));
            //FamilyMember.getAction() != null
                //FamilyMember.getAction() != ActionType.ALL
                    //tower.getType().sameType(FamilyMember.getAction()) == true
        assertTrue(tower.getFloors().get(0).isPlaceable(familyMember1, false, gameController));
                    //tower.getType().sameType(FamilyMember.getAction()) == false
        assertFalse(tower.getFloors().get(0).isPlaceable(familyMember2, false, gameController));
                //FamilyMember.getAction() == ActionType.ALL
        assertTrue(tower.getFloors().get(0).isPlaceable(familyMember3, false, gameController));

        tower.getFloors().get(0).placeFamilyMember(familyMember4); //Player: Erick, FamilyMember: ORANGE
        //FamilyMember.Ghost == false
        assertFalse(tower.getFloors().get(1).isPlaceable(familyMember5, false, gameController));
            //FamilyMember.getColor != FamilyColor.NEUTRAL
                //FamilyMember.samePlayer(fm)
                    //Fm.getColor != FamilyColor.NEUTRAL
        assertFalse(tower.getFloors().get(1).isPlaceable(familyMember6, false, gameController));
            //FamilyMember.getColor == FamilyColor.NEUTRAL
        assertTrue(tower.getFloors().get(1).isPlaceable(familyMember7, false, gameController));
                //!FamilyMember.samePlayer(fm)
        familyMember7.setPlayer(new Player("Paolo", new ClientInfo(ConnectionType.RMI, ClientType.CLI), PlayerColor.BLUE));
        assertTrue(tower.getFloors().get(1).isPlaceable(familyMember7, false, gameController));

        tower.reinitialize();
        tower.getFloors().get(0).placeFamilyMember(familyMember7);
                    //Fm,getColor == FamilyColor.NEUTRAL
        assertTrue(tower.getFloors().get(1).isPlaceable(familyMember6, false, gameController));
    }

    public void setUp () throws RemoteException {
        lobby = new Lobby();
        lobby.setUser("Erick", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        lobby.setUser("Paolo", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        lobby.setUser("Tom", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        serverRMI = new ServerRMIImpl(lobby);
        serverSOC = new ServerSOC(3000, lobby);

        player = new Player("Erick", new ClientInfo(ConnectionType.RMI, ClientType.CLI), PlayerColor.RED);
        familyColor1 = FamilyColor.BLACK;
        familyColor2 = FamilyColor.NEUTRAL;
        familyColor3 = FamilyColor.ORANGE;
        value = 7;
        reward1 = new Reward(RewardType.SERVANT, 4);
        discount = new HashSet<>();
        discount.add(reward1);
        game = new Game(lobby.getUsers(), serverRMI, serverSOC);
        gameController = new GameController(game, serverRMI, serverSOC);

        familyMember0 = new FamilyMember(null, value, discount); //Ghost = true;
        familyMember0.setPlayer(player);
        familyMember1 = new FamilyMember(ActionType.CHARACTER_TOWER, value, discount); //Ghost = true;
        familyMember1.setPlayer(player);
        familyMember2 = new FamilyMember(ActionType.BUILDING_TOWER, value, discount); //Ghost = true;
        familyMember2.setPlayer(player);
        familyMember3 = new FamilyMember(ActionType.ALL, value, discount); //Ghost = true;
        familyMember3.setPlayer(player);
        familyMember4 = new FamilyMember(player, FamilyColor.ORANGE);
        familyMember4.setValue(value);
        familyMember5 = new FamilyMember(player, FamilyColor.WHITE);
        familyMember5.setValue(value);
        familyMember6 = new FamilyMember(player, FamilyColor.BLACK);
        familyMember6.setValue(value);
        familyMember7 = new FamilyMember(player, FamilyColor.NEUTRAL);
        familyMember7.setValue(value);




        reward2 = new Reward(RewardType.WOOD, 2);
        rewards = new HashSet<>();
        rewards.add(reward2);
        players = new ArrayList<>();
        players.add(new Player("Erick", new ClientInfo(ConnectionType.RMI, ClientType.CLI), PlayerColor.RED));
        players.add(new Player("Erick", new ClientInfo(ConnectionType.RMI, ClientType.CLI), PlayerColor.RED));
        players.add(new Player("Erick", new ClientInfo(ConnectionType.RMI, ClientType.CLI), PlayerColor.RED));
        board = new Board(players);
        tower = new Tower(CardType.CHARACTER, board);
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