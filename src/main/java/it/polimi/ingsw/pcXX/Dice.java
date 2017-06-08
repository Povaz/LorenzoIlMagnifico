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

    public void throwDice(){
        Random rand = new Random();
        value = rand.nextInt(6) + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dice dice = (Dice) o;

        if (value != dice.value) return false;
        return color == dice.color;
    }

    @Override
    public int hashCode() {
        return color.hashCode();
    }

    public int getValue(){
        return value;
    }

    public FamilyColor getColor() {
        return color;
    }
}
