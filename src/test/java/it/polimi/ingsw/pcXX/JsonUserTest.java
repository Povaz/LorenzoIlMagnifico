package it.polimi.ingsw.pcXX;

import static org.apache.commons.io.FileUtils.readFileToString;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUserTest {
	
	public static void main (String args[]) throws JSONException, IOException{
		String password = cercaPassword("ShadowDD");
		System.out.println(password);
	}
	
	private static String cercaPassword (String name) throws JSONException, IOException{
		JSONObject user = fromPathToJSONObject("jsonFiles/Users.json");
		JSONArray users = user.getJSONArray("user");
		String password = null;
		int contatore=users.length();
		
		while(contatore>0){
			contatore--;
			user=users.getJSONObject(contatore);
			Iterator<?> keys = user.keys();
			while( keys.hasNext() ) {
			    String key = (String) keys.next();		    
			    if(name.equals(key)){
			    	return (String) user.get(key);
			    }
			}
			//se è uguale estrai password
			
		
		}
		return "Non presente";
		

		
		
	}
	
	private static JSONObject fromPathToJSONObject(String path) throws IOException, JSONException{
		return new JSONObject(readFileToString(new File(path)));
	}
}
