package it.polimi.ingsw.pc34.Model;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

/**
 * Created by trill on 17/06/2017.
 */
public class ModifierTest{

    // With only boolean in vaticanReportCard
    @Test
    public void updateTestVaticanReportCard1(){
        List<String> booleans = new ArrayList<>(Arrays.asList("cannotPlaceInMarket", "jumpFirstRound"));
        VaticanReportCard vaticanReportCard = new VaticanReportCard(0, 1, null, null, null,
                booleans, -1);

        Modifier expected = new Modifier();
        expected.setCannotPlaceInMarket(true);
        expected.setJumpFirstRound(true);
        expected.setColoredFamilyMemberModifier(-1);

        Modifier calculated = new Modifier();
        calculated.update(vaticanReportCard);

        assertEquals(expected, calculated);
    }

    // With only loseReward in vaticanReportCard
    @Test
    public void updateTestVaticanReportCard2(){
        List<Reward> loseRewards = new ArrayList<>(Arrays.asList(new Reward(RewardType.STONE, 1), new Reward(RewardType.WOOD, 2)));
        VaticanReportCard vaticanReportCard = new VaticanReportCard(0, 1, null,
                loseRewards, null, null, 0);

        Modifier expected = new Modifier();
        expected.getLoseRewards().add(new Reward(RewardType.STONE, 3));
        expected.getLoseRewards().add(new Reward(RewardType.WOOD, 2));

        Modifier calculated = new Modifier();
        calculated.getLoseRewards().add(new Reward(RewardType.STONE, 2));
        calculated.update(vaticanReportCard);

        assertEquals(expected, calculated);
    }

    // With only actionModifier (ActionType != ALL && ActionType != ANY_TOWER) in vaticanReportCard
    @Test
    public void updateTestVaticanReportCard3(){
        Map<ActionType, Integer> actionModifiers = new HashMap<>();
        actionModifiers.put(ActionType.TERRITORY_TOWER, -4);
        actionModifiers.put(ActionType.VENTURE_TOWER, -2);
        VaticanReportCard vaticanReportCard = new VaticanReportCard(0, 1, null,
                null, actionModifiers, null, 0);

        Modifier expected = new Modifier();
        expected.getActionModifiers().replace(ActionType.TERRITORY_TOWER, -4);
        expected.getActionModifiers().replace(ActionType.VENTURE_TOWER, -1);

        Modifier calculated = new Modifier();
        calculated.getActionModifiers().replace(ActionType.VENTURE_TOWER, 1);
        calculated.update(vaticanReportCard);

        assertEquals(expected, calculated);
    }

    // With only int
    @Test
    public void updateTestPermanentLeaderCard1(){
        PermanentLeaderCard permanentLeaderCard = new PermanentLeaderCard(null, null, null, null,
                2, 3, false, false,
                null, true, 5, null,
                false, false, false);

        Modifier expected = new Modifier();
        expected.setNeutralFamilyMemberModifier(2);
        expected.setColoredFamilyMemberModifier(3);
        expected.setPermanentDice(true);
        expected.setPermanentDiceValue(5);

        Modifier calculated = new Modifier();
        calculated.update(permanentLeaderCard);

        assertEquals(expected, calculated);
    }

    // With only boolean
    @Test
    public void updateTestPermanentLeaderCard2(){
        PermanentLeaderCard permanentLeaderCard = new PermanentLeaderCard(null, null, null, null,
                0, 0, true, true,
                null, false, 0, null,
                true, true, false);

        Modifier expected = new Modifier();
        expected.setDoubleFastRewardDevelopmentCard(true);
        expected.setPlaceInBusyActionSpot(true);
        expected.setNotSatisfyMilitaryPointForTerritory(true);
        expected.setNotPayTollBusyTower(true);

        Modifier calculated = new Modifier();
        calculated.update(permanentLeaderCard);

        assertEquals(expected, calculated);
    }

    // With only bonusRewardChurchSupport
    @Test
    public void updateTestPermanentLeaderCard3(){
        List<Reward> bonusChurchSupport = new ArrayList<>(Arrays.asList(new Reward(RewardType.VICTORY_POINT, 4)));
        PermanentLeaderCard permanentLeaderCard = new PermanentLeaderCard(null, null, null, null,
                0, 0, false, false,
                bonusChurchSupport, false, 0, null,
                false, false, false);

        Modifier expected = new Modifier();
        expected.getBonusChurchSupport().add(new Reward(RewardType.VICTORY_POINT, 5));

        Modifier calculated = new Modifier();
        calculated.getBonusChurchSupport().add(new Reward(RewardType.VICTORY_POINT, 1));
        calculated.update(permanentLeaderCard);

        assertEquals(expected, calculated);
    }

    // With only discounts
    @Test
    public void updateTestPermanentLeaderCard4(){
        Map<CardType, List<List<Reward>>> discounts = new HashMap<>();
        discounts.put(CardType.TERRITORY, Arrays.asList(new ArrayList<>(Arrays.asList(new Reward(RewardType.COIN, 3)))));
        PermanentLeaderCard permanentLeaderCard = new PermanentLeaderCard(null, null, null, null,
                0, 0, false, false,
                null, false, 0, discounts,
                false, false, false);

        Modifier expected = new Modifier();
        expected.getDiscounts().get(CardType.TERRITORY).add(new ArrayList<>(Arrays.asList(new Reward(RewardType.COIN, 5))));

        Modifier calculated = new Modifier();
        calculated.getDiscounts().get(CardType.TERRITORY).add(new ArrayList<>(Arrays.asList(new Reward(RewardType.COIN, 2))));
        calculated.update(permanentLeaderCard);

        assertEquals(expected, calculated);
    }

    // With only boolean
    @Test
    public void updateTestDevelopmentCard1(){
        CharacterCard characterCard = new CharacterCard(null, null, 1, null, null, null,
                true, null, null, null, null);

        Modifier expected = new Modifier();
        expected.setNoBonusTowerResource(true);

        Modifier calculated = new Modifier();
        calculated.update(characterCard);

        assertEquals(expected, calculated);
    }

    // With only discounts
    @Test
    public void updateTestDevelopmentCard2(){
        Map<CardType, List<List<Reward>>> discounts = new HashMap<>();
        discounts.put(CardType.TERRITORY, Arrays.asList(new ArrayList<>(Arrays.asList(new Reward(RewardType.COIN, 3)))));
        CharacterCard characterCard = new CharacterCard(null, null, 1, null, null, null,
                false, discounts, null, null, null);

        Modifier expected = new Modifier();
        expected.getDiscounts().get(CardType.TERRITORY).add(new ArrayList<>(Arrays.asList(new Reward(RewardType.COIN, 5))));

        Modifier calculated = new Modifier();
        calculated.getDiscounts().get(CardType.TERRITORY).add(new ArrayList<>(Arrays.asList(new Reward(RewardType.COIN, 2))));
        calculated.update(characterCard);

        assertEquals(expected, calculated);
    }

    // With only actionModifiers
    @Test
    public void updateTestDevelopmentCard3(){
        Map<ActionType, Integer> actionModifiers = new HashMap<>();
        actionModifiers.put(ActionType.TERRITORY_TOWER, -4);
        actionModifiers.put(ActionType.VENTURE_TOWER, -2);
        CharacterCard characterCard = new CharacterCard(null, null, 1, null, null, null,
                false, null, actionModifiers, null, null);

        Modifier expected = new Modifier();
        expected.getActionModifiers().replace(ActionType.TERRITORY_TOWER, -4);
        expected.getActionModifiers().replace(ActionType.VENTURE_TOWER, -1);

        Modifier calculated = new Modifier();
        calculated.getActionModifiers().replace(ActionType.VENTURE_TOWER, 1);
        calculated.update(characterCard);

        assertEquals(expected, calculated);
    }

    // ActionType != ALL && ActionType != ANY_TOWER
    @Test
    public void updateActionModifiersTest1(){
        Map<ActionType, Integer> expected = new HashMap<>();
        expected.put(ActionType.HARVEST, 0);
        expected.put(ActionType.PRODUCE, 0);
        expected.put(ActionType.TERRITORY_TOWER, 0);
        expected.put(ActionType.BUILDING_TOWER, -1);
        expected.put(ActionType.CHARACTER_TOWER, 0);
        expected.put(ActionType.VENTURE_TOWER, 0);
        expected.put(ActionType.COUNCIL_PALACE, 0);
        expected.put(ActionType.MARKET, 0);

        Modifier modifier = new Modifier();
        modifier.getActionModifiers().replace(ActionType.BUILDING_TOWER, -4);
        Map<ActionType, Integer> actionModifiersToAdd = new HashMap<>();
        actionModifiersToAdd.put(ActionType.BUILDING_TOWER, 3);
        modifier.updateActionModifiers(actionModifiersToAdd);

        Map<ActionType, Integer> calculated = modifier.getActionModifiers();

        assertEquals(expected, calculated);
    }

    // ActionType == ANY_TOWER
    @Test
    public void updateActionModifiersTest2(){
        Map<ActionType, Integer> expected = new HashMap<>();
        expected.put(ActionType.HARVEST, 3);
        expected.put(ActionType.PRODUCE, 0);
        expected.put(ActionType.TERRITORY_TOWER, -2);
        expected.put(ActionType.BUILDING_TOWER, 1);
        expected.put(ActionType.CHARACTER_TOWER, -1);
        expected.put(ActionType.VENTURE_TOWER, -2);
        expected.put(ActionType.COUNCIL_PALACE, 0);
        expected.put(ActionType.MARKET, 0);

        Modifier modifier = new Modifier();
        modifier.getActionModifiers().put(ActionType.HARVEST, 3);
        modifier.getActionModifiers().put(ActionType.BUILDING_TOWER, 3);
        modifier.getActionModifiers().put(ActionType.CHARACTER_TOWER, 1);
        Map<ActionType, Integer> actionModifiersToAdd = new HashMap<>();
        actionModifiersToAdd.put(ActionType.ANY_TOWER, -2);
        modifier.updateActionModifiers(actionModifiersToAdd);

        Map<ActionType, Integer> calculated = modifier.getActionModifiers();

        assertEquals(expected, calculated);
    }

    // ActionType == ANY_TOWER + TERRITORY_TOWER
    @Test
    public void updateActionModifiersTest3(){
        Map<ActionType, Integer> expected = new HashMap<>();
        expected.put(ActionType.HARVEST, 0);
        expected.put(ActionType.PRODUCE, 0);
        expected.put(ActionType.TERRITORY_TOWER, -3);
        expected.put(ActionType.BUILDING_TOWER, -2);
        expected.put(ActionType.CHARACTER_TOWER, -2);
        expected.put(ActionType.VENTURE_TOWER, -2);
        expected.put(ActionType.COUNCIL_PALACE, 0);
        expected.put(ActionType.MARKET, 0);

        Modifier modifier = new Modifier();
        modifier.getActionModifiers().put(ActionType.TERRITORY_TOWER, -5);
        Map<ActionType, Integer> actionModifiersToAdd = new HashMap<>();
        actionModifiersToAdd.put(ActionType.ANY_TOWER, -2);
        actionModifiersToAdd.put(ActionType.TERRITORY_TOWER, 4);
        modifier.updateActionModifiers(actionModifiersToAdd);

        Map<ActionType, Integer> calculated = modifier.getActionModifiers();

        assertEquals(expected, calculated);
    }

    // ActionType == ALL
    @Test
    public void updateActionModifiersTest4(){
        Map<ActionType, Integer> expected = new HashMap<>();
        expected.put(ActionType.HARVEST, 1);
        expected.put(ActionType.PRODUCE, 1);
        expected.put(ActionType.TERRITORY_TOWER, -3);
        expected.put(ActionType.BUILDING_TOWER, 1);
        expected.put(ActionType.CHARACTER_TOWER, 1);
        expected.put(ActionType.VENTURE_TOWER, 1);
        expected.put(ActionType.COUNCIL_PALACE, 3);
        expected.put(ActionType.MARKET, 1);

        Modifier modifier = new Modifier();
        modifier.getActionModifiers().put(ActionType.TERRITORY_TOWER, -4);
        Map<ActionType, Integer> actionModifiersToAdd = new HashMap<>();
        actionModifiersToAdd.put(ActionType.ALL, 1);
        actionModifiersToAdd.put(ActionType.COUNCIL_PALACE, 2);
        modifier.updateActionModifiers(actionModifiersToAdd);

        Map<ActionType, Integer> calculated = modifier.getActionModifiers();

        assertEquals(expected, calculated);
    }

    // ActionType == ALL + ANY_TOWER
    @Test
    public void updateActionModifiersTest5(){
        Map<ActionType, Integer> expected = new HashMap<>();
        expected.put(ActionType.HARVEST, 4);
        expected.put(ActionType.PRODUCE, 1);
        expected.put(ActionType.TERRITORY_TOWER, -5);
        expected.put(ActionType.BUILDING_TOWER, -1);
        expected.put(ActionType.CHARACTER_TOWER, -1);
        expected.put(ActionType.VENTURE_TOWER, -1);
        expected.put(ActionType.COUNCIL_PALACE, 1);
        expected.put(ActionType.MARKET, -1);

        Modifier modifier = new Modifier();
        modifier.getActionModifiers().put(ActionType.TERRITORY_TOWER, -4);
        modifier.getActionModifiers().put(ActionType.HARVEST, 3);
        Map<ActionType, Integer> actionModifiersToAdd = new HashMap<>();
        actionModifiersToAdd.put(ActionType.ALL, 1);
        actionModifiersToAdd.put(ActionType.MARKET, -2);
        actionModifiersToAdd.put(ActionType.ANY_TOWER, -2);
        modifier.updateActionModifiers(actionModifiersToAdd);

        Map<ActionType, Integer> calculated = modifier.getActionModifiers();

        assertEquals(expected, calculated);
    }

    // list != null && listToAdd != null
    @Test
    public void addListToOtherListTest1(){
        Modifier modifier = new Modifier();
        List<Reward> calculated = new ArrayList<>(Arrays.asList(new Reward(RewardType.COIN, 1), new Reward(RewardType.WOOD, 2)));
        List<Reward> listToAdd = new ArrayList<>(Arrays.asList(new Reward(RewardType.COIN, 3), new Reward(RewardType.STONE, 1)));

        List<Reward> expected = new ArrayList<>(Arrays.asList(new Reward(RewardType.COIN, 4),
                new Reward(RewardType.WOOD, 2), new Reward(RewardType.STONE, 1)));

        modifier.addListToOtherList(calculated, listToAdd);

        assertEquals(expected, calculated);
    }

    // list != null && listToAdd == null
    @Test
    public void addListToOtherListTest2(){
        Modifier modifier = new Modifier();
        List<Reward> calculated = new ArrayList<>(Arrays.asList(new Reward(RewardType.COIN, 1), new Reward(RewardType.WOOD, 2)));
        List<Reward> listToAdd = null;

        List<Reward> expected = new ArrayList<>(Arrays.asList(new Reward(RewardType.COIN, 1), new Reward(RewardType.WOOD, 2)));

        modifier.addListToOtherList(calculated, listToAdd);

        assertEquals(expected, calculated);
    }

    // Without more than one choose
    @Test
    public void updateCostDiscountDevelopmentCardTest1(){
        Modifier modifier = new Modifier();
        List<List<Reward>> territoryDiscount = modifier.getDiscounts().get(CardType.TERRITORY);
        territoryDiscount.add(new ArrayList<>(Arrays.asList(new Reward(RewardType.STONE, 1))));

        Map<CardType, List<List<Reward>>> expected = new HashMap<>();
        List<List<Reward>> expectedTerritory = new LinkedList<>();
        expectedTerritory.add(new ArrayList<>(Arrays.asList(new Reward(RewardType.STONE, 1), new Reward(RewardType.WOOD, 1))));
        expected.put(CardType.TERRITORY, expectedTerritory);
        expected.put(CardType.BUILDING, new LinkedList<>());
        expected.put(CardType.CHARACTER, new LinkedList<>());
        expected.put(CardType.VENTURE, new LinkedList<>());

        Map<CardType, List<List<Reward>>> toAdd = new HashMap<>();
        List<List<Reward>> toAddTerritory = new LinkedList<>();
        toAddTerritory.add(new ArrayList<>(Arrays.asList(new Reward(RewardType.WOOD, 1))));
        toAdd.put(CardType.TERRITORY, toAddTerritory);
        toAdd.put(CardType.BUILDING, new LinkedList<>());
        toAdd.put(CardType.CHARACTER, new LinkedList<>());
        toAdd.put(CardType.VENTURE, new LinkedList<>());

        modifier.updateCostDiscountDevelopmentCard(toAdd);
        Map<CardType, List<List<Reward>>> calculated = modifier.getDiscounts();

        assertEquals(expected, calculated);
    }

    // Without doppioni to remove
    @Test
    public void updateCostDiscountDevelopmentCardTest2(){
        Modifier modifier = new Modifier();
        List<List<Reward>> territoryDiscount = modifier.getDiscounts().get(CardType.TERRITORY);
        territoryDiscount.add(new ArrayList<>(Arrays.asList(new Reward(RewardType.STONE, 2))));

        Map<CardType, List<List<Reward>>> expected = new HashMap<>();
        List<List<Reward>> expectedTerritory = new LinkedList<>();
        expectedTerritory.add(new ArrayList<>(Arrays.asList(new Reward(RewardType.STONE, 3))));
        expectedTerritory.add(new ArrayList<>(Arrays.asList(new Reward(RewardType.STONE, 2), new Reward(RewardType.WOOD, 1))));
        expected.put(CardType.TERRITORY, expectedTerritory);
        expected.put(CardType.BUILDING, new LinkedList<>());
        expected.put(CardType.CHARACTER, new LinkedList<>());
        expected.put(CardType.VENTURE, new LinkedList<>());

        Map<CardType, List<List<Reward>>> toAdd = new HashMap<>();
        List<List<Reward>> toAddTerritory = new LinkedList<>();
        toAddTerritory.add(new ArrayList<>(Arrays.asList(new Reward(RewardType.STONE, 1))));
        toAddTerritory.add(new ArrayList<>(Arrays.asList(new Reward(RewardType.WOOD, 1))));
        toAdd.put(CardType.TERRITORY, toAddTerritory);
        toAdd.put(CardType.BUILDING, new LinkedList<>());
        toAdd.put(CardType.CHARACTER, new LinkedList<>());
        toAdd.put(CardType.VENTURE, new LinkedList<>());

        modifier.updateCostDiscountDevelopmentCard(toAdd);
        Map<CardType, List<List<Reward>>> calculated = modifier.getDiscounts();

        assertEquals(expected, calculated);
    }

    // With doppioni to remove
    @Test
    public void updateCostDiscountDevelopmentCardTest3(){
        Modifier modifier = new Modifier();
        List<List<Reward>> territoryDiscount = modifier.getDiscounts().get(CardType.TERRITORY);
        territoryDiscount.add(new ArrayList<>(Arrays.asList(new Reward(RewardType.STONE, 1))));
        territoryDiscount.add(new ArrayList<>(Arrays.asList(new Reward(RewardType.WOOD, 1))));

        Map<CardType, List<List<Reward>>> expected = new HashMap<>();
        List<List<Reward>> expectedTerritory = new LinkedList<>();
        expectedTerritory.add(new ArrayList<>(Arrays.asList(new Reward(RewardType.STONE, 2))));
        expectedTerritory.add(new ArrayList<>(Arrays.asList(new Reward(RewardType.WOOD, 1), new Reward(RewardType.STONE, 1))));
        expectedTerritory.add(new ArrayList<>(Arrays.asList(new Reward(RewardType.WOOD, 2))));
        expected.put(CardType.TERRITORY, expectedTerritory);
        expected.put(CardType.BUILDING, new LinkedList<>());
        expected.put(CardType.CHARACTER, new LinkedList<>());
        expected.put(CardType.VENTURE, new LinkedList<>());

        Map<CardType, List<List<Reward>>> toAdd = new HashMap<>();
        List<List<Reward>> toAddTerritory = new LinkedList<>();
        toAddTerritory.add(new ArrayList<>(Arrays.asList(new Reward(RewardType.STONE, 1))));
        toAddTerritory.add(new ArrayList<>(Arrays.asList(new Reward(RewardType.WOOD, 1))));
        toAdd.put(CardType.TERRITORY, toAddTerritory);
        toAdd.put(CardType.BUILDING, new LinkedList<>());
        toAdd.put(CardType.CHARACTER, new LinkedList<>());
        toAdd.put(CardType.VENTURE, new LinkedList<>());

        modifier.updateCostDiscountDevelopmentCard(toAdd);
        Map<CardType, List<List<Reward>>> calculated = modifier.getDiscounts();

        assertEquals(expected, calculated);
    }
}
