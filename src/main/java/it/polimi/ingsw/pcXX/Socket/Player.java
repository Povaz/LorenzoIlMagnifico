package it.polimi.ingsw.pcXX.Socket;

import java.net.Socket;

public class Player {
	private String name;
	private Socket socket;
	
	public Player(Socket socket){
		this.socket=socket;
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
}


/*COSE DA FARE E DOVE SONO ARRIVATO:
Far partire timer quando si arriva a due giocatori e startare game quando si è arrivati a 5 o scade timer
Creare nuovo Game se nessuno è aperto
Più notifiche per io giocatori e gestire meglio l'output
Migliorare gestione Password e Utenti

*
*
*/