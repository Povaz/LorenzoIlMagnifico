package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.JSONUtility;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.json.JSONException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Povaz on 14/06/2017.
 */
public class CardSpotTest extends TestCase {
    private BuildingSpot buildingSpot;
    private CharacterSpot characterSpot;
    private TerritorySpot territorySpot;
    private VentureSpot ventureSpot;

    private List<DevelopmentCard> buildingCards;
    private List<DevelopmentCard> characterCards;
    private List<DevelopmentCard> territoryCards;
    private List<DevelopmentCard> ventureCards;

    public CardSpotTest (String name) {
        super(name);
    }

    @org.junit.Test
    public void testPlaceCard() {
        for (DevelopmentCard dC : buildingCards) {
            buildingSpot.placeCard(dC);
        }

        for (int i = 0; i < 6; i++) {
            assertTrue("Carte posizionate correttamente", buildingSpot.getCards().get(i).equals(buildingCards.get(i)));
        }


    }

    @org.junit.Test
    public void testCanPlaceCard () {
        for (DevelopmentCard dC : characterCards) {
            characterSpot.placeCard(dC);
        }

        assertFalse("CardSpot pieno", characterSpot.canPlaceCard());

        characterSpot.getCards().remove(5);

        assertTrue("CardSpot non pieno", characterSpot.canPlaceCard());

    }

    @org.junit.Test
    public void testEstimateVictoryPointBuilding() {
        Reward reward = new Reward(RewardType.VICTORY_POINT, 0);

        assertEquals(reward, buildingSpot.estimateVictoryPoint());
    }

    @org.junit.Test
    public void testEstimateVictoryPointCharacter() {
        int i = 1;
        int c = 2;

        assertEquals(new Reward(RewardType.VICTORY_POINT, 0), characterSpot.estimateVictoryPoint());

        for (DevelopmentCard dC : characterCards) {
            characterSpot.placeCard(dC);
            assertEquals(new Reward(RewardType.VICTORY_POINT, i), characterSpot.estimateVictoryPoint());
            i += c;
            c += 1;
        }
    }

    @org.junit.Test
    public void testEstimateVictoryPointVenture() {
        for (DevelopmentCard dC : ventureCards) {
            ventureSpot.placeCard(dC);
        }

        assertEquals(new Reward(RewardType.VICTORY_POINT, 25), ventureSpot.estimateVictoryPoint()); //Calcolati dalle prime 6 Venture Card
    }

    @org.junit.Test
    public void testEstimateVictoryPointTerritory() {
        List<Reward> rewards = new ArrayList<>();
        rewards.add(new Reward(RewardType.VICTORY_POINT, 0));
        rewards.add(new Reward(RewardType.VICTORY_POINT, 0));
        rewards.add(new Reward(RewardType.VICTORY_POINT, 0));
        rewards.add(new Reward(RewardType.VICTORY_POINT, 1));
        rewards.add(new Reward(RewardType.VICTORY_POINT, 4));
        rewards.add(new Reward(RewardType.VICTORY_POINT, 10));
        rewards.add(new Reward(RewardType.VICTORY_POINT, 20));

        assertEquals(rewards.get(0), ventureSpot.estimateVictoryPoint());

        for (DevelopmentCard dC : territoryCards) {
            territorySpot.placeCard(dC);
            assertEquals(rewards.get(ventureSpot.getCards().size()), ventureSpot.estimateVictoryPoint());
        }
    }

    public void setUp() throws JSONException, IOException {
        buildingCards = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            buildingCards.add(JSONUtility.getBuildingCard(1, i));
        }

        characterCards = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            characterCards.add(JSONUtility.getCharacterCard(1, i));
        }

        territoryCards = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            territoryCards.add(JSONUtility.getTerritoryCard(1, i));
        }

        ventureCards = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ventureCards.add(JSONUtility.getVentureCard(1, i));
        }

        buildingSpot = new BuildingSpot();
        characterSpot = new CharacterSpot();
        territorySpot = new TerritorySpot();
        ventureSpot = new VentureSpot();
    }

    public static TestSuite suite () {
        TestSuite suite = new TestSuite();
        suite.addTest( new CardSpotTest("testPlaceCard"));
        suite.addTest( new CardSpotTest("testCanPlaceCard"));
        suite.addTest( new CardSpotTest("testEstimateVictoryPointBuilding"));
        suite.addTest( new CardSpotTest("testEstimateVictoryPointCharacter"));
        suite.addTest( new CardSpotTest("testEstimateVictoryPointTerritory"));
        suite.addTest( new CardSpotTest("testEstimateVictoryPointVenture"));
        return suite;
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }
 }
