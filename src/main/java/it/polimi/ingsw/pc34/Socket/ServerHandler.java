package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import it.polimi.ingsw.pc34.Controller.BooleanCreated;
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
	private ObjectOutputStream socketOut;
	private String graphicType;
	
	private static final Logger LOGGER = Logger.getLogger(ServerHandler.class.getName());
	
	private BooleanCreated isNotConnect = new BooleanCreated();
	private String answer = null;
	
	public ServerHandler(Socket socket, Lobby lobby, ServerSOC serverSoc){
		this.socket = socket;
		this.fase = 0;
		this.lobbyFlow = new LobbyFlow(lobby, serverSoc, this);
		this.serverSoc = serverSoc;
		this.stateGame = null;
		try {
			this.socketOut = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "warning", e);
		}
	}
	
	//set Fase, a variable used to direct the messages to a specific flow(lobby or game)
	public void setFase(int fase){
		this.fase = fase;
		if(fase==1){
			sendToClient("Type: /playturn for an action; /chat to send message(you can always do this action, only if you are not doing something else);  /afk to disconnect from the game(you can always do this action, at any time)");
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
	
	public String getGraphicType(){
		return graphicType;
	}
	
	//method to send messages to client
	public void sendToClient(String message) {
		if(message==null){
			return;
		}
		else if(message.equals("This Client has been disconnected")){
			setFase(0);
			//todo togliere riferimento in GameController
			try {
				serverSoc.getServer().disconnectPlayerSoc(username);
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "warning", e);
			}
			message += "\nTake your decision : /login or /register?";
			lobbyFlow.reset();
			stateGame = null;
		}
		else if(message.equals("Reconnecting to the game...")){
			setFase(1);
			stateGame = null;
		}
		else if(message.equals("Action has been executed") && fase==1){
			stateGame = null;
			message += "\nInsert new command: /playturn, /chat, /afk";
		}
		else if(message.equals("You have already placed a family member!")){
			stateGame = null;
			message += "\nInsert new command: /playturn, /chat, /afk";
		}
		try {
			socketOut.writeObject(message);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "warning", e);
		}
	}
	
	//method to receive messages from client
	private String receiveFromClient() throws IOException{
		@SuppressWarnings("resource")
		Scanner socketIn = new Scanner(socket.getInputStream());
		String received = socketIn.nextLine();
		if(answer!=null && received.equals("yes")){
			answer=received;
		}
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
			this.graphicType=receiveFromClient();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		sendToClient("Take your decision : /login or /register?");
		while (true){
			try {
				line = receiveFromClient();
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "warning", e);
			}
			if(answer!=null && line.equals("yes")){
				continue;
			}
			String answer = null;
			//to lobby flow
			if(fase==0){
				try {
					answer = toLobbyHandler(line);
				} catch (JSONException|IOException e) {
					LOGGER.log(Level.WARNING, "warning", e);
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
								LOGGER.log(Level.WARNING, "warning", e);
							}
							if(answer==null){
								break;
							}
							if(answer.equals("What action you want to do? 1-action 2-place Leader Card 3-activate Leader Card 4-exchange Leader Card 5-skip")){
								stateGame = line;
							}
							break;
						case "/afk" :
							try {
								answer = toGameHandler(line);
							} catch (IOException e) {
								LOGGER.log(Level.WARNING, "warning", e);
							}
							stateGame = null;
							break;
						case "/chat" :
							answer = "Insert a message : ";
							stateGame = line;
							break;
						default :
							answer = "Wrong input, Type: /playturn for an action; /chat to send message; /afk to disconnect from the game(you can always do it, at any moment)";
							break;
						
					}
				}
				else {
					//you can always type /afk
					if(line.equals("/afk")){
						try {
							answer = toGameHandler(line);
						} catch (IOException e) {
							LOGGER.log(Level.WARNING, "warning", e);
						}
						sendToClient(answer);
						stateGame = null;
						continue;
					}
					
					//you have decided yet the type of action to perform
					switch (stateGame){
						case "/playturn" :
							try {
								answer = toGameHandler(line);
							} catch (IOException e) {
								LOGGER.log(Level.WARNING, "warning", e);
							}
							break;
						case "/chat" : 
							stateGame = null;
							try {
								gameController.sendMessageChat(line, username + " ");
							} catch (IOException e) {
								LOGGER.log(Level.WARNING, "warning", e);
							}
							answer = "Type: /playturn for an action; /chat to send message;  /afk to disconnect from the game";
							break;
						case "/vaticansupport" :
							try {
								answer = toGameHandler(line);
							} catch (IOException e) {
								LOGGER.log(Level.WARNING, "warning", e);
							}
							break;
						default :
							answer = "Wrong input, Type: /playturn for an action; /chat to send message; /afk to disconnect from the game";
							break;
					}
				}
			}

			sendToClient(answer);
		}
	}

	public boolean isNotConnected() {
		boolean result;
		answer = "waiting";
		sendToClient("Are you connected?");
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
            @Override
            public void run() {
            	if(answer.equals("waiting")){
            		isNotConnect.put(true);
            		answer=null;
            	}
            	else{
            		isNotConnect.put(false);
            		answer = null;
            	}
            }
        }, 2000);
		
		result = isNotConnect.get();
		return result;
	}

}
