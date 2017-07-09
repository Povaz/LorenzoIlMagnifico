package it.polimi.ingsw.pc34.Controller.Action;

import it.polimi.ingsw.pc34.Controller.Game;
import it.polimi.ingsw.pc34.Model.*;
import it.polimi.ingsw.pc34.RMI.ServerRMIImpl;
import it.polimi.ingsw.pc34.Socket.ServerSOC;
import it.polimi.ingsw.pc34.SocketRMICongiunction.*;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by trill on 09/07/2017.
 */
public class PlaceMarketTest{

    @Test
    public void testPlaceMarket() throws Exception{
        // Initialize game
        Lobby lobby = new Lobby();
        ServerRMIImpl serverRMI = new ServerRMIImpl(lobby);
        ServerSOC serverSOC = new ServerSOC(1337, lobby);
        Map<String, ClientInfo> users = new HashMap<>();
        users.put("Cugola", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        users.put("Affetti", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        Game game = new Game(users, serverRMI, serverSOC);


        // +++ RMI +++
        // Set family member value 1
        game.getPlayers().get(0).getPlayerBoard().getFamilyMembers().get(0).setValue(1);

        // Initialize action
        PlaceMarket placeMarketRMI = new PlaceMarket(game, game.getBoard().getMarket().get(0), game.getPlayers().get(0).getPlayerBoard().getFamilyMembers().get(0));

        // Check if can do action
        assertTrue(placeMarketRMI.canDoAction());

        // Check if action is done correctly
        Counter expectedRMI = game.getPlayers().get(0).getPlayerBoard().getCounter();
        expectedRMI.sum(new Reward(RewardType.COIN, 5));

        placeMarketRMI.doAction();
        Counter calculatedRMI = game.getPlayers().get(0).getPlayerBoard().getCounter();

        assertEquals(expectedRMI, calculatedRMI);


        // +++ SOCKET +++
        // Set family member value 1
        game.getPlayers().get(1).getPlayerBoard().getFamilyMembers().get(0).setValue(1);

        // Initialize action
        PlaceMarket placeMarketSOC = new PlaceMarket(game, game.getBoard().getMarket().get(1), game.getPlayers().get(1).getPlayerBoard().getFamilyMembers().get(0));

        // Check if can do action
        assertTrue(placeMarketSOC.canDoAction());

        // Check if action is done correctly
        Counter expectedSOC = game.getPlayers().get(0).getPlayerBoard().getCounter();
        expectedSOC.sum(new Reward(RewardType.SERVANT, 5));

        placeMarketSOC.doAction();
        Counter calculatedSOC = game.getPlayers().get(0).getPlayerBoard().getCounter();

        assertEquals(expectedSOC, calculatedSOC);
    }
}
