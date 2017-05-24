package it.polimi.ingsw.pcXX.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class CreateGameHandler extends TimerTask {
	private ArrayList <Person> players=new ArrayList <Person>();
	private int counter=0;
	private Timer countdown = new Timer();
	private TimerTask newGame = new Game(); 
	
	public void addPlayer(Socket newSocket) {
		
		//crea nuova persona col socket
		players.add(new Person(newSocket));
		
		counter++;
		if(counter==2){
			countdown.schedule(newGame, 10000);
		}
		
		//inserisce nome player nel server
		try {
			Scanner in = new Scanner(((players.get(counter-1)).getSocket()).getInputStream());
			players.get(counter-1).setName(in.nextLine());
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//notifica a tutti i giocatori
		notifyPlayers ( ((players.get(counter-1)).getName()) + " si è aggiunto alla partita!!");
		
		if(counter==5){
			countdown.cancel();
			countdown.schedule(newGame, 0);
			//Passa al nuovo gioco l'arraylist delle Persone
			//reinizializza il tutto
			players.clear();
			counter=0;
		}
		
		//
	}
	
	
	public void notifyPlayers (String message){
		System.out.println("invio notifica: "+message);
		for(int i=counter;i>0;i--){
			Socket instance = ((players.get(i-1)).getSocket());
			try {
				PrintWriter out = new PrintWriter(instance.getOutputStream());
				out.println(message);
				out.flush();
			}
			// chiudo gli stream e il socket 	
			catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	
}
