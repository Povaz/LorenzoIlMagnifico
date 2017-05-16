package it.polimi.ingsw.pcXX;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import static org.apache.commons.io.FileUtils.readFileToString;

public class fromCardToJson {
	
	public static Integer getInt(JSONObject obj, String name){
		try{
			return new Integer(obj.getInt(name));
		} catch(JSONException e){
			return null;
		}
	}
	
	public static String getString(JSONObject obj, String name){
		try{
			return obj.getString(name);
		} catch(JSONException e){
			return null;
		}
	}
	
	public static JSONObject fromPathToJSONObject(String path) throws IOException, JSONException{
		return new JSONObject(readFileToString(new File("JsonGenerico.json")));
	}
}
