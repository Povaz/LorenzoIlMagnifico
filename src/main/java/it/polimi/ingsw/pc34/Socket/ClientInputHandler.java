package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

//class that deals whit input from server: receives messages and prints them to the client
public class ClientInputHandler extends Thread{
	private ObjectInputStream socketIn;
	private int graphicType;
	
	private static final Logger LOGGER = Logger.getLogger(ClientInputHandler.class.getName());
	
	public ClientInputHandler (Socket socketServer, int graphicType) throws IOException{
		this.graphicType = graphicType;
		this.socketIn = new ObjectInputStream(socketServer.getInputStream());
		
	}

	private String receiveFromServer() throws IOException{
		String received = null;
		try {
			received = (String) socketIn.readObject();
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.WARNING, "warning", e);
		}
		if(received == null){
			return null;
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
				line = receiveFromServer();
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "warning", e);
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
