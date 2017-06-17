package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientInputHandler extends Thread{
	Socket socketServer;
	
	public ClientInputHandler (Socket socketServer){
		this.socketServer = socketServer; 
	}

	private static String receiveFromServer(Socket socketServer) throws IOException{
		Scanner socketIn = new Scanner(socketServer.getInputStream());
		String received = socketIn.nextLine();
		return received;	
	}
	
	@SuppressWarnings("resource")
	public void run(){
		String line = null;
		while (true){
			try {
				line = receiveFromServer(socketServer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(line);
			System.out.println("");
		}
	}

}
