package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.JSONUtility;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.json.JSONException;

import java.io.IOException;
import java.util.*;

/**
 * Created by Povaz on 14/06/2017.
 */
public class AllCardSpotTest extends TestCase {
    private BuildingSpot buildingSpot;
    private CharacterSpot characterSpot;
    private TerritorySpot territorySpot;
    private VentureSpot ventureSpot;

    private List<DevelopmentCard> buildingCards;
    private List<DevelopmentCard> characterCards;
    private List<DevelopmentCard> territoryCards;
    private List<DevelopmentCard> ventureCards;

    public AllCardSpotTest(String name) {
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
        Set<Reward> reward = new HashSet<>();
        assertEquals(reward, buildingSpot.estimateVictoryPoint());
    }

    @org.junit.Test
    public void testEstimateVictoryPointCharacter() {
        int i = 1;
        int c = 2;

        assertEquals(new HashSet<>(), characterSpot.estimateVictoryPoint());

        for (DevelopmentCard dC : characterCards) {
            characterSpot.placeCard(dC);
            Set<Reward> expected = new HashSet<>();
            expected.add(new Reward(RewardType.VICTORY_POINT, i));
            assertEquals(expected, characterSpot.estimateVictoryPoint());
            i += c;
            c += 1;
        }
    }

    @org.junit.Test
    public void testEstimateVictoryPointVenture() {
        for (DevelopmentCard dC : ventureCards) {
            ventureSpot.placeCard(dC);
        }

        assertEquals(new HashSet<Reward>(), ventureSpot.estimateVictoryPoint()); //Calcolati dalle prime 6 Venture Card
    }

    @org.junit.Test
    public void testEstimateVictoryPointTerritory() {
        List<Set<Reward>> rewards = new ArrayList<>();
        rewards.add(new HashSet<>());
        rewards.add(new HashSet<>());
        rewards.add(new HashSet<>());
        rewards.add(Collections.singleton(new Reward(RewardType.VICTORY_POINT, 1)));
        rewards.add(Collections.singleton(new Reward(RewardType.VICTORY_POINT, 4)));
        rewards.add(Collections.singleton(new Reward(RewardType.VICTORY_POINT, 10)));
        rewards.add(Collections.singleton(new Reward(RewardType.VICTORY_POINT, 20)));

        assertEquals(rewards.get(0), ventureSpot.estimateVictoryPoint());

        for (DevelopmentCard dC : territoryCards) {
            territorySpot.placeCard(dC);
            assertEquals(rewards.get(ventureSpot.getCards().size()), ventureSpot.estimateVictoryPoint());
        }
    }

    public void setUp() throws JSONException, IOException {
        buildingCards = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            buildingCards.add(JSONUtility.getCard(1, i, CardType.BUILDING));
        }

        characterCards = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            characterCards.add(JSONUtility.getCard(1, i, CardType.CHARACTER));
        }

        territoryCards = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            territoryCards.add(JSONUtility.getCard(1, i, CardType.TERRITORY));
        }

        ventureCards = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ventureCards.add(JSONUtility.getCard(1, i, CardType.VENTURE));
        }

        buildingSpot = new BuildingSpot();
        characterSpot = new CharacterSpot();
        territorySpot = new TerritorySpot();
        ventureSpot = new VentureSpot();
    }

    public static TestSuite suite () {
        TestSuite suite = new TestSuite();
        suite.addTest( new AllCardSpotTest("testPlaceCard"));
        suite.addTest( new AllCardSpotTest("testCanPlaceCard"));
        suite.addTest( new AllCardSpotTest("testEstimateVictoryPointBuilding"));
        suite.addTest( new AllCardSpotTest("testEstimateVictoryPointCharacter"));
        suite.addTest( new AllCardSpotTest("testEstimateVictoryPointTerritory"));
        suite.addTest( new AllCardSpotTest("testEstimateVictoryPointVenture"));
        return suite;
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }
 }
