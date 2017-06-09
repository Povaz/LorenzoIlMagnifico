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
				break;
			case "militaryPointsModifier":
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
				break;
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
		cardString+="militaryPointsModifier: "+ militaryPointsModifier + "\n";
		cardString+="coinModifier: "+ coinModifier + "\n";
		cardString+="servantModifier: "+ servantModifier + "\n";
		cardString+="woodModifier: "+ woodModifier + "\n";
		cardString+="stoneModifier: "+ stoneModifier + "\n";
		cardString+="harvestModifier: "+ harvestModifier + "\n";
		cardString+="productionModifier: "+ productionModifier + "\n";
		cardString+="territoryTowerModifier: "+ territoryTowerModifier + "\n";
		cardString+="buildingTowerModifier: "+ buildingTowerModifier + "\n";
		cardString+="characterTowerModifier: "+ characterTowerModifier + "\n";
		cardString+="ventureTowerModifier: "+ ventureTowerModifier + "\n";
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
		return cardString;
	}
	
	public boolean equals(Object one){
		if (this == one) return true;
		if (one == null || getClass() != one.getClass()) return false;
		if (!super.equals(one)) return false;

		VaticanReportCard that = (VaticanReportCard) one;
		if(period!=that.period){
			return false;
		}
		if(number!=that.number){
			return false;
		}
		if(coloredFamilyMemberModifier!=that.coloredFamilyMemberModifier){
			return false;
		}
		if(militaryPointsModifier!=that.militaryPointsModifier){
			return false;
		}
		if(coinModifier!=that.coinModifier){
			return false;
		}
		if(servantModifier!=that.servantModifier){
			return false;
		}
		if(woodModifier!=that.woodModifier){
			return false;
		}
		if(stoneModifier!=that.stoneModifier){
			return false;
		}
		if(harvestModifier!=that.harvestModifier){
			return false;
		}
		if(productionModifier!=that.productionModifier){
			return false;
		}
		if(territoryTowerModifier!=that.territoryTowerModifier){
			return false;
		}
		if(buildingTowerModifier!=that.buildingTowerModifier){
			return false;
		}
		if(characterTowerModifier!=that.characterTowerModifier){
			return false;
		}
		if(ventureTowerModifier!=that.ventureTowerModifier){
			return false;
		}
		if(cannotPlaceInMarket!=that.cannotPlaceInMarket){
			return false;
		}
		if(servantValueHalved!=that.servantValueHalved){
			return false;
		}
		if(jumpFirstRound!=that.jumpFirstRound){
			return false;
		}
		if(notEarnVictoryPointFromTerritory!=that.notEarnVictoryPointFromTerritory){
			return false;
		}
		if(notEarnVictoryPointFromCharacter!=that.notEarnVictoryPointFromCharacter){
			return false;
		}
		if(notEarnVictoryPointFromVenture!=that.notEarnVictoryPointFromVenture){
			return false;
		}
		if(loseVictoryPointFromVictoryPoint!=that.loseVictoryPointFromVictoryPoint){
			return false;
		}
		if(loseVictoryPointFromMilitaryPoint!=that.loseVictoryPointFromMilitaryPoint){
			return false;
		}
		if(loseVictoryPointFromBuildingCost!=that.loseVictoryPointFromBuildingCost){
			return false;
		}
		if(loseVictoryPointFromResource!=that.loseVictoryPointFromResource){
			return false;
		}
		return true;
	}
	
	
}
