package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

//class that deals whit input from server: receives messages and prints them to the client
public class ClientInputHandler extends Thread{
	private Socket socketServer;
	private ObjectInputStream socketIn;
	private int graphicType;
	
	public ClientInputHandler (Socket socketServer, int graphicType) throws IOException{
		this.socketServer = socketServer; 
		this.graphicType = graphicType;
		this.socketIn = new ObjectInputStream(socketServer.getInputStream());
		
	}

	private String receiveFromServer(Socket socketServer) throws IOException{
		String received = null;
		try {
			received = (String) socketIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
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
			if(graphicType==2){
				//qui metodo che manda a GUI
			}
			if(line!=null){
				System.out.println(line);
			}
		}
	}

}
