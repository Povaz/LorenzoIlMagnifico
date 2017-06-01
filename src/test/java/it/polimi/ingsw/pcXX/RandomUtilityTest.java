package it.polimi.ingsw.pcXX;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by trill on 31/05/2017.
 */
public class RandomUtilityTest {
    @Test
    public void randomIntArray1(){
        int start = -2;
        int end = 4;
        int[] array = RandomUtility.randomIntArray(start, end);
        System.out.println(Arrays.toString(array));

        // L'array ha la lunghezza per contenere almeno una volta gli elementi tra start e end
        if(array.length != end - start + 1)
            assertTrue(false);

        // Ogni elemento dell'array appartiene a [start; end]
        for(int i = 0; i < array.length; i++){
            if(array[i] > end || array[i] < start)
                assertTrue(false);
        }

        // Non ci possono essere occorrenze uguali nell'array
        for(int i = 0; i < array.length - 1; i++){
            for(int j = i + 1; j < array.length; j++){
                if(array[j] == array[i])
                    assertTrue(false);
            }
        }
    }

    @Test
    public void randomIntArray2(){
        int start = 2;
        int end = 5;
        int length = 3;
        int[] array = RandomUtility.randomIntArray(start, end, length);
        System.out.println(Arrays.toString(array));

        // L'array ha la lunghezza per contenere almeno una volta gli elementi tra start e end
        if(array.length != length)
            assertTrue(false);

        // Ogni elemento dell'array appartiene a [start; end]
        for(int i = 0; i < array.length; i++){
            if(array[i] > end || array[i] < start)
                assertTrue(false);
        }

        // Non ci possono essere occorrenze uguali nell'array
        for(int i = 0; i < array.length - 1; i++){
            for(int j = i + 1; j < array.length; j++){
                if(array[j] == array[i])
                    assertTrue(false);
            }
        }
    }
}