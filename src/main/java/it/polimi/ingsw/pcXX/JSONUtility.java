package it.polimi.ingsw.pcXX;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static org.apache.commons.io.FileUtils.readFileToString;

public class JSONUtility {

	public static void main(String[] args) {
		try {
			TerritoryCard carta = (TerritoryCard) getCard(1,1, CardType.TERRITORY);
			System.out.println(carta);
		} catch(Exception e){
			e.printStackTrace();
		}
	}






	public static DevelopmentCard getCard(int period, int number, CardType cardType) throws JSONException, IOException{
		switch(cardType){
			case TERRITORY:
				return getTerritoryCard(period, number);
			/*case BUILDING:
				return getBuildingCard(period, number);
			case CHARACTER:
				return getCharacterCard(period, number);
			case VENTURE:
				return getVentureCard(period, number);*/
			default:
				return null;
		}
	}

	private static TerritoryCard getTerritoryCard(int period, int number) throws JSONException, IOException{
		JSONObject card = fromPathToJSONObject("jsonFiles/TerritoryCard.json");
		card = getPeriodAndNumberCard(period, number, card);

		String name = card.getString("name");
		Set<Reward> fastRewards = getFastReward(card);
		int diceHarvestAction = card.getInt("diceHarvestAction");
		Set<Reward> earnings = getRetrieved(card);

		return new TerritoryCard(name, period, fastRewards, diceHarvestAction, earnings);
	}

	private static JSONObject getPeriodAndNumberCard(int period, int number, JSONObject jsonObject) throws JSONException{
		JSONArray cards = jsonObject.getJSONArray("card");
		JSONObject obj = cards.getJSONObject(period - 1);
		cards = obj.getJSONArray("period");
		return cards.getJSONObject(number);
	}

	private static Set<Reward> getFastReward(JSONObject card){
		try {
			JSONArray fastRewards = card.getJSONArray("fastReward");
			return getReward(fastRewards);
		} catch(JSONException e){
			return null;
		}
	}

	private static Set<Reward> getRetrieved(JSONObject card) throws JSONException{
		JSONArray retrieved = card.getJSONArray("retrieved");
		return getReward(retrieved);
	}

	private static Set<Reward> getReward(JSONArray jsonRewards) throws JSONException{
		Set<Reward> rewardSet = new HashSet<Reward>();
		for(int i = 0; i < jsonRewards.length(); i++){
			JSONObject reward = jsonRewards.getJSONObject(i);

			int quantity = reward.getInt("quantity");
			String rewardType = reward.getString("type");

			try{
				ResourceType type = ResourceType.valueOf(rewardType);
				rewardSet.add(new Resource(type, quantity));
			} catch(IllegalArgumentException e1){
				try{
					PointType type = PointType.valueOf(rewardType);
					rewardSet.add(new Point(type,quantity));
				} catch(IllegalArgumentException e2){
					if(rewardType.equals("COUNCIL_PRIVILEGE"))
						rewardSet.add(new CouncilPrivilege(quantity));
				}
			}
		}
		return rewardSet;
	}

	public static Integer getIntegerNoException(JSONObject obj, String name){
		try{
			return new Integer(obj.getInt(name));
		} catch(JSONException e){
			return null;
		}
	}
	
	public static String getStringNoException(JSONObject obj, String name){
		try{
			return obj.getString(name);
		} catch(JSONException e){
			return null;
		}
	}

	public static JSONArray getArrayNoException(JSONObject obj, String name){
		try{
			return obj.getJSONArray(name);
		} catch(JSONException e){
			return null;
		}
	}
	
	public static JSONObject fromPathToJSONObject(String path) throws IOException, JSONException{
		return new JSONObject(readFileToString(new File(path)));
	}
}
