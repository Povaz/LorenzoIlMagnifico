package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.rmi.RemoteException;

import org.json.JSONException;

import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;

//a class that is a state machine, based on state do differents things
public class LobbyFlow {
	//states
	private boolean start;
	private boolean login;
	private boolean registration;
	private boolean logged;
	private boolean inFlow = false;
	
	private String username;
	private String password;
	private Lobby lobby;
	private ServerSOC serverSoc;
	private ServerHandler serverHandler;
	
	public LobbyFlow(Lobby lobby, ServerSOC serverSoc, ServerHandler serverHandler){
		start = true;
		login = false;
		registration = false;
		logged = false;
		username = null;
		password = null;
		this.lobby = lobby;
		this.serverSoc = serverSoc;
		this.serverHandler = serverHandler;
	}
	
    public void setInFlow(){
    	this.inFlow = false;
    }
	
	//reset machine for users that go afk in the game
	public void reset(){
		username = null;
		password = null;
		start = true;
		logged = false;
	}
	
	//function that search if a user is logged in a game or in a lobby
	private boolean searchUserLogged (String username) throws RemoteException {
        boolean loggedInLobby = lobby.searchUser(username);
        boolean loggedInGame = serverSoc.getServer().searchLogged(username);
        return loggedInLobby||loggedInGame;
    }
	
	//check if the user went afk in the game
	private boolean checkUserDisconnected (String username) throws RemoteException {
        boolean disconnected = serverSoc.getServer().isDisconnected(username);
        return disconnected;
    }
	
	//flow of the state machine
	public String flow (String asked) throws JSONException, IOException{
		if(!inFlow) {
    		inFlow = true;
			//state start
			if(start){
				if(asked.equals("/login")){
					login = true;
					start = false;
					setInFlow();
					return "Ok tell me username : ('/back' to go back)";
				}
				else if(asked.equals("/registration")){
					registration = true;
					start = false;
					setInFlow();
					return "Ok tell me username :";
				}
				else{
					setInFlow();
					return "Not valid input. /login, /registration, /exit";
				}
			}
			
			//state login
			else if(login){
				//user input is a username
				if(username==null){
					//write /back to go back to login or registration decision
					if(asked.equals("/back")){
						start = true;
						login = false;
						setInFlow();
						return("/login, /registration, /exit?");
					}
					else{
						username = asked;
						setInFlow();
						return "Ok tell me password";	
					}
				}
				//user input is a password
				else{
					password = asked;
					boolean result;
					boolean yetLogged;
					boolean disconnected = false;
					yetLogged = searchUserLogged(username);
					if(yetLogged){
						disconnected = checkUserDisconnected(username);
					}
	
					result = JSONUtility.checkLogin(username, password);
					//user logged yet but afk
					if(result && yetLogged && disconnected){
						login = false;
						logged = true;
						serverHandler.setName(username);
						serverSoc.reconnect(username, serverHandler);
						setInFlow();
						return "Reconnected to the game";
					}
					
					//user logging in
					else if(result && !yetLogged){
						start = false;
						login = false;
						logged = true;
						serverHandler.setName(username);
						serverSoc.addPlayer (serverHandler, username);
						setInFlow();
						if(serverHandler.getGraphicType().equals("1")){
							return "logged ('/logout' to log out)";
						}
						else{
							serverHandler.setSendGUI(true);
							return "Login successful";
						}
					}
					
					//failed combination
					else if(!result){	
						username = null;
						password = null;
						setInFlow();
						if(serverHandler.getGraphicType().equals("2")){	
							login = false;
							start = true;
							serverHandler.setSendGUI(true);
							return "Wrong combination!";
						}
						return "wrong combination! Tell me username : ";
					}
					
					//user logged yet
					else {	
						username = null;
						password = null;
						setInFlow();
						if(serverHandler.getGraphicType().equals("2")){
							login = false;
							start = true;
							serverHandler.setSendGUI(true);
							return "User logged yet!";
						}
						return "User logged yet! Tell me username : ";
					}
				}
			}
			
			//state registration
			else if(registration){
				//user input is a username
				if(username==null){
					//write /back to go back to login or registration decision
					if(asked.equals("/back")){
						start = true;
						registration = false;
						setInFlow();
						return("/login, /registration, /exit?");
					}
					else{
						username = asked;
						setInFlow();
						return "Ok tell me password";	
					}
				}
				//user input is a password
				else{
					password = asked;
					boolean result = JSONUtility.checkRegister(username, password);
					username = null;
					password = null;
					
					//result of the registratiom
					if(result){	
						start = true;
						registration = false;
						setInFlow();
						if(serverHandler.getGraphicType().equals("2")){	
							serverHandler.setSendGUI(true);
							return "Registration Successful";
						}
						return "Registration successful! /login, /registration, /exit";
					}
					else{	
						setInFlow();
						if(serverHandler.getGraphicType().equals("2")){	
							serverHandler.setSendGUI(true);
							registration = false;
							start = true;
							return "Registration failed!";
						}
						return "Registration failed, retry! Tell me username : ";
					}
				}
			}
			
			//state logged
			else if(logged){
				
				//process of logout from lobby
				if(asked.equals("/logout")){
					logged = false;
					start = true;
					lobby.removeUser(username);
					serverSoc.removePlayerLobby(serverHandler);
					username = null;
					password = null;
					if(lobby.getUsers().size() == 1) {
						lobby.stopTimer();
			        }
					setInFlow();
					if(serverHandler.getGraphicType().equals("2")){	
						serverHandler.setSendGUI(true);
						return "Logout successful";
					}
					return "Logged out . . . What you want to do? /login, /registration, /exit?";
				}
			}
			
			//return if logged and input different from /logout
			setInFlow();
			return "You are logged yet...";
		}
    	else{
	    	return "I am still processing a request";
	   	}
	}
		
}
