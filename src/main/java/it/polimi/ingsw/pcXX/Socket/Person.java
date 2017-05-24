package it.polimi.ingsw.pcXX.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Person implements Runnable{
	private String name;
	private Socket socket;
	private CreateGameHandler newGame;
	
	public Person(Socket socket){
		this.socket = socket; 
	}
	
	public Person(Socket socket, CreateGameHandler newGame){
		this.socket = socket;
		this.newGame = newGame; 
	}
	
	public Person(String name, Socket socket){
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
	
	public String login (String username, String password){
		//////////////////////////////////////////////////////////////////////////////////////////////////CODICEEEEEEEEE;
		return "ok";
	}
	
	public void sendToClient(String message) throws IOException{
		PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
		socketOut.println(message);
		socketOut.flush();
	}
	
	public String receiveFromClient() throws IOException{
		Scanner socketIn = new Scanner(socket.getInputStream());
		String received = socketIn.nextLine();
		System.out.println(received);
		return received;
	}
	
	public void run() {
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
		String login = login(username, password);
		
		while(true){
			if(login.equals("ok")){
				name=username;
				try {
					sendToClient("confirmed");
				} catch (IOException e) {
					e.printStackTrace();
				}
				newGame.addPlayer(name, socket);
				break;
			}
			else if(login.equals("new")){
				name=username;
				try {
					sendToClient("new");
				} catch (IOException e) {
					e.printStackTrace();
				}
				newGame.addPlayer(name, socket);
				break;
			}
			try {
				sendToClient("Password sbagliata, ritenta!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//fine perchè ora viene gestito dal CreateGameHandler e poi parte il game
		//CORREGGERE BUG PER CUI 2O CLIENT NON RICEVE PIU NULLA
	}
	
	
}

