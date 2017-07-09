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
	
	@SuppressWarnings("static-access")
	public ClientInputHandler (Socket socketServer, ClientSOC clientSoc, int graphicType) throws IOException{
		client = clientSoc;
		this.graphicType = graphicType;
		this.socketIn = new ObjectInputStream(socketServer.getInputStream());
		this.socketServer = socketServer;
	}

	//receive an object board to send to GUI for update graphic
	public BoardView receiveBoard() throws ClassNotFoundException, IOException{
		BoardView received = null;
		System.out.println("sto per ricevere la board");
		received = (BoardView) socketIn.readObject();
		System.out.println("ricevuta!");
		return received;
	}
	
	//receive a string from server
	public static String receiveFromServer() throws IOException{
		String received = null;
		try {
			received = (String) socketIn.readObject();
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.WARNING, "warning", e);
		}
		catch (OptionalDataException e1) {
			return "error124";
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
	
	@SuppressWarnings("static-access")
	public void run(){
		String line = null;
		while (true){
			System.out.println("posso ricevere altro");
			try {
				line = receiveFromServer();
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "warning", e);
			}
			//essentially particular gui cases
			if(graphicType == 2){
				client.setYouCanSend(true);
			}
			if(line!=null && !line.equals("You can send!") && graphicType == 2){
				System.out.println("into if " + line);
				if(line.equals("Logout successful")){
					client.getSynchronizedMessageForGUI().put(line);
					client.getSynchronizedMessageToChangeWindow().put("/login");
				}
				else if(line.equals("sent You have already placed a family member!")){
					client.getSynchronizedMessageForGUI().put("No");
					client.setYouCanSend(true);
				}
				else if(line.equals("Yes") || line.equals("No")){
					client.getSynchronizedMessageForGUI().put(line);
				}
				else if(line.equals("/game")){
					client.setStartingGame(true);
					client.getSynchronizedMessageByGUI().put("exit");			
					System.out.println("settato a exit!");                     
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					client.getSynchronizedMessageToChangeWindow().put("/game");
				}
				else if(line.contains("/chat")){
					System.out.println(line);
					client.getChatIn().put(line);
					System.out.println("sono dopo la put");
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
				else if(line.contains("/retry")){
					String [] messageRet = line.split("/retry");
					try {
						client.getClientOutputHandler().sendToServer(messageRet[1]);
					} catch (IOException e) {
						e.printStackTrace();
					}   
					
				}
				else if(line.contains("Timeout expired")){
					client.getSynchronizedMessageToChangeWindow().put("/login");
					client.getSynchronizedMessageInfo().put(line);
					client.getSynchronizedMessageByGUI().put("skipCommand");
				}
				else if(line.contains("The winner is")){
					client.getSynchronizedMessageToChangeWindow().put("/login");
					client.getSynchronizedMessageInfo().put(line);
					client.getSynchronizedMessageByGUI().put("skipCommand");
				}
				//open new window possibilities
				else if(line.contains("/numberservant")){
					client.getSynchronizedMessageToChangeWindow().put("/numberservant");
					String [] servantsOwned = line.split("/numberservant");
					client.getSynchronizedMessageInfo().put(servantsOwned[1]);   
				}
				else if(line.contains("/exchangeprivilege")){
					client.getSynchronizedMessageToChangeWindow().put("/exchangeprivilege");
					String [] messageInfo = line.split("/exchangeprivilege");
					client.getSynchronizedMessageInfo().put(messageInfo[1]);  
				}
				else if(line.contains("/leadercard")){
					client.getSynchronizedMessageToChangeWindow().put("/leadercard");
					String [] messageInfo = line.split("/leadercard");
					client.getSynchronizedMessageInfo().put(messageInfo[1]);  
				}
				else if(line.contains("/supportvatican")){
					client.getSynchronizedMessageToChangeWindow().put("/supportvatican");
					String [] messageInfo = line.split("/supportvatican");
					client.getSynchronizedMessageInfo().put(messageInfo[1]);  
				}
				else if(line.contains("/choosetrade")){
					client.getSynchronizedMessageToChangeWindow().put("/choosetrade");
					String [] messageInfo = line.split("/choosetrade");
					client.getSynchronizedMessageInfo().put(messageInfo[1]);  
				}
				else if(line.contains("/choosediscount")){
					client.getSynchronizedMessageToChangeWindow().put("/choosediscount");
					String [] messageInfo = line.split("/choosediscount");
					client.getSynchronizedMessageInfo().put(messageInfo[1]);  
				}
				else if(line.contains("/paymilitarypoint")){
					client.getSynchronizedMessageToChangeWindow().put("/paymilitarypoint");
					String [] messageInfo = line.split("/paymilitarypoint");
					client.getSynchronizedMessageInfo().put(messageInfo[1]);  
				}
				else if(line.contains("/bonusaction")){
					client.getSynchronizedMessageToChangeWindow().put("/bonusaction");
					String [] messageInfo = line.split("/bonusaction");
					client.getSynchronizedMessageInfo().put(messageInfo[1]);  
				}
			}
			//for cli clients, just stamps the received line 
			else if(line!=null && graphicType!=2){
				System.out.println(line);
			}
			if(line.equals("errore124")){
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
