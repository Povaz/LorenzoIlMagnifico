package it.polimi.ingsw.pcXX;

public class VaticanReportCard {
	private int number;
	private int period;
	private int coloredFamilyMemberModifier = 0;
	private int militaryPointsModifier = 0;
	private int coinModifier = 0;
	private int servantModifier = 0;
	private int woodModifier = 0;
	private int stoneModifier = 0;
	private int harvestModifier = 0;
	private int productionModifier = 0;
	private int territoryTowerModifier = 0;
	private int buildingTowerModifier = 0;
	private int characterTowerModifier = 0;
	private int ventureTowerModifier = 0;
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
	
	public VaticanReportCard (int number, int period, String attribute, int value){
		
		this.number = number;
		this.period = period;
		switch(attribute){
			case "coloredFamilyMemberModifier":
				coloredFamilyMemberModifier = value;
			case "militaryPointsModifier":
				militaryPointsModifier = value;
			case "coinModifier":
				coinModifier = value;
			case "servantModifier":
				servantModifier = value;
			case "resourceModifier":
				woodModifier = value;
				stoneModifier = value;
			case "harvestModifier":
				harvestModifier = value;
			case "productionModifier":
				productionModifier = value;
			case "territoryTowerModifier":
				territoryTowerModifier = value;
			case "buildingTowerModifier":
				buildingTowerModifier = value;
			case "characterTowerModifier":
				characterTowerModifier = value;
			case "ventureTowerModifier":
				ventureTowerModifier = value;
			case "cannotPlaceInMarket":
				cannotPlaceInMarket = true;
			case "servantValueHalved":
				servantValueHalved = true;
			case "jumpFirstRound":
				jumpFirstRound = true;
			case "notEarnVictoryPointFromTerritory":
				notEarnVictoryPointFromTerritory = true;
			case "notEarnVictoryPointFromCharacter":
				notEarnVictoryPointFromCharacter = true;
			case "notEarnVictoryPointFromVenture":
				notEarnVictoryPointFromVenture = true;
			case "loseVictoryPointFromVictoryPoint":
				loseVictoryPointFromVictoryPoint = true;
			case "loseVictoryPointFromMilitaryPoint":
				loseVictoryPointFromMilitaryPoint = true;
			case "loseVictoryPointFromBuildingCost":
				loseVictoryPointFromBuildingCost = true;
			case "loseVictoryPointFromResource":
				loseVictoryPointFromResource = true;
		}
		
	}
	
	public int getPeriod(){
		return period;
	}
	
	public int getNumber(){
		return number;
	}
	
	public int getMilitaryPointsModifier(){
		return militaryPointsModifier;
	}
}
