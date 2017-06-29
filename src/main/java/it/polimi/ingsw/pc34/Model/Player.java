package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.Controller.PlayerState;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;

import java.util.List;
import java.util.function.IntToDoubleFunction;

/**
 * Created by trill on 30/05/2017.
 */
public class Player{
    private final String username;
    private final ConnectionType connectionType;
    private final PlayerColor color;
    private final PlayerBoard playerBoard;
    private boolean isYourTurn = false;
    private boolean placedFamilyMember = false;
    private boolean disconnected = false;

    private PlayerState first_state;
    private PlayerState second_state;


    public Player(String username, ConnectionType connectionType, PlayerColor color){
        this.username = username;
        this.connectionType = connectionType;
        this.color = color;
        this.playerBoard = new PlayerBoard(this);
        this.first_state = PlayerState.WAITING;
        this.second_state = PlayerState.WAITING;
    }

    public boolean sameColor(Player other){
        return color.equals(other.color);
    }

    public ConnectionType getConnectionType() {
        return connectionType;
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

    public boolean isYourTurn(){
        return isYourTurn;
    }

    public void setYourTurn(boolean yourTurn){
        isYourTurn = yourTurn;
    }

    public boolean isPlacedFamilyMember(){
        return placedFamilyMember;
    }

    public void setPlacedFamilyMember(boolean placedFamilyMember){
        this.placedFamilyMember = placedFamilyMember;
    }

    public synchronized PlayerState getFirst_state() {
        return first_state;
    }

    public synchronized PlayerState getSecond_state() {
        return second_state;
    }

    public synchronized void putFirst_State(PlayerState first_state) {
        this.first_state = first_state;
    }

    public synchronized void putSecond_State(PlayerState second_state) {
        this.second_state = second_state;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }
}
