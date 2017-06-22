package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientInputHandler extends Thread{
	Socket socketServer;
	Scanner socketIn;
	
	public ClientInputHandler (Socket socketServer) throws IOException{
		this.socketServer = socketServer; 
		this.socketIn = new Scanner(socketServer.getInputStream());
	}

	private String receiveFromServer(Socket socketServer) throws IOException{
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
		}
	}

}
