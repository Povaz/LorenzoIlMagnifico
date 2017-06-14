package it.polimi.ingsw.pc34;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.pc34.Model.ActionType;
import it.polimi.ingsw.pc34.Model.Reward;
import it.polimi.ingsw.pc34.Model.VaticanReportCard;
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

