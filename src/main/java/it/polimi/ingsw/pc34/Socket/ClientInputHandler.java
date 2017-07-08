package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.pc34.View.GUI.BoardView;

//class that deals whit input from server: receives messages and prints them to the client
public class ClientInputHandler extends Thread{
	private static ObjectInputStream socketIn;
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

	public BoardView receiveBoard() throws ClassNotFoundException, IOException{
		BoardView received = null;
		System.out.println("sto per ricevere la board");
		received = (BoardView) socketIn.readObject();
		System.out.println("ricevuta!");
		return received;
	}
	
	public static String receiveFromServer() throws IOException{
		String received = null;
		try {
			received = (String) socketIn.readObject();
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.WARNING, "warning", e);
		}
		catch (OptionalDataException e1) {
			System.out.println("boh");
			//da modificare perchè risolto col synchronized !!!!!!!!!!!!!!!!
			return "belaaaaaaaa";
		}
		System.out.println("received : " + received);
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
			System.out.println("posso ricevere altro");
			try {
				line = receiveFromServer();
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "warning", e);
			}
			if(graphicType == 2){
				client.setYouCanSend(true);
			}
			if(line!=null && !line.equals("You can send!") && graphicType == 2){
				System.out.println("into if " + line);
				if(line.equals("Logout successful")){
					client.getSynchronizedMessageForGUI().put(line);
					client.getSynchronizedMessageToChangeWindow().put("/login");
				}
				else if(line.equals("/game")){
					client.getSynchronizedMessageToChangeWindow().put("/game");
				}
				else if(line.equals("/update")){
					client.getSynchronizedMessageToChangeWindow().put("/update");
					BoardView boardView = null;
					try {
						boardView = receiveBoard();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					client.setBoardView(boardView);
				}
				else if(line.equals("/numberservant")){
					client.getSynchronizedMessageToChangeWindow().put("/numberservant");
				}
				else if(line.equals("/exchangeprivilege")){
					client.getSynchronizedMessageToChangeWindow().put("/exchangeprivilege");
				}
				else if(line.equals("/leadercard")){
					client.getSynchronizedMessageToChangeWindow().put("/leadercard");
				}
				else if(line.equals("/supportvatican")){
					client.getSynchronizedMessageToChangeWindow().put("/supportvatican");
				}
				else if(line.equals("/choosetrade")){
					client.getSynchronizedMessageToChangeWindow().put("/choosetrade");
				}
				else if(line.equals("/choosediscount")){
					client.getSynchronizedMessageToChangeWindow().put("/choosediscount");
				}
				else if(line.equals("/paymilitarypoint")){
					client.getSynchronizedMessageToChangeWindow().put("/paymilitarypoint");
				}
				else{
					client.getSynchronizedMessageForGUI().put(line);
				}
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
