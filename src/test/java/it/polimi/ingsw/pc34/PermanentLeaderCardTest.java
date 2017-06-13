package it.polimi.ingsw.pc34;

import it.polimi.ingsw.pc34.Model.PermanentLeaderCard;
import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;

import static it.polimi.ingsw.pc34.JSONUtility.getPermanentLeaderCard;
import static junit.framework.TestCase.assertFalse;

/**
 * Created by Povaz on 04/06/2017.
 */
public class PermanentLeaderCardTest {

    @Test
    public void testCreationCard () throws JSONException, IOException {
        PermanentLeaderCard[] permanentLeaderCards = new PermanentLeaderCard[10];

        for (int i = 0; i < permanentLeaderCards.length - 1; i++) {
            permanentLeaderCards[i] = getPermanentLeaderCard(i);
            assertFalse ("Permanent Leader Card Number " + i + "is empty", permanentLeaderCards[i] == null);
            System.out.println(permanentLeaderCards[i].toString() + "\n");
        }
    }
}
