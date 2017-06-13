package it.polimi.ingsw.pc34;


import java.io.IOException;

import it.polimi.ingsw.pc34.Model.VaticanReportCard;
import org.json.JSONException;
import org.junit.Test;

public class VaticanReportTest {
		@Test
	    public void testVaticanReport() throws JSONException, IOException{
	        int period = 3;
			int number = 2;
			VaticanReportCard testVatican = JSONUtility.getVaticanReportCard(period, number);
			int period2 = 3;
			int number2 = 2;
			VaticanReportCard testVatican2 = JSONUtility.getVaticanReportCard(period2, number2);
			
	        System.out.println(testVatican.toString());
	        System.out.println(testVatican2.toString());
	        System.out.println(testVatican.equals(testVatican2));
	    }
}

