package it.polimi.ingsw.pc34.View.GUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by trill on 22/06/2017.
 */
public class PersonalBoardView{
    private final String username;

    // Rewards
    private int coin;
    private int wood;
    private int stone;
    private int servant;
    private int faithPoint;
    private int militaryPoint;
    private int victoryPoint;

    // Cards
    private List<String> territoryCards;
    private List<String> buildingCards;
    private List<String> characterCards;
    private List<String> ventureCards;
    private List<String> leaderCards;
    private List<String> leaderCardsState;
    private String personalBonusTile;

    // FamilyMembers
    private List<String> familyMembers;

    public PersonalBoardView(String username){
        this.username = username;
        coin = 0;
        wood = 0;
        stone = 0;
        servant = 0;
        faithPoint = 0;
        militaryPoint = 0;
        victoryPoint = 0;

        territoryCards = new ArrayList<>(Arrays.asList("", "", "", "", "", ""));
        buildingCards = new ArrayList<>(Arrays.asList("", "", "", "", "", ""));
        characterCards= new ArrayList<>(Arrays.asList("", "", "", "", "", ""));
        ventureCards = new ArrayList<>(Arrays.asList("", "", "", "", "", ""));

        leaderCards = new ArrayList<>(Arrays.asList("", "", "", ""));
        leaderCardsState = new ArrayList<>(Arrays.asList("", "", "", ""));

        familyMembers = new ArrayList<>(Arrays.asList("", "", ""));

        personalBonusTile = "";
    }

    public void setValues(PersonalBoardView other){
        this.coin = other.coin;
        this.wood = other.wood;
        this.stone = other.stone;
        this.servant = other.servant;
        this.faithPoint = other.faithPoint;
        this.militaryPoint = other.militaryPoint;
        this.victoryPoint = other.victoryPoint;

        this.territoryCards = other.territoryCards;
        this.buildingCards = other.buildingCards;
        this.characterCards= other.characterCards;
        this.ventureCards = other.ventureCards;

        this.leaderCards = other.leaderCards;
        this.leaderCardsState = other.leaderCardsState;

        this.familyMembers = other.familyMembers;

        this.personalBonusTile = other.personalBonusTile;
    }

    public String getUsername(){
        return username;
    }

    public int getCoin(){
        return coin;
    }

    public int getWood(){
        return wood;
    }

    public int getStone(){
        return stone;
    }

    public int getServant(){
        return servant;
    }

    public int getFaithPoint(){
        return faithPoint;
    }

    public int getMilitaryPoint(){
        return militaryPoint;
    }

    public int getVictoryPoint(){
        return victoryPoint;
    }

    public List<String> getTerritoryCards(){
        return territoryCards;
    }

    public List<String> getBuildingCards(){
        return buildingCards;
    }

    public List<String> getCharacterCards(){
        return characterCards;
    }

    public List<String> getVentureCards(){
        return ventureCards;
    }

    public List<String> getLeaderCards(){
        return leaderCards;
    }

    public List<String> getLeaderCardsState(){
        return leaderCardsState;
    }

    public String getPersonalBonusTile(){
        return personalBonusTile;
    }

    public List<String> getFamilyMembers(){
        return familyMembers;
    }

    public void setCoin(int coin){
        this.coin = coin;
    }
}
