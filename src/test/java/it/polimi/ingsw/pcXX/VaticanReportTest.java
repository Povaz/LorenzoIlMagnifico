package it.polimi.ingsw.pcXX;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.json.JSONException;
import org.junit.Test;

public class VaticanReportTest {
		@Test
	    public void testVaticanReport() throws JSONException, IOException{
	        int period = 1;
			int number = 1;
			VaticanReportCard testVatican = JSONUtility.getVaticanReportCard(period, number);
			
	        System.out.println(testVatican.getPeriod());
	        System.out.println(testVatican.getNumber());
	        System.out.println(testVatican.getMilitaryPointsModifier());
	        
	    }
}

