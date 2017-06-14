package it.polimi.ingsw.pc34;

import it.polimi.ingsw.pc34.Model.VaticanReportCard;

import java.io.IOException;
import org.json.JSONException;
import org.junit.Test;

public class VaticanReportTest {
		@Test
	    public void testVaticanReport() throws JSONException, IOException{
	        int period = 3;
			int number = 3;
			VaticanReportCard testVatican = JSONUtility.getVaticanReportCard(period, number);
			
	        System.out.println(testVatican.toString());
	        
	    }
}

