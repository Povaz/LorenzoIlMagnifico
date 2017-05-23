package it.polimi.ingsw.pcXX.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class CreateGameHandler {
	private ArrayList <Person> players=new ArrayList <Person>();
	private int counter=0;
	
	public void createPlayer(Socket neww) {
		players.add(new Person(neww));
		
		counter++;
		
		//inserisce nome player nel server
		try {
			Scanner in = new Scanner(((players.get(counter-1)).getSocket()).getInputStream());
			players.get(counter-1).setName(in.nextLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//notifica a tutti i giocatori
		notifyPlayers ( ((players.get(counter-1)).getName()) + " si è aggiunto alla partita!!");
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
