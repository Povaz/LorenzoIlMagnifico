package it.polimi.ingsw.pcXX.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONException;

import it.polimi.ingsw.pcXX.JSONUtility;
import it.polimi.ingsw.pcXX.RMI.UserLogin;
import it.polimi.ingsw.pcXX.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pcXX.SocketRMICongiunction.Server;

public class SeverLoginUser implements Runnable{
	private String name;
	private Socket socket;
	
	public SeverLoginUser(Socket socket){
		this.socket = socket; 
	}
	
	public SeverLoginUser(String name, Socket socket){
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
	
	synchronized public void sendToClient(String message) throws IOException{
		PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
		socketOut.println(message);
		socketOut.flush();
		Scanner socketIn = new Scanner(socket.getInputStream());
		String confirm = socketIn.nextLine();
	}
	
	synchronized public String receiveFromClient() throws IOException{
		Scanner socketIn = new Scanner(socket.getInputStream());
		String received = socketIn.nextLine();
		System.out.println("RECEIVED : " + received);
		System.out.println("");
		return received;
	}
	
	private boolean searchUserLogged (String name) throws RemoteException {
        Set<String> usernames = Server.usersInLobby.keySet();
        for (String username : usernames) {
            if ( (name.equals(username)) && (name.equals(username))) {
                return true;
            }
        }
        return false;
    }
	
	synchronized public void run() {
		String decision = null;
		boolean logged = false;
		while(!logged){
			try {
				decision = receiveFromClient();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			String username = null;
			try {
				username = receiveFromClient();
				if(username.equals("/back")){
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
					// TODO Auto-generated catch block
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
					ServerSOC.addPlayer(username, socket);
					logged= true;
				}
			}
			else if(result==false){	
				try {
					sendToClient("Wrong Combination!");
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
	}
	
	
}

