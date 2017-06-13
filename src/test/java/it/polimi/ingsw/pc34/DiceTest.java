package it.polimi.ingsw.pc34;

import static org.junit.Assert.*;

import it.polimi.ingsw.pc34.Model.Dice;
import it.polimi.ingsw.pc34.Model.FamilyColor;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by trill on 22/05/2017.
 */
public class DiceTest{

	@Test
    public void testThrowDice(){
        int[] occorrenze = new int[6];

        Dice dice = new Dice(FamilyColor.ORANGE);
        assertFalse("Dado minore di 1", dice.getValue() < 1);
        assertFalse("Dado maggiore di 6", dice.getValue() > 6);

        for(int i = 0; i < 100000; i++){
            dice.throwDice();
            assertFalse("Dado minore di 1", dice.getValue() < 1);
            assertFalse("Dado maggiore di 6", dice.getValue() > 6);
            occorrenze[dice.getValue() - 1]++;
        }

        System.out.println(Arrays.toString(occorrenze));
        assertTrue(true);
    }
}