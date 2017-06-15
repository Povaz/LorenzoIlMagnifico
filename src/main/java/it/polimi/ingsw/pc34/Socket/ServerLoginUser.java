package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONException;

import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Server;

public class ServerLoginUser implements Runnable{
	private String name;
	private Socket socket;
	private Lobby lobby;
	private ServerSOC serverSoc;
	
	public ServerLoginUser(Socket socket, Lobby lobby, ServerSOC serverSoc){
		this.socket = socket; 
		this.lobby = lobby;
		this.serverSoc = serverSoc;
	}
	
	public ServerLoginUser(String name, Socket socket){
		this.socket = socket;
		this.name = name; 
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	public String getName(){
		return name;
	}
	
	synchronized private void sendToClient(String message) throws IOException{
		PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
		socketOut.println(message);
		socketOut.flush();
	}
	
	synchronized private String receiveFromClient() throws IOException{
		Scanner socketIn = new Scanner(socket.getInputStream());
		String received = socketIn.nextLine();
		System.out.println("RECEIVED : " + received);
		System.out.println("");
		return received;
	}
	
	private boolean searchUserLogged (String username) throws RemoteException {
        return lobby.searchUser(username);
    }
	
	public void run() {
		String decision = null;
		boolean logged = false;
		boolean askDecision = true;
		while(!logged){
			if(askDecision){
				try {
					decision = receiveFromClient();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
			askDecision = false;
			String username = null;
			try {
				username = receiveFromClient();	
				if(username.equals("/back")){
					askDecision=true;
					continue;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			String password = null;
			try {
				password = receiveFromClient();		
			} catch (IOException e) {
				e.printStackTrace();
			}
			boolean result = false;
			boolean yetLogged = false;
			if(decision.equals("1")){
				try {
					result = JSONUtility.checkLogin(username, password);
				} catch (JSONException | IOException e1) {
					e1.printStackTrace();
				}
				try {
					yetLogged = searchUserLogged(username);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			else{
				try {
					result = JSONUtility.checkRegister(username, password);
				} catch (JSONException | IOException e1) {
					e1.printStackTrace();
				}
			}
			if(result && !yetLogged){
				name=username;
				try {
					sendToClient("confirmed");
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(decision.equals("1")){
					try {
						serverSoc.addPlayer (this, username);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					logged= true;
				}
			}
			else if(!result){	
				try {
					sendToClient("wrong combination!");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {	
				try {
					sendToClient("User yet Logged! Retry!");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//starta handler comunicazione 
		ServerComunicationHandler handler = new ServerComunicationHandler(lobby, serverSoc, socket, name);
		try {
			handler.initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}

