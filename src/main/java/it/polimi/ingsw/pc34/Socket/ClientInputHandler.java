package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

//class that deals whit input from server: receives messages and prints them to the client
public class ClientInputHandler extends Thread{
	Socket socketServer;
	Scanner socketIn;
	
	public ClientInputHandler (Socket socketServer) throws IOException{
		this.socketServer = socketServer; 
		this.socketIn = new Scanner(socketServer.getInputStream());
	}

	private String receiveFromServer(Socket socketServer) throws IOException{
		String received = socketIn.nextLine();
		if(received.equals("Are you connected?")){
			ClientOutputHandler.sendToServer("yes");
			return null;
		}
		return received;	
	}
	
	public void run(){
		String line = null;
		while (true){
			try {
				line = receiveFromServer(socketServer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(line!=null){
				System.out.println(line);
			}
		}
	}

}
