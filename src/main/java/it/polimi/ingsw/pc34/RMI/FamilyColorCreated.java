package it.polimi.ingsw.pc34.RMI;

import it.polimi.ingsw.pc34.Model.FamilyColor;

import java.beans.IntrospectionException;

/**
 * Created by Povaz on 18/06/2017.
 */
public class FamilyColorCreated {
    private FamilyColor familyColor;
    private boolean available = false;

    public synchronized FamilyColor get() {
        System.out.println("Check Get");
        while(available == false) {
            try {
                wait();
                System.out.println("Check Get infinite");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        available = false;
        notifyAll();
        return familyColor;
    }

    public synchronized void put (FamilyColor familyColor) {
        while(available == true) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.familyColor = familyColor;
        available = true;
        notifyAll();
    }
}
