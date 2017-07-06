package it.polimi.ingsw.pc34.View.GUI;

import it.polimi.ingsw.pc34.Model.PlayerBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by trill on 22/06/2017.
 */
public class PlayerBoardView {
    private String username;
    private String color;

    // Rewards
    private String coin;
    private String wood;
    private String stone;
    private String servant;
    private String faithPoint;
    private String militaryPoint;
    private String victoryPoint;

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

    public PlayerBoardView(PlayerBoard toCopy){
        username = toCopy.getPlayer().getUsername();
        color = toCopy.getColor().toString();

        coin = "" + toCopy.getCounter().getCoin().getQuantity();
        wood = "" + toCopy.getCounter().getWood().getQuantity();
        stone = "" + toCopy.getCounter().getStone().getQuantity();
        servant = "" + toCopy.getCounter().getServant().getQuantity();
        faithPoint = "" + toCopy.getCounter().getFaithPoint().getQuantity();
        militaryPoint = "" + toCopy.getCounter().getMilitaryPoint().getQuantity();
        victoryPoint = "" + toCopy.getCounter().getVictoryPoint().getQuantity();

        territoryCards = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            if(i < toCopy.getTerritorySpot().getCards().size()){
                territoryCards.add(toCopy.getTerritorySpot().getCards().get(i).getPath());
            }
            else{
                territoryCards.add("");
            }
        }
        buildingCards = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            if(i < toCopy.getBuildingSpot().getCards().size()){
                buildingCards.add(toCopy.getBuildingSpot().getCards().get(i).getPath());
            }
            else{
                buildingCards.add("");
            }
        }
        characterCards= new ArrayList<>();
        for(int i = 0; i < 6; i++){
            if(i < toCopy.getCharacterSpot().getCards().size()){
                characterCards.add(toCopy.getCharacterSpot().getCards().get(i).getPath());
            }
            else{
                characterCards.add("");
            }
        }
        ventureCards = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            if(i < toCopy.getVentureSpot().getCards().size()){
                ventureCards.add(toCopy.getVentureSpot().getCards().get(i).getPath());
            }
            else{
                ventureCards.add("");
            }
        }

        leaderCards = new ArrayList<>();
        leaderCardsState = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            int inHand = toCopy.getLeaderCardsInHand().size();
            int immediatePlaced = toCopy.getImmediateLeaderCardsPositionated().size();
            int permanentPlaced = toCopy.getPermanentLeaderCardsPositionated().size();
            if(i < inHand){
                leaderCards.add(toCopy.getLeaderCardsInHand().get(i).getPath());
                leaderCardsState.add("IN HAND");
            }
            else if((i - inHand) < immediatePlaced){
                leaderCards.add(toCopy.getImmediateLeaderCardsPositionated().get(i - inHand).getPath());
                if(toCopy.getImmediateLeaderCardsPositionated().get(i - inHand).isActivated()){
                    leaderCardsState.add("ACTIVATED");
                }
                else{
                    leaderCardsState.add("PLACED");
                }
            }
            else if((i - inHand - immediatePlaced) < permanentPlaced){
                leaderCards.add(toCopy.getPermanentLeaderCardsPositionated().get(i - inHand - immediatePlaced).getPath());
                leaderCardsState.add("PLACED");
            }
            else{
                leaderCards.add("LeaderCard0.png");
                leaderCardsState.add("");
            }
        }

        familyMembers = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            if(toCopy.getFamilyMembers().get(i).isUsed()){
                familyMembers.add("");
            }
            else{
                familyMembers.add(toCopy.getPlayer().getColor() + toCopy.getFamilyMembers().get(i).getColor().toString() + ".png");
            }
        }

        if(toCopy.getPersonalBonusTile() == null){
            personalBonusTile = "";
        }
        else{
            personalBonusTile = toCopy.getPersonalBonusTile().getPath();
        }
    }

    public void setValues(PlayerBoardView other){
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

    public String getCoin(){
        return coin;
    }

    public String getWood(){
        return wood;
    }

    public String getStone(){
        return stone;
    }

    public String getServant(){
        return servant;
    }

    public String getFaithPoint(){
        return faithPoint;
    }

    public String getMilitaryPoint(){
        return militaryPoint;
    }

    public String getVictoryPoint(){
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

    public String getColor(){
        return color;
    }
}
