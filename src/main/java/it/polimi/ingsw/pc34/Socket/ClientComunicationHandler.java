package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import it.polimi.ingsw.pc34.SocketRMICongiunction.Client;

public class ClientComunicationHandler extends Thread{
	Socket socketServer;
	ClientSOC client;
	
	public ClientComunicationHandler (Socket socketServer, ClientSOC client){
		this.socketServer = socketServer; 
		this.client = client;
	}

	synchronized private void sendToServer(String message) throws IOException{
		PrintWriter out = new PrintWriter(socketServer.getOutputStream(), true);
		out.println(message);
		out.flush();
	}
	
	@SuppressWarnings("resource")
	public void run(){
		Scanner input;
		while(true){
			String message = null;
			input = new Scanner(System.in);
			message = input.nextLine();
			if(!isInterrupted()){
				break;
			}
			try {
				sendToServer(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch(message){
				case "/logout":
					ClientSOC newSoc = null;
					try {
						newSoc = new ClientSOC();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} 
					Client newStart = new Client(newSoc);
					newStart.startClientSOC();
					return;
				default:
					System.out.println("Invalid Input");
					continue;
			}
		}
	}

}
