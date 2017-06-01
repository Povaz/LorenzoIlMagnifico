package it.polimi.ingsw.pcXX;

import java.util.ArrayList;
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
    private List<Point> losePoints = new ArrayList<>();
    private List<Resource> loseResources = new ArrayList<>();

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
}
