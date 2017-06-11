package it.polimi.ingsw.pcXX;

import java.util.*;

/**
 * Created by trill on 23/05/2017.
 */
public class Modifier{

    // Scomuniche
    private final List<Reward> loseRewards = new ArrayList<>(); // TODO

    private boolean cannotPlaceInMarket = false;
    private boolean servantValueHalved = false;
    private boolean jumpFirstRound = false;

    private boolean notEarnVictoryPointFromTerritory = false;
    private boolean notEarnVictoryPointFromCharacter = false;
    private boolean notEarnVictoryPointFromVenture = false;
    private boolean loseVictoryPointFromVictoryPoint = false;
    private boolean loseVictoryPointFromMilitaryPoint = false;
    private boolean loseVictoryPointFromBuildingCost = false;
    private boolean loseVictoryPointFromResource = false;


	// Leader card permanenti
	private int neutralFamilyMemberModifier = 0;
	private boolean doubleFastRewardDevelopmentCard = false;
	private boolean placeInBusyActionSpot = false;

	private boolean permanentDice = false;
	private int permanentDiceValue = 0;

	private boolean notSatisfyMilitaryPointForTerritory = false;
	private boolean notPayTollBusyTower = false;

	private final List<Reward> bonusChurchSupport = new ArrayList<>(); // TODO


	// Carte sviluppo
	private boolean noBonusTowerResource = false;


	// Leader card permanenti + Carte sviluppo
	private final Map<CardType, List<List<Reward>>> discounts = new HashMap<>(); // TODO

	// Scomuniche + Leader card permanenti
	private int coloredFamilyMemberModifier = 0;

	// Scomuniche + Carte sviluppo
	private final Map<ActionType, Integer> actionModifiers = new HashMap<>();



    public Modifier(){
		discounts.put(CardType.TERRITORY, new LinkedList<>());
		discounts.put(CardType.BUILDING, new LinkedList<>());
		discounts.put(CardType.CHARACTER, new LinkedList<>());
		discounts.put(CardType.VENTURE, new LinkedList<>());

		actionModifiers.put(ActionType.HARVEST, 0);
		actionModifiers.put(ActionType.PRODUCE, 0);
		actionModifiers.put(ActionType.TERRITORY_TOWER, 0);
		actionModifiers.put(ActionType.BUILDING_TOWER, 0);
		actionModifiers.put(ActionType.CHARACTER_TOWER, 0);
		actionModifiers.put(ActionType.VENTURE_TOWER, 0);
		actionModifiers.put(ActionType.COUNCIL_PALACE, 0);
		actionModifiers.put(ActionType.MARKET, 0);
    }

    public void update(VaticanReportCard vaticanReportCard){
		this.coloredFamilyMemberModifier += vaticanReportCard.getColoredFamilyMemberModifier();

		for(Reward r : vaticanReportCard.getLoseRewards()){
			this.loseRewards.add(new Reward(r));
		}

		if(vaticanReportCard.isCannotPlaceInMarket()){
			this.cannotPlaceInMarket = true;
		}
		if(vaticanReportCard.isServantValueHalved()){
			this.servantValueHalved = true;
		}
		if(vaticanReportCard.isJumpFirstRound()){
			this.jumpFirstRound = true;
		}

		if(vaticanReportCard.isNotEarnVictoryPointFromTerritory()){
			this.notEarnVictoryPointFromTerritory = true;
		}
		if(vaticanReportCard.isNotEarnVictoryPointFromCharacter()){
			this.notEarnVictoryPointFromCharacter = true;
		}
		if(vaticanReportCard.isNotEarnVictoryPointFromVenture()){
			this.notEarnVictoryPointFromVenture = true;
		}
		if(vaticanReportCard.isLoseVictoryPointFromVictoryPoint()){
			this.loseVictoryPointFromVictoryPoint = true;
		}
		if(vaticanReportCard.isLoseVictoryPointFromMilitaryPoint()){
			this.loseVictoryPointFromMilitaryPoint = true;
		}
		if(vaticanReportCard.isLoseVictoryPointFromBuildingCost()){
			this.loseVictoryPointFromBuildingCost = true;
		}
		if(vaticanReportCard.isLoseVictoryPointFromResource()){
			this.loseVictoryPointFromResource = true;
		}

		updateActionModifiers(vaticanReportCard.getActionModifiers());
		updateLoseRewards(vaticanReportCard.getLoseRewards());
	}

	public void update(PermanentLeaderCard permanentLeaderCard){

		this.neutralFamilyMemberModifier += permanentLeaderCard.getNeutralFamilyMemberModifier();
		this.coloredFamilyMemberModifier += permanentLeaderCard.getColoredFamilyMemberModifier();

		if(permanentLeaderCard.isDoubleFastRewardDevelopmentCard()){
			this.doubleFastRewardDevelopmentCard = true;
		}
		if(permanentLeaderCard.isPlaceInBusyActionSpot()){
			this.placeInBusyActionSpot = true;
		}

		if(permanentLeaderCard.isPermanentDice()){
			this.permanentDice = true;
			this.permanentDiceValue = permanentLeaderCard.getPermanentDiceValue();
		}

		if(permanentLeaderCard.isNotSatisfyMilitaryPointForTerritory()){
			this.notSatisfyMilitaryPointForTerritory = true;
		}
		if(permanentLeaderCard.isNotPayTollBusyTower()){
			this.notPayTollBusyTower = true;
		}

		updateBonusRewardChurchSupport(permanentLeaderCard.getBonusRewardChurchSupport());
		updateCostDiscountDevelopmentCard(permanentLeaderCard.getCostDiscountDevelopmentCard());
	}

	public void update(DevelopmentCard developmentCard){
		if(developmentCard instanceof TerritoryCard){
			return;
		}
		if(developmentCard instanceof BuildingCard){
			return;
		}
		if(developmentCard instanceof CharacterCard){
			update((CharacterCard) developmentCard);
			return;
		}
		if(developmentCard instanceof VentureCard){
			return;
		}
	}

	private void update(CharacterCard characterCard){
		if(characterCard.isNoBonusTowerResource()){
			this.noBonusTowerResource = true;
		}

		updateActionModifiers(characterCard.getActionModifiers());
		updateCostDiscountDevelopmentCard(characterCard.getDiscounts());
	}


	private void updateActionModifiers(Map<ActionType, Integer> actionModifiersToAdd){
		for(ActionType aToAdd : actionModifiersToAdd.keySet()){
			if(aToAdd != null){
				if(aToAdd == ActionType.ALL){
					for(ActionType aT : actionModifiers.keySet()){
						actionModifiers.replace(aT, actionModifiers.get(aT) + actionModifiersToAdd.get(aToAdd));
					}
				}
				else if(aToAdd == ActionType.ANY_TOWER){
					actionModifiers.replace(ActionType.TERRITORY_TOWER, actionModifiers.get(ActionType.TERRITORY_TOWER) + actionModifiersToAdd.get(aToAdd));
					actionModifiers.replace(ActionType.BUILDING_TOWER, actionModifiers.get(ActionType.BUILDING_TOWER) + actionModifiersToAdd.get(aToAdd));
					actionModifiers.replace(ActionType.CHARACTER_TOWER, actionModifiers.get(ActionType.CHARACTER_TOWER) + actionModifiersToAdd.get(aToAdd));
					actionModifiers.replace(ActionType.VENTURE_TOWER, actionModifiers.get(ActionType.VENTURE_TOWER) + actionModifiersToAdd.get(aToAdd));
				}
				else{
					actionModifiers.replace(aToAdd, actionModifiers.get(aToAdd) + actionModifiersToAdd.get(aToAdd));
				}
			}
		}
	}

	private void updateLoseRewards(List<Reward> loseRewardsToAdd){
		addListToOtherList(loseRewards, loseRewardsToAdd);
	}

	private void updateBonusRewardChurchSupport(List<Reward> bonusToAdd){
		addListToOtherList(bonusChurchSupport, bonusToAdd);
	}

	private void addListToOtherList(List<Reward> list, List<Reward> listToAdd){
		for(int i = 0; i < listToAdd.size(); i++){
			boolean added = false;
			for(int j = 0; j < list.size(); j++){
				if(list.get(j).getType() == listToAdd.get(i).getType()){
					list.get(j).sumQuantity(listToAdd.get(i));
					added = true;
				}
			}
			if(!added){
				list.add(new Reward(listToAdd.get(i)));
			}
		}
	}

	// TODO TODOTODOTODO test!
	public void updateCostDiscountDevelopmentCard(Map<CardType, List<List<Reward>>> discountsToAdd){
		for(CardType cT : discountsToAdd.keySet()){
			if(cT != null){
				int nToCopy = discountsToAdd.get(cT).size();
				int length = discounts.get(cT).size();
				// copia gli elementi di discount tante quante sono le scelta da aggiungere
				for(int i = 0; i < nToCopy; i++){
					for(int j = 0; j < length; j++){
						discounts.get(cT).add(new ArrayList<>(discounts.get(cT).get(j)));
					}
				}
				// aggiungi ad ogni sconto una scelta in modo che non vengano sommati gli stessi sconti più volte
				for(int i = 0; i < nToCopy; i++){
					for(int j = i * length; j < (i + 1) * length ; j++){
						addListToOtherList(discounts.get(cT).get(j), discountsToAdd.get(cT).get(i));
					}
				}
				// elimina i doppioni
				for(int i = 0; i < discounts.get(cT).size() - 1; i++){
					for(int j = i + 1; j < discounts.get(cT).size(); j++){
						if(discounts.get(cT).get(i).equals(discounts.get(cT).get(j))){
							discounts.remove(j);
							j--;
						}
					}
				}
			}
		}
	}

    @Override
    public String toString(){
    	String modifierString = null;
    	modifierString += "neutralFamilyMemberModifier : " + neutralFamilyMemberModifier + "\n"; 
    	modifierString += "coloredFamilyMemberModifier : " + coloredFamilyMemberModifier + "\n";
    	modifierString += "doubleFastRewardDevelopmentCard : " + doubleFastRewardDevelopmentCard + "\n";
    	modifierString += "placeInBusyActionSpot : " +  placeInBusyActionSpot + "\n";
    	modifierString += "permanentDice : " + permanentDice + "\n";
    	modifierString += "permanentDiceValue : " + permanentDiceValue + "\n";
    	modifierString += "notSatisfyMilitaryPointForTerritory : " + notSatisfyMilitaryPointForTerritory + "\n";
    	modifierString += "notPayTollBusyTower : " + notPayTollBusyTower + "\n";
    	/*modifierString += "harvestModifier : " + harvestModifier +  "\n";
    	modifierString += "productionModifier : " + productionModifier + "\n";
    	modifierString += "territoryTowerModifier : " + territoryTowerModifier + "\n";
    	modifierString += "buildingTowerModifier : " + buildingTowerModifier + "\n";
    	modifierString += "characterTowerModifier : " + characterTowerModifier + "\n";
    	modifierString += "ventureTowerModifier : " + ventureTowerModifier + "\n";*/
    	modifierString += "cannotPlaceInMarket : " + cannotPlaceInMarket + "\n";
    	modifierString += "servantValueHalved : " + servantValueHalved + "\n";
    	modifierString += "jumpFirstRound : " + jumpFirstRound + "\n";
    	modifierString += "notEarnVictoryPointFromTerritory : " + notEarnVictoryPointFromTerritory + "\n";
    	modifierString += "notEarnVictoryPointFromCharacter : " + notEarnVictoryPointFromCharacter + "\n";
    	modifierString += "notEarnVictoryPointFromVenture : " + notEarnVictoryPointFromVenture + "\n";
    	modifierString += "loseVictoryPointFromVictoryPoint : " + loseVictoryPointFromVictoryPoint + "\n";
    	modifierString += "loseVictoryPointFromMilitaryPoint : " + loseVictoryPointFromMilitaryPoint + "\n";
    	modifierString += "loseVictoryPointFromBuildingCost : " + loseVictoryPointFromBuildingCost + "\n";
    	modifierString += "loseVictoryPointFromResource : " + loseVictoryPointFromResource + "\n";
    	modifierString += "noBonusTowerResource : " + noBonusTowerResource + "\n";
    	int contatore=1;
    	Reward element;
    	Iterator<Reward> iteratorBonusChurchSupport= bonusChurchSupport.iterator();
    	while(iteratorBonusChurchSupport.hasNext()){
    	  element = (Reward) iteratorBonusChurchSupport.next();
    	  modifierString += "Bonus Church Support n° " + contatore + " : " + element.toString() + "\n";
    	  contatore++;
    	}
    	contatore=1;
    	Iterator<Reward> iteratorLosePoints= loseRewards.iterator();
    	while(iteratorLosePoints.hasNext()){
    	  element = (Reward) iteratorLosePoints.next();
    	  modifierString += "Lose Points n° " + contatore + " : " + element.toString() + "\n";
    	  contatore++;
    	}
    	//Mancano i discount finali delle carte
    	
    	return modifierString;
    }

	public List<Reward> getLoseRewards() {
		return loseRewards;
	}

	public boolean isCannotPlaceInMarket() {
		return cannotPlaceInMarket;
	}

	public boolean isServantValueHalved() {
		return servantValueHalved;
	}

	public boolean isJumpFirstRound() {
		return jumpFirstRound;
	}

	public boolean isNotEarnVictoryPointFromTerritory() {
		return notEarnVictoryPointFromTerritory;
	}

	public boolean isNotEarnVictoryPointFromCharacter() {
		return notEarnVictoryPointFromCharacter;
	}

	public boolean isNotEarnVictoryPointFromVenture() {
		return notEarnVictoryPointFromVenture;
	}

	public boolean isLoseVictoryPointFromVictoryPoint() {
		return loseVictoryPointFromVictoryPoint;
	}

	public boolean isLoseVictoryPointFromMilitaryPoint() {
		return loseVictoryPointFromMilitaryPoint;
	}

	public boolean isLoseVictoryPointFromBuildingCost() {
		return loseVictoryPointFromBuildingCost;
	}

	public boolean isLoseVictoryPointFromResource() {
		return loseVictoryPointFromResource;
	}

	public int getNeutralFamilyMemberModifier() {
		return neutralFamilyMemberModifier;
	}

	public boolean isDoubleFastRewardDevelopmentCard() {
		return doubleFastRewardDevelopmentCard;
	}

	public boolean isPlaceInBusyActionSpot() {
		return placeInBusyActionSpot;
	}

	public boolean isPermanentDice() {
		return permanentDice;
	}

	public int getPermanentDiceValue() {
		return permanentDiceValue;
	}

	public boolean isNotSatisfyMilitaryPointForTerritory() {
		return notSatisfyMilitaryPointForTerritory;
	}

	public boolean isNotPayTollBusyTower() {
		return notPayTollBusyTower;
	}

	public List<Reward> getBonusChurchSupport() {
		return bonusChurchSupport;
	}

	public boolean isNoBonusTowerResource() {
		return noBonusTowerResource;
	}

	public Map<CardType, List<List<Reward>>> getDiscounts() {
		return discounts;
	}

	public int getColoredFamilyMemberModifier() {
		return coloredFamilyMemberModifier;
	}

	public Map<ActionType, Integer> getActionModifiers() {
		return actionModifiers;
	}
}
