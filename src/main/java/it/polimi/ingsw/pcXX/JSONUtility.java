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
			TerritoryCard carta1 = (TerritoryCard) getCard(1,1, CardType.TERRITORY);
			System.out.println(carta1 + "\n");
			TerritoryCard carta2 = (TerritoryCard) getCard(2,2, CardType.TERRITORY);
			System.out.println(carta2 + "\n");

			BuildingCard carta3 = (BuildingCard) getCard(1,1, CardType.BUILDING);
			System.out.println(carta3 + "\n");
			BuildingCard carta4 = (BuildingCard) getCard(2,2, CardType.BUILDING);
			System.out.println(carta4 + "\n");
		} catch(Exception e){
			e.printStackTrace();
		}
	}






	public static DevelopmentCard getCard(int period, int number, CardType cardType) throws JSONException, IOException{
		switch(cardType){
			case TERRITORY:
				return getTerritoryCard(period, number);
			case BUILDING:
				return getBuildingCard(period, number);
			/*case CHARACTER:
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

		String name = getName(card);
		Set<Reward> fastRewards = getFastRewards(card);
		int diceHarvestAction = card.getInt("diceHarvestAction");
		Set<Reward> earnings = getEarnings(card);

		return new TerritoryCard(name, period, fastRewards, diceHarvestAction, earnings);
	}

	private static BuildingCard getBuildingCard(int period, int number) throws JSONException, IOException{
		JSONObject card = fromPathToJSONObject("jsonFiles/BuildingCard.json");
		card = getPeriodAndNumberCard(period, number, card);

		String name = getName(card);
		Set<Reward> fastRewards = getFastRewards(card);
		Set<Reward> costs = getCosts(card);
		int diceProductionAction = card.getInt("diceProductionAction");
		Set<Reward> earnings = getEarnings(card);
		Set<Trade> trades = getTrades(card);
		RewardForReward rewardForReward = getRewardForReward(card);
		RewardForCard rewardForCard = getRewardForCard(card);

		return new BuildingCard(name, period, costs, fastRewards, diceProductionAction, earnings, trades, rewardForReward,
				rewardForCard);
	}

	private static JSONObject getPeriodAndNumberCard(int period, int number, JSONObject jsonObject) throws JSONException{
		JSONArray cards = jsonObject.getJSONArray("cards");
		JSONObject obj = cards.getJSONObject(period - 1);
		cards = obj.getJSONArray("period");
		return cards.getJSONObject(number);
	}

	private static String getName(JSONObject card) throws JSONException{
		return card.getString("name");
	}

	private static Set<Reward> getCosts(JSONObject card){
		try {
			JSONArray costs = card.getJSONArray("costs");
			return getRewards(costs);
		} catch(JSONException e){
			return null;
		}
	}

	private static Set<Reward> getFastRewards(JSONObject card){
		try {
			JSONArray fastRewards = card.getJSONArray("fastRewards");
			return getRewards(fastRewards);
		} catch(JSONException e){
			return null;
		}
	}

	private static Set<Reward> getEarnings(JSONObject card){
		try {
			JSONArray earnings = card.getJSONArray("earnings");
			return getRewards(earnings);
		} catch(JSONException e){
			return null;
		}
	}

	private static Set<Trade> getTrades(JSONObject card){
		try {
			Set<Trade> tradesSet = new HashSet<Trade>();
			JSONArray trades = card.getJSONArray("trades");
			for(int i = 0; i < trades.length(); i++){
				JSONArray give = trades.getJSONObject(i).getJSONArray("give");
				Set<Reward> giveSet = getRewards(give);
				JSONArray take = trades.getJSONObject(i).getJSONArray("take");
				Set<Reward> takeSet = getRewards(take);
				tradesSet.add(new Trade(giveSet, takeSet));
			}
			return tradesSet;
		} catch(JSONException e){
			return null;
		}
	}

	private static RewardForReward getRewardForReward(JSONObject card){
		try{
			JSONObject rewardForReward = card.getJSONObject("rewardForReward");
			JSONArray earned = rewardForReward.getJSONArray("earned");
			Set<Reward> earnedSet = getRewards(earned);
			JSONObject owned = rewardForReward.getJSONObject("owned");
			Reward ownedRew = getReward(owned);
			return new RewardForReward(earnedSet, ownedRew);
		} catch(JSONException e){
			return null;
		}
	}

	private static RewardForCard getRewardForCard(JSONObject card){
		try{
			JSONObject rewardForCard = card.getJSONObject("rewardForCard");
			JSONArray earned = rewardForCard.getJSONArray("earned");
			Set<Reward> earnedSet = getRewards(earned);
			CardType owned = CardType.valueOf(rewardForCard.getString("owned"));
			return new RewardForCard(earnedSet, owned);
		} catch(JSONException e){
			return null;
		}
	}

	private static Set<Reward> getRewards(JSONArray jsonRewards) throws JSONException{
		Set<Reward> rewardSet = new HashSet<Reward>();
		for(int i = 0; i < jsonRewards.length(); i++){
			JSONObject reward = jsonRewards.getJSONObject(i);
			rewardSet.add(getReward(reward));
		}
		return rewardSet;
	}

	private static Reward getReward(JSONObject reward) throws JSONException{
		int quantity = reward.getInt("quantity");
		String rewardType = reward.getString("type");

		try{
			ResourceType type = ResourceType.valueOf(rewardType);
			return new Resource(type, quantity);
		} catch(IllegalArgumentException e1){
			try{
				PointType type = PointType.valueOf(rewardType);
				return new Point(type,quantity);
			} catch(IllegalArgumentException e2){
				if(rewardType.equals("COUNCIL_PRIVILEGE"))
					return new CouncilPrivilege(quantity);
				throw new JSONException("type");
			}
		}
	}

	private static Integer getIntegerNoException(JSONObject obj, String name){
		try{
			return new Integer(obj.getInt(name));
		} catch(JSONException e){
			return null;
		}
	}
	
	private static String getStringNoException(JSONObject obj, String name){
		try{
			return obj.getString(name);
		} catch(JSONException e){
			return null;
		}
	}

	private static JSONArray getArrayNoException(JSONObject obj, String name){
		try{
			return obj.getJSONArray(name);
		} catch(JSONException e){
			return null;
		}
	}
	
	private static JSONObject fromPathToJSONObject(String path) throws IOException, JSONException{
		return new JSONObject(readFileToString(new File(path)));
	}
}
