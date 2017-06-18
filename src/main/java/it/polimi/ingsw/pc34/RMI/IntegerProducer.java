package it.polimi.ingsw.pc34.RMI;

/**
 * Created by Povaz on 17/06/2017.
 */
public class IntegerProducer extends Thread {
    private IntegerCreated integerCreated;
    private int choose = 0;
    int min;
    int max;

    public IntegerProducer (IntegerCreated integerCreated) {
        this.integerCreated = integerCreated;
    }

    public IntegerProducer (IntegerCreated integerCreated, int min, int max) {
        this.integerCreated = integerCreated;
        this.min = min;
        this.max = max;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public void setChoose (int choose) {
        this.choose = choose;
    }

    public void run () {
        integerCreated.put(choose);
    }
}
