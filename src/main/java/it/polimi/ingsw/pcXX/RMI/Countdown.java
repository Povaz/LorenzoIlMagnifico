package it.polimi.ingsw.pcXX.RMI;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Povaz on 29/05/2017.
 */
public class Countdown {
    private Timer timer;
    private static int interval;

    public Countdown () {
        this.timer = new Timer();
        this.interval = 10;
    }

    public void startCountdown () {
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                --interval;
                System.out.println(interval);
                if (interval == 0) {
                    System.out.println("Start Game");
                    timer.cancel();
                }
            }
        }, 0,1000);
    }

    public void stopCountdown () {
        this.timer.cancel();
    }

    public static void main (String[] args) {
        Countdown countdown = new Countdown();

        countdown.startCountdown();
    }
}
