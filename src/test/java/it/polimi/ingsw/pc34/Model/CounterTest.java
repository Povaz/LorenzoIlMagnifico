package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Povaz on 02/07/2017.
 */
public class CounterTest extends TestCase {
    private Counter emptyCounter;
    private Counter orderCounter1;
    private Counter orderCounter2;
    private Counter orderCounter3;
    private Counter orderCounter4;
    private Counter orderCounter5;
    private Counter copiedCounter;

    private Reward coin;
    private Reward wood;
    private Set<Reward> coinSet;
    private List<Reward> woodSet;


    public CounterTest(String name) {
        super(name);
    }

    @Test
    public void counterProducerTest() {
        //Check EmptyCounter
        assertEquals(new Reward(RewardType.COIN, 0), emptyCounter.getCoin());
        assertEquals(new Reward(RewardType.WOOD, 0), emptyCounter.getWood());
        assertEquals(new Reward(RewardType.STONE, 0), emptyCounter.getStone());
        assertEquals(new Reward(RewardType.SERVANT, 0), emptyCounter.getServant());
        assertEquals(new Reward(RewardType.MILITARY_POINT, 0), emptyCounter.getMilitaryPoint());
        assertEquals(new Reward(RewardType.FAITH_POINT, 0), emptyCounter.getFaithPoint());
        assertEquals(new Reward(RewardType.VICTORY_POINT, 0), emptyCounter.getVictoryPoint());

        assertEquals(new Reward(RewardType.COIN, 0), copiedCounter.getCoin());
        assertEquals(new Reward(RewardType.WOOD, 0), copiedCounter.getWood());
        assertEquals(new Reward(RewardType.STONE, 0), copiedCounter.getStone());
        assertEquals(new Reward(RewardType.SERVANT, 0), copiedCounter.getServant());
        assertEquals(new Reward(RewardType.MILITARY_POINT, 0), copiedCounter.getMilitaryPoint());
        assertEquals(new Reward(RewardType.FAITH_POINT, 0), copiedCounter.getFaithPoint());
        assertEquals(new Reward(RewardType.VICTORY_POINT, 0), copiedCounter.getVictoryPoint());

        assertEquals(new Reward(RewardType.COIN, 5), orderCounter1.getCoin());
        assertEquals(new Reward(RewardType.WOOD, 2), orderCounter1.getWood());
        assertEquals(new Reward(RewardType.STONE, 2), orderCounter1.getStone());
        assertEquals(new Reward(RewardType.SERVANT, 3), orderCounter1.getServant());
        assertEquals(new Reward(RewardType.MILITARY_POINT, 0), orderCounter1.getMilitaryPoint());
        assertEquals(new Reward(RewardType.FAITH_POINT, 0), orderCounter1.getFaithPoint());
        assertEquals(new Reward(RewardType.VICTORY_POINT, 0), orderCounter1.getVictoryPoint());

        assertEquals(new Reward(RewardType.COIN, 6), orderCounter2.getCoin());

        assertEquals(new Reward(RewardType.COIN, 7), orderCounter3.getCoin());

        assertEquals(new Reward(RewardType.COIN, 8), orderCounter4.getCoin());

        assertEquals(new Reward(RewardType.COIN, 9), orderCounter5.getCoin());
    }

    public void subtractSumTest() {
        orderCounter1.subtract(coin);
        assertEquals(new Reward(RewardType.COIN, 0), orderCounter1.getCoin());

        orderCounter1.sum(coin);
        assertEquals(new Reward(RewardType.COIN, 5), orderCounter1.getCoin());

        Set<Reward> coinTest = new HashSet<>();
        coinTest.add(coin);

        orderCounter1.subtract(coinTest);
        assertEquals(new Reward(RewardType.COIN, 0), orderCounter1.getCoin());

        orderCounter1.sum(coin);
        assertEquals(new Reward(RewardType.COIN, 5), orderCounter1.getCoin());
    }

    public void sumSubtractWithLoseDiscountTest() throws TooMuchTimeException {
        orderCounter1.sumWithLose(coinSet, woodSet);

        assertEquals(new Reward (RewardType.COIN, 10), orderCounter1.getCoin());
        assertEquals(new Reward (RewardType.WOOD, 2), orderCounter1.getWood());

        orderCounter1.subtractWithDiscount(coinSet, woodSet);

        assertEquals(new Reward (RewardType.COIN, 5), orderCounter1.getCoin());
        assertEquals(new Reward (RewardType.WOOD, 2), orderCounter1.getWood());
    }

    public void removeRewardFromSetTest() {
        Set<Reward> set = new HashSet<>();
        set.add(new Reward(RewardType.SERVANT, 8));
        set.add(new Reward(RewardType.COIN, 7));
        set.add(new Reward(RewardType.WOOD, 8));

        List<Reward> list = new ArrayList<>();
        list.add(new Reward(RewardType.COIN, 5));
        list.add(new Reward(RewardType.WOOD, 8));

        Set<Reward> remained = emptyCounter.removeRewardFromSet(set, list);

        Reward coinTest = new Reward (RewardType.COIN, 2);
        Reward servantTest = new Reward(RewardType.SERVANT, 8);
        Reward woodTest = new Reward(RewardType.WOOD, 0);
        assertTrue(remained.contains(coinTest));
        assertTrue(remained.contains(servantTest));
        assertTrue(remained.contains(woodTest));
    }

    public void sumSubtractCounterTest() {
        orderCounter4.subtract(orderCounter3);
        assertEquals(new Reward (RewardType.COIN, 1), orderCounter4.getCoin());
        assertEquals(new Reward (RewardType.WOOD, 0), orderCounter4.getWood());
        assertEquals(new Reward (RewardType.STONE, 0), orderCounter4.getStone());
        assertEquals(new Reward (RewardType.SERVANT, 0), orderCounter4.getServant());
        assertEquals(new Reward (RewardType.MILITARY_POINT, 0), orderCounter4.getMilitaryPoint());
        assertEquals(new Reward (RewardType.FAITH_POINT, 0), orderCounter4.getFaithPoint());
        assertEquals(new Reward (RewardType.VICTORY_POINT, 0), orderCounter4.getVictoryPoint());

        orderCounter4.sum(orderCounter3);
        assertEquals(new Reward (RewardType.COIN, 8), orderCounter4.getCoin());
        assertEquals(new Reward (RewardType.WOOD, 2), orderCounter4.getWood());
        assertEquals(new Reward (RewardType.STONE, 2), orderCounter4.getStone());
        assertEquals(new Reward (RewardType.SERVANT, 3), orderCounter4.getServant());
        assertEquals(new Reward (RewardType.MILITARY_POINT, 0), orderCounter4.getMilitaryPoint());
        assertEquals(new Reward (RewardType.FAITH_POINT, 0), orderCounter4.getFaithPoint());
        assertEquals(new Reward (RewardType.VICTORY_POINT, 0), orderCounter4.getVictoryPoint());
    }

    public void checkTest() {
        orderCounter1.subtract(orderCounter3);
        assertFalse(orderCounter1.check());

        orderCounter1.sum(orderCounter3);
        assertTrue(orderCounter1.check());
    }

    public void roundTest() {
        emptyCounter.sum(new Reward(RewardType.COIN, -1));
        emptyCounter.sum(new Reward(RewardType.WOOD, -1));
        emptyCounter.sum(new Reward(RewardType.STONE, -1));
        emptyCounter.sum(new Reward(RewardType.SERVANT, -1));
        emptyCounter.sum(new Reward(RewardType.MILITARY_POINT, 35));
        emptyCounter.sum(new Reward(RewardType.FAITH_POINT, 35));
        emptyCounter.sum(new Reward(RewardType.VICTORY_POINT, -1));

        emptyCounter.round();

        assertEquals(new Reward (RewardType.COIN, 0), emptyCounter.getCoin());
        assertEquals(new Reward (RewardType.WOOD, 0), emptyCounter.getWood());
        assertEquals(new Reward (RewardType.STONE, 0), emptyCounter.getStone());
        assertEquals(new Reward (RewardType.SERVANT, 0), emptyCounter.getServant());
        assertEquals(new Reward (RewardType.MILITARY_POINT, 25), emptyCounter.getMilitaryPoint());
        assertEquals(new Reward (RewardType.FAITH_POINT, 30), emptyCounter.getFaithPoint());
        assertEquals(new Reward (RewardType.VICTORY_POINT, 0), emptyCounter.getVictoryPoint());
    }

    public void giveSameRewardTest(){
        assertEquals(orderCounter5.getCoin(), orderCounter5.giveSameReward(new Reward(RewardType.COIN, 0)));
        assertEquals(orderCounter5.getWood(), orderCounter5.giveSameReward(new Reward(RewardType.WOOD, 0)));
        assertEquals(orderCounter5.getStone(), orderCounter5.giveSameReward(new Reward(RewardType.STONE, 0)));
        assertEquals(orderCounter5.getServant(), orderCounter5.giveSameReward(new Reward(RewardType.SERVANT, 0)));
        assertEquals(orderCounter5.getMilitaryPoint(), orderCounter5.giveSameReward(new Reward(RewardType.MILITARY_POINT, 0)));
        assertEquals(orderCounter5.getFaithPoint(), orderCounter5.giveSameReward(new Reward(RewardType.FAITH_POINT, 0)));
        assertEquals(orderCounter5.getVictoryPoint(), orderCounter5.giveSameReward(new Reward(RewardType.VICTORY_POINT, 0)));
        assertEquals(null, orderCounter5.giveSameReward(new Reward(RewardType.COUNCIL_PRIVILEGE, 0)));
    }
    public void setUp () {
        emptyCounter = new Counter();
        copiedCounter = new Counter (emptyCounter);
        orderCounter1 = new Counter(1);
        orderCounter2 = new Counter(2);
        orderCounter3 = new Counter(3);
        orderCounter4 = new Counter(4);
        orderCounter5 = new Counter(5);

        coin = new Reward(RewardType.COIN, 5);
        wood = new Reward(RewardType.WOOD, 1);

        coinSet = new HashSet<>();
        woodSet = new ArrayList<>();
        coinSet.add(coin);
        woodSet.add(wood);
    }

    public static TestSuite suite() {
        TestSuite testSuite = new TestSuite();
        testSuite.addTest(new CounterTest("counterProducerTest"));
        testSuite.addTest(new CounterTest("sumSubtractWithLoseDiscountTest"));
        testSuite.addTest(new CounterTest("removeRewardFromSetTest"));
        testSuite.addTest(new CounterTest("sumSubtractCounterTest"));
        testSuite.addTest(new CounterTest("checkTest"));
        testSuite.addTest(new CounterTest("roundTest"));
        testSuite.addTest(new CounterTest("giveSameRewardTest"));
        return testSuite;
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
