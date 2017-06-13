package it.polimi.ingsw.pcXX;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.*;

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
        Set<Reward> earnings = new HashSet<>();
        earnings.add(new Reward(RewardType.COIN, 1));

        TerritoryCard expected = new TerritoryCard("Commercial Hub", 1, null, 1, earnings);


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
        Set<Reward> fastRewards = new HashSet<>();
        fastRewards.add(new Reward(RewardType.MILITARY_POINT, 2));
        fastRewards.add(new Reward(RewardType.SERVANT, 1));

        Set<Reward> earnings = new HashSet<>();
        earnings.add(new Reward(RewardType.MILITARY_POINT, 1));
        earnings.add(new Reward(RewardType.SERVANT, 2));

        TerritoryCard expected = new TerritoryCard("Fortified Town", 3, fastRewards, 2, earnings);

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
        Set<Reward> costs = new HashSet<>();
        costs.add(new Reward(RewardType.WOOD, 1));
        costs.add(new Reward(RewardType.STONE, 3));

        Set<Reward> fastRewards = new HashSet<>();
        fastRewards.add(new Reward(RewardType.VICTORY_POINT, 5));

        Set<Reward> earned = new HashSet<>();
        earned.add(new Reward(RewardType.COIN, 1));
        RewardForCard rewardForCard = new RewardForCard(earned, CardType.BUILDING);

        BuildingCard expected = new BuildingCard("Mint", 1, costs, fastRewards, 5,
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
        Set<Reward> costs = new HashSet<>();
        costs.add(new Reward(RewardType.WOOD, 2));
        costs.add(new Reward(RewardType.COIN, 1));

        Set<Reward> fastRewards = new HashSet<>();
        fastRewards.add(new Reward(RewardType.VICTORY_POINT, 3));

        Set<Reward> give1 = new HashSet<>();
        give1.add(new Reward(RewardType.WOOD, 1));
        Set<Reward> take1 = new HashSet<>();
        take1.add(new Reward(RewardType.COIN, 3));
        Set<Reward> give2 = new HashSet<>();
        give2.add(new Reward(RewardType.WOOD, 2));
        Set<Reward> take2 = new HashSet<>();
        take2.add(new Reward(RewardType.COIN, 5));
        List<Trade> trades = new ArrayList<>();
        trades.add(new Trade(give1, take1));
        trades.add(new Trade(give2, take2));

        BuildingCard expected = new BuildingCard("Carpenter's Shop", 1, costs, fastRewards, 4,
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
        Set<Reward> costs = new HashSet<>();
        costs.add(new Reward(RewardType.WOOD, 2));
        costs.add(new Reward(RewardType.COIN, 2));
        costs.add(new Reward(RewardType.STONE, 2));

        Set<Reward> fastRewards = new HashSet<>();
        fastRewards.add(new Reward(RewardType.VICTORY_POINT, 8));

        Set<Reward> earnings = new HashSet<>();
        earnings.add(new Reward(RewardType.MILITARY_POINT, 2));
        earnings.add(new Reward(RewardType.VICTORY_POINT, 2));

        BuildingCard expected = new BuildingCard("Stronghold", 2, costs, fastRewards, 6,
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
        Set<Reward> costs = new HashSet<>();
        costs.add(new Reward(RewardType.WOOD, 2));
        costs.add(new Reward(RewardType.COIN, 2));
        costs.add(new Reward(RewardType.STONE, 4));

        Set<Reward> fastRewards = new HashSet<>();
        fastRewards.add(new Reward(RewardType.VICTORY_POINT, 9));

        Set<Reward> earnings = new HashSet<>();
        earnings.add(new Reward(RewardType.COUNCIL_PRIVILEGE, 1));
        earnings.add(new Reward(RewardType.VICTORY_POINT, 2));

        BuildingCard expected = new BuildingCard("Fortress", 3, costs, fastRewards, 5,
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
        Set<Reward> costs = new HashSet<>();
        costs.add(new Reward(RewardType.COIN, 2));

        Set<Reward> fastRewards = new HashSet<>();
        fastRewards.add(new Reward(RewardType.MILITARY_POINT, 3));

        Map<ActionType, Integer> actionModifiers = new HashMap<>();
        actionModifiers.put(ActionType.TERRITORY_TOWER, 2);

        CharacterCard expected = new CharacterCard("Warlord", 1, costs, fastRewards, null,
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
        Set<Reward> costs = new HashSet<>();
        costs.add(new Reward(RewardType.COIN, 4));

        Map<ActionType, Integer> actionModifiers = new HashMap<>();
        actionModifiers.put(ActionType.BUILDING_TOWER, 2);

        List<Reward> discounts1 = new ArrayList<>();
        discounts1.add(new Reward(RewardType.WOOD, 1));
        List<Reward> discounts2 = new ArrayList<>();
        discounts2.add(new Reward(RewardType.STONE, 1));
        Map<CardType, List<List<Reward>>> costDiscounts = new HashMap<>();
        costDiscounts.put(CardType.TERRITORY, new ArrayList<>());
        costDiscounts.put(CardType.BUILDING, new ArrayList<>());
        costDiscounts.put(CardType.CHARACTER, new ArrayList<>());
        costDiscounts.put(CardType.VENTURE, new ArrayList<>());
        costDiscounts.get(CardType.BUILDING).add(discounts1);
        costDiscounts.get(CardType.BUILDING).add(discounts2);

        CharacterCard expected = new CharacterCard("Stonemason", 1, costs, null, null,
                false, costDiscounts, actionModifiers, null, null);

        CharacterCard calculated;
        try {
            calculated = (CharacterCard) JSONUtility.getCard(1, 1, CardType.CHARACTER);
        } catch(Exception e){
            calculated = null;
        }
        System.out.println(expected);
        assertEquals(expected, calculated);
    }

    // With noBonusTowerResource
    public void testJSONImportCharacterCard3(){
        Set<Reward> costs = new HashSet<>();
        costs.add(new Reward(RewardType.COIN, 2));

        Set<Reward> fastRewards = new HashSet<>();
        fastRewards.add(new Reward(RewardType.FAITH_POINT, 4));

        CharacterCard expected = new CharacterCard("Preacher", 1, costs, fastRewards, null,
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
        Set<Reward> costs = new HashSet<>();
        costs.add(new Reward(RewardType.COIN, 4));

        Set<Reward> discounts = new HashSet<>();
        discounts.add(new Reward(RewardType.STONE, 1));
        discounts.add(new Reward(RewardType.WOOD, 1));
        List<FamilyMember> actions = new LinkedList<>();
        actions.add(new FamilyMember(ActionType.BUILDING_TOWER, 6, discounts));

        CharacterCard expected = new CharacterCard("Architect", 2, costs, null, actions,
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
        Set<Reward> costs = new HashSet<>();
        costs.add(new Reward(RewardType.COIN, 7));

        Set<Reward> earned = new HashSet<>();
        earned.add(new Reward(RewardType.VICTORY_POINT, 2));
        RewardForCard rewardForCard = new RewardForCard(earned, CardType.CHARACTER);

        CharacterCard expected = new CharacterCard("Paramour", 3, costs, null, null,
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
        Set<Reward> costs = new HashSet<>();
        costs.add(new Reward(RewardType.COIN, 5));

        Set<Reward> earned = new HashSet<>();
        earned.add(new Reward(RewardType.VICTORY_POINT, 1));
        RewardForReward rewardForReward = new RewardForReward(earned, new Reward(RewardType.MILITARY_POINT, 2));

        CharacterCard expected = new CharacterCard("General", 3, costs, null, null,
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
        Set<Reward> costs = new HashSet<>();
        costs.add(new Reward(RewardType.COIN, 6));

        Set<Reward> fastRewards = new HashSet<>();
        fastRewards.add(new Reward(RewardType.COUNCIL_PRIVILEGE, 1));

        List<FamilyMember> actions = new LinkedList<>();
        actions.add(new FamilyMember(ActionType.ANY_TOWER, 7, null));

        CharacterCard expected = new CharacterCard("Ambassador", 3, costs, fastRewards, actions,
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
        Set<Reward> fastRewards = new HashSet<>();
        fastRewards.add(new Reward(RewardType.COIN, 3));

        Reward militaryPointNeeded = new Reward(RewardType.MILITARY_POINT, 3);

        Reward militaryPointPrice = new Reward(RewardType.MILITARY_POINT, 2);

        Reward victoryPointEarned = new Reward(RewardType.VICTORY_POINT, 5);

        VentureCard expected = new VentureCard("Military Campaign", 1, null, fastRewards, null,
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
        Set<Reward> costs = new HashSet<>();
        costs.add(new Reward(RewardType.COIN, 3));
        costs.add(new Reward(RewardType.SERVANT, 2));

        Reward victoryPointEarned = new Reward(RewardType.VICTORY_POINT, 5);

        List<FamilyMember> actions = new LinkedList<>();
        actions.add(new FamilyMember(ActionType.HARVEST, 4, null));

        VentureCard expected = new VentureCard("Improving the Canals", 2, costs, null, actions,
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
