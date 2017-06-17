package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientOutputHandler extends Thread{
	Socket socketServer;
	
	public ClientOutputHandler (Socket socketServer){
		this.socketServer = socketServer; 
	}

	private void sendToServer(String message) throws IOException{
		PrintWriter out = new PrintWriter(socketServer.getOutputStream(), true);
		out.println(message);
		out.flush();
	}
	
	@SuppressWarnings("resource")
	public void run(){;
		Scanner input;
		while(true){
			String message = null;
			input = new Scanner(System.in);
			message = input.nextLine();
			try {
				sendToServer(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
