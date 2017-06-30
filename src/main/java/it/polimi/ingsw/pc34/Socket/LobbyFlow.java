package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.rmi.RemoteException;

import org.json.JSONException;

import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;

public class LobbyFlow {
	private boolean start;
	private boolean login;
	private boolean register;
	private boolean logged;
	private String username;
	private String password;
	private Lobby lobby;
	private ServerSOC serverSoc;
	private ServerHandler serverHandler;
	
	public LobbyFlow(Lobby lobby, ServerSOC serverSoc, ServerHandler serverHandler){
		start = true;
		login = false;
		register = false;
		logged = false;
		username = null;
		password = null;
		this.lobby = lobby;
		this.serverSoc = serverSoc;
		this.serverHandler = serverHandler;
	}
	
	public void reset(){
		username = null;
		password = null;
		logged = false;
	}
	
	private boolean searchUserLogged (String username) throws RemoteException {
        boolean loggedInLobby = lobby.searchUser(username);
        boolean loggedInGame = serverSoc.getServer().searchLogged(username);
        return loggedInLobby||loggedInGame;
    }
	
	private boolean checkUserDisconnected (String username) throws RemoteException {
        boolean disconnected = serverSoc.getServer().isDisconnected(username);
        return disconnected;
    }
	
	public String flow (String asked) throws JSONException, IOException{
		if(start){
			if(asked.equals("/login")){
				login = true;
				start = false;
				return "Ok tell me username : ('/back' to go back)";
			}
			else if(asked.equals("/register")){
				register = true;
				start = false;
				return "Ok tell me username :";
			}
			else{
				return "Not valid input. /login or /register";
			}
		}
		else if(login){
			if(username==null){
				if(asked.equals("/back")){
					start = true;
					login = false;
					return("/login or /register?");
				}
				else{
					username = asked;
					return "Ok tell me password";	
				}
			}
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
				if(result && yetLogged && disconnected){
					login = false;
					logged = true;
					serverHandler.setName(username);
					serverSoc.reconnect(username, serverHandler);
					return "Reconnecting to the game...";
				}
				else if(result && !yetLogged){
					start = true;
					login = false;
					logged = true;
					serverHandler.setName(username);
					serverSoc.addPlayer (serverHandler, username);
					return "logged ('/logout' to log out)";
				}
				else if(!result){	
					username = null;
					password = null;
					return "wrong combination! Tell me username : ";
				}
				else {	
					username = null;
					password = null;
					return "User yet Logged! Tell me username : ";
				}
			}
		}
		else if(register){
			if(username==null){
				if(asked.equals("/back")){
					start = true;
					register = false;
					return("/login or /register?");
				}
				else{
					username = asked;
					return "Ok tell me password";	
				}
			}
			else{
				password = asked;
				boolean result = JSONUtility.checkRegister(username, password);
				username = null;
				password = null;
				if(result){	
					start = true;
					register = false;
					return "Registration successful! /login or /register";
				}
				else{	
					return "Registration failed, retry! Tell me username : ";
				}
			}
		}
		else if(logged){
			if(asked.equals("/logout")){
				logged = false;
				start = true;
				lobby.removeUser (username);
				serverSoc.removePlayer(username);
				username = null;
				password = null;
				if(lobby.getUsers().size() == 1) {
					lobby.stopTimer();
		        }
				return "Logged out . . . What you want to do? /login or /register?";
			}
		}
		return "You are logged yet...";
	}
}
