package it.polimi.ingsw.pcXX;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;

import static it.polimi.ingsw.pcXX.JSONUtility.getImmediateLeaderCard;
import static junit.framework.TestCase.assertFalse;

/**
 * Created by Povaz on 04/06/2017.
 */
public class ImmediateLeaderCardTest {

    @Test
    public void testCreationCard () throws JSONException, IOException  {
        ImmediateLeaderCard[] immediateLeaderCards = new ImmediateLeaderCard[10];

        for (int i = 0; i < immediateLeaderCards.length - 1; i++) {
            immediateLeaderCards[i] = getImmediateLeaderCard(i);
            assertFalse("Immediate Leader Card number " + i + "is empty", immediateLeaderCards[i] == null);
            System.out.println(immediateLeaderCards[i].toString() + "\n");
        }

    }
}
