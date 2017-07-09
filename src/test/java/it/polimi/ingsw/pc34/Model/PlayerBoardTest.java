package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientInfo;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by trill on 09/07/2017.
 */
public class PlayerBoardTest {
    @Test
    public void testEarnFinalVictoryPoint(){
        Player player = new Player("Cugola", new ClientInfo(ConnectionType.RMI, ClientType.GUI), PlayerColor.RED);
        PlayerBoard playerBoard = new PlayerBoard(player);

        try {
            for(int i = 0; i < 4; i++) {
                playerBoard.getTerritorySpot().getCards().add(JSONUtility.getCard(1, i, CardType.TERRITORY));
                playerBoard.getBuildingSpot().getCards().add(JSONUtility.getCard(1, i, CardType.BUILDING));
                playerBoard.getCharacterSpot().getCards().add(JSONUtility.getCard(1, i, CardType.CHARACTER));
                playerBoard.getVentureSpot().getCards().add(JSONUtility.getCard(1, i, CardType.VENTURE));
            }
        } catch(Exception e){
            assertFalse(true);
        }

        // CardSpot:
        // Territory: 4 points
        // Building: 0 point
        // Character: 10 points
        // Venture: 0 point

        // Other:
        // Venture points: 4 + 5 + 3 + 4 = 16
        // Rewards: 0

        Reward expected = new Reward(RewardType.VICTORY_POINT, 30);

        playerBoard.earnFinalVictoryPoint();
        Reward calculated = playerBoard.getCounter().getVictoryPoint();

        assertEquals(expected, calculated);
    }

    @Test public void testEarnVictoryPointFromVentureCards(){
        Player player = new Player("Cugola", new ClientInfo(ConnectionType.RMI, ClientType.GUI), PlayerColor.RED);
        PlayerBoard playerBoard = new PlayerBoard(player);

        try {
            for(int i = 0; i < 4; i++) {
                playerBoard.getVentureSpot().getCards().add(JSONUtility.getCard(1, i, CardType.VENTURE));
            }
        } catch(Exception e){
            assertFalse(true);
        }

        Reward expected = new Reward(RewardType.VICTORY_POINT, 16);

        playerBoard.earnVictoryPointFromVentureCards();
        Reward calculated = playerBoard.getCounter().getVictoryPoint();

        System.out.println(playerBoard);

        assertEquals(expected, calculated);
    }
}
