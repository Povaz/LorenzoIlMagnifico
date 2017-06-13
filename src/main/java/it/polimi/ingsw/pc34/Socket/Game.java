package it.polimi.ingsw.pc34.Socket;

import java.util.TimerTask;

public class Game extends TimerTask {
	public void run(){
		System.out.println("Game iniziato");
		System.out.println("");
		CreateGameHandler.notifyPlayers("Game Iniziato");
		CreateGameHandler.clear();
		
	}
}