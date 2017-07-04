package it.polimi.ingsw.pc34.RMI;

/**
 * Created by Povaz on 04/07/2017.
 */
public class SynchronizedString {
    private String message;
    private boolean available;

    public synchronized String get() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        available = false;
        notifyAll();
        return message;
    }

    public synchronized void put(String message) {
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.message = message;
        available = true;
        notifyAll();
    }
}
