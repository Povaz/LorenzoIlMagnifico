package it.polimi.ingsw.pcXX;

import java.util.Random;

/**
 * Created by trill on 31/05/2017.
 */
public class RandomUtility{
    public static int randomInt(int min, int max){
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }

    public static int[] randomIntArray(int start, int end){
        Random rand = new Random();
        int length = end - start + 1;
        int[] array = new int[length];
        for(int i = 0; i < array.length; i++){
            array[i] = start + i;
        }
        for(int iteration = 0; iteration < 10; iteration++){
            for(int i = 0; i < array.length; i++){
                int randInt = rand.nextInt(length);
                int temp = array[i];
                array[i] = array[randInt];
                array[randInt] = temp;
            }
        }
        return array;
    }

    public static int[] randomIntArray(int start, int end, int lenght){
        if(start > end){
            throw new IllegalArgumentException("start > end");
        }
        if(lenght > end - start + 1){
            throw new IllegalArgumentException("lenght > end - start + 1");
        }
        Random rand = new Random();
        int[] array = new int[lenght];
        for(int i = 0; i < array.length;){
            boolean repeated = false;
            array[i] = rand.nextInt(end - start + 1) + start;
            for(int j = 0; j < i && !repeated; j++){
                if(array[j] == array[i])
                    repeated = true;
            }
            if(!repeated){
                i++;
            }
        }
        return array;
    }
}
