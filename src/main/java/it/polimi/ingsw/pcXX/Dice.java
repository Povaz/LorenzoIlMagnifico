package it.polimi.ingsw.pcXX;

import java.util.Random;

/**
 * Created by trill on 22/05/2017.
 */
public class Dice{
    private int value;
    private final FamilyColor color;

    public Dice(FamilyColor color){
        this.color = color;
        this.value = 1;
        throwDice();
    }

    public int getValue(){
        return value;
    }

    public void throwDice(){
        Random rand = new Random();
        value = rand.nextInt(6) + 1;
    }
}
