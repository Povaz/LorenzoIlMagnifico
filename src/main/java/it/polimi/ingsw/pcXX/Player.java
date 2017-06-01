package it.polimi.ingsw.pcXX;

import java.util.ArrayList;

/**
 * Created by trill on 30/05/2017.
 */
public class Player{
    private final String username;
    private final PlayerColor color;
    private final PlayerBoard playerBoard;

    public Player(String username, PlayerColor color, int playerOrder, PersonalBonusTile personalBonusTile, ArrayList<LeaderCard> leaderCards){
        this.username = username;
        this.color = color;
        this.playerBoard = new PlayerBoard(color, playerOrder, personalBonusTile, leaderCards);
    }

    public boolean sameColor(Player other){
        return color.equals(other.color);
    }
}
