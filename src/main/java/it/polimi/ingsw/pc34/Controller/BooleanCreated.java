package it.polimi.ingsw.pc34.Controller;

/**
 * Created by Povaz on 20/06/2017.
 */
public class BooleanCreated {
    private boolean choose;
    private boolean available = false;

    public synchronized boolean get() {
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

    public synchronized void put(boolean choose) {
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
