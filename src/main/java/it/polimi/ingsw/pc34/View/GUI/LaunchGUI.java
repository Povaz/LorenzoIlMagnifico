package it.polimi.ingsw.pc34.View.GUI;


import javafx.application.Application;

/**
 * Created by trill on 04/07/2017.
 */
public class LaunchGUI implements Runnable{
    @Override
    public void run(){
        Application.launch(Main.class);
    }
}
