package it.polimi.ingsw.pcXX;

import java.util.List;
import java.util.Map;

public class VaticanReportCard{
	private final int number;
	private final int period;

	private int coloredFamilyMemberModifier = 0;

	private List<Reward> loseRewards;
	private Map<ActionType, Integer> actionModifiers;

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

	public VaticanReportCard (int number, int period, List<Reward> loseRewards, Map<ActionType, Integer> actionModifiers,
							  String attribute, int value){
		this.number = number;
		this.period = period;
		this.loseRewards = loseRewards;
		this.actionModifiers = actionModifiers;
		switch(attribute){
			case "coloredFamilyMemberModifier":
				coloredFamilyMemberModifier = value;
				break;
			/*case "militaryPointsModifier":
				militaryPointsModifier = value;
				break;
			case "coinModifier":
				coinModifier = value;
				break;
			case "servantModifier":
				servantModifier = value;
				break;
			case "resourceModifier":
				woodModifier = value;
				stoneModifier = value;
				break;
			case "harvestModifier":
				harvestModifier = value;
				break;
			case "productionModifier":
				productionModifier = value;
				break;
			case "territoryTowerModifier":
				territoryTowerModifier = value;
				break;
			case "buildingTowerModifier":
				buildingTowerModifier = value;
				break;
			case "characterTowerModifier":
				characterTowerModifier = value;
				break;
			case "ventureTowerModifier":
				ventureTowerModifier = value;
				break;*/
			case "cannotPlaceInMarket":
				cannotPlaceInMarket = true;
				break;
			case "servantValueHalved":
				servantValueHalved = true;
				break;
			case "jumpFirstRound":
				jumpFirstRound = true;
				break;
			case "notEarnVictoryPointFromTerritory":
				notEarnVictoryPointFromTerritory = true;
				break;
			case "notEarnVictoryPointFromCharacter":
				notEarnVictoryPointFromCharacter = true;
				break;
			case "notEarnVictoryPointFromVenture":
				notEarnVictoryPointFromVenture = true;
				break;
			case "loseVictoryPointFromVictoryPoint":
				loseVictoryPointFromVictoryPoint = true;
				break;
			case "loseVictoryPointFromMilitaryPoint":
				loseVictoryPointFromMilitaryPoint = true;
				break;
			case "loseVictoryPointFromBuildingCost":
				loseVictoryPointFromBuildingCost = true;
				break;
			case "loseVictoryPointFromResource":
				loseVictoryPointFromResource = true;
				break;
		}
	}
	
	public String toString(){
		String cardString = super.toString();
		cardString+="\nperiod: "+ period + "\n";
		cardString+="number: "+ number + "\n";
		cardString+="coloredFamilyMemberModifier: "+ coloredFamilyMemberModifier +"\n";
		cardString+="cannotPlaceInMarket: "+ cannotPlaceInMarket + "\n";
		cardString+="servantValueHalved: "+ servantValueHalved + "\n";
		cardString+="jumpFirstRound: "+ jumpFirstRound + "\n";
		cardString+="notEarnVictoryPointFromTerritory: "+ notEarnVictoryPointFromTerritory + "\n";
		cardString+="notEarnVictoryPointFromCharacter: "+ notEarnVictoryPointFromCharacter + "\n";
		cardString+="notEarnVictoryPointFromVenture: "+ notEarnVictoryPointFromVenture + "\n";
		cardString+="loseVictoryPointFromVictoryPoint: "+ loseVictoryPointFromVictoryPoint + "\n";
		cardString+="loseVictoryPointFromMilitaryPoint: "+ loseVictoryPointFromMilitaryPoint + "\n";
		cardString+="loseVictoryPointFromBuildingCost: "+ loseVictoryPointFromBuildingCost + "\n";
		cardString+="loseVictoryPointFromResource: "+ loseVictoryPointFromResource + "\n";

		if(loseRewards != null){
			cardString += "loseRewards:  ";
			for(Reward r : loseRewards){
				cardString += r.toString() + ";  ";
			}
		}

		if(actionModifiers != null){
			cardString += "actionModifiers:\n";
			for(ActionType aT : actionModifiers.keySet()){
				cardString += aT + "  " + actionModifiers.get(aT) + "\n";
			}
		}

		return cardString;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		VaticanReportCard that = (VaticanReportCard) o;

		if (number != that.number) return false;
		if (period != that.period) return false;
		if (coloredFamilyMemberModifier != that.coloredFamilyMemberModifier) return false;
		if (cannotPlaceInMarket != that.cannotPlaceInMarket) return false;
		if (servantValueHalved != that.servantValueHalved) return false;
		if (jumpFirstRound != that.jumpFirstRound) return false;
		if (notEarnVictoryPointFromTerritory != that.notEarnVictoryPointFromTerritory) return false;
		if (notEarnVictoryPointFromCharacter != that.notEarnVictoryPointFromCharacter) return false;
		if (notEarnVictoryPointFromVenture != that.notEarnVictoryPointFromVenture) return false;
		if (loseVictoryPointFromVictoryPoint != that.loseVictoryPointFromVictoryPoint) return false;
		if (loseVictoryPointFromMilitaryPoint != that.loseVictoryPointFromMilitaryPoint) return false;
		if (loseVictoryPointFromBuildingCost != that.loseVictoryPointFromBuildingCost) return false;
		if (loseVictoryPointFromResource != that.loseVictoryPointFromResource) return false;
		if (!loseRewards.equals(that.loseRewards)) return false;
		return actionModifiers.equals(that.actionModifiers);
	}

	@Override
	public int hashCode() {
		int result = number;
		result = 31 * result + period;
		result = 31 * result + coloredFamilyMemberModifier;
		result = 31 * result + loseRewards.hashCode();
		result = 31 * result + actionModifiers.hashCode();
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
		return result;
	}

	public int getNumber() {
		return number;
	}

	public int getPeriod() {
		return period;
	}

	public int getColoredFamilyMemberModifier() {
		return coloredFamilyMemberModifier;
	}

	public List<Reward> getLoseRewards() {
		return loseRewards;
	}

	public Map<ActionType, Integer> getActionModifiers() {
		return actionModifiers;
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
}
