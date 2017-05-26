package it.polimi.ingsw.pcXX.Socket;

import java.util.TimerTask;

public class Game extends TimerTask {
	public void run(){
		System.out.println("Game iniziato");
		System.out.println("");
		CreateGameHandler.notifyPlayers("Game Iniziato");
		CreateGameHandler.clear();
		
	}
}