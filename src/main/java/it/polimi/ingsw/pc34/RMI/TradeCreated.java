package it.polimi.ingsw.pc34.RMI;

import it.polimi.ingsw.pc34.Model.Trade;

/**
 * Created by Povaz on 20/06/2017.
 */
public class TradeCreated {
    private Trade trade;
    private boolean available = false;

    public synchronized Trade get() {
        while (available == false) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        available = false;
        notifyAll();
        return trade;
    }

    public synchronized void put (Trade trade) {
        while (available == true) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.trade = trade;
        available = true;
        notifyAll();
    }
}
