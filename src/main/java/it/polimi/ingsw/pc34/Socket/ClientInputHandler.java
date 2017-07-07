package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

//class that deals whit input from server: receives messages and prints them to the client
public class ClientInputHandler extends Thread{
	private ObjectInputStream socketIn;
	private int graphicType;
	private Socket socketServer;
	private ClientSOC client;
	
	private static final Logger LOGGER = Logger.getLogger(ClientInputHandler.class.getName());
	
	public ClientInputHandler (Socket socketServer, ClientSOC clientSoc, int graphicType) throws IOException{
		client = clientSoc;
		this.graphicType = graphicType;
		this.socketIn = new ObjectInputStream(socketServer.getInputStream());
		this.socketServer = socketServer;
	}

	private String receiveFromServer() throws IOException{
		String received = null;
		try {
			received = (String) socketIn.readObject();
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.WARNING, "warning", e);
		}
		catch (OptionalDataException e1) {
			System.out.println("boh");
			return "belaaaaaaaa";
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
			if(line!=null && graphicType==2){
				client.getSynchronizedMessageForGUI().put(line);
			}
			else if(line!=null && graphicType!=2){
				System.out.println(line);
			}
			if(line.equals("belaaaaaaaa")){
				try {
					this.socketIn = new ObjectInputStream(socketServer.getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
				continue;
			}
		}
	}

}
