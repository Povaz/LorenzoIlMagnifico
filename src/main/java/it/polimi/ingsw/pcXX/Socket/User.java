package it.polimi.ingsw.pcXX.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.json.JSONException;

import it.polimi.ingsw.pcXX.JSONUtility;

public class User implements Runnable{
	private String name;
	private Socket socket;
	private CreateGameHandler newGame;
	
	public User(Socket socket){
		this.socket = socket; 
	}
	
	public User(Socket socket, CreateGameHandler newGame){
		this.socket = socket;
		this.newGame = newGame; 
	}
	
	public User(String name, Socket socket){
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
	
	synchronized public void run() {
		String decision = null;
		try {
			decision = receiveFromClient();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		while(true){
			String username = null;
			try {
				username = receiveFromClient();
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
			if(decision.equals("1")){
				try {
					result = JSONUtility.checkLogin(username, password);
				} catch (JSONException | IOException e1) {
					e1.printStackTrace();
				}
			}
			else{
				try {
					result = JSONUtility.checkRegister(username, password);
				} catch (JSONException | IOException e1) {
					e1.printStackTrace();
				}
			}
			if(result){
				name=username;
				try {
					sendToClient("confirmed");
				} catch (IOException e) {
					e.printStackTrace();
				}
				newGame.addPlayer(name, socket);
				break;
			}
			else{	
				try {
					sendToClient("wrong combination");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//fine perchè ora viene gestito dal CreateGameHandler e poi parte il game
	}
	
	
}

