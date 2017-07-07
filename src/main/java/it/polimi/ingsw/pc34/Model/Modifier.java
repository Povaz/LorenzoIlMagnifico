package it.polimi.ingsw.pc34.Model;

import java.util.*;

/**
 * Created by trill on 23/05/2017.
 */
public class Modifier{

    // Scomuniche
    private final List<Reward> loseRewards = new ArrayList<>();

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

	private final List<Reward> bonusChurchSupport = new ArrayList<>();


	// Carte sviluppo
	private boolean noBonusTowerResource = false;


	// Leader card permanenti + Carte sviluppo
	private final Map<CardType, List<List<Reward>>> discounts = new HashMap<>();

	// Scomuniche + Leader card permanenti
	private int coloredFamilyMemberModifier = 0;

	// Scomuniche + Carte sviluppo
	private final Map<ActionType, Integer> actionModifiers = new HashMap<>();

    public Modifier(){
		discounts.put(CardType.TERRITORY, new LinkedList<>());
		discounts.put(CardType.BUILDING, new LinkedList<>());
		discounts.put(CardType.CHARACTER, new LinkedList<>());
		discounts.put(CardType.VENTURE, new LinkedList<>());

		// TODO elimina giù
		discounts.get(CardType.BUILDING).add(new ArrayList<>(Arrays.asList(new Reward(RewardType.STONE,1), new Reward(RewardType.COIN,2))));
		discounts.get(CardType.BUILDING).add(new ArrayList<>(Arrays.asList(new Reward(RewardType.STONE,2), new Reward(RewardType.WOOD,1))));

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
		updateCostDiscountDevelopmentCard(permanentLeaderCard.getDiscounts());
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


	void updateActionModifiers(Map<ActionType, Integer> actionModifiersToAdd){
		if(actionModifiersToAdd == null){
			return;
		}
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
		if(loseRewardsToAdd == null){
			return;
		}
		addListToOtherList(loseRewards, loseRewardsToAdd);
	}

	private void updateBonusRewardChurchSupport(List<Reward> bonusToAdd){
		if(bonusToAdd == null){
			return;
		}
		addListToOtherList(bonusChurchSupport, bonusToAdd);
	}

	void addListToOtherList(List<Reward> list, List<Reward> listToAdd){
		if(list == null || listToAdd == null){
			return;
		}
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

	void updateCostDiscountDevelopmentCard(Map<CardType, List<List<Reward>>> discountsToAdd){
		if(discountsToAdd == null){
			return;
		}
		for(CardType cT : discountsToAdd.keySet()){
			if(cT != null){
				int nToCopy = discountsToAdd.get(cT).size();
				int length = discounts.get(cT).size();
				// copia gli elementi di discount tante quante sono le scelta da aggiungere
				for(int i = 0; i < nToCopy - 1; i++){
					for(int j = 0; j < length; j++){
						List<Reward> copy = new ArrayList<>();
						for(Reward r : discounts.get(cT).get(j)){
							copy.add(new Reward(r));
						}
						discounts.get(cT).add(new ArrayList<>(copy));
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
						Set<Reward> temp1 = new HashSet<>(discounts.get(cT).get(i));
						Set<Reward> temp2 = new HashSet<>(discounts.get(cT).get(j));
						if(temp1.equals(temp2)){
							discounts.get(cT).remove(j);
							j--;
						}
					}
				}
			}
		}
	}

	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Modifier modifier = (Modifier) o;

		if (cannotPlaceInMarket != modifier.cannotPlaceInMarket) return false;
		if (servantValueHalved != modifier.servantValueHalved) return false;
		if (jumpFirstRound != modifier.jumpFirstRound) return false;
		if (notEarnVictoryPointFromTerritory != modifier.notEarnVictoryPointFromTerritory) return false;
		if (notEarnVictoryPointFromCharacter != modifier.notEarnVictoryPointFromCharacter) return false;
		if (notEarnVictoryPointFromVenture != modifier.notEarnVictoryPointFromVenture) return false;
		if (loseVictoryPointFromVictoryPoint != modifier.loseVictoryPointFromVictoryPoint) return false;
		if (loseVictoryPointFromMilitaryPoint != modifier.loseVictoryPointFromMilitaryPoint) return false;
		if (loseVictoryPointFromBuildingCost != modifier.loseVictoryPointFromBuildingCost) return false;
		if (loseVictoryPointFromResource != modifier.loseVictoryPointFromResource) return false;
		if (neutralFamilyMemberModifier != modifier.neutralFamilyMemberModifier) return false;
		if (doubleFastRewardDevelopmentCard != modifier.doubleFastRewardDevelopmentCard) return false;
		if (placeInBusyActionSpot != modifier.placeInBusyActionSpot) return false;
		if (permanentDice != modifier.permanentDice) return false;
		if (permanentDiceValue != modifier.permanentDiceValue) return false;
		if (notSatisfyMilitaryPointForTerritory != modifier.notSatisfyMilitaryPointForTerritory) return false;
		if (notPayTollBusyTower != modifier.notPayTollBusyTower) return false;
		if (noBonusTowerResource != modifier.noBonusTowerResource) return false;
		if (coloredFamilyMemberModifier != modifier.coloredFamilyMemberModifier) return false;
		if (!loseRewards.equals(modifier.loseRewards)) return false;
		if (!bonusChurchSupport.equals(modifier.bonusChurchSupport)) return false;
		if (!discounts.equals(modifier.discounts)) return false;
		return actionModifiers.equals(modifier.actionModifiers);
	}

	@Override
	public int hashCode(){
		int result = loseRewards.hashCode();
		result = 31 * result + (cannotPlaceInMarket ? 1 : 0);
		result = 31 * result + (servantValueHalved ? 1 : 0);
		result = 31 * result + (jumpFirstRound ? 1 : 0);
		result = 31 * result + (notEarnVictoryPointFromTerritory ? 1 : 0);
		result = 31 * result + (notEarnVictoryPointFromCharacter ? 1 : 0);
		result = 31 * result + (notEarnVictoryPointFromVenture ? 1 : 0);
		result = 31 * result + (loseVictoryPointFromVictoryPoint ? 1 : 0);
		result = 31 * result + (loseVictoryPointFromMilitaryPoint ? 1 : 0);
		result = 31 * result + (loseVictoryPointFromBuildingCost ? 1 : 0);
		result = 31 * result + (loseVictoryPointFromResource ? 1 : 0);
		result = 31 * result + neutralFamilyMemberModifier;
		result = 31 * result + (doubleFastRewardDevelopmentCard ? 1 : 0);
		result = 31 * result + (placeInBusyActionSpot ? 1 : 0);
		result = 31 * result + (permanentDice ? 1 : 0);
		result = 31 * result + permanentDiceValue;
		result = 31 * result + (notSatisfyMilitaryPointForTerritory ? 1 : 0);
		result = 31 * result + (notPayTollBusyTower ? 1 : 0);
		result = 31 * result + bonusChurchSupport.hashCode();
		result = 31 * result + (noBonusTowerResource ? 1 : 0);
		result = 31 * result + discounts.hashCode();
		result = 31 * result + coloredFamilyMemberModifier;
		result = 31 * result + actionModifiers.hashCode();
		return result;
	}

	@Override
    public String toString(){
    	StringBuilder bld = new StringBuilder();
    	bld.append("neutralFamilyMemberModifier: " + neutralFamilyMemberModifier + "\n");
		bld.append("coloredFamilyMemberModifier: " + coloredFamilyMemberModifier + "\n");
		bld.append("doubleFastRewardDevelopmentCard: " + doubleFastRewardDevelopmentCard + "\n");
		bld.append("placeInBusyActionSpot: " +  placeInBusyActionSpot + "\n");
		bld.append("permanentDice: " + permanentDice + "\n");
		bld.append("permanentDiceValue: " + permanentDiceValue + "\n");
		bld.append("notSatisfyMilitaryPointForTerritory: " + notSatisfyMilitaryPointForTerritory + "\n");
		bld.append("notPayTollBusyTower: " + notPayTollBusyTower + "\n");
		bld.append("cannotPlaceInMarket: " + cannotPlaceInMarket + "\n");
		bld.append("servantValueHalved: " + servantValueHalved + "\n");
		bld.append("jumpFirstRound: " + jumpFirstRound + "\n");
		bld.append("notEarnVictoryPointFromTerritory: " + notEarnVictoryPointFromTerritory + "\n");
		bld.append("notEarnVictoryPointFromCharacter: " + notEarnVictoryPointFromCharacter + "\n");
		bld.append("notEarnVictoryPointFromVenture: " + notEarnVictoryPointFromVenture + "\n");
		bld.append("loseVictoryPointFromVictoryPoint: " + loseVictoryPointFromVictoryPoint + "\n");
		bld.append("loseVictoryPointFromMilitaryPoint: " + loseVictoryPointFromMilitaryPoint + "\n");
		bld.append("loseVictoryPointFromBuildingCost: " + loseVictoryPointFromBuildingCost + "\n");
		bld.append("loseVictoryPointFromResource: " + loseVictoryPointFromResource + "\n");
		bld.append("noBonusTowerResource: " + noBonusTowerResource + "\n");

		bld.append("bonusChurchSupport:\n");
    	for(Reward r : bonusChurchSupport){
			bld.append("  " + r.toString() + "\n");
		}

		bld.append("loseRewards:\n");
    	for(Reward r : loseRewards){
			bld.append("  " + r.toString() + "\n");
		}

		bld.append("actionModifiers:\n");
    	for(ActionType aT : actionModifiers.keySet()){
			bld.append("  " + aT.toString() + ": " + actionModifiers.get(aT) + "\n");
		}

		bld.append("discounts:\n");
    	for(CardType cT : discounts.keySet()){
			bld.append("  " + cT.toString() + ":\n");
    		int i = 0;
    		for(List<Reward> l : discounts.get(cT)){
				bld.append("    " + i + ".");
    			for(Reward r : l){
					bld.append("  " + r.toString() + ";");
				}
				bld.append("\n");
				i++;
			}
		}

    	return bld.toString();
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

	public void setCannotPlaceInMarket(boolean cannotPlaceInMarket) {
		this.cannotPlaceInMarket = cannotPlaceInMarket;
	}

	public void setServantValueHalved(boolean servantValueHalved) {
		this.servantValueHalved = servantValueHalved;
	}

	public void setJumpFirstRound(boolean jumpFirstRound) {
		this.jumpFirstRound = jumpFirstRound;
	}

	public void setNotEarnVictoryPointFromTerritory(boolean notEarnVictoryPointFromTerritory) {
		this.notEarnVictoryPointFromTerritory = notEarnVictoryPointFromTerritory;
	}

	public void setNotEarnVictoryPointFromCharacter(boolean notEarnVictoryPointFromCharacter) {
		this.notEarnVictoryPointFromCharacter = notEarnVictoryPointFromCharacter;
	}

	public void setNotEarnVictoryPointFromVenture(boolean notEarnVictoryPointFromVenture) {
		this.notEarnVictoryPointFromVenture = notEarnVictoryPointFromVenture;
	}

	public void setLoseVictoryPointFromVictoryPoint(boolean loseVictoryPointFromVictoryPoint) {
		this.loseVictoryPointFromVictoryPoint = loseVictoryPointFromVictoryPoint;
	}

	public void setLoseVictoryPointFromMilitaryPoint(boolean loseVictoryPointFromMilitaryPoint) {
		this.loseVictoryPointFromMilitaryPoint = loseVictoryPointFromMilitaryPoint;
	}

	public void setLoseVictoryPointFromBuildingCost(boolean loseVictoryPointFromBuildingCost) {
		this.loseVictoryPointFromBuildingCost = loseVictoryPointFromBuildingCost;
	}

	public void setLoseVictoryPointFromResource(boolean loseVictoryPointFromResource) {
		this.loseVictoryPointFromResource = loseVictoryPointFromResource;
	}

	public void setNeutralFamilyMemberModifier(int neutralFamilyMemberModifier) {
		this.neutralFamilyMemberModifier = neutralFamilyMemberModifier;
	}

	public void setDoubleFastRewardDevelopmentCard(boolean doubleFastRewardDevelopmentCard) {
		this.doubleFastRewardDevelopmentCard = doubleFastRewardDevelopmentCard;
	}

	public void setPlaceInBusyActionSpot(boolean placeInBusyActionSpot) {
		this.placeInBusyActionSpot = placeInBusyActionSpot;
	}

	public void setPermanentDice(boolean permanentDice) {
		this.permanentDice = permanentDice;
	}

	public void setPermanentDiceValue(int permanentDiceValue) {
		this.permanentDiceValue = permanentDiceValue;
	}

	public void setNotSatisfyMilitaryPointForTerritory(boolean notSatisfyMilitaryPointForTerritory) {
		this.notSatisfyMilitaryPointForTerritory = notSatisfyMilitaryPointForTerritory;
	}

	public void setNotPayTollBusyTower(boolean notPayTollBusyTower) {
		this.notPayTollBusyTower = notPayTollBusyTower;
	}

	public void setNoBonusTowerResource(boolean noBonusTowerResource) {
		this.noBonusTowerResource = noBonusTowerResource;
	}

	public void setColoredFamilyMemberModifier(int coloredFamilyMemberModifier) {
		this.coloredFamilyMemberModifier = coloredFamilyMemberModifier;
	}
}
