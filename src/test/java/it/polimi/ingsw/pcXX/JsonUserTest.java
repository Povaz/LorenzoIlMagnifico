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
		
		//Test che dà in ingresso username e restituisce la password
		String password = cercaPassword("IngComesiscrive");
		System.out.println();
		System.out.println(password);
	}
	
	private static String cercaPassword (String name) throws JSONException, IOException{
		
		//Estrae il file mettendolo in un JSONObject
		JSONObject users = fromPathToJSONObject("jsonFiles/Users.json");
		
		//Estrae l'oggetto users mettendolo in un JSONObject
		JSONObject user  = users.getJSONObject("users");
		
		String password = null;
		
		//Estrae tutte le keys(in questo caso gli username) e li mette in una lista(?)
		Iterator<?> keys = user.keys();
		
		//ciclo che controlla le keys e quando trova una corrispondenza estrae la password e la ritorna
		while( keys.hasNext() ) {
			   String key = (String) keys.next();		    
			   System.out.println(key);
			   if(name.equals(key)){
				   return (String) user.get(key);
			   }
		}
		
		//se la password non è presente esce dal ciclo e qua si può chiamare un altro metodo che crea un nuovo user
		//ovviamente si cancella la riga seguente, è solo messa per far funzionare il programma
		return "Non presente";
		
		//A TE ERICK
	}
	
	//Metodo che estrae da un file un JSONObject
	private static JSONObject fromPathToJSONObject(String path) throws IOException, JSONException{
		return new JSONObject(readFileToString(new File(path)));
	}
}
