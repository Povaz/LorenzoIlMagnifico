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
public class BuyCardTest {

    // With territoryCard purchasable
    @Test
    public void testBuyCard1() throws Exception {
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
        // Set card in territoryTower Floor 0
        try {
            game.getBoard().getTerritoryTower().getFloors().get(0).setCard(JSONUtility.getCard(1, 0, CardType.TERRITORY));
        } catch (Exception e) {
            assertFalse(false);
        }

        // Initialize action
        BuyCard buyCardRMI = new BuyCard(game, game.getBoard().getTerritoryTower().getFloors().get(0), game.getPlayers().get(0).getPlayerBoard().getFamilyMembers().get(0));

        // Check if can do action
        assertTrue(buyCardRMI.canDoAction());


        // Check if action is done correctly
        Counter expectedRMI = game.getPlayers().get(0).getPlayerBoard().getCounter();
        DevelopmentCard experctedCardRMI = game.getBoard().getTerritoryTower().getFloors().get(0).getCard();

        buyCardRMI.doAction();
        Counter calculatedRMI = game.getPlayers().get(0).getPlayerBoard().getCounter();
        DevelopmentCard calculatedCardRMI = game.getPlayers().get(0).getPlayerBoard().getTerritorySpot().getCards().get(0);

        assertEquals(expectedRMI, calculatedRMI);
        assertEquals(experctedCardRMI, calculatedCardRMI);


        // +++ SOCKET +++
        // Set family member value 3
        game.getPlayers().get(1).getPlayerBoard().getFamilyMembers().get(0).setValue(3);
        // Set card in territoryTower Floor 1
        try {
            game.getBoard().getTerritoryTower().getFloors().get(1).setCard(JSONUtility.getCard(1, 1, CardType.TERRITORY));
        } catch (Exception e) {
            assertFalse(false);
        }

        // Initialize action
        BuyCard buyCardSOC = new BuyCard(game, game.getBoard().getTerritoryTower().getFloors().get(1), game.getPlayers().get(1).getPlayerBoard().getFamilyMembers().get(0));

        // Check if can do action
        assertTrue(buyCardSOC.canDoAction());


        // Check if action is done correctly
        Counter expectedSOC = game.getPlayers().get(1).getPlayerBoard().getCounter();
        expectedSOC.subtract(new Reward(RewardType.COIN, 3));
        expectedSOC.sum(new Reward(RewardType.WOOD, 1));
        DevelopmentCard experctedCardSOC = game.getBoard().getTerritoryTower().getFloors().get(1).getCard();

        buyCardSOC.doAction();
        Counter calculatedSOC = game.getPlayers().get(1).getPlayerBoard().getCounter();
        DevelopmentCard calculatedCardSOC = game.getPlayers().get(1).getPlayerBoard().getTerritorySpot().getCards().get(0);

        assertEquals(expectedSOC, calculatedSOC);
        assertEquals(experctedCardSOC, calculatedCardSOC);
    }

    // With buildingCard purchasable
    @Test
    public void testBuyCard2() throws Exception {
        // Initialize game
        Lobby lobby = new Lobby();
        ServerRMIImpl serverRMI = new ServerRMIImpl(lobby);
        ServerSOC serverSOC = new ServerSOC(1337, lobby);
        Map<String, ClientInfo> users = new HashMap<>();
        users.put("Cugola", new ClientInfo(ConnectionType.RMI, ClientType.CLI));
        users.put("Affetti", new ClientInfo(ConnectionType.SOCKET, ClientType.CLI));
        Game game = new Game(users, serverRMI, serverSOC);

        // +++ RMI +++
        // Set family member value 1 and counter
        game.getPlayers().get(0).getPlayerBoard().getFamilyMembers().get(0).setValue(1);
        game.getPlayers().get(0).getPlayerBoard().getCounter().sum(new Reward(RewardType.STONE, 1));
        // Set card in buildingTower Floor 0
        try {
            game.getBoard().getBuildingTower().getFloors().get(0).setCard(JSONUtility.getCard(1, 0, CardType.BUILDING));
        } catch (Exception e) {
            assertFalse(false);
        }

        // Initialize action
        BuyCard buyCardRMI = new BuyCard(game, game.getBoard().getBuildingTower().getFloors().get(0), game.getPlayers().get(0).getPlayerBoard().getFamilyMembers().get(0));

        // Check if can do action
        assertTrue(buyCardRMI.canDoAction());


        // Check if action is done correctly
        Counter expectedRMI = game.getPlayers().get(0).getPlayerBoard().getCounter();
        expectedRMI.subtract(new Reward(RewardType.WOOD, 1));
        expectedRMI.subtract(new Reward(RewardType.STONE, 3));
        expectedRMI.sum(new Reward(RewardType.VICTORY_POINT, 5));
        DevelopmentCard experctedCardRMI = game.getBoard().getBuildingTower().getFloors().get(0).getCard();

        buyCardRMI.doAction();
        Counter calculatedRMI = game.getPlayers().get(0).getPlayerBoard().getCounter();
        DevelopmentCard calculatedCardRMI = game.getPlayers().get(0).getPlayerBoard().getBuildingSpot().getCards().get(0);

        assertEquals(expectedRMI, calculatedRMI);
        assertEquals(experctedCardRMI, calculatedCardRMI);


        // +++ SOCKET +++
        // Set family member value 3 and counter
        game.getPlayers().get(1).getPlayerBoard().getFamilyMembers().get(0).setValue(3);
        game.getPlayers().get(1).getPlayerBoard().getCounter().sum(new Reward(RewardType.WOOD, 1));
        // Set card in buildingTower Floor 1
        try {
            game.getBoard().getBuildingTower().getFloors().get(1).setCard(JSONUtility.getCard(1, 1, CardType.BUILDING));
        } catch (Exception e) {
            assertFalse(false);
        }

        // Initialize action
        BuyCard buyCardSOC = new BuyCard(game, game.getBoard().getBuildingTower().getFloors().get(1), game.getPlayers().get(1).getPlayerBoard().getFamilyMembers().get(0));

        // Check if can do action
        assertTrue(buyCardSOC.canDoAction());


        // Check if action is done correctly
        Counter expectedSOC = game.getPlayers().get(1).getPlayerBoard().getCounter();
        expectedSOC.subtract(new Reward(RewardType.COIN, 3));
        expectedSOC.subtract(new Reward(RewardType.WOOD, 3));
        expectedSOC.subtract(new Reward(RewardType.STONE, 1));
        expectedSOC.sum(new Reward(RewardType.VICTORY_POINT, 5));
        DevelopmentCard experctedCardSOC = game.getBoard().getBuildingTower().getFloors().get(1).getCard();

        buyCardSOC.doAction();
        Counter calculatedSOC = game.getPlayers().get(1).getPlayerBoard().getCounter();
        DevelopmentCard calculatedCardSOC = game.getPlayers().get(1).getPlayerBoard().getBuildingSpot().getCards().get(0);

        assertEquals(expectedSOC, calculatedSOC);
        assertEquals(experctedCardSOC, calculatedCardSOC);
    }

    // With characterCard purchasable
    @Test
    public void testBuyCard3() throws Exception {
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
        // Set card in characterTower Floor 0
        try {
            game.getBoard().getCharacterTower().getFloors().get(0).setCard(JSONUtility.getCard(1, 0, CardType.CHARACTER));
        } catch (Exception e) {
            assertFalse(false);
        }

        // Initialize action
        BuyCard buyCardRMI = new BuyCard(game, game.getBoard().getCharacterTower().getFloors().get(0), game.getPlayers().get(0).getPlayerBoard().getFamilyMembers().get(0));

        // Check if can do action
        assertTrue(buyCardRMI.canDoAction());


        // Check if action is done correctly
        Counter expectedRMI = game.getPlayers().get(0).getPlayerBoard().getCounter();
        expectedRMI.subtract(new Reward(RewardType.COIN, 2));
        expectedRMI.sum(new Reward(RewardType.MILITARY_POINT, 3));
        DevelopmentCard experctedCardRMI = game.getBoard().getCharacterTower().getFloors().get(0).getCard();

        buyCardRMI.doAction();
        Counter calculatedRMI = game.getPlayers().get(0).getPlayerBoard().getCounter();
        DevelopmentCard calculatedCardRMI = game.getPlayers().get(0).getPlayerBoard().getCharacterSpot().getCards().get(0);

        assertEquals(expectedRMI, calculatedRMI);
        assertEquals(experctedCardRMI, calculatedCardRMI);


        // +++ SOCKET +++
        // Set family member value 3 and counter
        game.getPlayers().get(1).getPlayerBoard().getFamilyMembers().get(0).setValue(3);
        game.getPlayers().get(1).getPlayerBoard().getCounter().sum(new Reward(RewardType.COIN, 5));
        // Set card in characterTower Floor 1
        try {
            game.getBoard().getCharacterTower().getFloors().get(1).setCard(JSONUtility.getCard(1, 1, CardType.CHARACTER));
        } catch (Exception e) {
            assertFalse(false);
        }

        // Initialize action
        BuyCard buyCardSOC = new BuyCard(game, game.getBoard().getCharacterTower().getFloors().get(1), game.getPlayers().get(1).getPlayerBoard().getFamilyMembers().get(0));

        // Check if can do action
        assertTrue(buyCardSOC.canDoAction());


        // Check if action is done correctly
        Counter expectedSOC = game.getPlayers().get(1).getPlayerBoard().getCounter();
        expectedSOC.subtract(new Reward(RewardType.COIN, 7));

        DevelopmentCard experctedCardSOC = game.getBoard().getCharacterTower().getFloors().get(1).getCard();

        buyCardSOC.doAction();
        Counter calculatedSOC = game.getPlayers().get(1).getPlayerBoard().getCounter();
        DevelopmentCard calculatedCardSOC = game.getPlayers().get(1).getPlayerBoard().getCharacterSpot().getCards().get(0);

        assertEquals(expectedSOC, calculatedSOC);
        assertEquals(experctedCardSOC, calculatedCardSOC);
    }

    // With ventureCard purchasable
    @Test
    public void testBuyCard4() throws Exception {
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
        // Set card in ventureTower Floor 0
        try {
            game.getBoard().getVentureTower().getFloors().get(0).setCard(JSONUtility.getCard(1, 0, CardType.VENTURE));
        } catch (Exception e) {
            assertFalse(false);
        }

        // Initialize action
        BuyCard buyCardRMI = new BuyCard(game, game.getBoard().getVentureTower().getFloors().get(0), game.getPlayers().get(0).getPlayerBoard().getFamilyMembers().get(0));

        // Check if can do action
        assertTrue(buyCardRMI.canDoAction());


        // Check if action is done correctly
        Counter expectedRMI = game.getPlayers().get(0).getPlayerBoard().getCounter();
        expectedRMI.subtract(new Reward(RewardType.COIN, 4));
        expectedRMI.sum(new Reward(RewardType.MILITARY_POINT, 5));
        DevelopmentCard experctedCardRMI = game.getBoard().getVentureTower().getFloors().get(0).getCard();

        buyCardRMI.doAction();
        Counter calculatedRMI = game.getPlayers().get(0).getPlayerBoard().getCounter();
        DevelopmentCard calculatedCardRMI = game.getPlayers().get(0).getPlayerBoard().getVentureSpot().getCards().get(0);

        assertEquals(expectedRMI, calculatedRMI);
        assertEquals(experctedCardRMI, calculatedCardRMI);


        // +++ SOCKET +++
        // Set family member value 3
        game.getPlayers().get(1).getPlayerBoard().getFamilyMembers().get(0).setValue(3);
        // Set card in ventureTower Floor 1
        try {
            game.getBoard().getVentureTower().getFloors().get(1).setCard(JSONUtility.getCard(1, 1, CardType.VENTURE));
        } catch (Exception e) {
            assertFalse(false);
        }

        // Initialize action
        BuyCard buyCardSOC = new BuyCard(game, game.getBoard().getVentureTower().getFloors().get(1), game.getPlayers().get(1).getPlayerBoard().getFamilyMembers().get(0));

        // Check if can do action
        assertTrue(buyCardSOC.canDoAction());


        // Check if action is done correctly
        Counter expectedSOC = game.getPlayers().get(1).getPlayerBoard().getCounter();
        expectedSOC.subtract(new Reward(RewardType.COIN, 4));
        expectedSOC.subtract(new Reward(RewardType.WOOD, 1));
        expectedSOC.subtract(new Reward(RewardType.STONE, 1));
        expectedSOC.sum(new Reward(RewardType.FAITH_POINT, 1));
        DevelopmentCard experctedCardSOC = game.getBoard().getVentureTower().getFloors().get(1).getCard();

        buyCardSOC.doAction();
        Counter calculatedSOC = game.getPlayers().get(1).getPlayerBoard().getCounter();
        DevelopmentCard calculatedCardSOC = game.getPlayers().get(1).getPlayerBoard().getVentureSpot().getCards().get(0);

        assertEquals(expectedSOC, calculatedSOC);
        assertEquals(experctedCardSOC, calculatedCardSOC);
    }
}
