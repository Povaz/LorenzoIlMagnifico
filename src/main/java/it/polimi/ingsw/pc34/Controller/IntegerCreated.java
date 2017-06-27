package it.polimi.ingsw.pc34.Controller;

import java.nio.channels.InterruptedByTimeoutException;

/**
 * Created by Povaz on 17/06/2017.
 */
public class IntegerCreated {
    private int choose;
    private boolean available = false;

    public synchronized int get () {
        while(available == false) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        available = false;
        notifyAll();
        return choose;
    }

    public synchronized void put (int choose) {
        while (available == true) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.choose = choose;
        available = true;
        notifyAll();
    }
}
