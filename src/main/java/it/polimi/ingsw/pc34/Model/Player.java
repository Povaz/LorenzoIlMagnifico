package it.polimi.ingsw.pc34.Model;

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

    public boolean sameColor(Player other){
        return color.equals(other.color);
    }

    public String toString(){
    	String playerString = null;
    	playerString+="Username : " + username + "\n";
    	playerString+="Color : " + color + "\n";	
    	return playerString;
    }

    public String getUsername(){
        return username;
    }

    public PlayerColor getColor(){
        return color;
    }

    public PlayerBoard getPlayerBoard(){
        return playerBoard;
    }
}
