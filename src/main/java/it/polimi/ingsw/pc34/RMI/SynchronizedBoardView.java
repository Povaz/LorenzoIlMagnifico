package it.polimi.ingsw.pc34.RMI;

import it.polimi.ingsw.pc34.View.GUI.BoardView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.Serializable;

/**
 * Created by Povaz on 04/07/2017.
 */
public class SynchronizedBoardView implements Serializable{
    private BoardView board;
    private BooleanProperty available = new SimpleBooleanProperty(false);

    public BooleanProperty getAvailable() {
        return available;
    }

    public synchronized BoardView get() {
        while (!available.getValue()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        available.set(false);
        notifyAll();
        return board;
    }

    public synchronized void put(BoardView boardView) {
        while (available.getValue()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.board = boardView;
        available.set(true);
        notifyAll();
    }
}
