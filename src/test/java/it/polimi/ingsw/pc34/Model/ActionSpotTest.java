package it.polimi.ingsw.pc34.Model;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Povaz on 14/06/2017.
 */
public class ActionSpotTest extends TestCase {
    private ActionSpot actionSpot;
    private ActionSpot actionSpot1;
    private ActionSpot actionSpot2;
    private FamilyMember familyMember;


    public ActionSpotTest(String name) {
        super(name);
    }

    @Test
    public void testIsPlaceable() throws RemoteException, IOException{
        Set<Reward> rewards = new HashSet<>();
        rewards.add(new Reward(RewardType.COIN, 5));
        FamilyMember familyMember = new FamilyMember(ActionType.MARKET, 6, rewards);

        boolean active = true;
        boolean unrestricted = true;
        int diceValue = 1;

        boolean canPlaceInBusyActionSpot;

        ActionSpot actionSpot = new Market(active, unrestricted, diceValue, rewards);
        familyMember.setUsed(false);
        actionSpot.setBusy(false);
        canPlaceInBusyActionSpot = true;
        familyMember.setValue(6);
        assertTrue("Familiare posizionato", actionSpot.isPlaceable(familyMember, canPlaceInBusyActionSpot, null));

        familyMember.setUsed(true);
        assertFalse("Familiare già usato", actionSpot.isPlaceable(familyMember, canPlaceInBusyActionSpot, null));

        familyMember.setUsed(false);
        actionSpot.setBusy(true);
        canPlaceInBusyActionSpot = false;
        assertFalse("ActionSpot occupato", actionSpot.isPlaceable(familyMember, canPlaceInBusyActionSpot, null));

        diceValue = 5;
        assertFalse("Dado troppo basso", actionSpot.isPlaceable(familyMember, canPlaceInBusyActionSpot, null));

    }


    //La chiamata al metodo non modifica in alcun modo gli attributi di FamilyMember e di ActionSpot
    @Test
    public void testPlaceFamilyMemberReinitialize () {
        actionSpot.placeFamilyMember(familyMember);
        assertFalse("Familiare diventa occupato", familyMember.isUsed()); //Dovrebbe essere True
        //assertTrue("Familiare occupa l'actionSpot", familyMember.equals(actionSpot.getOccupiedBy().get(1)));
        assertFalse("ActionSpot restricted: ActionSpot busy", actionSpot.isBusy()); //Dovrebbe essere True

        actionSpot.reinitialize();
        assertFalse("ActionSpot free", actionSpot.isBusy());
        assertTrue("OccupiedBy free", actionSpot.getOccupiedBy().isEmpty());
    }

    @Test
    public void testEquals() {
        assertTrue("Sono lo stesso oggetto", actionSpot.equals(actionSpot1));
        assertFalse("Sono lo stesso oggetto", actionSpot.equals(actionSpot2));

    }

    public void setUp() {
        Set<Reward> rewards = new HashSet<>();
        rewards.add(new Reward(RewardType.COIN, 5));
        familyMember = new FamilyMember(ActionType.MARKET, 6, rewards);

        boolean active = true;
        boolean unrestricted = false;
        int diceValue = 1;

        actionSpot = new Market(active, unrestricted, diceValue, rewards);

        actionSpot1 = new Market(active, unrestricted, diceValue, rewards);
        actionSpot2 = new Market(!active, unrestricted, diceValue, rewards);

    }

    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new ActionSpotTest("testIsPlaceable"));
        suite.addTest(new ActionSpotTest("testPlaceFamilyMemberReinitialize"));
        suite.addTest(new ActionSpotTest("testEquals"));
        return suite;
    }

    public static void main (String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
