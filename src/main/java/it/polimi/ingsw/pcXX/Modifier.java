package it.polimi.ingsw.pcXX;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by trill on 23/05/2017.
 */
public class Modifier{

    // Leader card
    private int neutralFamilyMemberModifier = 0;
    private int coloredFamilyMemberModifier = 0;
    private boolean doubleFastRewardDevelopmentCard = false;
    private boolean placeInBusyActionSpot = false;

    private boolean permanentDice = false;
    private int permanentDiceValue = 0;

    private boolean notSatisfyMilitaryPointForTerritory = false;
    private boolean notPayTollBusyTower = false;

    private List<Reward> bonusChurchSupport = new ArrayList<>();

    // Scomuniche
    private List<Reward> losePoints = new ArrayList<>();
    private List<Reward> loseResources = new ArrayList<>();

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

    // Carte sviluppo
    private boolean noBonusTowerResource = false;
    private List<List<Reward>> territoryDiscount = new ArrayList<>();
    private List<List<Reward>> buildingDiscount = new ArrayList<>();
    private List<List<Reward>> characterDiscount = new ArrayList<>();
    private List<List<Reward>> ventureDiscount = new ArrayList<>();


    public Modifier(){

    }
    
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
    	modifierString += "harvestModifier : " + harvestModifier +  "\n";
    	modifierString += "productionModifier : " + productionModifier + "\n";
    	modifierString += "territoryTowerModifier : " + territoryTowerModifier + "\n";
    	modifierString += "buildingTowerModifier : " + buildingTowerModifier + "\n";
    	modifierString += "characterTowerModifier : " + characterTowerModifier + "\n";
    	modifierString += "ventureTowerModifier : " + ventureTowerModifier + "\n";
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
    	Iterator<Reward> iteratorLosePoints= losePoints.iterator();
    	while(iteratorLosePoints.hasNext()){
    	  element = (Reward) iteratorLosePoints.next();
    	  modifierString += "Lose Points n° " + contatore + " : " + element.toString() + "\n";
    	  contatore++;
    	}
    	contatore=1;
    	Iterator<Reward> iteratorLoseResources= loseResources.iterator();
    	while(iteratorLoseResources.hasNext()){
    	  element = (Reward) iteratorLoseResources.next();
    	  modifierString += "Lose Resources n° " + contatore + " : " + element.toString() + "\n";
    	  contatore++;
    	}
    	//Mancano i discount finali delle carte
    	
    	return modifierString;
    }
}
