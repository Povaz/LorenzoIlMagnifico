package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Povaz on 02/07/2017.
 */
public class FamilyMemberTest extends TestCase {
    private Player player;
    private FamilyColor familyColor;
    private ActionType actionType;
    private int value;
    private Set<Reward> discount;
    public Reward reward;

    private FamilyMember familyMember1;
    private FamilyMember familyMember2;

    public FamilyMemberTest(String name) {
        super(name);
    }

    public void testCostructor(){
        assertFalse(familyMember1.isUsed());
        assertFalse(familyMember2.isUsed());

        assertEquals(0, familyMember1.getValue());
        assertEquals(7, familyMember2.getValue());

        assertEquals(new Reward(RewardType.SERVANT, 0), familyMember1.getServantUsed());
        assertEquals(new Reward(RewardType.SERVANT, 0), familyMember1.getServantUsed());

        assertFalse(familyMember1.isGhost());
        assertTrue(familyMember2.isGhost());

        assertEquals(player, familyMember1.getPlayer());
        assertEquals(null, familyMember2.getPlayer());

        assertEquals(FamilyColor.BLACK, familyMember1.getColor());
        assertEquals(FamilyColor.NEUTRAL, familyMember2.getColor());

        assertEquals(null, familyMember1.getAction());
        assertEquals(ActionType.ANY_TOWER, familyMember2.getAction());

        assertTrue(familyMember2.getDiscounts().contains(new Reward(RewardType.COIN, 5)));
    }

    public void samePlayerTest() {
        FamilyMember familyMember3 = new FamilyMember(player, familyColor);
        assertTrue(familyMember1.samePlayer(familyMember3));
    }

    public void setUp () {
        player = new Player("Erick", ConnectionType.RMI, PlayerColor.RED);
        familyColor = FamilyColor.BLACK;
        actionType = ActionType.ANY_TOWER;
        value = 7;
        reward = new Reward(RewardType.COIN, 5);
        discount = new HashSet<>();
        discount.add(reward);

        familyMember1 = new FamilyMember(player, familyColor);
        familyMember2 = new FamilyMember(actionType, value, discount);
    }

    public static TestSuite suite() {
        TestSuite testSuite = new TestSuite();
        testSuite.addTest(new FamilyMemberTest("testCostructor"));
        testSuite.addTest(new FamilyMemberTest("samePlayerTest"));
        return testSuite;
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
