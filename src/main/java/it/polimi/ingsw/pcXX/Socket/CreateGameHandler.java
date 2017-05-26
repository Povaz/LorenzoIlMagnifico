package it.polimi.ingsw.pcXX.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CreateGameHandler {
	private static ArrayList <Person> players=new ArrayList <Person>();
	private static int counter=0;
	private static Timer countdown = new Timer();
	private static TimerTask newGame = new Game(); 
	
	synchronized public void addPlayer(String name, Socket socket) {
		
		//crea nuova persona col socket
		players.add(new Person(name, socket));
	
		counter++;
		if(counter==2){
			countdown.schedule(newGame, 10000);
			System.out.println("inizio countdown (10sec)");
		}
		
		//notifica a tutti i giocatori
		notifyPlayers ( ((players.get(counter-1)).getName()) + " si è aggiunto alla partita!!");
		
		if(counter==5){
			countdown.cancel();
			countdown.schedule(newGame, 0);
		}
		
		//
	}
	
	
	synchronized public static void notifyPlayers (String message){
		System.out.println("invio notifica: "+message);
		Socket instance;
		PrintWriter out = null;
		System.out.println("counter="+counter);
		for(int i=0;i<counter;i++){
			System.out.println(i);
			instance = ((players.get(i)).getSocket());
			try {
				out = new PrintWriter(instance.getOutputStream(), true);
				System.out.println("fatto");
			} catch (IOException e) {
				e.printStackTrace();
			}
			out.println(message);
			out.flush();
		}
	}
	
	
	synchronized public static void clear(){
		players.clear();
		counter=0;
		countdown = new Timer();
		newGame = new Game(); 
	}
	
}
