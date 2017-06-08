package it.polimi.ingsw.pcXX;

import it.polimi.ingsw.pcXX.Exception.TooMuchTimeException;

import java.util.List;

/**
 * Created by trill on 30/05/2017.
 */
public class Player{
    private final String username;
    private final PlayerColor color;
    private final PlayerBoard playerBoard;
    
    public Player(String username, PlayerColor color, PersonalBonusTile personalBonusTile, List<LeaderCard> leaderCards){
        this.username = username;
        this.color = color;
        this.playerBoard = new PlayerBoard(this, personalBonusTile, leaderCards);
    }

    public String getUsername() {
        return username;
    }

    public PlayerColor getColor() {
        return color;
    }

    public PlayerBoard getPlayerBoard() {
        return playerBoard;
    }

    public boolean sameColor(Player other){
        return color.equals(other.color);
    }

    public boolean placeFamilyMember(FamilyMember familyMember, ActionSpot actionSpot) throws TooMuchTimeException {
        if(actionSpot instanceof Market){
            return placeMarket(familyMember, (Market) actionSpot);
        }
        if(actionSpot instanceof CouncilPalace){
            return placeCouncilPalace(familyMember, (CouncilPalace) actionSpot);
        }
        if(actionSpot instanceof HarvestArea){
            return placeHarvestArea(familyMember, (HarvestArea) actionSpot);
        }
        if(actionSpot instanceof ProductionArea){
            return placeProductionArea(familyMember, (ProductionArea) actionSpot);
        }
        if(actionSpot instanceof Floor){
            return placeFloor(familyMember, (Floor) actionSpot);
        }
        return false;
    }

    private boolean placeMarket(FamilyMember familyMember, Market market) throws TooMuchTimeException{
        if(market.isPlaceable(familyMember)){
            return market.place(familyMember);
        }
        return false;
    }

    private boolean placeCouncilPalace(FamilyMember familyMember, CouncilPalace councilPalace) throws TooMuchTimeException{
        if(councilPalace.isPlaceable(familyMember)){
            return councilPalace.place(familyMember);
        }
        return false;
    }

    private boolean placeHarvestArea(FamilyMember familyMember, HarvestArea harvestArea) throws TooMuchTimeException{
        if(harvestArea.isPlaceable(familyMember)){
            return harvestArea.place(familyMember);
        }
        return false;
    }

    private boolean placeProductionArea(FamilyMember familyMember, ProductionArea productionArea) throws TooMuchTimeException{
        if(productionArea.isPlaceable(familyMember)){
            return productionArea.place(familyMember);
        }
        return false;
    }

    private boolean placeFloor(FamilyMember familyMember, Floor floor) throws TooMuchTimeException{
        if(floor.isPlaceable(familyMember)){
            return floor.place(familyMember);
        }
        return false;
    }
    
    public String toString(){
    	String playerString = null;
    	playerString+="Username : " + username + "\n";
    	playerString+="Color : " + color + "\n";	
    	return playerString;
    }
}
