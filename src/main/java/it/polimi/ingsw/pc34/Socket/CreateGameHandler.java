package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class CreateGameHandler {
	private static ArrayList <SeverLoginUser> players=new ArrayList <SeverLoginUser>();
	private static int counter=0;
	private static Timer countdown = new Timer();
	private static TimerTask newGame = new Game(); 
	
	synchronized public void addPlayer(String name, Socket socket) {
		
		//crea nuovo utente
		players.add (new SeverLoginUser(name, socket));
	
		counter++;
		
		if(counter==2){
			countdown.schedule(newGame, 10000);
			System.out.println("INIZIO COUNTDOWN (10sec)");
			System.out.println("");
		}
		
		//notifica a tutti i giocatori
		notifyPlayers ( ((players.get(counter-1)).getName()) + " si è aggiunto alla partita!! Ora sono presenti " + counter + " giocatori");
		
		if(counter==5){
			countdown.cancel();
			countdown = new Timer();
			newGame = new Game(); 
			countdown.schedule(newGame, 10);
		}
		
		//
	}
	
	
	synchronized public static void notifyPlayers (String message){
		System.out.println("INVIO NOTIFICA : " + message);
		System.out.println("");
		Socket instance;
		PrintWriter out = null;
		for(int i=0;i<counter;i++){
			instance = ((players.get(i)).getSocket());
			try {
				out = new PrintWriter(instance.getOutputStream(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			out.println(message);
			out.flush();
			Scanner socketIn = null;
			try {
				socketIn = new Scanner(instance.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			String confirm = socketIn.nextLine();
		}
	}
	
	
	synchronized public static void clear(){
		players.clear();
		counter=0;
		countdown = new Timer();
		newGame = new Game(); 
	}
	
}
