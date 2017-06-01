package it.polimi.ingsw.pcXX;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by trill on 20/05/2017.
 */
public class JSONUtilityTest extends TestCase{
    public JSONUtilityTest(String testName){
        super(testName);
    }

    public static Test suite(){
        return new TestSuite(JSONUtilityTest.class);
    }

    // With username in User.json
    public void testCheckLogin(){
        String username = "username";
        String password = "password";

        try {
            assertTrue(JSONUtility.checkLogin(username, password));
        } catch(Exception e){
            assertTrue("Exception", false);
        }
    }

    // With username in User.json
    public void testCheckRegister(){
        String username = "username";
        String password = "password";

        try {
            assertFalse(JSONUtility.checkRegister(username, password));
        } catch(Exception e){
            assertTrue("Exception", false);
        }
    }

    // Without fastRewards
    public void testJSONImportTerritoryCard1(){
        Set<Reward> earnings = new HashSet<Reward>();
        earnings.add(new Resource(ResourceType.COIN, 1));

        TerritoryCard expected = new TerritoryCard("Avamposto Commerciale", 1, null, 1, earnings);


        TerritoryCard calculated;
        try {
            calculated = (TerritoryCard) JSONUtility.getCard(1, 0, CardType.TERRITORY);
        } catch(Exception e){
            calculated = null;
        }

        assertEquals(expected, calculated);
    }

    // With fastRewards, both Points ant Rewards
    public void testJSONImportTerritoryCard2(){
        Set<Reward> fastRewards = new HashSet<Reward>();
        fastRewards.add(new Point(PointType.MILITARY_POINT, 2));
        fastRewards.add(new Resource(ResourceType.SERVANT, 1));

        Set<Reward> earnings = new HashSet<Reward>();
        earnings.add(new Point(PointType.MILITARY_POINT, 1));
        earnings.add(new Resource(ResourceType.SERVANT, 2));

        TerritoryCard expected = new TerritoryCard("Città Fortificata", 3, fastRewards, 2, earnings);

        TerritoryCard calculated;
        try {
            calculated = (TerritoryCard) JSONUtility.getCard(3, 7, CardType.TERRITORY);
        } catch(Exception e){
            calculated = null;
        }

        assertEquals(expected, calculated);
    }

    // With rewardForCard
    public void testJSONImportBuildingCard1(){
        Set<Reward> costs = new HashSet<Reward>();
        costs.add(new Resource(ResourceType.WOOD, 1));
        costs.add(new Resource(ResourceType.STONE, 3));

        Set<Reward> fastRewards = new HashSet<Reward>();
        fastRewards.add(new Point(PointType.VICTORY_POINT, 5));

        Set<Reward> earned = new HashSet<Reward>();
        earned.add(new Resource(ResourceType.COIN, 1));
        RewardForCard rewardForCard = new RewardForCard(earned, CardType.BUILDING);

        BuildingCard expected = new BuildingCard("Zecca", 1, costs, fastRewards, 5,
                null, null, null, rewardForCard);

        BuildingCard calculated;
        try {
            calculated = (BuildingCard) JSONUtility.getCard(1, 0, CardType.BUILDING);
        } catch(Exception e){
            calculated = null;
        }

        assertEquals(expected, calculated);
    }

    // With double trade
    public void testJSONImportBuildingCard2(){
        Set<Reward> costs = new HashSet<Reward>();
        costs.add(new Resource(ResourceType.WOOD, 2));
        costs.add(new Resource(ResourceType.COIN, 1));

        Set<Reward> fastRewards = new HashSet<Reward>();
        fastRewards.add(new Point(PointType.VICTORY_POINT, 3));

        Set<Reward> give1 = new HashSet<Reward>();
        give1.add(new Resource(ResourceType.WOOD, 1));
        Set<Reward> take1 = new HashSet<Reward>();
        take1.add(new Resource(ResourceType.COIN, 3));
        Set<Reward> give2 = new HashSet<Reward>();
        give2.add(new Resource(ResourceType.WOOD, 2));
        Set<Reward> take2 = new HashSet<Reward>();
        take2.add(new Resource(ResourceType.COIN, 5));
        ArrayList<Trade> trades = new ArrayList<>();
        trades.add(new Trade(give1, take1));
        trades.add(new Trade(give2, take2));

        BuildingCard expected = new BuildingCard("Falegnameria", 1, costs, fastRewards, 4,
                null, trades, null, null);

        BuildingCard calculated;
        try {
            calculated = (BuildingCard) JSONUtility.getCard(1, 4, CardType.BUILDING);
        } catch(Exception e){
            calculated = null;
        }

        assertEquals(expected, calculated);
    }

    // With earnings
    public void testJSONImportBuildingCard3(){
        Set<Reward> costs = new HashSet<Reward>();
        costs.add(new Resource(ResourceType.WOOD, 2));
        costs.add(new Resource(ResourceType.COIN, 2));
        costs.add(new Resource(ResourceType.STONE, 2));

        Set<Reward> fastRewards = new HashSet<Reward>();
        fastRewards.add(new Point(PointType.VICTORY_POINT, 8));

        Set<Reward> earnings = new HashSet<Reward>();
        earnings.add(new Point(PointType.MILITARY_POINT, 2));
        earnings.add(new Point(PointType.VICTORY_POINT, 2));

        BuildingCard expected = new BuildingCard("Fortezza", 2, costs, fastRewards, 6,
                earnings, null, null, null);

        BuildingCard calculated;
        try {
            calculated = (BuildingCard) JSONUtility.getCard(2, 7, CardType.BUILDING);
        } catch(Exception e){
            calculated = null;
        }

        assertEquals(expected, calculated);
    }

    // With earnings and councilPrivilege
    public void testJSONImportBuildingCard4(){
        Set<Reward> costs = new HashSet<Reward>();
        costs.add(new Resource(ResourceType.WOOD, 2));
        costs.add(new Resource(ResourceType.COIN, 2));
        costs.add(new Resource(ResourceType.STONE, 4));

        Set<Reward> fastRewards = new HashSet<Reward>();
        fastRewards.add(new Point(PointType.VICTORY_POINT, 9));

        Set<Reward> earnings = new HashSet<Reward>();
        earnings.add(new CouncilPrivilege(1));
        earnings.add(new Point(PointType.VICTORY_POINT, 2));

        BuildingCard expected = new BuildingCard("Castelletto", 3, costs, fastRewards, 5,
                earnings, null, null, null);

        BuildingCard calculated;
        try {
            calculated = (BuildingCard) JSONUtility.getCard(3, 3, CardType.BUILDING);
        } catch(Exception e){
            calculated = null;
        }

        assertEquals(expected, calculated);
    }

    // With actionModifier
    public void testJSONImportCharacterCard1(){
        Set<Reward> costs = new HashSet<Reward>();
        costs.add(new Resource(ResourceType.COIN, 2));

        Set<Reward> fastRewards = new HashSet<Reward>();
        fastRewards.add(new Point(PointType.MILITARY_POINT, 3));

        LinkedList<ActionModifier> actionModifiers = new LinkedList<ActionModifier>();
        actionModifiers.add(new ActionModifier(ActionType.TERRITORY_TOWER, 2));

        CharacterCard expected = new CharacterCard("Condottiero", 1, costs, fastRewards, null,
                false, null, actionModifiers, null, null);

        CharacterCard calculated;
        try {
            calculated = (CharacterCard) JSONUtility.getCard(1, 0, CardType.CHARACTER);
        } catch(Exception e){
            calculated = null;
        }

        assertEquals(expected, calculated);
    }

    // With costDiscount and double discounts
    public void testJSONImportCharacterCard2(){
        Set<Reward> costs = new HashSet<Reward>();
        costs.add(new Resource(ResourceType.COIN, 4));

        LinkedList<ActionModifier> actionModifiers = new LinkedList<ActionModifier>();
        actionModifiers.add(new ActionModifier(ActionType.BUILDING_TOWER, 2));

        Set<Reward> discounts1 = new HashSet<Reward>();
        discounts1.add(new Resource(ResourceType.WOOD, 1));
        Set<Reward> discounts2 = new HashSet<Reward>();
        discounts2.add(new Resource(ResourceType.STONE, 1));
        LinkedList<CostDiscount> costDiscounts = new LinkedList<CostDiscount>();
        costDiscounts.add(new CostDiscount(CardType.BUILDING, discounts1));
        costDiscounts.add(new CostDiscount(CardType.BUILDING, discounts2));

        CharacterCard expected = new CharacterCard("Costruttore", 1, costs, null, null,
                false, costDiscounts, actionModifiers, null, null);

        CharacterCard calculated;
        try {
            calculated = (CharacterCard) JSONUtility.getCard(1, 1, CardType.CHARACTER);
        } catch(Exception e){
            calculated = null;
        }

        assertEquals(expected, calculated);
    }

    // With noBonusTowerResource
    public void testJSONImportCharacterCard3(){
        Set<Reward> costs = new HashSet<Reward>();
        costs.add(new Resource(ResourceType.COIN, 2));

        Set<Reward> fastRewards = new HashSet<Reward>();
        fastRewards.add(new Point(PointType.FAITH_POINT, 4));

        CharacterCard expected = new CharacterCard("Predicatore", 1, costs, fastRewards, null,
                true, null, null, null, null);

        CharacterCard calculated;
        try {
            calculated = (CharacterCard) JSONUtility.getCard(1, 6, CardType.CHARACTER);
        } catch(Exception e){
            calculated = null;
        }

        assertEquals(expected, calculated);
    }

    // With action(with discount)
    public void testJSONImportCharacterCard4(){
        Set<Reward> costs = new HashSet<Reward>();
        costs.add(new Resource(ResourceType.COIN, 4));

        Set<Reward> discounts = new HashSet<Reward>();
        discounts.add(new Resource(ResourceType.STONE, 1));
        discounts.add(new Resource(ResourceType.WOOD, 1));
        LinkedList<GhostFamilyMember> actions = new LinkedList<GhostFamilyMember>();
        actions.add(new GhostFamilyMember(ActionType.BUILDING_TOWER, 6, discounts));

        CharacterCard expected = new CharacterCard("Architetto", 2, costs, null, actions,
                false, null, null, null, null);

        CharacterCard calculated;
        try {
            calculated = (CharacterCard) JSONUtility.getCard(2, 1, CardType.CHARACTER);
        } catch(Exception e){
            calculated = null;
        }

        assertEquals(expected, calculated);
    }

    // With rewardForCard
    public void testJSONImportCharacterCard5(){
        Set<Reward> costs = new HashSet<Reward>();
        costs.add(new Resource(ResourceType.COIN, 7));

        Set<Reward> earned = new HashSet<Reward>();
        earned.add(new Point(PointType.VICTORY_POINT, 2));
        RewardForCard rewardForCard = new RewardForCard(earned, CardType.CHARACTER);

        CharacterCard expected = new CharacterCard("Cortigiana", 3, costs, null, null,
                false, null, null, null, rewardForCard);

        CharacterCard calculated;
        try {
            calculated = (CharacterCard) JSONUtility.getCard(3, 2, CardType.CHARACTER);
        } catch(Exception e){
            calculated = null;
        }

        assertEquals(expected, calculated);
    }

    // With rewardForReward
    public void testJSONImportCharacterCard6(){
        Set<Reward> costs = new HashSet<Reward>();
        costs.add(new Resource(ResourceType.COIN, 5));

        Set<Reward> earned = new HashSet<Reward>();
        earned.add(new Point(PointType.VICTORY_POINT, 1));
        RewardForReward rewardForReward = new RewardForReward(earned, new Point(PointType.MILITARY_POINT, 2));

        CharacterCard expected = new CharacterCard("Generale", 3, costs, null, null,
                false, null, null, rewardForReward, null);

        CharacterCard calculated;
        try {
            calculated = (CharacterCard) JSONUtility.getCard(3, 6, CardType.CHARACTER);
        } catch(Exception e){
            calculated = null;
        }

        assertEquals(expected, calculated);
    }

    // With action(without discount) and fastRewards
    public void testJSONImportCharacterCard7(){
        Set<Reward> costs = new HashSet<Reward>();
        costs.add(new Resource(ResourceType.COIN, 6));

        Set<Reward> fastRewards = new HashSet<Reward>();
        fastRewards.add(new CouncilPrivilege(1));

        LinkedList<GhostFamilyMember> actions = new LinkedList<GhostFamilyMember>();
        actions.add(new GhostFamilyMember(ActionType.ANY_TOWER, 7, null));

        CharacterCard expected = new CharacterCard("Ambasciatore", 3, costs, fastRewards, actions,
                false, null, null, null, null);

        CharacterCard calculated;
        try {
            calculated = (CharacterCard) JSONUtility.getCard(3, 7, CardType.CHARACTER);
        } catch(Exception e){
            calculated = null;
        }

        assertEquals(expected, calculated);
    }

    // With action(with discount)
    public void testJSONImportVentureCard1(){
        Set<Reward> fastRewards = new HashSet<Reward>();
        fastRewards.add(new Resource(ResourceType.COIN, 3));

        Point militaryPointNeeded = new Point(PointType.MILITARY_POINT, 3);

        Point militaryPointPrice = new Point(PointType.MILITARY_POINT, 2);

        Point victoryPointEarned = new Point(PointType.VICTORY_POINT, 5);

        VentureCard expected = new VentureCard("Campagna Militare", 1, null, fastRewards, null,
                militaryPointNeeded, militaryPointPrice, victoryPointEarned);

        VentureCard calculated;
        try {
            calculated = (VentureCard) JSONUtility.getCard(1, 4, CardType.VENTURE);
        } catch(Exception e){
            calculated = null;
        }

        assertEquals(expected, calculated);
    }

    // With action(with discount)
    public void testJSONImportVentureCard2(){
        Set<Reward> costs = new HashSet<Reward>();
        costs.add(new Resource(ResourceType.COIN, 3));
        costs.add(new Resource(ResourceType.SERVANT, 2));

        Point victoryPointEarned = new Point(PointType.VICTORY_POINT, 5);

        LinkedList<GhostFamilyMember> actions = new LinkedList<GhostFamilyMember>();
        actions.add(new GhostFamilyMember(ActionType.HARVEST, 4, null));

        VentureCard expected = new VentureCard("Scavare Canalizzazioni", 2, costs, null, actions,
                null, null, victoryPointEarned);

        VentureCard calculated;
        try {
            calculated = (VentureCard) JSONUtility.getCard(2, 4, CardType.VENTURE);
        } catch(Exception e){
            calculated = null;
        }

        assertEquals(expected, calculated);
    }
}
