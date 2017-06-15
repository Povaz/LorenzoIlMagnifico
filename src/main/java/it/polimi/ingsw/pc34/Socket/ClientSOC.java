package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import static it.polimi.ingsw.pc34.View.TerminalInput.askNumber;

public class ClientSOC implements Runnable {
	private String username;
	private final String ip = "127.0.0.1";
	private final int port = 1337;
	private static Socket socketServer;
	
	public ClientSOC() throws UnknownHostException, IOException{
		socketServer = new Socket (ip, port);
	}

	public void run() {
		System.out.println("Connection established");
		System.out.println("");
		boolean logged = false;
		while(!logged){
			try {
				logged=login();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		System.out.println("Waiting Game to Start. . . \nWrite '/logout' to logout ");
		System.out.println("");
		
		ClientComunicationHandler sch = new ClientComunicationHandler (socketServer , this);
		Thread output = new Thread(sch);
		output.start();
		
		//sempre pronto a ricevere notifiche
		String line = null;
		synchronized(this){
			while (true){
				try {
					line = receiveFromServer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(line);
				System.out.println("");
				if(line.equals("Game Started")){
					break;
				}
				else if(line.equals("logout")){
					break;
				}
			}
		}
		return;
	}
	
	private boolean login() throws IOException{
		String decision = null;
		boolean cycle = true;
		while(cycle){
			decision = loginVsRegister();
			Scanner in = new Scanner(System.in);
			sendToServer(decision);
			String username = sendDataLog(decision);
			if(!username.equals("/back")){
				cycle=false;
			}
		}
		if(decision.equals("1")){
			return true;
		}
		else{
			return false;
		}
	}
	
	private static String loginVsRegister (){
		Scanner in = new Scanner (System.in);
		String decision;
		while(true){
			System.out.println("What you want to do? Insert number: \n 1-login \n 2-register");
			decision = in.nextLine();
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
	
	private static String sendDataLog ( String decision) throws IOException{
		String username;
		String password;
		Scanner in = new Scanner (System.in); 
		while(true){	
			System.out.println("(write '/back' to go back to the previous selection)");
			System.out.println("Username :");	
			username = in.nextLine();
			sendToServer(username);
			if (username.equals("/back")){
				return username;
			}		
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
					System.out.println("Successful Register! Welcome " + username + "!");
					System.out.println("");
					return username;
				}
			}
			else if (receivedRespPass.equals("wrong combination!")){
				if(decision.equals("1")){
					System.out.println("Wrong combination, retry");
					System.out.println("");
				}
				else{
					System.out.println("Register failed! Retry");
					System.out.println("");
				}
			}
		}
	}
	
	synchronized private static void sendToServer(String message) throws IOException{
		PrintWriter socketOut = new PrintWriter(socketServer.getOutputStream(), true);
		socketOut.println(message);
		socketOut.flush();
	}
	
	synchronized private static String receiveFromServer() throws IOException{
		Scanner socketIn = new Scanner(socketServer.getInputStream());
		String received = socketIn.nextLine();
		//PrintWriter socketOut = new PrintWriter(socketServer.getOutputStream(), true);
		//socketOut.println("ok");
		//socketOut.flush();
		return received;
		
	}
	
	@SuppressWarnings("resource")

	synchronized public static String askAction(){
		System.out.println("Select Action: \n 1-Set Family Member \n 2-Use Leader Card \n 3-Draw Leader Card \n 4-Lose your Turn");
		int numberAction = askNumber(1, 4);
		switch(numberAction){
			case 1:
				System.out.println("Select Family Member Color: \n 1-White \n 2-Orange \n 3-Black \n 4-No Color");
				int numberColorFamilyMember = askNumber(1, 4);
				System.out.println("Select Spot: \n 1-Card \n 2-Market \n 3-Production \n 4-Harvest \n 5-Council Palace");
				int numberSpot = askNumber(1, 5);
				int numberServant;
				switch(numberSpot){
					case 1:
						System.out.println("Select Tower \n (insert from 1 to 4)");
						int numberTower = askNumber(1, 4);
						System.out.println("Select Floor \n (insert from 1 to 4)");
						int numberFloor = askNumber(1, 4);
						System.out.println("Select Number of Servant To Use \n (from 0 to 100)");
						numberServant = askNumber(0,100); 
						return (Integer.toString(numberAction) + "," + Integer.toString(numberColorFamilyMember) + "," + Integer.toString(numberSpot) + "," + Integer.toString(numberTower) + "," + Integer.toString(numberFloor) + "," + Integer.toString(numberServant));
					case 2:
						System.out.println("Select Market's Spot \n ( 1-5 Coins 2-5 Servants 3-2 Coins+3 Military Points 4-2 Council Privileges)");
						int numberMarket = askNumber(1, 5);
						System.out.println("Select Number of Servant To Use \n (from 0 to 100)");
						numberServant = askNumber(0,100); 
						return (Integer.toString(numberAction) + "," + Integer.toString(numberColorFamilyMember) + "," + Integer.toString(numberSpot) + "," + Integer.toString(numberMarket) + "," + Integer.toString(numberServant));
					case 3:
						System.out.println("Select Production's Spot \n (1-normal 2-infinite)");
						int numberProduction = askNumber(1, 2);
						System.out.println("Select Number of Servant To Use \n (from 0 to 100)");
						numberServant = askNumber(0,100); 
						return (Integer.toString(numberAction) + "," + Integer.toString(numberColorFamilyMember) + "," + Integer.toString(numberSpot) + "," + Integer.toString(numberProduction) + "," + Integer.toString(numberServant));
					case 4:
						System.out.println("Select Harvest's Spot \n (1-normal 2-infinite)");
						int numberHarvest = askNumber(1, 2);
						System.out.println("Select Number of Servant To Use \n (from 0 to 100)");
						numberServant = askNumber(0,100); 
						return (Integer.toString(numberAction) + "," + Integer.toString(numberColorFamilyMember) + "," + Integer.toString(numberSpot) + "," + Integer.toString(numberHarvest) + "," + Integer.toString(numberServant));		
					case 5:
						System.out.println("Select Number of Servant To Use (from 0 to 100)");
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
	
}