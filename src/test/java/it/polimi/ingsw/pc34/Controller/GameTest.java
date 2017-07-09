package it.polimi.ingsw.pc34.Controller;

import it.polimi.ingsw.pc34.Model.*;
import it.polimi.ingsw.pc34.RMI.ServerRMIImpl;
import it.polimi.ingsw.pc34.Socket.ServerSOC;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientInfo;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by trill on 09/07/2017.
 */
public class GameTest {

    @Test
    public void testCalculateRealValueFamilyMember() throws Exception{
        // Initialize game
        Lobby lobby = new Lobby();
        ServerRMIImpl serverRMI = new ServerRMIImpl(lobby);
        ServerSOC serverSOC = new ServerSOC(1337, lobby);
        Map<String, ClientInfo> users = new HashMap<>();
        users.put("Cugola", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        users.put("Affetti", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        Game game = new Game(users, serverRMI, serverSOC);
        PlayerBoard playerBoard = game.getPlayers().get(0).getPlayerBoard();

        FamilyMember familyMember = playerBoard.getFamilyMembers().get(0);
        familyMember.setValue(2);
        familyMember.setServantUsed(new Reward(RewardType.SERVANT, 3));
        playerBoard.getModifier().setServantValueHalved(true);
        game.calculateRealValueFamilyMember(familyMember);

        int expected = 3;

        int calculated = familyMember.getRealValue();

        assertEquals(expected, calculated);
    }

    // Three all same MilitaryPoints
    @Test
    public void testEarnVictoryPointFromMilitaryPoint1() throws Exception{
        // Initialize game
        Lobby lobby = new Lobby();
        ServerRMIImpl serverRMI = new ServerRMIImpl(lobby);
        ServerSOC serverSOC = new ServerSOC(1337, lobby);
        Map<String, ClientInfo> users = new HashMap<>();
        users.put("Cugola", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        users.put("Affetti", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        users.put("Perry", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        Game game = new Game(users, serverRMI, serverSOC);

        game.getPlayers().get(0).getPlayerBoard().getCounter().sum(new Reward(RewardType.MILITARY_POINT, 5));
        game.getPlayers().get(1).getPlayerBoard().getCounter().sum(new Reward(RewardType.MILITARY_POINT, 5));
        game.getPlayers().get(2).getPlayerBoard().getCounter().sum(new Reward(RewardType.MILITARY_POINT, 5));

        Reward expectedFirst = new Reward(RewardType.VICTORY_POINT, 5);
        Reward expectedSecond = new Reward(RewardType.VICTORY_POINT, 2);
        Reward expectedOther = new Reward(RewardType.VICTORY_POINT, 0);

        game.earnVictoryPointFromMilitaryPoint(game.getPlayers());

        assertEquals(expectedFirst, game.getPlayers().get(0).getPlayerBoard().getCounter().getVictoryPoint());
        assertEquals(expectedFirst, game.getPlayers().get(1).getPlayerBoard().getCounter().getVictoryPoint());
        assertEquals(expectedFirst, game.getPlayers().get(2).getPlayerBoard().getCounter().getVictoryPoint());
    }

    // Two with same MilitaryPoints, third with less
    @Test
    public void testEarnVictoryPointFromMilitaryPoint2() throws Exception{
        // Initialize game
        Lobby lobby = new Lobby();
        ServerRMIImpl serverRMI = new ServerRMIImpl(lobby);
        ServerSOC serverSOC = new ServerSOC(1337, lobby);
        Map<String, ClientInfo> users = new HashMap<>();
        users.put("Cugola", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        users.put("Affetti", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        users.put("Perry", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        Game game = new Game(users, serverRMI, serverSOC);

        game.getPlayers().get(0).getPlayerBoard().getCounter().sum(new Reward(RewardType.MILITARY_POINT, 5));
        game.getPlayers().get(1).getPlayerBoard().getCounter().sum(new Reward(RewardType.MILITARY_POINT, 5));
        game.getPlayers().get(2).getPlayerBoard().getCounter().sum(new Reward(RewardType.MILITARY_POINT, 2));

        Reward expectedFirst = new Reward(RewardType.VICTORY_POINT, 5);
        Reward expectedSecond = new Reward(RewardType.VICTORY_POINT, 2);
        Reward expectedOther = new Reward(RewardType.VICTORY_POINT, 0);

        game.earnVictoryPointFromMilitaryPoint(game.getPlayers());

        assertEquals(expectedFirst, game.getPlayers().get(0).getPlayerBoard().getCounter().getVictoryPoint());
        assertEquals(expectedFirst, game.getPlayers().get(1).getPlayerBoard().getCounter().getVictoryPoint());
        assertEquals(expectedOther, game.getPlayers().get(2).getPlayerBoard().getCounter().getVictoryPoint());
    }

    // One with MilitaryPoints, second and third with same but less
    @Test
    public void testEarnVictoryPointFromMilitaryPoint3() throws Exception{
        // Initialize game
        Lobby lobby = new Lobby();
        ServerRMIImpl serverRMI = new ServerRMIImpl(lobby);
        ServerSOC serverSOC = new ServerSOC(1337, lobby);
        Map<String, ClientInfo> users = new HashMap<>();
        users.put("Cugola", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        users.put("Affetti", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        users.put("Perry", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        Game game = new Game(users, serverRMI, serverSOC);

        game.getPlayers().get(0).getPlayerBoard().getCounter().sum(new Reward(RewardType.MILITARY_POINT, 5));
        game.getPlayers().get(1).getPlayerBoard().getCounter().sum(new Reward(RewardType.MILITARY_POINT, 2));
        game.getPlayers().get(2).getPlayerBoard().getCounter().sum(new Reward(RewardType.MILITARY_POINT, 2));

        Reward expectedFirst = new Reward(RewardType.VICTORY_POINT, 5);
        Reward expectedSecond = new Reward(RewardType.VICTORY_POINT, 2);
        Reward expectedOther = new Reward(RewardType.VICTORY_POINT, 0);

        game.earnVictoryPointFromMilitaryPoint(game.getPlayers());

        assertEquals(expectedFirst, game.getPlayers().get(0).getPlayerBoard().getCounter().getVictoryPoint());
        assertEquals(expectedSecond, game.getPlayers().get(1).getPlayerBoard().getCounter().getVictoryPoint());
        assertEquals(expectedSecond, game.getPlayers().get(2).getPlayerBoard().getCounter().getVictoryPoint());
    }

    // All with different MiliaryPoints
    @Test
    public void testEarnVictoryPointFromMilitaryPoint4() throws Exception{
        // Initialize game
        Lobby lobby = new Lobby();
        ServerRMIImpl serverRMI = new ServerRMIImpl(lobby);
        ServerSOC serverSOC = new ServerSOC(1337, lobby);
        Map<String, ClientInfo> users = new HashMap<>();
        users.put("Cugola", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        users.put("Affetti", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        users.put("Perry", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        Game game = new Game(users, serverRMI, serverSOC);

        game.getPlayers().get(0).getPlayerBoard().getCounter().sum(new Reward(RewardType.MILITARY_POINT, 5));
        game.getPlayers().get(1).getPlayerBoard().getCounter().sum(new Reward(RewardType.MILITARY_POINT, 3));
        game.getPlayers().get(2).getPlayerBoard().getCounter().sum(new Reward(RewardType.MILITARY_POINT, 2));

        Reward expectedFirst = new Reward(RewardType.VICTORY_POINT, 5);
        Reward expectedSecond = new Reward(RewardType.VICTORY_POINT, 2);
        Reward expectedOther = new Reward(RewardType.VICTORY_POINT, 0);

        game.earnVictoryPointFromMilitaryPoint(game.getPlayers());

        assertEquals(expectedFirst, game.getPlayers().get(0).getPlayerBoard().getCounter().getVictoryPoint());
        assertEquals(expectedSecond, game.getPlayers().get(1).getPlayerBoard().getCounter().getVictoryPoint());
        assertEquals(expectedOther, game.getPlayers().get(2).getPlayerBoard().getCounter().getVictoryPoint());
    }

    // All with different VictoryPoints
    @Test
    public void tesCalculateWinner1() throws Exception{
        // Initialize game
        Lobby lobby = new Lobby();
        ServerRMIImpl serverRMI = new ServerRMIImpl(lobby);
        ServerSOC serverSOC = new ServerSOC(1337, lobby);
        Map<String, ClientInfo> users = new HashMap<>();
        users.put("Cugola", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        users.put("Affetti", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        users.put("Perry", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        Game game = new Game(users, serverRMI, serverSOC);

        game.getPlayers().get(0).getPlayerBoard().getCounter().sum(new Reward(RewardType.VICTORY_POINT, 3));
        game.getPlayers().get(1).getPlayerBoard().getCounter().sum(new Reward(RewardType.VICTORY_POINT, 5));
        game.getPlayers().get(2).getPlayerBoard().getCounter().sum(new Reward(RewardType.VICTORY_POINT, 2));

        Player expected = game.getPlayers().get(1);

        Player calculated = game.calculateWinner(game.getPlayers());

        assertEquals(expected, calculated);
    }

    // All with same VictoryPoints
    @Test
    public void tesCalculateWinner2() throws Exception{
        // Initialize game
        Lobby lobby = new Lobby();
        ServerRMIImpl serverRMI = new ServerRMIImpl(lobby);
        ServerSOC serverSOC = new ServerSOC(1337, lobby);
        Map<String, ClientInfo> users = new HashMap<>();
        users.put("Cugola", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        users.put("Affetti", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        users.put("Perry", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        Game game = new Game(users, serverRMI, serverSOC);

        game.getPlayers().get(0).getPlayerBoard().getCounter().sum(new Reward(RewardType.VICTORY_POINT, 5));
        game.getPlayers().get(1).getPlayerBoard().getCounter().sum(new Reward(RewardType.VICTORY_POINT, 5));
        game.getPlayers().get(2).getPlayerBoard().getCounter().sum(new Reward(RewardType.VICTORY_POINT, 5));

        Player expected = game.getPlayers().get(0);

        Player calculated = game.calculateWinner(game.getPlayers());

        assertEquals(expected, calculated);
    }
}
