package it.polimi.ingsw.pc34.Model;

import java.util.List;
import java.util.Map;

public class VaticanReportCard{
	private final int number;
	private final int period;

	private final int coloredFamilyMemberModifier;

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
							  List<String> booleans, int coloredFamilyMemberModifier){
		this.number = number;
		this.period = period;
		this.loseRewards = loseRewards;
		this.actionModifiers = actionModifiers;
		this.coloredFamilyMemberModifier = coloredFamilyMemberModifier;
		if(booleans!=(null)){
			for(int i=0; i < booleans.size(); i++){
				switch(booleans.get(i)){
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
		}
		
	}
	
	public String toString(){
		StringBuilder bld = new StringBuilder();
		bld.append("    Period: "+ period + "\n");
		bld.append("    Number: "+ number + "\n");

		if(coloredFamilyMemberModifier != 0){
			bld.append("    coloredFamilyMemberModifier: " + coloredFamilyMemberModifier + "\n");
		}
		if(cannotPlaceInMarket){
			bld.append("    cannotPlaceInMarket: true\n");
		}
		if(servantValueHalved){
			bld.append("    servantValueHalved: true\n");
		}
		if(jumpFirstRound){
			bld.append("    jumpFirstRound: true\n");
		}
		if(notEarnVictoryPointFromTerritory){
			bld.append("    notEarnVictoryPointFromTerritory: true\n");
		}
		if(notEarnVictoryPointFromCharacter){
			bld.append("    notEarnVictoryPointFromCharacter: true\n");
		}
		if(notEarnVictoryPointFromVenture){
			bld.append("    notEarnVictoryPointFromVenture: true\n");
		}
		if(loseVictoryPointFromVictoryPoint){
			bld.append("    loseVictoryPointFromVictoryPoint: true\n");
		}
		if(loseVictoryPointFromMilitaryPoint){
			bld.append("    loseVictoryPointFromMilitaryPoint: true\n");
		}
		if(loseVictoryPointFromBuildingCost){
			bld.append("    loseVictoryPointFromBuildingCost: true\n");
		}
		if(loseVictoryPointFromResource){
			bld.append("    loseVictoryPointFromResource: true\n");
		}

		if(loseRewards != null){
			bld.append("    LoseRewards:  ");
			for(Reward r : loseRewards){
				bld.append(r.toString() + "; ");
			}
			bld.append("\n");
		}

		if(actionModifiers != null){
			bld.append("    Action modifiers:\n");
			for(ActionType aT : actionModifiers.keySet()){
				bld.append("      " + aT + "  " + actionModifiers.get(aT) + "\n");
			}
		}

		return bld.toString();
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
