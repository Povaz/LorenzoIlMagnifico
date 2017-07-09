package it.polimi.ingsw.pc34.RMI;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

/**
 * Created by Povaz on 04/07/2017.
 */
@SuppressWarnings({ "unused", "restriction", "serial" })
public class SynchronizedString implements Serializable{
    private String message;
    private BooleanProperty available = new SimpleBooleanProperty(false);

    public BooleanProperty getAvailable() {
        return available;
    }

    public synchronized String get() {
        while (!available.getValue()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        available.set(false);
        notifyAll();
        return message;
    }

    public synchronized void put(String message) {
        while (available.getValue()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.message = message;
        available.set(true);
        notifyAll();
    }
}
