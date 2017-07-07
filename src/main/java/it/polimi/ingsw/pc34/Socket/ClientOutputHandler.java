package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

//class that deals whit output to the server: gets input lines and sends them to the server
public class ClientOutputHandler extends Thread{
	private static Socket socketServer;
	private int graphicType;
	private ClientSOC client;
	
	public ClientOutputHandler (Socket socketServer, ClientSOC client, int graphicType){
		ClientOutputHandler.socketServer = socketServer; 
		this.graphicType = graphicType;
		this.client = client;
	}

	synchronized static void sendToServer(String message) throws IOException{
		PrintWriter out = new PrintWriter(socketServer.getOutputStream(), true);
		out.println(message);
		out.flush();
		System.out.println("sent : " + message);
	}
	
	public void run(){;
		Scanner input;
		if(graphicType==1){
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
		else{
			while(true){
				String message = client.getSynchronizedMessageByGUI().get();
				try {
					sendToServer(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				client.setYouCanSend(false);				
				if(message.equals("/exit")){
					System.exit(0);
				}
				while(!client.getYouCanSend()){
				}
				System.out.println("posso mandare altro");
			}
		}
		
	}

}
