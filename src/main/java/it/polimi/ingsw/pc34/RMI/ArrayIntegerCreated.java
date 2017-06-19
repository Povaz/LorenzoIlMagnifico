package it.polimi.ingsw.pc34.RMI;

/**
 * Created by Povaz on 19/06/2017.
 */
public class ArrayIntegerCreated {
    private int[] rewardArray;
    private boolean available = false;

    public synchronized int[] get (){
        while (available == false) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        available = false;
        notifyAll();
        return rewardArray;
    }

    public synchronized void put (int[] rewardArray ) {
        while(available == true) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.rewardArray = rewardArray;
        available = true;
        notifyAll();
    }
}
