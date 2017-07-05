package it.polimi.ingsw.pc34.View.GUI;

import it.polimi.ingsw.pc34.Model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by trill on 01/07/2017.
 */
public class BoardView implements Serializable{
    private List<PlayerBoardView> players;

    private int turn;

    private List<String> territoryCards;
    private List<String> buildingCards;
    private List<String> characterCards;
    private List<String> ventureCards;

    private String blackDice;
    private String whiteDice;
    private String orangeDice;

    private List<String> vaticanReports;
    private List<List<String>> reported;

    private List<String> order;

    private List<String> councilPalace;
    private List<String> territoryTower;
    private List<String> buildingTower;
    private List<String> characterTower;
    private List<String> ventureTower;
    private List<String> market;
    private List<List<String>> harvestArea;
    private List<List<String>> productionaArea;

    public BoardView(Board board, List<PlayerBoard> playerBoards){
        players = new ArrayList<>();
        for(int i = 0; i < playerBoards.size(); i++){
            players.add(new PlayerBoardView(playerBoards.get(i)));
        }

        turn = board.getTurn() + 2 * board.getPeriod() - 2;

        territoryCards = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            DevelopmentCard card = board.getTerritoryTower().getFloors().get(i).getCard();
            if(card == null){
                territoryCards.add("");
            }
            else{
                territoryCards.add(card.getPath());
            }
        }
        buildingCards = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            DevelopmentCard card = board.getBuildingTower().getFloors().get(i).getCard();
            if(card == null){
                buildingCards.add("");
            }
            else{
                buildingCards.add(card.getPath());
            }
        }
        characterCards = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            DevelopmentCard card = board.getCharacterTower().getFloors().get(i).getCard();
            if(card == null){
                characterCards.add("");
            }
            else{
                characterCards.add(card.getPath());
            }
        }
        ventureCards = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            DevelopmentCard card = board.getVentureTower().getFloors().get(i).getCard();
            if(card == null){
                ventureCards.add("");
            }
            else{
                ventureCards.add(card.getPath());
            }
        }

        blackDice = new StringBuilder(board.getDices().get(0).getValue()).toString();
        whiteDice = new StringBuilder(board.getDices().get(1).getValue()).toString();
        orangeDice = new StringBuilder(board.getDices().get(2).getValue()).toString();

        vaticanReports = new ArrayList<>();
        reported = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            VaticanReportSpot vaticanSpot = board.getVaticanReportSpot().get(i);
            vaticanReports.add(vaticanSpot.getVaticanReportCard().getPath());
            List<String> rep = new ArrayList<>();
            for(int j = 0; j < 5; j++){
                if(j < vaticanSpot.getReported().size()){
                    rep.add(vaticanSpot.getReported().get(j).getColor().toString());
                }
                else{
                    rep.add("");
                }
            }
            reported.add(rep);
        }

        order = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            if(i < board.getOrder().getShown().size()){
                order.add(board.getOrder().getShown().get(i).getColor().toString());
            }
            else{
                order.add("");
            }
        }

        councilPalace = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            List<FamilyMember> councilOccupied = board.getCouncilPalace().getOccupiedBy();
            if(i < councilOccupied.size()){
                FamilyMember f = councilOccupied.get(i);
                councilPalace.add(f.getPlayer().getColor().toString() + f.getColor().toString());
            }
            else{
                councilPalace.add("");
            }
        }

        territoryTower = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            List<FamilyMember> towerOccupied = board.getTerritoryTower().getFloors().get(i).getOccupiedBy();
            if(towerOccupied.size() > 0){
                FamilyMember f = towerOccupied.get(0);
                territoryTower.add(f.getPlayer().getColor().toString() + f.getColor().toString());
            }
            else{
                territoryTower.add("");
            }
        }
        buildingTower = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            List<FamilyMember> towerOccupied = board.getBuildingTower().getFloors().get(i).getOccupiedBy();
            if(towerOccupied.size() > 0){
                FamilyMember f = towerOccupied.get(0);
                buildingTower.add(f.getPlayer().getColor().toString() + f.getColor().toString());
            }
            else{
                buildingTower.add("");
            }
        }
        characterTower = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            List<FamilyMember> towerOccupied = board.getCharacterTower().getFloors().get(i).getOccupiedBy();
            if(towerOccupied.size() > 0){
                FamilyMember f = towerOccupied.get(0);
                characterTower.add(f.getPlayer().getColor().toString() + f.getColor().toString());
            }
            else{
                characterTower.add("");
            }
        }
        ventureTower = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            List<FamilyMember> towerOccupied = board.getVentureTower().getFloors().get(i).getOccupiedBy();
            if(towerOccupied.size() > 0){
                FamilyMember f = towerOccupied.get(0);
                ventureTower.add(f.getPlayer().getColor().toString() + f.getColor().toString());
            }
            else{
                ventureTower.add("");
            }
        }

        market = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            List<Market> marketBoard = board.getMarket();
            if(i < marketBoard.size()){
                List<FamilyMember> marketOccupied = marketBoard.get(i).getOccupiedBy();
                if(marketOccupied.size() > 0){
                    FamilyMember f = marketOccupied.get(0);
                    market.add(f.getPlayer().getColor().toString() + f.getColor().toString());
                }
                else{
                    market.add("");
                }
            }
            else{
                market.add("");
            }
        }

        harvestArea = new ArrayList<>();
        if(board.getHarvestArea().get(0).getOccupiedBy().size() > 0){
            FamilyMember f = board.getHarvestArea().get(0).getOccupiedBy().get(0);
            harvestArea.add(Arrays.asList(f.getPlayer().getColor().toString() + f.getColor().toString()));
        }
        else{
            harvestArea.add(Arrays.asList(""));
        }
        List<String> harvestMembers = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            List<HarvestArea> harvestBoard = board.getHarvestArea();
            if(harvestBoard.size() > 1){
                List<FamilyMember> harvestOccupied = harvestBoard.get(1).getOccupiedBy();
                if(i < harvestOccupied.size()){
                    FamilyMember f = harvestOccupied.get(i);
                    harvestMembers.add(f.getPlayer().getColor().toString() + f.getColor().toString());
                }
                else{
                    harvestMembers.add("");
                }
            }
            else{
                harvestMembers.add("");
            }
        }
        harvestArea.add(harvestMembers);

        productionaArea = new ArrayList<>();
        if(board.getProductionArea().get(0).getOccupiedBy().size() > 0){
            FamilyMember f = board.getProductionArea().get(0).getOccupiedBy().get(0);
            productionaArea.add(Arrays.asList(f.getPlayer().getColor().toString() + f.getColor().toString()));
        }
        else{
            productionaArea.add(Arrays.asList(""));
        }
        List<String> productionMembers = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            List<ProductionArea> productionBoard = board.getProductionArea();
            if(productionBoard.size() > 1){
                List<FamilyMember> productionOccupied = productionBoard.get(1).getOccupiedBy();
                if(i < productionOccupied.size()){
                    FamilyMember f = productionOccupied.get(i);
                    productionMembers.add(f.getPlayer().getColor().toString() + f.getColor().toString());
                }
                else{
                    productionMembers.add("");
                }
            }
            else{
                productionMembers.add("");
            }
        }
        productionaArea.add(productionMembers);
    }

    public List<PlayerBoardView> getPlayers(){
        return players;
    }

    public int getTurn(){
        return turn;
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

    public String getBlackDice(){
        return blackDice;
    }

    public String getWhiteDice(){
        return whiteDice;
    }

    public String getOrangeDice(){
        return orangeDice;
    }

    public List<String> getVaticanReports(){
        return vaticanReports;
    }

    public List<List<String>> getReported(){
        return reported;
    }

    public List<String> getOrder(){
        return order;
    }

    public List<String> getCouncilPalace(){
        return councilPalace;
    }

    public List<String> getTerritoryTower(){
        return territoryTower;
    }

    public List<String> getBuildingTower(){
        return buildingTower;
    }

    public List<String> getCharacterTower(){
        return characterTower;
    }

    public List<String> getVentureTower(){
        return ventureTower;
    }

    public List<String> getMarket(){
        return market;
    }

    public List<List<String>> getHarvestArea(){
        return harvestArea;
    }

    public List<List<String>> getProductionaArea(){
        return productionaArea;
    }
}
