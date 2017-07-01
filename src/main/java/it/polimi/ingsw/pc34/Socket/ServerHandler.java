package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.json.JSONException;

import it.polimi.ingsw.pc34.Controller.GameController;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;

//class that is assigned one per one to a client, and has to deal with server's comunication with the client
public class ServerHandler implements Runnable{
	private Socket socket;
	private int fase; 
	private LobbyFlow lobbyFlow;
	private String username;
	private ServerSOC serverSoc;
	private GameController gameController;
	private String stateGame;
	
	
	public ServerHandler(Socket socket, Lobby lobby, ServerSOC serverSoc){
		this.socket = socket;
		this.fase = 0;
		this.lobbyFlow = new LobbyFlow(lobby, serverSoc, this);
		this.serverSoc = serverSoc;
		this.stateGame = null;
	}
	
	//set Fase, a variable used to direct the messages to a specific flow(lobby or game)
	public void setFase(int fase){
		this.fase = fase;
		if(fase==1){
			try {
				sendToClient("Type: /playturn for an action; /chat to send message;  /stampinfo to stamp info;  /afk to disconnect from the game");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//other sets and gets
	public void setGameController(GameController gameController){
		this.gameController= gameController;
	}
	
	public void setName(String username){
		this.username = username;
	}
	
	public void setStateGame(String stateGame){
		this.stateGame = stateGame;
		System.out.println("settato a " + stateGame);
	}

	public String getName(){
		return username;
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	//method to send messages to client
	public void sendToClient(String message) throws IOException{
		if(message==null){
			return;
		}
		else if(message.equals("This Client has been disconnected")){
			setFase(0);
			//todo togliere riferimento in GameController
			serverSoc.getServer().disconnectPlayerSoc(username);
			message += "\nTake your decision : /login or /register?";
			lobbyFlow.reset();
			stateGame = null;
		}
		else if(message.equals("Reconnecting to the game...")){
			setFase(1);
			stateGame = null;
		}
		else if(message.equals("Action has been executed")&&fase==1){
			stateGame = null;
			message += "\nInsert new command: /playturn, /chat, /stampinfo, /afk";
		}
		else if(message.equals("You have already placed a family member!")){
			stateGame = null;
			message += "\nInsert new command: /playturn, /chat, /stampinfo, /afk";
		}
		PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
		socketOut.println(message);
		socketOut.flush();
	}
	
	//method to receive messages from client
	private String receiveFromClient() throws IOException{
		@SuppressWarnings("resource")
		Scanner socketIn = new Scanner(socket.getInputStream());
		String received = socketIn.nextLine();
		return received;
	}
	
	//directs input to the Lobby Flow
	private String toLobbyHandler(String asked) throws JSONException, IOException{
		String answer=lobbyFlow.flow(asked);
		return answer;
	}
	
	//directs input to the Game Flow
	private String toGameHandler(String asked) throws IOException{
		String answer=gameController.flow(asked, username);
		return answer;
	}
	
	public void run(){
		String line = null;
		try {
			sendToClient("Take your decision : /login or /register?");
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		while (true){
			try {
				line = receiveFromClient();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String answer = null;
			
			//to lobby flow
			if(fase==0){
				try {
					answer = toLobbyHandler(line);
				} catch (JSONException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			//to game flow
			if(fase==1){
				//have to decide the type of action
				if(stateGame==null) {
					switch (line){
						case "/playturn" :
							try {
								answer = toGameHandler(line);
							} catch (IOException e) {
								e.printStackTrace();
							}
							if(answer.equals("What action you want to do? 1-action 2-place Leader Card 3-activate Leader Card 4-exchange Leader Card 5-skip")){
								stateGame = line;
							}
							break;
						case "/afk" :
							try {
								answer = toGameHandler(line);
							} catch (IOException e) {
								e.printStackTrace();
							}
							stateGame = null;
							break;
						case "/chat" :
							answer = "Insert a message : ";
							stateGame = line;
							break;
						//SI PUO' METTERE ANCHE FUORI
						case "/stampinfo" : 
							//answer = toStampInfoHandler(line);
							answer = "command still not implemented, Type: /playturn for an action; /chat to send message;  /stampinfo to stamp info;  /afk to disconnect from the game";
							break;
						default :
							answer = "Wrong input, Type: /playturn for an action; /chat to send message;  /stampinfo to stamp info;  /afk to disconnect from the game";
							break;
						
					}
				}
				else {
					//you can always type /afk
					if(line.equals("/afk")){
						try {
							answer = toGameHandler(line);
						} catch (IOException e) {
							e.printStackTrace();
						}
						stateGame = null;
						try {
							sendToClient(answer);
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					}
					
					//you have decided yet the type of action to perform
					switch (stateGame){
						case "/playturn" :
							try {
								answer = toGameHandler(line);
							} catch (IOException e) {
								e.printStackTrace();
							}
							break;
						case "/chat" : 
							stateGame = null;
							try {
								gameController.sendMessageChat(line, username);
							} catch (IOException e) {
								e.printStackTrace();
							}
							answer = "Type: /playturn for an action; /chat to send message;  /stampinfo to stamp info;  /afk to disconnect from the game";
							break;
						case "/vaticansupport" :
							try {
								answer = toGameHandler(line);
							} catch (IOException e) {
								e.printStackTrace();
							}
							break;
						default :
							answer = "Wrong input, Type: /playturn for an action; /chat to send message;  /stampinfo to stamp info;  /afk to disconnect from the game";
							break;
					}
				}
			}
			
			//send to client the answer
			try {
				sendToClient(answer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
