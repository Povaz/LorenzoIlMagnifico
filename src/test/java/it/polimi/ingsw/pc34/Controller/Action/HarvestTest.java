package it.polimi.ingsw.pc34.Controller.Action;

import it.polimi.ingsw.pc34.Controller.Game;
import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.Model.*;
import it.polimi.ingsw.pc34.RMI.ServerRMIImpl;
import it.polimi.ingsw.pc34.Socket.ServerSOC;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientInfo;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by trill on 09/07/2017.
 */
public class HarvestTest {

    @Test
    public void testHavest() throws Exception {
        // Initialize game
        Lobby lobby = new Lobby();
        ServerRMIImpl serverRMI = new ServerRMIImpl(lobby);
        ServerSOC serverSOC = new ServerSOC(1337, lobby);
        Map<String, ClientInfo> users = new HashMap<>();
        users.put("Cugola", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        users.put("Affetti", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        Game game = new Game(users, serverRMI, serverSOC);

        // +++ RMI +++
        // Set family member value 3
        game.getPlayers().get(0).getPlayerBoard().getFamilyMembers().get(0).setValue(3);
        // Set card in territorySpot 1-4
        try {
            for(int i = 0; i < 4; i++){
                game.getPlayers().get(0).getPlayerBoard().getTerritorySpot().getCards().add(JSONUtility.getCard(1, i, CardType.TERRITORY));
            }
        } catch (Exception e) {
            assertFalse(false);
        }

        // Initialize action
        Harvest harvestRMI = new Harvest(game, game.getBoard().getHarvestArea().get(0), game.getPlayers().get(0).getPlayerBoard().getFamilyMembers().get(0));

        // Check if can do action
        assertTrue(harvestRMI.canDoAction());


        // Check if action is done correctly
        Counter expectedRMI = game.getPlayers().get(0).getPlayerBoard().getCounter();
        expectedRMI.sum(new Reward(RewardType.COIN, 2));
        expectedRMI.sum(new Reward(RewardType.WOOD, 1));
        expectedRMI.sum(new Reward(RewardType.SERVANT, 1));
        expectedRMI.sum(game.getPlayers().get(0).getPlayerBoard().getPersonalBonusTile().getHarvestRewards());

        harvestRMI.doAction();
        Counter calculatedRMI = game.getPlayers().get(0).getPlayerBoard().getCounter();

        assertEquals(expectedRMI, calculatedRMI);


        // +++ SOCKET +++
        // Set family member value 3
        game.getPlayers().get(1).getPlayerBoard().getFamilyMembers().get(0).setValue(3);
        // Set card in territorySpot 1-4
        try {
            for(int i = 0; i < 4; i++){
                game.getPlayers().get(1).getPlayerBoard().getTerritorySpot().getCards().add(JSONUtility.getCard(1, i, CardType.TERRITORY));
            }
        } catch (Exception e) {
            assertFalse(false);
        }

        // Initialize action
        Harvest harvestSOC = new Harvest(game, game.getBoard().getHarvestArea().get(0), game.getPlayers().get(1).getPlayerBoard().getFamilyMembers().get(0));

        // Check if can do action
        assertFalse(harvestSOC.canDoAction());
    }
}
