package it.polimi.ingsw.pc34.Model;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by Povaz on 14/06/2017.
 */
public class BuildingCardTest extends TestCase {

    public BuildingCardTest(String name) {
        super(name);
    }

    public void setUp () {

    }

    public static TestSuite suite() {
        TestSuite testSuite = new TestSuite();

        return testSuite;
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
