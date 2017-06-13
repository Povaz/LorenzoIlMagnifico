package it.polimi.ingsw.pc34;


import java.io.IOException;

import it.polimi.ingsw.pc34.Model.PersonalBonusTile;
import org.json.JSONException;
import org.junit.Test;

public class PersonalBonusTileTest {
		@Test
	    public void testPersonalBonusTile() throws JSONException, IOException{
			int number = 1;
			PersonalBonusTile testTile = JSONUtility.getPersonalBonusTile(number);
			int number2 = 2;
			PersonalBonusTile testTile2 = JSONUtility.getPersonalBonusTile(number2);
			
	        System.out.println(testTile.toString());
	        System.out.println(testTile2.toString());
	        
	    }
}

