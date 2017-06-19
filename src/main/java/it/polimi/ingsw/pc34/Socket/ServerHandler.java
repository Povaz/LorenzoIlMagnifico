package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import it.polimi.ingsw.pc34.Model.GameController;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;

public class ServerHandler implements Runnable{
	private Socket socket;
	private int fase; 
	private LobbyFlow lobbyFlow;
	private GameFlow gameFlow;
	private String username;
	private Lobby lobby;
	private ServerSOC serverSoc;
	private GameController gameController;
	private Integer numberPlayers;
	
	public ServerHandler(Socket socket, Lobby lobby, ServerSOC serverSoc){
		this.socket = socket;
		this.fase = 0;
		this.lobby = lobby;
		this.lobbyFlow = new LobbyFlow(lobby, serverSoc, this);
		this.serverSoc = serverSoc;
	}
	
	public void setFase(int fase){
		this.fase = fase;
		if(fase==1 && gameFlow == null){
			gameFlow = new GameFlow();
			try {
				sendToClient("Type: /action for an Action;  /skip to skip this turn  /drawleadercard to use a LeaderCard  /activateleadercard to activate a Leader Card  /chat to send message;  /stampinfo to stamp info");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setGameController(GameController gameController){
		this.gameController= gameController;
		gameFlow.setController(gameController);
	}
	
	public void setName(String username){
		this.username = username;
	}
	
	public String getName(){
		return username;
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	public GameFlow getGameFlow(){
		return gameFlow;
	}
	
	private void sendToClient(String message) throws IOException{
		PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
		socketOut.println(message);
		socketOut.flush();
	}
	
	private String receiveFromClient() throws IOException{
		Scanner socketIn = new Scanner(socket.getInputStream());
		String received = socketIn.nextLine();
		System.out.println("RECEIVED : " + received);
		System.out.println("");
		return received;
	}
	
	private String toLobbyHandler(String asked) throws JSONException, IOException{
		String answer=lobbyFlow.flow(asked);
		return answer;
	}
	
	private String toGameHandler(String asked){
		String answer=gameFlow.flow(asked);
		return answer;
	}
	
	public void run(){
		String line = null;
		try {
			sendToClient("Fai la tua scelta : login o register?");
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
			//lobby
			if(fase==0){
				try {
					answer = toLobbyHandler(line);
				} catch (JSONException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					sendToClient(answer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//game
			if(fase==1){
				if(!gameController.getCurrentPlayer().equals(username) && !line.equals("/chat") && !line.equals("/stampinfo")){
					answer = "Non è il tuo turno!";
				}
				else{
					if(numberPlayers==null){
						numberPlayers=gameController.getNumberPlayers();
						gameFlow.setPlayerNumber(numberPlayers);
					}
					answer = toGameHandler(line);
				}
				try {
					sendToClient(answer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
