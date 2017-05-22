package it.polimi.ingsw.pcXX;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

/**
 * Created by trill on 22/05/2017.
 */
public class OrderTest extends TestCase{
    public OrderTest(String testName){
        super(testName);
    }

    public static Test suite(){
        return new TestSuite(OrderTest.class);
    }

    public void testRemoveBottomDuplicates(){
        ArrayList<PlayerColor> expected = new ArrayList<PlayerColor>();
        expected.add(PlayerColor.BLUE);
        expected.add(PlayerColor.GREEN);
        expected.add(PlayerColor.YELLOW);
        expected.add(PlayerColor.PURPLE);
        expected.add(PlayerColor.RED);

        ArrayList<PlayerColor> calculated = new ArrayList<PlayerColor>();
        calculated.add(PlayerColor.BLUE);
        calculated.add(PlayerColor.GREEN);
        calculated.add(PlayerColor.BLUE);
        calculated.add(PlayerColor.YELLOW);
        calculated.add(PlayerColor.PURPLE);
        calculated.add(PlayerColor.YELLOW);
        calculated.add(PlayerColor.PURPLE);
        calculated.add(PlayerColor.RED);
        //Order.removeBottomDuplicates(calculated);

        assertEquals(expected, calculated);
    }

    public void testCalculateRealOrder(){
        ArrayList<PlayerColor> expected = new ArrayList<PlayerColor>();
        expected.add(PlayerColor.BLUE);
        expected.add(PlayerColor.GREEN);
        expected.add(PlayerColor.YELLOW);
        expected.add(PlayerColor.PURPLE);
        expected.add(PlayerColor.RED);
        expected.add(PlayerColor.BLUE);
        expected.add(PlayerColor.GREEN);
        expected.add(PlayerColor.YELLOW);
        expected.add(PlayerColor.PURPLE);
        expected.add(PlayerColor.RED);
        expected.add(PlayerColor.BLUE);
        expected.add(PlayerColor.GREEN);
        expected.add(PlayerColor.YELLOW);
        expected.add(PlayerColor.PURPLE);
        expected.add(PlayerColor.RED);
        expected.add(PlayerColor.BLUE);
        expected.add(PlayerColor.GREEN);
        expected.add(PlayerColor.YELLOW);
        expected.add(PlayerColor.PURPLE);
        expected.add(PlayerColor.RED);

        ArrayList<PlayerColor> calculated = new ArrayList<PlayerColor>();
        calculated.add(PlayerColor.BLUE);
        calculated.add(PlayerColor.GREEN);
        calculated.add(PlayerColor.YELLOW);
        calculated.add(PlayerColor.PURPLE);
        calculated.add(PlayerColor.RED);
        //calculated = Order.calculateRealOrder(calculated);

        assertEquals(expected, calculated);
    }
}
