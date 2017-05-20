package it.polimi.ingsw.pcXX;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashSet;
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
        give1.add(new Resource(ResourceType.WOOD, 2));
        Set<Reward> take2 = new HashSet<Reward>();
        take1.add(new Resource(ResourceType.COIN, 5));
        Set<Trade> trades = new HashSet<Trade>();
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
}
