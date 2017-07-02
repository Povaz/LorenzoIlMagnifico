package it.polimi.ingsw.pc34.View.GUI;


import javafx.beans.property.*;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

/**
 * Created by trill on 22/06/2017.
 */
public class PersonalBoardView extends Observable{
    private final String username;

    // Rewards
    private IntegerProperty coin;
    private IntegerProperty wood;
    private IntegerProperty stone;
    private IntegerProperty servant;
    private IntegerProperty faithPoint;
    private IntegerProperty militaryPoint;
    private IntegerProperty victoryPoint;

    // Cards
    private ListProperty<String> territoryCards;
    private ListProperty<String> buildingCards;
    private ListProperty<String> characterCards;
    private ListProperty<String> ventureCards;
    private ListProperty<String> leaderCards;
    private ListProperty<String> leaderCardsState;
    private StringProperty personalBonusTile;

    // FamilyMembers
    private ListProperty<String> familyMembers;

    public PersonalBoardView(String username){
        this.username = username;
        coin = new SimpleIntegerProperty(0);
        wood = new SimpleIntegerProperty(0);
        stone = new SimpleIntegerProperty(0);
        servant = new SimpleIntegerProperty(0);
        faithPoint = new SimpleIntegerProperty(0);
        militaryPoint = new SimpleIntegerProperty(0);
        victoryPoint = new SimpleIntegerProperty(0);

        List<String> tCards = new ArrayList<>(Arrays.asList("", "", "", "", "", ""));
        ObservableList<String> obsTCards = FXCollections.observableArrayList(tCards);
        territoryCards = new SimpleListProperty<>(obsTCards);
        List<String> bCards = new ArrayList<>(Arrays.asList("", "", "", "", "", ""));
        ObservableList<String> obsBCards = FXCollections.observableArrayList(bCards);
        buildingCards = new SimpleListProperty<>(obsBCards);
        List<String> cCards = new ArrayList<>(Arrays.asList("", "", "", "", "", ""));
        ObservableList<String> obsCCards = FXCollections.observableArrayList(cCards);
        characterCards= new SimpleListProperty<>(obsCCards);
        List<String> vCards = new ArrayList<>(Arrays.asList("", "", "", "", "", ""));
        ObservableList<String> obsVCards = FXCollections.observableArrayList(vCards);
        ventureCards = new SimpleListProperty<>(obsVCards);

        List<String> leader = new ArrayList<>(Arrays.asList("", "", "", ""));
        ObservableList<String> obsLeader = FXCollections.observableArrayList(leader);
        leaderCards = new SimpleListProperty<>(obsLeader);
        List<String> leaderState = new ArrayList<>(Arrays.asList("", "", "", ""));
        ObservableList<String> obsLeaderState = FXCollections.observableArrayList(leaderState);
        leaderCardsState = new SimpleListProperty<>(obsLeaderState);

        List<String> family = new ArrayList<>(Arrays.asList("", "", "", "", "", ""));
        ObservableList<String> obsFamily = FXCollections.observableArrayList(family);
        familyMembers = new SimpleListProperty<>(obsFamily);

        personalBonusTile = new SimpleStringProperty("");
    }

    public String getUsername(){
        return username;
    }

    public int getCoin(){
        return coin.get();
    }

    public IntegerProperty coinProperty(){
        return coin;
    }

    public void setCoin(int coin){
        this.coin.set(coin);
    }

    public int getWood(){
        return wood.get();
    }

    public IntegerProperty woodProperty(){
        return wood;
    }

    public void setWood(int wood){
        this.wood.set(wood);
    }

    public int getStone(){
        return stone.get();
    }

    public IntegerProperty stoneProperty(){
        return stone;
    }

    public void setStone(int stone){
        this.stone.set(stone);
    }

    public int getServant(){
        return servant.get();
    }

    public IntegerProperty servantProperty(){
        return servant;
    }

    public void setServant(int servant){
        this.servant.set(servant);
    }

    public int getFaithPoint(){
        return faithPoint.get();
    }

    public IntegerProperty faithPointProperty(){
        return faithPoint;
    }

    public void setFaithPoint(int faithPoint){
        this.faithPoint.set(faithPoint);
    }

    public int getMilitaryPoint(){
        return militaryPoint.get();
    }

    public IntegerProperty militaryPointProperty(){
        return militaryPoint;
    }

    public void setMilitaryPoint(int militaryPoint){
        this.militaryPoint.set(militaryPoint);
    }

    public int getVictoryPoint(){
        return victoryPoint.get();
    }

    public IntegerProperty victoryPointProperty(){
        return victoryPoint;
    }

    public void setVictoryPoint(int victoryPoint){
        this.victoryPoint.set(victoryPoint);
    }

    public ObservableList<String> getTerritoryCards(){
        return territoryCards.get();
    }

    public ListProperty<String> territoryCardsProperty(){
        return territoryCards;
    }

    public void setTerritoryCards(ObservableList<String> territoryCards){
        this.territoryCards.set(territoryCards);
    }

    public ObservableList<String> getBuildingCards(){
        return buildingCards.get();
    }

    public ListProperty<String> buildingCardsProperty(){
        return buildingCards;
    }

    public void setBuildingCards(ObservableList<String> buildingCards){
        this.buildingCards.set(buildingCards);
    }

    public ObservableList<String> getCharacterCards(){
        return characterCards.get();
    }

    public ListProperty<String> characterCardsProperty(){
        return characterCards;
    }

    public void setCharacterCards(ObservableList<String> characterCards){
        this.characterCards.set(characterCards);
    }

    public ObservableList<String> getVentureCards(){
        return ventureCards.get();
    }

    public ListProperty<String> ventureCardsProperty(){
        return ventureCards;
    }

    public void setVentureCards(ObservableList<String> ventureCards){
        this.ventureCards.set(ventureCards);
    }

    public ObservableList<String> getFamilyMembers(){
        return familyMembers.get();
    }

    public ListProperty<String> familyMembersProperty(){
        return familyMembers;
    }

    public void setFamilyMembers(ObservableList<String> familyMembers){
        this.familyMembers.set(familyMembers);
    }

    public ObservableList<String> getLeaderCards(){
        return leaderCards.get();
    }

    public ListProperty<String> leaderCardsProperty(){
        return leaderCards;
    }

    public void setLeaderCards(ObservableList<String> leaderCards){
        this.leaderCards.set(leaderCards);
    }

    public ObservableList<String> getLeaderCardsState(){
        return leaderCardsState.get();
    }

    public ListProperty<String> leaderCardsStateProperty(){
        return leaderCardsState;
    }

    public void setLeaderCardsState(ObservableList<String> leaderCardsState){
        this.leaderCardsState.set(leaderCardsState);
    }

    public String getPersonalBonusTile(){
        return personalBonusTile.get();
    }

    public StringProperty personalBonusTileProperty(){
        return personalBonusTile;
    }

    public void setPersonalBonusTile(String personalBonusTile){
        this.personalBonusTile.set(personalBonusTile);
    }
}
