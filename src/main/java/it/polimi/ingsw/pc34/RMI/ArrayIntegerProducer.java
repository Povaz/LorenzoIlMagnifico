package it.polimi.ingsw.pc34.RMI;

/**
 * Created by Povaz on 19/06/2017.
 */
public class ArrayIntegerProducer extends Thread{
    private ArrayIntegerCreated arrayIntegerCreated;
    private int[] rewardArray;

    public ArrayIntegerProducer (ArrayIntegerCreated arrayIntegerCreated) {
        this.arrayIntegerCreated = arrayIntegerCreated;
    }

    public void setRewardArray (int[] rewardArray) {
        this.rewardArray = rewardArray;
    }

    public void run () {
        arrayIntegerCreated.put(rewardArray);
    }
}
