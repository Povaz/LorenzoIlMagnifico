package it.polimi.ingsw.pc34.Model;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

/**
 * Created by Povaz on 02/07/2017.
 */
public class CardTypeTest extends TestCase {
    private ArrayList<CardType> testCard;

    public CardTypeTest (String name) {
        super(name);
    }

    public void sameTypeTest() {
        assertTrue(testCard.get(0).equals(CardType.TERRITORY));
        assertTrue(testCard.get(1).equals(CardType.BUILDING));
        assertTrue(testCard.get(2).equals(CardType.CHARACTER));
        assertTrue(testCard.get(3).equals(CardType.VENTURE));

        assertFalse(testCard.get(0).equals(CardType.VENTURE));
        assertFalse(testCard.get(1).equals(CardType.CHARACTER));
        assertFalse(testCard.get(2).equals(CardType.BUILDING));
        assertFalse(testCard.get(3).equals(CardType.TERRITORY));
    }

    public void setUp() {
        testCard = new ArrayList<>();
        testCard.add(CardType.TERRITORY);
        testCard.add(CardType.BUILDING);
        testCard.add(CardType.CHARACTER);
        testCard.add(CardType.VENTURE);
    }

    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new CardTypeTest("sameTypeTest"));
        return suite;
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
