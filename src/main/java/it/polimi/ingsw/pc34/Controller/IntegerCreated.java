package it.polimi.ingsw.pc34.Controller;

import java.nio.channels.InterruptedByTimeoutException;

/**
 * Created by Povaz on 17/06/2017.
 */
public class IntegerCreated {
    private Integer choose;
    private boolean available = false;

    public synchronized Integer get () {
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

    public synchronized void put (Integer choose) {
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
