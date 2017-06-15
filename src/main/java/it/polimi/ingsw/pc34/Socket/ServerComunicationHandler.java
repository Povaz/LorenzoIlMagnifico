package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;
import it.polimi.ingsw.pc34.SocketRMICongiunction.NotificationType;

public class ServerComunicationHandler {
	String username;
	Lobby lobby;
	ServerSOC serverSoc;
	Socket socket;
	
	public ServerComunicationHandler (Lobby lobby, ServerSOC serverSoc, Socket socket, String username){
		this.lobby = lobby;
		this.serverSoc = serverSoc;
		this.socket = socket;
		this.username = username;
	}
	
	//metodo ricezione
	synchronized private String receiveFromClient() throws IOException{
		Scanner in = new Scanner(socket.getInputStream());
		String received = in.nextLine();
		System.out.println("RECEIVED BY " + username + " : " + received);
		System.out.println("");
		return received;
	}
	
	synchronized private void sendToClient(String message) throws IOException{
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.println(message);
		out.flush();
	}
	
	//metodo Logout
	private void logout() throws IOException{
		//azioni da fare: logout da lobby, da ServerSOC, fa partire nuova pagina login(non lo fa perchè il server dovrebbe accettare un nuovo 'processo'), killa processo attuale
		sendToClient("logout");
		lobby.removeUser (username);
		serverSoc.removePlayer(username);
		if(lobby.getUsers().size() == 1) {
			lobby.stopTimer();
        }
		return;
	}
	
	public void initialize () throws IOException {
		while(true){
			String inMessage = receiveFromClient();
			if(inMessage.equals("/logout")){
				try {
					logout();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lobby.notifyAllUsers(NotificationType.USERLOGOUT, username);
				break;
			}
		}
		return;
	}

}
