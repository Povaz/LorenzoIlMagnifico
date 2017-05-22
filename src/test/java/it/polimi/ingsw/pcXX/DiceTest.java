package it.polimi.ingsw.pcXX;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;

/**
 * Created by trill on 22/05/2017.
 */
public class DiceTest extends TestCase{
    public DiceTest(String testName){
        super(testName);
    }

    public static Test suite(){
        return new TestSuite(DiceTest.class);
    }

    public void testThrowDice(){
        int[] occorrenze = new int[6];

        Dice dice = new Dice(FamilyColor.ORANGE);
        assertFalse("Minore di 1", dice.getValue() < 1);
        assertFalse("Maggiore di 6", dice.getValue() > 6);

        for(int i = 0; i < 10000; i++){
            dice.throwDice();
            assertFalse("Minore di 1", dice.getValue() < 1);
            assertFalse("Maggiore di 6", dice.getValue() > 6);
            occorrenze[dice.getValue() - 1]++;
        }

        System.out.println(Arrays.toString(occorrenze));
        assertTrue(true);
    }
}
