package it.polimi.ingsw.pc34.View.GUI;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import javax.smartcardio.Card;
import java.util.List;

/**
 * Created by trill on 22/06/2017.
 */
public class PersonalBoardView{
    private StringProperty username;

    // Rewards
    private IntegerProperty coin;
    private IntegerProperty wood;
    private IntegerProperty stone;
    private IntegerProperty servant;
    private IntegerProperty faithPoint;
    private IntegerProperty militaryPoint;
    private IntegerProperty victoryPoint;

    // Cards
    private ListProperty<StringProperty> territoryCards;
    private ListProperty<StringProperty> buildingCards;
    private ListProperty<StringProperty> characterCards;
    private ListProperty<StringProperty> ventureCards;
    private List<String> leaderCards;
    private List<String> leaderCardsState;
    private String personalBonusTile;

    // FamilyMembers
    private ListProperty<StringProperty> familyMembers;

    public PersonalBoardView(){
        username = new SimpleStringProperty("");
        coin = new SimpleIntegerProperty(0);
        wood = new SimpleIntegerProperty(0);
        stone = new SimpleIntegerProperty(0);
        servant = new SimpleIntegerProperty(0);
        faithPoint = new SimpleIntegerProperty(0);
        militaryPoint = new SimpleIntegerProperty(0);
        victoryPoint = new SimpleIntegerProperty(0);

        territoryCards = new SimpleListProperty<>();
        buildingCards = new SimpleListProperty<>();
        characterCards= new SimpleListProperty<>();
        ventureCards = new SimpleListProperty<>();
        for(int i = 0; i < 6; i++){
            territoryCards.add(new SimpleStringProperty(""));
            buildingCards.add(new SimpleStringProperty(""));
            characterCards.add(new SimpleStringProperty(""));
            ventureCards.add(new SimpleStringProperty(""));
        }

        familyMembers = new SimpleListProperty<>();
        for(int i = 0; i < 3; i++){
            familyMembers.add(new SimpleStringProperty(""));
        }
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

    public ObservableList<StringProperty> getTerritoryCards(){
        return territoryCards.get();
    }

    public ListProperty<StringProperty> territoryCardsProperty(){
        return territoryCards;
    }

    public void setTerritoryCards(ObservableList<StringProperty> territoryCards){
        this.territoryCards.set(territoryCards);
    }

    public ObservableList<StringProperty> getBuildingCards(){
        return buildingCards.get();
    }

    public ListProperty<StringProperty> buildingCardsProperty(){
        return buildingCards;
    }

    public void setBuildingCards(ObservableList<StringProperty> buildingCards){
        this.buildingCards.set(buildingCards);
    }

    public ObservableList<StringProperty> getCharacterCards(){
        return characterCards.get();
    }

    public ListProperty<StringProperty> characterCardsProperty(){
        return characterCards;
    }

    public void setCharacterCards(ObservableList<StringProperty> characterCards){
        this.characterCards.set(characterCards);
    }

    public ObservableList<StringProperty> getVentureCards(){
        return ventureCards.get();
    }

    public ListProperty<StringProperty> ventureCardsProperty(){
        return ventureCards;
    }

    public void setVentureCards(ObservableList<StringProperty> ventureCards){
        this.ventureCards.set(ventureCards);
    }

    public ObservableList<StringProperty> getFamilyMembers(){
        return familyMembers.get();
    }

    public ListProperty<StringProperty> familyMembersProperty(){
        return familyMembers;
    }

    public void setFamilyMembers(ObservableList<StringProperty> familyMembers){
        this.familyMembers.set(familyMembers);
    }
}
