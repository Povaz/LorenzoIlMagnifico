package it.polimi.ingsw.pcXX;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.*;

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

	/*public static void main(String[] args) {
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
			System.out.println(v2 + "\n");

			System.out.println(checkLogin("lacieoz", "LoL"));
			System.out.println(checkRegister("lacieoz", "LoL"));
		} catch(Exception e){
			e.printStackTrace();
		}
	}*/

	public static synchronized boolean checkLogin(String username, String password) throws JSONException, IOException{
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
		Set<Reward> fastRewards = getFastRewards(card);
		int diceHarvestAction = card.getInt("diceHarvestAction");
		Set<Reward> earnings = getEarnings(card);

		return new TerritoryCard(name, period, fastRewards, diceHarvestAction, earnings);
	}

	private static BuildingCard getBuildingCard(int period, int number) throws JSONException, IOException{
		JSONObject card = fromPathToJSONObject(buildingCardPath);
		card = getPeriodAndNumberCard(period, number, card);

		String name = getName(card);
		Set<Reward> costs = getCosts(card);
		Set<Reward> fastRewards = getFastRewards(card);
		int diceProductionAction = card.getInt("diceProductionAction");
		Set<Reward> earnings = getEarnings(card);
		List<Trade> trades = getTrades(card);
		RewardForReward rewardForReward = getRewardForReward(card);
		RewardForCard rewardForCard = getRewardForCard(card);

		return new BuildingCard(name, period, costs, fastRewards, diceProductionAction, earnings, trades, rewardForReward,
				rewardForCard);
	}

	private static CharacterCard getCharacterCard(int period, int number) throws JSONException, IOException{
		JSONObject card = fromPathToJSONObject(characterCardPath);
		card = getPeriodAndNumberCard(period, number, card);

		String name = getName(card);
		Set<Reward> costs = getCosts(card);
		Set<Reward> fastRewards = getFastRewards(card);
		List<GhostFamilyMember> actions = getActions(card);
		boolean noBonusTowerResource = getBooleanNoException(card, "noBonusTowerResource");
		List<CostDiscount> discounts = getDiscounts(card);
		List<ActionModifier> actionModifiers = getActionModifiers(card);
		RewardForReward rewardForReward = getRewardForReward(card);
		RewardForCard rewardForCard = getRewardForCard(card);

		return new CharacterCard(name, period, costs, fastRewards, actions, noBonusTowerResource, discounts, actionModifiers,
				rewardForReward, rewardForCard);
	}

	private static VentureCard getVentureCard(int period, int number) throws JSONException, IOException{
		JSONObject card = fromPathToJSONObject(ventureCardPath);
		card = getPeriodAndNumberCard(period, number, card);

		String name = getName(card);
		Set<Reward> costs = getCosts(card);
		Set<Reward> fastRewards = getFastRewards(card);
		List<GhostFamilyMember> actions = getActions(card);
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

	private static List<GhostFamilyMember> getActions(JSONObject card){
		try{
			List<GhostFamilyMember> actionList = new LinkedList<>();
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

	private static List<CostDiscount> getDiscounts(JSONObject card){
		try{
			JSONArray costDiscounts = card.getJSONArray("costDiscounts");
			List<CostDiscount> discountList = new LinkedList<>();
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

	private static List<ActionModifier> getActionModifiers(JSONObject card){
		try{
			List<ActionModifier> actionModifierList = new LinkedList<>();
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
	
	//Codice Di Lacieoz
	public static VaticanReportCard getVaticanReportCard(int period, int number) throws JSONException, IOException {
		JSONObject card = fromPathToJSONObject(vaticanReportCardPath);
		card = getPeriodAndNumberCard(period, number-1, card);
		String attribute = getAttribute(card);
		int value = getValue(card);
		
		return new VaticanReportCard(number, period, attribute, value);
	}
	
	private static String getAttribute (JSONObject card) throws JSONException {
		return card.getString("attribute");
	}
	
	private static int getValue (JSONObject card) throws JSONException {
		return card.getInt("value");
	}
	//Fine Codice Di Lacieoz

	//Codice di Povaz
	public static PermanentLeaderCard getPermanentLeaderCard (int index) throws JSONException, IOException {
		JSONObject permanentLeaderCard = fromPathToJSONObject(permanentLeaderCardPath);

		try {
			JSONArray permanentLeaderCards = permanentLeaderCard.getJSONArray("PermanentLeaderCard");
			permanentLeaderCard = permanentLeaderCards.getJSONObject(index);
		}
		catch (JSONException e) {
			System.out.println("Index Error");
			return null;
		}

		String name = getName(permanentLeaderCard);
		Set<Reward> activationRewardRequirement = getRewardSetWithException(permanentLeaderCard, "activationRewardRequirement");
		Map<CardType, Integer> activationCardTypeRequirement = getActivationCardTypeRequirement(permanentLeaderCard);
		int neutralFamilyMemberModifier = getIntegerNoException(permanentLeaderCard, "neutralFamilyMemberModifier");
		int coloredFamilyMemberModifier = getIntegerNoException(permanentLeaderCard, "coloredFamilyMemberModifier");
		boolean doubleFastRewardDevelopmentCard = getBooleanNoException(permanentLeaderCard, "doubleFastRewardDevelopmentCard");
		boolean placeInBusyActionSpot = getBooleanNoException(permanentLeaderCard, "placeInBusyActionSpot");
		Set<Reward> bonusRewardChurchSupport = getRewardSetWithException(permanentLeaderCard, "bonusRewardChurchSupport");
		boolean permanentDice = getBooleanNoException(permanentLeaderCard, "permanentDice");
		int permanentDiceValue = getIntegerNoException(permanentLeaderCard, "permanentDiceValue");
		Set<Reward> costDiscountDevelopmentCard = getRewardSetWithException(permanentLeaderCard, "costDiscountDevelopmentCard");
		boolean notSatisfyMilitaryPointForTerritory = getBooleanNoException(permanentLeaderCard, "notSatisfyMilitaryPointForTerritory");
		boolean notPayTollBusyTower = getBooleanNoException(permanentLeaderCard, "notPayTollBusyTower");
		boolean copyOtherCard = getBooleanNoException(permanentLeaderCard, "copyOtherCard");

		return new PermanentLeaderCard(name, activationRewardRequirement, activationCardTypeRequirement, neutralFamilyMemberModifier, coloredFamilyMemberModifier,
				doubleFastRewardDevelopmentCard, placeInBusyActionSpot, bonusRewardChurchSupport, permanentDice, permanentDiceValue, costDiscountDevelopmentCard, notSatisfyMilitaryPointForTerritory,
				notPayTollBusyTower, copyOtherCard);

	}

	public static Set<Reward> getRewardSetWithException (JSONObject obj, String name) throws JSONException {
		Set<Reward> rewardSet = new HashSet<>();
		try {
			rewardSet = getRewardSet(obj.getJSONArray(name));
		}
		catch (JSONException e) {
			Resource blankResource = new Resource(ResourceType.COIN, 0);
			rewardSet.add(blankResource);
			Point blankPoint = new Point(PointType.VICTORY_POINT, 0);
			rewardSet.add(blankPoint);
		}
		return rewardSet;
	}

	public static Map<CardType, Integer> getActivationCardTypeRequirement (JSONObject leaderCard) throws JSONException {
		try {
			JSONArray activationCardTypeRequirement = leaderCard.getJSONArray("activationCardCost");
			return getCardTypeRequirementMap(activationCardTypeRequirement);
		}
		catch (JSONException e) {
			return null;
		}
	}

	public static Map<CardType, Integer> getCardTypeRequirementMap (JSONArray jsonCardTypeRequirementMap) throws JSONException {
		try {
			Map<CardType, Integer> cardTypeRequirementMap = new HashMap<CardType, Integer>();
			for (int i = 0; i < jsonCardTypeRequirementMap.length(); i++) {
				JSONObject cardTypeRequirement = jsonCardTypeRequirementMap.getJSONObject(i);
				cardTypeRequirementMap.put(getCardType(cardTypeRequirement), getQuantity(cardTypeRequirement));
			}
			return cardTypeRequirementMap;
		}
		catch (JSONException e) {
			return null;
		}
	}

	public static CardType getCardType (JSONObject cardTypeRequirement) throws JSONException {
		try {
			String cardTypeRequirementString = cardTypeRequirement.getString("type");
			CardType cardType = CardType.valueOf(cardTypeRequirementString);
			return cardType;
		}
		catch (JSONException e) {
			return null;
		}
	}

	public static int getQuantity (JSONObject cardTypeRequirement) throws JSONException {
		try {
			return cardTypeRequirement.getInt("quantity");
		}
		catch (JSONException e) {
			return 0;
		}
	}

	public static ImmediateLeaderCard getImmediateLeaderCard (int index) throws JSONException, IOException {
		JSONObject immediateLeaderCard = fromPathToJSONObject(immediateLeaderCardPath);

		try {
			JSONArray immediateLeaderCards = immediateLeaderCard.getJSONArray("ImmediateLeaderCard");
			immediateLeaderCard = immediateLeaderCards.getJSONObject(index);
		}
		catch (JSONException e) {
			System.out.println("Index error");
		}

		String name = getName(immediateLeaderCard);
		Set<Reward> activationRewardRequirement = getRewardSetWithException(immediateLeaderCard, "activationRewardRequirement");
		Map<CardType, Integer> activationCardTypeRequirement = getActivationCardTypeRequirement(immediateLeaderCard);
		boolean activated = getBooleanNoException(immediateLeaderCard,"activated");
		Set<Reward> reward = getRewardSetWithException(immediateLeaderCard,"reward");
		boolean changeColoredFamilyMemberValue = getBooleanNoException(immediateLeaderCard, "changeColoredFamilyMemberValue");
		int newValueColoredFamilyMemberValue = getIntegerNoException(immediateLeaderCard, "newValueColoredFamilyMember");
		List<GhostFamilyMember> actions = getActions(immediateLeaderCard);

		return new ImmediateLeaderCard(name, activationRewardRequirement, activationCardTypeRequirement, activated, reward, changeColoredFamilyMemberValue,
				newValueColoredFamilyMemberValue, actions);
	}
	//Fine codice Povaz

	private static JSONObject fromPathToJSONObject(String path) throws IOException, JSONException{
		return new JSONObject(readFileToString(new File(path)));
	}
}
