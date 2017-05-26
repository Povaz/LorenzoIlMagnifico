package it.polimi.ingsw.pcXX;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.writeStringToFile;

public class JSONUtility {

	public static void main(String[] args) {
		try {
			/*TerritoryCard t1 = (TerritoryCard) getCard(1, 0, CardType.TERRITORY);
			System.out.println(t1 + "\n");
			TerritoryCard t2 = (TerritoryCard) getCard(3, 7, CardType.TERRITORY);
			System.out.println(t2 + "\n");

			BuildingCard b1 = (BuildingCard) getCard(1,0, CardType.BUILDING);
			System.out.println(b1 + "\n");
			BuildingCard b2 = (BuildingCard) getCard(1,4, CardType.BUILDING);
			System.out.println(b2 + "\n");
			BuildingCard b3 = (BuildingCard) getCard(2,7, CardType.BUILDING);
			System.out.println(b3 + "\n");
			BuildingCard b4 = (BuildingCard) getCard(3,3, CardType.BUILDING);
			System.out.println(b4 + "\n");

			CharacterCard c1 = (CharacterCard) getCard(1,0, CardType.CHARACTER);
			System.out.println(c1 + "\n");
			CharacterCard c2 = (CharacterCard) getCard(1,1, CardType.CHARACTER);
			System.out.println(c2 + "\n");

			VentureCard v1 = (VentureCard) getCard(1,4, CardType.VENTURE);
			System.out.println(v1 + "\n");
			VentureCard v2 = (VentureCard) getCard(2,4, CardType.VENTURE);
			System.out.println(v2 + "\n");*/

			System.out.println(checkLogin("lacieoz", "LoL"));
			System.out.println(checkRegister("lacieoz", "LoL"));
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public static synchronized boolean checkLogin(String username, String password) throws JSONException, IOException{
		JSONObject users = fromPathToJSONObject("jsonFiles/Login.json");
		username = encryptString(username);
		password = encryptString(password);

		if(users.has(username)){
			return password.equals(getPassword(users, username));
		}
		else{
			return false;
		}
	}

	public static synchronized boolean checkRegister(String username, String password) throws JSONException, IOException{
		JSONObject users = fromPathToJSONObject("jsonFiles/Login.json");
		username = encryptString(username);
		password = encryptString(password);

		if(users.has(username)){
			return false;
		}
		else{
			addUsernamePassword(users, username, password);
		}
		return true;
	}

	private static String getPassword(JSONObject users, String username) throws JSONException{
		return users.getString(username);
	}

	private static void addUsernamePassword(JSONObject users, String username, String password) throws JSONException, IOException{
		users.put(username, password);
		writeStringToFile(new File("jsonFiles/Login.json"), users.toString());
	}

	private static String encryptString(String string){
		try{
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] stringByte = string.getBytes("UTF-8");
			byte[] encryptedByte = messageDigest.digest(stringByte);
			String encrypted = new String(encryptedByte, "UTF-8");
			return encrypted;
		} catch(Exception e){
			return string;
		}
	}

	public static DevelopmentCard getCard(int period, int number, CardType cardType) throws JSONException, IOException{
		switch(cardType){
			case TERRITORY:
				return getTerritoryCard(period, number);
			case BUILDING:
				return getBuildingCard(period, number);
			case CHARACTER:
				return getCharacterCard(period, number);
			case VENTURE:
				return getVentureCard(period, number);
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
		Set<Reward> costs = getCosts(card);
		Set<Reward> fastRewards = getFastRewards(card);
		int diceProductionAction = card.getInt("diceProductionAction");
		Set<Reward> earnings = getEarnings(card);
		ArrayList<Trade> trades = getTrades(card);
		RewardForReward rewardForReward = getRewardForReward(card);
		RewardForCard rewardForCard = getRewardForCard(card);

		return new BuildingCard(name, period, costs, fastRewards, diceProductionAction, earnings, trades, rewardForReward,
				rewardForCard);
	}

	private static CharacterCard getCharacterCard(int period, int number) throws JSONException, IOException{
		JSONObject card = fromPathToJSONObject("jsonFiles/CharacterCard.json");
		card = getPeriodAndNumberCard(period, number, card);

		String name = getName(card);
		Set<Reward> costs = getCosts(card);
		Set<Reward> fastRewards = getFastRewards(card);
		LinkedList<GhostFamilyMember> actions = getActions(card);
		boolean noBonusTowerResource = getBooleanNoException(card, "noBonusTowerResource");
		LinkedList<CostDiscount> discounts = getDiscounts(card);
		LinkedList<ActionModifier> actionModifiers = getActionModifiers(card);
		RewardForReward rewardForReward = getRewardForReward(card);
		RewardForCard rewardForCard = getRewardForCard(card);

		return new CharacterCard(name, period, costs, fastRewards, actions, noBonusTowerResource, discounts, actionModifiers,
				rewardForReward, rewardForCard);
	}

	private static VentureCard getVentureCard(int period, int number) throws JSONException, IOException{
		JSONObject card = fromPathToJSONObject("jsonFiles/VentureCard.json");
		card = getPeriodAndNumberCard(period, number, card);

		String name = getName(card);
		Set<Reward> costs = getCosts(card);
		Set<Reward> fastRewards = getFastRewards(card);
		LinkedList<GhostFamilyMember> actions = getActions(card);
		Point militaryPointNeeded;
		Point militaryPointPrice;
		try{
			militaryPointNeeded = new Point(PointType.MILITARY_POINT, card.getInt("militaryPointNeeded"));
			militaryPointPrice = new Point(PointType.MILITARY_POINT, card.getInt("militaryPointPrice"));
		} catch(JSONException e){
			militaryPointNeeded = null;
			militaryPointPrice = null;
		}
		Point victoryPointEarned = new Point(PointType.VICTORY_POINT, card.getInt("victoryPointEarned"));

		return new VentureCard(name, period, costs, fastRewards, actions, militaryPointNeeded, militaryPointPrice,
				victoryPointEarned);
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
			return getRewardSet(costs);
		} catch(JSONException e){
			return null;
		}
	}

	private static Set<Reward> getFastRewards(JSONObject card){
		try {
			JSONArray fastRewards = card.getJSONArray("fastRewards");
			return getRewardSet(fastRewards);
		} catch(JSONException e){
			return null;
		}
	}

	private static Set<Reward> getEarnings(JSONObject card){
		try {
			JSONArray earnings = card.getJSONArray("earnings");
			return getRewardSet(earnings);
		} catch(JSONException e){
			return null;
		}
	}

	private static ArrayList<Trade> getTrades(JSONObject card){
		try {
			ArrayList<Trade> tradesArray = new ArrayList<>();
			JSONArray trades = card.getJSONArray("trades");
			for(int i = 0; i < trades.length(); i++){
				JSONArray give = trades.getJSONObject(i).getJSONArray("give");
				Set<Reward> giveSet = getRewardSet(give);
				JSONArray take = trades.getJSONObject(i).getJSONArray("take");
				Set<Reward> takeSet = getRewardSet(take);
				tradesArray.add(new Trade(giveSet, takeSet));
			}
			return tradesArray;
		} catch(JSONException e){
			return null;
		}
	}

	private static LinkedList<GhostFamilyMember> getActions(JSONObject card){
		try{
			LinkedList<GhostFamilyMember> actionList = new LinkedList<GhostFamilyMember>();
			JSONArray actions = card.getJSONArray("actions");
			for(int i = 0; i < actions.length(); i++){
				JSONObject actionObj = actions.getJSONObject(i);
				ActionType actionType = ActionType.valueOf(actionObj.getString("actionType"));
				int value = actionObj.getInt("value");
				Set<Reward> discounts;
				try {
					discounts = getRewardSet(actionObj.getJSONArray("discounts"));
				} catch(JSONException e){
					discounts = null;
				}
				actionList.add(new GhostFamilyMember(actionType, value, discounts));
			}
			return actionList;
		} catch(JSONException e){
			return null;
		}
	}

	private static LinkedList<CostDiscount> getDiscounts(JSONObject card){
		try{
			JSONArray costDiscounts = card.getJSONArray("costDiscounts");
			LinkedList<CostDiscount> discountList = new LinkedList<CostDiscount>();
			for(int i = 0; i < costDiscounts.length(); i++){
				JSONObject discountObj = costDiscounts.getJSONObject(i);
				CardType cardType = CardType.valueOf(discountObj.getString("cardType"));

				JSONArray options = discountObj.getJSONArray("options");
				for(int j = 0; j < options.length(); j++){
					JSONObject discounts = options.getJSONObject(j);
					Set<Reward> rewards = getRewardSet(discounts.getJSONArray("discounts"));
					discountList.add(new CostDiscount(cardType, rewards));
				}
			}
			return discountList;
		} catch(JSONException e){
			return null;
		}
	}

	private static LinkedList<ActionModifier> getActionModifiers(JSONObject card){
		try{
			LinkedList<ActionModifier> actionModifierList = new LinkedList<ActionModifier>();
			JSONArray actionModifiers = card.getJSONArray("actionModifiers");
			for(int i = 0; i < actionModifiers.length(); i++){
				JSONObject modifierObj = actionModifiers.getJSONObject(i);
				ActionType type = ActionType.valueOf(modifierObj.getString("type"));
				int modifier = modifierObj.getInt("modifier");
				actionModifierList.add(new ActionModifier(type, modifier));
			}
			return actionModifierList;
		} catch(JSONException e){
			return null;
		}
	}

	private static RewardForReward getRewardForReward(JSONObject card){
		try{
			JSONObject rewardForReward = card.getJSONObject("rewardForReward");
			JSONArray earned = rewardForReward.getJSONArray("earned");
			Set<Reward> earnedSet = getRewardSet(earned);
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
			Set<Reward> earnedSet = getRewardSet(earned);
			CardType owned = CardType.valueOf(rewardForCard.getString("owned"));
			return new RewardForCard(earnedSet, owned);
		} catch(JSONException e){
			return null;
		}
	}

	private static Set<Reward> getRewardSet(JSONArray jsonRewards) throws JSONException{
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

	private static boolean getBooleanNoException(JSONObject obj, String name){
		try{
			return obj.getBoolean(name);
		} catch(JSONException e){
			return false;
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
