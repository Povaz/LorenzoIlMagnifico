package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.Controller.PlayerState;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientInfo;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ClientType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;

/**
 * Created by trill on 30/05/2017.
 */
public class Player{
    private final String username;
    private ConnectionType connectionType;
    private ClientType clientType;
    private final PlayerColor color;
    private final PlayerBoard playerBoard;
    private boolean isYourTurn = false;
    private boolean placedFamilyMember = false;
    private boolean disconnected = false;

    private PlayerState first_state;
    private PlayerState second_state;

    public Player(String username, ClientInfo clientInfo, PlayerColor color){
        this.username = username;
        this.connectionType = clientInfo.getConnectionType();
        this.clientType = clientInfo.getClientType();
        this.color = color;
        this.playerBoard = new PlayerBoard(this);
        this.first_state = PlayerState.WAITING;
        this.second_state = PlayerState.WAITING;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public boolean sameColor(Player other){
        return color.equals(other.color);
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public ClientType getClientType() { return clientType; }

    public String toString(){
    	String playerString = "";
    	playerString+="Username : " + username + "\n";
    	playerString+="Color : " + color + "\n";	
    	return playerString;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return username.equals(player.username);
    }

    @Override
    public int hashCode(){
        return username.hashCode();
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
