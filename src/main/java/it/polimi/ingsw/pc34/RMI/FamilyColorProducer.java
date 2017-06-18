package it.polimi.ingsw.pc34.RMI;

import it.polimi.ingsw.pc34.Model.FamilyColor;

/**
 * Created by Povaz on 18/06/2017.
 */
public class FamilyColorProducer extends Thread {
    private FamilyColorCreated familyColorCreated;
    private FamilyColor familyColor;

    public FamilyColorProducer (FamilyColorCreated familyColorCreated) {
        this.familyColorCreated = familyColorCreated;
    }

    public void setFamilyColor(FamilyColor familyColor) {
        this.familyColor = familyColor;
    }

    public void run () {
        familyColorCreated.put(familyColor);
    }
}
