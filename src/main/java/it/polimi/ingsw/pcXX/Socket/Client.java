package it.polimi.ingsw.pcXX.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.json.JSONException;

import static it.polimi.ingsw.pcXX.TerminalInput.askNumber;

public class Client {
	private String username;
	private final String ip = "127.0.0.1";
	private final int port = 1337;
	private static Socket socketServer;
	
	
	public void setName(String username){
		this.username=username;
	}
	
	synchronized public void startClient() throws IOException {
		System.out.println("Waiting Game to Start. . .");
		System.out.println("");
		
		//sempre pronto a ricevere notifiche
		String line;
		synchronized(this){while (true){
			line = receiveFromServer();
			System.out.println(line);
			System.out.println("");
			if(line.equals("Game Started")){
				break;
			}
		}}
		return;
	}
	
	synchronized public static void sendToServer(String message) throws IOException{
		PrintWriter socketOut = new PrintWriter(socketServer.getOutputStream(), true);
		socketOut.println(message);
		socketOut.flush();
	}
	
	synchronized public static String receiveFromServer() throws IOException{
		Scanner socketIn = new Scanner(socketServer.getInputStream());
		String received = socketIn.nextLine();
		PrintWriter socketOut = new PrintWriter(socketServer.getOutputStream(), true);
		socketOut.println("ok");
		socketOut.flush();
		return received;
		
	}
	
	synchronized private static String loginVsRegister (Scanner in){
		
		String decision;
		while(true){
			System.out.println("What you want to do? Insert number: \n 1-login \n 2-register");
			decision = in.nextLine();
			//prima era qua l'invio al server
			if(decision.equals("1")){
				System.out.println("LOGIN:");
				break;
			}
			else if(decision.equals("2")){
				System.out.println("REGISTER:");
				break;
			}
			System.out.println("Wrong input: Retry");
		}
		return decision;
	}
	synchronized private static String sendDataLog (Scanner in, String decision) throws IOException{
		String username;
		String password;
		
		while(true){	
			System.out.println("(write '/back' to go back to the previous selection)");
			System.out.println("Username :");	
			username = in.nextLine();
			if (username.equals("/back")){
				decision=loginVsRegister(in);
				return sendDataLog(in, decision);
			}
			sendToServer(username);		
			System.out.println("Password : ");
			password = in.nextLine();
			sendToServer(password);
			String receivedRespPass=receiveFromServer();
			System.out.println("");
			System.out.println(receivedRespPass);
			System.out.println("");
			
			if(receivedRespPass.equals("confirmed")){
				if(decision.equals("1")){
					System.out.println("Succesful Login! Welcome back " + username + "!");
					System.out.println("");
					return username;
				}
				else{
					System.out.println("Successful Register! Welcome" + username + "!");
					System.out.println("");
					return username;
				}
			}
			else if (receivedRespPass.equals("wrong combination")){
				if(decision.equals("1")){
					System.out.println("Combinazione errata, riprova");
					System.out.println("");
				}
				else{
					System.out.println("Registrazione fallita! Ritenta");
					System.out.println("");
				}
			}
		}
	}
	
	synchronized private static String login() throws IOException{
		String decision;
	    Scanner in = new Scanner(System.in);
		decision= loginVsRegister(in);
		sendToServer(decision);
		String username = sendDataLog(in, decision);
		in.close();
		return username;
	}
	
	@SuppressWarnings("resource")

	public synchronized static String askAction(){
		System.out.println("Select Action: \n 1-Set Family Member \n 2-Use Leader Card \n 3-Draw Leader Card \n 4-Lose your Turn");
		int numberAction = askNumber(1, 4);
		switch(numberAction){
			case 1:
				System.out.println("Select Family Member Color: \n 1-Black \n 2-Orange \n 3-White \n 4-No Color");
				int numberColorFamilyMember = askNumber(1, 4);
				System.out.println("Select Spot: \n 1-Card \n 2-Market \n 3-Production \n 4-Harvest \n 5-Council Palace");
				int numberSpot = askNumber(1, 5);
				int numberServant;
				switch(numberSpot){
					case 1:
						System.out.println("Select Tower");
						int numberTower = askNumber(1, 4);
						System.out.println("Select Floor");
						int numberFloor = askNumber(1, 4);
						System.out.println("Select Number Servant To Use (from 0 to 100)");
						numberServant = askNumber(0,100); 
						return (Integer.toString(numberAction) + "," + Integer.toString(numberColorFamilyMember) + "," + Integer.toString(numberSpot) + "," + Integer.toString(numberTower) + "," + Integer.toString(numberFloor) + "," + Integer.toString(numberServant));
					case 2:
						System.out.println("Select Market's Spot");
						int numberMarket = askNumber(1, 5);
						System.out.println("Select Number Servant To Use (from 0 to 100)");
						numberServant = askNumber(0,100); 
						return (Integer.toString(numberAction) + "," + Integer.toString(numberColorFamilyMember) + "," + Integer.toString(numberSpot) + "," + Integer.toString(numberMarket) + "," + Integer.toString(numberServant));
					case 3:
						System.out.println("Select Production's Spot");
						int numberProduction = askNumber(1, 2);
						System.out.println("Select Number Servant To Use (from 0 to 100)");
						numberServant = askNumber(0,100); 
						return (Integer.toString(numberAction) + "," + Integer.toString(numberColorFamilyMember) + "," + Integer.toString(numberSpot) + "," + Integer.toString(numberProduction) + "," + Integer.toString(numberServant));
					case 4:
						System.out.println("Select Harvest's Spot");
						int numberHarvest = askNumber(1, 2);
						System.out.println("Select Number Servant To Use (from 0 to 100)");
						numberServant = askNumber(0,100); 
						return (Integer.toString(numberAction) + "," + Integer.toString(numberColorFamilyMember) + "," + Integer.toString(numberSpot) + "," + Integer.toString(numberHarvest) + "," + Integer.toString(numberServant));		
					case 5:
						System.out.println("Select Number Servant To Use (from 0 to 100)");
						numberServant = askNumber(0,100); 
						return (Integer.toString(numberAction) + "," + Integer.toString(numberColorFamilyMember) + "," + Integer.toString(numberSpot) + "," + Integer.toString(numberServant));
				}
			case 2:
				System.out.println("Select Leader Card's Number");
				int numberLeaderCard = askNumber(1, 3);
				return (Integer.toString(numberAction) + "," + Integer.toString(numberLeaderCard));
			case 3: 
				System.out.println("Select Leader Card's Number");
				int numberLeaderCard2 = askNumber(1, 3);
				return (Integer.toString(numberAction) + "," + Integer.toString(numberLeaderCard2));
			case 4:
				return (Integer.toString(numberAction));
		}
		return "";
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		//Fa il login
		
		Client client = new Client();
		Client.socketServer = new Socket (client.ip, client.port);
		
		System.out.println("Connection established");
		System.out.println("");
		
		client.setName(login());
		
		try {
			client.startClient();
		}
		catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}