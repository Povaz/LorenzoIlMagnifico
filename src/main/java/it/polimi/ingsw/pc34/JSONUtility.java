package it.polimi.ingsw.pc34;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import it.polimi.ingsw.pc34.Model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.writeStringToFile;

public class JSONUtility {
	private static String territoryCardPath = "jsonFiles/TerritoryCard.json";
	private static String buildingCardPath = "jsonFiles/BuildingCard.json";
	private static String characterCardPath = "jsonFiles/CharacterCard.json";
	private static String ventureCardPath = "jsonFiles/VentureCard.json";
	private static String userPath = "jsonFiles/User.json";
	private static String vaticanReportCardPath = "jsonFiles/JsonVaticanReportCard.json";
	private static String permanentLeaderCardPath = "jsonFiles/JsonPermanentLeaderCard.json";
	private static String immediateLeaderCardPath = "jsonFiles/JsonImmediateLeaderCard.json";
	private static String personalBonusTilePath = "jsonFiles/PersonalBonusTile.json";
	private static String configPath = "jsonFiles/Config.json";
	
	public static synchronized boolean checkLogin(String username, String password) throws JSONException, IOException{
		if(username.length() > 15 || password.length() > 15){
			return false;
		}
		JSONObject users = fromPathToJSONObject(userPath);
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
		JSONObject users = fromPathToJSONObject(userPath);
		username = encryptString(username);
		password = encryptString(password);

		if(users.has(username)){
			return false;
		}
		else{
			addUsernamePassword(users, username, password);
			return true;
		}
	}

	private static String getPassword(JSONObject users, String username) throws JSONException{
		return users.getString(username);
	}

	private static void addUsernamePassword(JSONObject users, String username, String password) throws JSONException, IOException{
		users.put(username, password);
		writeStringToFile(new File(userPath), users.toString());
	}

	private static String encryptString(String string){
		try{
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] stringByte = string.getBytes("UTF-8");
			byte[] encryptedByte = messageDigest.digest(stringByte);
			String encrypted = new String(encryptedByte, "UTF-8");
			return encrypted;
		} catch(UnsupportedEncodingException e){
			return string;
		} catch(NoSuchAlgorithmException e){
			return string;
		}
	}

	public static long getTimeoutLobby() throws JSONException, IOException{
		JSONObject config = fromPathToJSONObject(configPath);
		return config.getInt("timeoutLobby");

	}

	public static long getTimeoutTurn() throws JSONException, IOException{
		JSONObject config = fromPathToJSONObject(configPath);
		return config.getInt("timeoutTurn");
	}

	public static Set<Reward> getSpotRewards(ActionType actionType, int numberSpot) throws JSONException, IOException{
		JSONObject config = fromPathToJSONObject(configPath);
		JSONObject reward = config.getJSONArray(actionType.toString()).getJSONObject(numberSpot);
		return getRewardSet(reward.getJSONArray("rewards"));
	}

	// TODO finisci public static Set<Reward> getFaith

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

	public static int getCardLength(int period, CardType cardType) throws JSONException, IOException{
		String path = null;
		switch(cardType){
			case TERRITORY:
				path = territoryCardPath;
				break;
			case BUILDING:
				path = buildingCardPath;
				break;
			case CHARACTER:
				path = characterCardPath;
				break;
			case VENTURE:
				path = ventureCardPath;
				break;
		}
		JSONObject cards = fromPathToJSONObject(path);
		JSONArray cardsArray = cards.getJSONArray("cards").getJSONObject(period - 1).getJSONArray("period");
		return cardsArray.length();
	}

	private static TerritoryCard getTerritoryCard(int period, int number) throws JSONException, IOException{
		JSONObject card = fromPathToJSONObject(territoryCardPath);
		card = getPeriodAndNumberCard(period, number, card);

		String name = getName(card);
		String path = getPath(card);
		Set<Reward> fastRewards = getFastRewards(card);
		int diceHarvestAction = card.getInt("diceHarvestAction");
		Set<Reward> earnings = getEarnings(card);

		return new TerritoryCard(name, path, period, fastRewards, diceHarvestAction, earnings);
	}

	private static BuildingCard getBuildingCard(int period, int number) throws JSONException, IOException{
		JSONObject card = fromPathToJSONObject(buildingCardPath);
		card = getPeriodAndNumberCard(period, number, card);

		String name = getName(card);
		String path = getPath(card);
		Set<Reward> costs = getCosts(card);
		Set<Reward> fastRewards = getFastRewards(card);
		int diceProductionAction = card.getInt("diceProductionAction");
		Set<Reward> earnings = getEarnings(card);
		List<Trade> trades = getTrades(card);
		RewardForReward rewardForReward = getRewardForReward(card);
		RewardForCard rewardForCard = getRewardForCard(card);

		return new BuildingCard(name, path, period, costs, fastRewards, diceProductionAction, earnings, trades, rewardForReward,
				rewardForCard);
	}

	private static CharacterCard getCharacterCard(int period, int number) throws JSONException, IOException{
		JSONObject card = fromPathToJSONObject(characterCardPath);
		card = getPeriodAndNumberCard(period, number, card);

		String name = getName(card);
		String path = getPath(card);
		Set<Reward> costs = getCosts(card);
		Set<Reward> fastRewards = getFastRewards(card);
		List<FamilyMember> actions = getActions(card);
		boolean noBonusTowerResource = getBooleanNoException(card, "noBonusTowerResource");
		Map<CardType,List<List<Reward>>> discounts = getDiscounts(card);
		Map<ActionType, Integer> actionModifiers = getActionModifiers(card);
		RewardForReward rewardForReward = getRewardForReward(card);
		RewardForCard rewardForCard = getRewardForCard(card);

		return new CharacterCard(name, path, period, costs, fastRewards, actions, noBonusTowerResource, discounts, actionModifiers,
				rewardForReward, rewardForCard);
	}

	private static VentureCard getVentureCard(int period, int number) throws JSONException, IOException{
		JSONObject card = fromPathToJSONObject(ventureCardPath);
		card = getPeriodAndNumberCard(period, number, card);

		String name = getName(card);
		String path = getPath(card);
		Set<Reward> costs = getCosts(card);
		Set<Reward> fastRewards = getFastRewards(card);
		List<FamilyMember> actions = getActions(card);
		Reward militaryPointNeeded;
		Reward militaryPointPrice;
		try{
			militaryPointNeeded = new Reward(RewardType.MILITARY_POINT, card.getInt("militaryPointNeeded"));
			militaryPointPrice = new Reward(RewardType.MILITARY_POINT, card.getInt("militaryPointPrice"));
		} catch(JSONException e){
			militaryPointNeeded = null;
			militaryPointPrice = null;
		}
		Reward victoryPointEarned = new Reward(RewardType.VICTORY_POINT, card.getInt("victoryPointEarned"));

		return new VentureCard(name, path, period, costs, fastRewards, actions, militaryPointNeeded, militaryPointPrice,
				victoryPointEarned);
	}

	private static JSONObject getPeriodAndNumberCard(int period, int number, JSONObject jsonObject) throws JSONException{
		JSONArray cards = jsonObject.getJSONArray("cards");
		JSONObject obj = cards.getJSONObject(period - 1);
		cards = obj.getJSONArray("period");
		return cards.getJSONObject(number);
	}

	private static JSONObject getNumberCard(int number, JSONObject jsonObject) throws JSONException{
		JSONArray cards = jsonObject.getJSONArray("cards");
		return cards.getJSONObject(number);
	}

	private static String getName(JSONObject card) throws JSONException{
		return card.getString("name");
	}

	private static String getPath(JSONObject card) throws JSONException{
		return card.getString("path");
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

	private static Set<Reward> getActivationRewardCost(JSONObject card){
		try {
			JSONArray activationCardCost = card.getJSONArray("activationRewardCost");
			return getRewardSet(activationCardCost);
		} catch(JSONException e){
			return null;
		}
	}

	private static List<Reward> getBonusRewardChurchSupport(JSONObject card){
		try {
			JSONArray bonusRewardChurchSupport = card.getJSONArray("bonusRewardChurchSupport");
			return getRewardList(bonusRewardChurchSupport);
		} catch(JSONException e){
			return null;
		}
	}

	private static List<Reward> getLoseRewards(JSONObject card){
		try {
			JSONArray loseRewards = card.getJSONArray("loseRewards");
			return getRewardList(loseRewards);
		} catch(JSONException e){
			return null;
		}
	}

	private static List<Trade> getTrades(JSONObject card){
		try {
			List<Trade> tradesArray = new ArrayList<>();
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

	private static List<FamilyMember> getActions(JSONObject card){
		try{
			List<FamilyMember> actionList = new LinkedList<>();
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
				actionList.add(new FamilyMember(actionType, value, discounts));
			}
			return actionList;
		} catch(JSONException e){
			return null;
		}
	}

	private static Map<CardType, List<List<Reward>>> getDiscounts(JSONObject card){
		try{
			JSONArray costDiscounts = card.getJSONArray("costDiscounts");
			Map<CardType, List<List<Reward>>> discountMap = new HashMap<>();
			discountMap.put(CardType.TERRITORY, new ArrayList<>());
			discountMap.put(CardType.BUILDING, new ArrayList<>());
			discountMap.put(CardType.CHARACTER, new ArrayList<>());
			discountMap.put(CardType.VENTURE, new ArrayList<>());
			for(int i = 0; i < costDiscounts.length(); i++){
				JSONObject discountObj = costDiscounts.getJSONObject(i);
				CardType cardType = CardType.valueOf(discountObj.getString("cardType"));
				List<List<Reward>> discountList = discountMap.get(cardType);
				JSONArray options = discountObj.getJSONArray("options");
				for(int j = 0; j < options.length(); j++){
					JSONObject discounts = options.getJSONObject(j);
					List<Reward> rewards = getRewardList(discounts.getJSONArray("discounts"));
					discountList.add(rewards);
				}
			}
			return discountMap;
		} catch(JSONException e){
			return null;
		}
	}

	private static Map<ActionType, Integer> getActionModifiers(JSONObject card){
		try{
			Map<ActionType, Integer> actionModifierMap = new HashMap<>();
			JSONArray actionModifiers = card.getJSONArray("actionModifiers");
			for(int i = 0; i < actionModifiers.length(); i++){
				JSONObject modifierObj = actionModifiers.getJSONObject(i);
				ActionType type = ActionType.valueOf(modifierObj.getString("type"));
				int modifier = modifierObj.getInt("modifier");
				actionModifierMap.put(type, modifier);
			}
			return actionModifierMap;
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

	private static List<Reward> getRewardList(JSONArray jsonRewards) throws JSONException{
		List<Reward> rewardList = new ArrayList<>();
		for(int i = 0; i < jsonRewards.length(); i++){
			JSONObject reward = jsonRewards.getJSONObject(i);
			rewardList.add(getReward(reward));
		}
		return rewardList;
	}

	private static Set<Reward> getRewardSet(JSONArray jsonRewards) throws JSONException{
		Set<Reward> rewardSet = new HashSet<>();
		for(int i = 0; i < jsonRewards.length(); i++){
			JSONObject reward = jsonRewards.getJSONObject(i);
			rewardSet.add(getReward(reward));
		}
		return rewardSet;
	}

	private static Reward getReward(JSONObject reward) throws JSONException{
		int quantity = reward.getInt("quantity");
		String type = reward.getString("type");
		return new Reward(RewardType.valueOf(type), quantity);
	}

	private static Integer getIntegerNoException(JSONObject obj, String name){
		try{
			return new Integer(obj.getInt(name));
		} catch(JSONException e){
			return 0;
		}
	}

	private static boolean getBooleanNoException(JSONObject obj, String name){
		try{
			return obj.getBoolean(name);
		} catch(JSONException e){
			return false;
		}
	}

	public static int getVaticanReportLength(int period) throws JSONException, IOException{
		JSONObject cards = fromPathToJSONObject(vaticanReportCardPath);
		JSONArray cardsArray = cards.getJSONArray("cards").getJSONObject(period - 1).getJSONArray("period");
		return cardsArray.length();
	}

	public static VaticanReportCard getVaticanReportCard(int period, int number) throws JSONException, IOException{
		JSONObject card = fromPathToJSONObject(vaticanReportCardPath);
		card = getPeriodAndNumberCard(period, number, card);
		String path = getPath(card);
		List<String> booleans = getBooleansAttributes(card);
		List<Reward> loseRewards = getLoseRewards(card);
		Map<ActionType, Integer> actionModifiers = getActionModifiers(card);
		int coloredFamilyMemberModifier = getIntegerNoException(card, "coloredFamilyMemberModifier");
		return new VaticanReportCard(number, period, path, loseRewards, actionModifiers, booleans, coloredFamilyMemberModifier);
	}
	
	private static List<String> getBooleansAttributes (JSONObject card){
		List<String> attributes = new ArrayList<>();
		try{
			JSONArray booleanEffects = card.getJSONArray("booleans");
			for(int i=0; i < booleanEffects.length(); i++){
				JSONObject obj = booleanEffects.getJSONObject(i);
				attributes.add(obj.getString("attribute"));
			}
		}
		catch(JSONException e){
			attributes = null;
		}
		return attributes;
	}

	public static int getPersonalBonusTileLength() throws JSONException, IOException{
		JSONObject cards = fromPathToJSONObject(personalBonusTilePath);
		JSONArray cardsArray = cards.getJSONArray("PersonalBonusTile");
		return cardsArray.length();
	}

	public static PersonalBonusTile getPersonalBonusTile (int number) throws IOException, JSONException{
		//estrae file
		JSONObject tile = fromPathToJSONObject(personalBonusTilePath);
		//estrae oggetto
		tile = getNumberTile(number, tile);
		//estrae parametri
		String path = getPath(tile);
		int diceProduction = tile.getInt("diceProduction");   
		int diceHarvest = tile.getInt("diceHarvest");   
		Set<Reward> productionRewards = getRewards(tile, "productionRewards");
		Set<Reward> harvestRewards = getRewards(tile, "harvestRewards");
		return new PersonalBonusTile(path, diceProduction, diceHarvest, productionRewards, harvestRewards);
	}
	
	public static JSONObject getNumberTile(int number, JSONObject jsonObject) throws JSONException{
		JSONArray tiles = jsonObject.getJSONArray("PersonalBonusTile");
		return tiles.getJSONObject(number);
	}
	
	
	public static Set<Reward> getRewards (JSONObject jsonObject, String type) throws JSONException{
		JSONArray rewards = jsonObject.getJSONArray(type);
		return getRewardSet(rewards);
	}

	public static int getPermanentLeaderCardLength() throws JSONException, IOException{
		JSONObject cards = fromPathToJSONObject(permanentLeaderCardPath);
		JSONArray cardsArray = cards.getJSONArray("cards");
		return cardsArray.length();
	}

	public static PermanentLeaderCard getPermanentLeaderCard(int number) throws JSONException, IOException{
		JSONObject card = fromPathToJSONObject(permanentLeaderCardPath);
		card = getNumberCard(number, card);

		String name = getName(card);
		String path = getPath(card);
		Set<Reward> activationRewardCost = getActivationRewardCost(card);
		Map<CardType, Integer> activationCardCost = getActivationCardCost(card);
		int neutralFamilyMemberModifier = getIntegerNoException(card, "neutralFamilyMemberModifier");
		int coloredFamilyMemberModifier = getIntegerNoException(card, "coloredFamilyMemberModifier");
		boolean doubleFastRewardDevelopmentCard = getBooleanNoException(card, "doubleFastRewardDevelopmentCard");
		boolean placeInBusyActionSpot = getBooleanNoException(card, "placeInBusyActionSpot");
		boolean notPayTollBusyTower = getBooleanNoException(card, "notPayTollBusyTower");
		boolean copyOtherCard = getBooleanNoException(card, "copyOtherCard");
		List<Reward> bonusRewardChurchSupport = getBonusRewardChurchSupport(card);
		boolean permanentDice = getBooleanNoException(card, "permanentDice");
		int permanentDiceValue = getIntegerNoException(card, "permanentDiceValue");
		Map<CardType, List<List<Reward>>> discounts = getDiscounts(card);
		boolean notSatisfyMilitaryPointForTerritory = getBooleanNoException(card, "notSatisfyMilitaryPointForTerritory");

		return new PermanentLeaderCard(name, path, activationRewardCost, activationCardCost, neutralFamilyMemberModifier,
				coloredFamilyMemberModifier, doubleFastRewardDevelopmentCard, placeInBusyActionSpot,
				bonusRewardChurchSupport, permanentDice, permanentDiceValue, discounts,
				notSatisfyMilitaryPointForTerritory, notPayTollBusyTower, copyOtherCard);
	}

	public static int getImmediateLeaderCardLength() throws JSONException, IOException{
		JSONObject cards = fromPathToJSONObject(immediateLeaderCardPath);
		JSONArray cardsArray = cards.getJSONArray("cards");
		return cardsArray.length();
	}

	public static ImmediateLeaderCard getImmediateLeaderCard(int number) throws JSONException, IOException{
		JSONObject card = fromPathToJSONObject(immediateLeaderCardPath);
		card = getNumberCard(number, card);

		String name = getName(card);
		String path = getPath(card);
		Set<Reward> activationRewardCost = getActivationRewardCost(card);
		Map<CardType, Integer> activationCardCost = getActivationCardCost(card);
		Set<Reward> earnings = getEarnings(card);
		boolean permanentColoredFamilyMember = getBooleanNoException(card, "permanentColoredFamilyMember");
		int permanentColoredFamilyMemberValue = getIntegerNoException(card, "permanentColoredFamilyMemberValue");
		List<FamilyMember> actions = getActions(card);

		return new ImmediateLeaderCard (name, path, activationRewardCost, activationCardCost, earnings,
				permanentColoredFamilyMember, permanentColoredFamilyMemberValue, actions);
	}

	private static Map<CardType, Integer> getActivationCardCost(JSONObject card){
		try{
			JSONArray activationCardTypeRequirement = card.getJSONArray("activationCardCost");
			return getCardTypeInteger(activationCardTypeRequirement);
		}
		catch (JSONException e) {
			return null;
		}
	}

	private static Map<CardType, Integer> getCardTypeInteger(JSONArray jsonArrayCardTypeInteger){
		try {
			Map<CardType, Integer> cardTypeInteger = new HashMap<>();
			for(int i = 0; i < jsonArrayCardTypeInteger.length(); i++){
				JSONObject jsonCardTypeInteger = jsonArrayCardTypeInteger.getJSONObject(i);
				cardTypeInteger.put(getCardType(jsonCardTypeInteger), jsonCardTypeInteger.getInt("quantity"));
			}
			return cardTypeInteger;
		}
		catch(JSONException e){
			return null;
		}
	}

	private static CardType getCardType(JSONObject jsonObject){
		try{
			String cardTypeString = jsonObject.getString("type");
			CardType cardType = CardType.valueOf(cardTypeString);
			return cardType;
		}
		catch(JSONException e){
			return null;
		}
	}

	private static JSONObject fromPathToJSONObject(String path) throws IOException, JSONException{
		return new JSONObject(readFileToString(new File(path)));
	}
}
