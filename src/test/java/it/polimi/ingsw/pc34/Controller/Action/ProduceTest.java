package it.polimi.ingsw.pc34.Controller.Action;

import it.polimi.ingsw.pc34.Controller.Game;
import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.Model.CardType;
import it.polimi.ingsw.pc34.Model.Counter;
import it.polimi.ingsw.pc34.Model.Reward;
import it.polimi.ingsw.pc34.Model.RewardType;
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
public class ProduceTest {

    @Test
    public void testProduce() throws Exception {
        // Initialize game
        Lobby lobby = new Lobby();
        ServerRMIImpl serverRMI = new ServerRMIImpl(lobby);
        ServerSOC serverSOC = new ServerSOC(1337, lobby);
        Map<String, ClientInfo> users = new HashMap<>();
        users.put("Cugola", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        users.put("Affetti", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        Game game = new Game(users, serverRMI, serverSOC);

        // +++ RMI +++
        // Set family member value 5
        game.getPlayers().get(0).getPlayerBoard().getFamilyMembers().get(0).setValue(5);
        // Set card in buildingSpot 1-4
        try {
            for(int i = 0; i < 4; i++){
                game.getPlayers().get(0).getPlayerBoard().getBuildingSpot().getCards().add(JSONUtility.getCard(1, i, CardType.BUILDING));
            }
            game.getPlayers().get(0).getPlayerBoard().getTerritorySpot().getCards().add(JSONUtility.getCard(1, 0, CardType.TERRITORY));
        } catch (Exception e) {
            assertFalse(false);
        }

        // Initialize action
        Produce produceRMI = new Produce(game, game.getBoard().getProductionArea().get(0), game.getPlayers().get(0).getPlayerBoard().getFamilyMembers().get(0));

        // Check if can do action
        assertTrue(produceRMI.canDoAction());


        // Check if action is done correctly
        Counter expectedRMI = game.getPlayers().get(0).getPlayerBoard().getCounter();
        expectedRMI.sum(new Reward(RewardType.COIN, 5));
        expectedRMI.sum(game.getPlayers().get(0).getPlayerBoard().getPersonalBonusTile().getProductionRewards());

        produceRMI.doAction();
        Counter calculatedRMI = game.getPlayers().get(0).getPlayerBoard().getCounter();

        assertEquals(expectedRMI, calculatedRMI);


        // +++ SOCKET +++
        // Set family member value 3
        game.getPlayers().get(1).getPlayerBoard().getFamilyMembers().get(0).setValue(5);
        // Set card in buildingSpot 1-4
        try {
            for(int i = 0; i < 4; i++){
                game.getPlayers().get(1).getPlayerBoard().getBuildingSpot().getCards().add(JSONUtility.getCard(1, i, CardType.BUILDING));
            }
        } catch (Exception e) {
            assertFalse(false);
        }

        // Initialize action
        Produce produceSOC = new Produce(game, game.getBoard().getProductionArea().get(0), game.getPlayers().get(1).getPlayerBoard().getFamilyMembers().get(0));

        // Check if can do action
        assertFalse(produceSOC.canDoAction());
    }
}
