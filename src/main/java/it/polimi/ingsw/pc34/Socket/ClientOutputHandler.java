package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

//class that deals whit output to the server: gets input lines and sends them to the server
public class ClientOutputHandler extends Thread{
	private static Socket socketServer;
	
	public ClientOutputHandler (Socket socketServer){
		ClientOutputHandler.socketServer = socketServer; 
	}

	static void sendToServer(String message) throws IOException{
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
			if(message.equals("/exit")){
				System.exit(0);
			}
		}
	}

}
