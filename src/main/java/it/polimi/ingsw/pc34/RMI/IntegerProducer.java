package it.polimi.ingsw.pc34.RMI;

/**
 * Created by Povaz on 17/06/2017.
 */
public class IntegerProducer extends Thread {
    private IntegerCreated integerCreated;
    private int choose = 0;

    public IntegerProducer (IntegerCreated integerCreated) {
        this.integerCreated = integerCreated;
    }

    public void setChoose (int choose) {
        this.choose = choose;
    }

    public void run () {
        integerCreated.put(choose);
    }
}
