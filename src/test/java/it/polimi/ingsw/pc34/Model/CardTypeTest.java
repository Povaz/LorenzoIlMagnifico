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
        assertEquals(CardType.TERRITORY, testCard.get(0));
        assertEquals(CardType.BUILDING, testCard.get(1));
        assertEquals(CardType.CHARACTER, testCard.get(2));
        assertEquals(CardType.VENTURE, testCard.get(3));
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
