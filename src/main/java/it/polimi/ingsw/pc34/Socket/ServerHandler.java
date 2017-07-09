package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import it.polimi.ingsw.pc34.Controller.BooleanCreated;
import it.polimi.ingsw.pc34.Controller.GameController;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;
import it.polimi.ingsw.pc34.View.GUI.BoardView;

//class that is assigned one per one to a client, and has to deal with server's comunication with the client
public class ServerHandler implements Runnable{
	private Socket socket;
	private int fase; 
	private LobbyFlow lobbyFlow;
	private String username;
	private ServerSOC serverSoc;
	private GameController gameController;
	private String stateGame;
	private ObjectOutputStream socketOut;
	private String graphicType;
	private boolean sendGUI;
	private boolean guiIsReady = true;
	private boolean boardvieww = false;
	
	private static final Logger LOGGER = Logger.getLogger(ServerHandler.class.getName());
	
	private boolean confirm;
	private BooleanCreated isNotConnect = new BooleanCreated();
	private String answer = null;
	
	public ServerHandler(Socket socket, Lobby lobby, ServerSOC serverSoc){
		this.socket = socket;
		this.fase = 0;
		this.lobbyFlow = new LobbyFlow(lobby, serverSoc, this);
		this.serverSoc = serverSoc;
		this.stateGame = null;
		try {
			this.socketOut = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "warning", e);
		}
		sendGUI = false;
		confirm =true;
	}
	
	//boolean to send directly to gui interface
	public void setSendGUI(boolean value){
		sendGUI = value;
	}
	
	//set Fase, a variable used to direct the messages to a specific flow(lobby or game)
	public void setFase(int fase){
		this.fase = fase;
		if(fase==1){
			if(graphicType.equals("1")){
				sendToClient("Type: /playturn for an action; /chat to send message(you can always do this action, only if you are not doing something else);  /afk to disconnect from the game(you can always do this action, at any time)");
			}
			else if(graphicType.equals("2") && boardvieww == false){
				setSendGUI(true);
			    System.out.println(username + " sta per mandare /game"); 
				guiIsReady = false;
				sendToClient("/game"); 
				while(!guiIsReady){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("sono uscito dal blocco su GUIISREADY"); 
			}
			else{
				sendToClient("/game");
				return;
			}
		}
	}
	
	//other sets and gets
	public void setGameController(GameController gameController){
		this.gameController= gameController;
	}
	
	public void setName(String username){
		this.username = username;
	}
	
	public void setStateGame(String stateGame){
		this.stateGame = stateGame;
		System.out.println("settato a " + stateGame);
	}

	public String getName(){
		return username;
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	public String getGraphicType(){
		return graphicType;
	}
	
	//method to send messages to client
	synchronized public void sendToClient(String message) {
		if(message==null){
			return;
		}
		else if(message.equals("This Client has been disconnected")||message.equals("This game is finished")){
			setFase(0);
			//todo togliere riferimento in GameController e il tipo di interfaccia grafica
			try {
				serverSoc.getServer().disconnectPlayerSoc(username);
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "warning", e);
			}
			message += "\nInsert: /login to login, /logout to logout /registration to registrate a new user or /exit to close to application";
			lobbyFlow.reset();
			stateGame = null;
			if(graphicType.equals("2")){
				return;
			}
		}
		else if(message.equals("Reconnected to the game")){
			if(graphicType.equals("2")){
				sendGUI = true;
				sendToClient("Login successful");
				boardvieww = true;
				setFase(1);
				stateGame = null;
				return;
			}
			setFase(1);
			stateGame = null;
		}
		else if(message.equals("Action has been executed") && fase==1){
			stateGame = null;
			message += "\nInsert new command: /playturn, /chat, /afk";
			if(graphicType.equals("2")){
				return;
			}
		}
		else if(message.equals("You have already placed a family member!")){
			stateGame = null;
			message += "\nInsert new command: /playturn, /chat, /afk";
		}
		if(graphicType.equals("2") && fase == 0){
			if(sendGUI){
				sendGUI = false;
			}
			else{
				message = "You can send!";
			}
		}
		if(graphicType.equals("2")){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		try {
			socketOut.writeObject(message);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "warning", e);
		}
		System.out.println("sent " + message); 
	}
	
	synchronized public void sendToClientGUI (BoardView boardView){
		try {
			socketOut.writeObject("/update");
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "warning", e);
		}
		try {
			socketOut.writeObject(boardView);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "warning", e);
		}
	}
	
	//method to receive messages from client
	private String receiveFromClient() throws IOException{
		@SuppressWarnings("resource")
		Scanner socketIn = new Scanner(socket.getInputStream());
		String received = socketIn.nextLine();
		if(answer!=null && received.equals("yes")){
			answer=received;
		}
		System.out.println(received);
		return received;
	}
	
	//directs input to the Lobby Flow
	private String toLobbyHandler(String asked) throws JSONException, IOException{
		String answer=lobbyFlow.flow(asked);
		return answer;
	}
	
	//directs input to the Game Flow
	private String toGameHandler(String asked) throws IOException{
		String answer=gameController.flow(asked, username);
		return answer;
	}
	
	//open new window method sends specific messages to GUI to open particular windows
	public void openNewWindow(String message) throws RemoteException {
        sendToClient(message);
    }
    
    public void openNewWindow (String message, String toSynchro) {
    	sendToClient(message+(toSynchro));
    }
    
    public void openNewWindow(String message, int numberOfCP) {
        sendToClient(message+Integer.toString(numberOfCP));
    }
	
    public void openNewWindowDisconnect (String infoGUI) {
    	sendToClient("This Client has been disconnected");
    	sendToClient(infoGUI);
    }
    
    public void openNewWindowGameEnd (String message) {
    	sendToClient("This game is finished");
    	sendToClient(message);
    }
	
	public void run(){
		String line = null;
		try {
			this.graphicType=receiveFromClient();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(graphicType.equals("1")){
			confirm = false;
			sendToClient("Insert: /login to login, /logout to logout /registration to registrate a new user or /exit to close to application");
		}
		while (true){
			try {
				line = receiveFromClient();
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "warning", e);
			}
			if(confirm ){
				if(line.equals("1")){
					confirm = false;
					continue;
				}
				else{
					confirm = false;
				}
			}
			if(line.equals("ok") && graphicType.equals("2")){
				continue;
			}
			System.out.println("non entro " + guiIsReady);
			if(line.equals("3") && graphicType.equals("2") && guiIsReady == false){
				System.out.println("ricevuto 3 e sono nell'if che dici"); 
		        sendToClient("You can send!"); 
				guiIsReady = true;
				continue;
			}
			if(answer!=null && line.equals("yes")){
				continue;
			}
			String answer = null;
			System.out.println("manda in flow : " + line);
			//to lobby flow
			if(fase==0){
				if(line.equals("/exit")){
					break;
				}
				try {
					answer = toLobbyHandler(line);
				} catch (JSONException|IOException e) {
					LOGGER.log(Level.WARNING, "warning", e);
				}
			}
			//to game flow
			if(fase==1){
				if(graphicType.equals("2")){
					if(line.contains("/chat")){
						String [] mexChat = line.split("/chat");
						try {
							gameController.sendMessageChat(mexChat[1], username);
							System.out.println(mexChat[1]);
						} catch (IOException e) {
							e.printStackTrace();
						}
						continue;
					}
				}
				//have to decide the type of action
				if(stateGame==null) {
					switch (line){
						case "/playturn" :
							try {
								answer = toGameHandler(line);
							} catch (IOException e) {
								LOGGER.log(Level.WARNING, "warning", e);
							}
							if (graphicType.equals("2")) {
                                if (answer.equals("It isn't your turn")) {
                                    sendToClient("No");
                                }
                                if (answer.equals("I am still processing a request")) {
                                    sendToClient("/retry" + line);
                                }
                                else {
                                    sendToClient("Yes");
                                }
                            }
							if(answer==null){
								break;
							}
							if(answer.equals("What action you want to do? 1-action 2-place Leader Card 3-activate Leader Card 4-exchange Leader Card 5-skip")){
								stateGame = line;
							}
							break;
						case "/afk" :
							try {
								answer = toGameHandler(line);
							} catch (IOException e) {
								LOGGER.log(Level.WARNING, "warning", e);
							}
							stateGame = null;
							break;
						case "/chat" :
							answer = "Insert a message : ";
							stateGame = line;
							break;
						default :
							answer = "Wrong input, Type: /playturn for an action; /chat to send message; /afk to disconnect from the game(you can always do it, at any moment)";
							break;
						
					}
				}
				else {
					//you can always type /afk
					if(line.equals("/afk")){
						try {
							answer = toGameHandler(line);
						} catch (IOException e) {
							LOGGER.log(Level.WARNING, "warning", e);
						}
						sendToClient(answer);
						stateGame = null;
						continue;
					}
					
					//you have decided yet the type of action to perform
					switch (stateGame){
						case "/playturn" :
							try {
								answer = toGameHandler(line);
							} catch (IOException e) {
								LOGGER.log(Level.WARNING, "warning", e);
							}
							if (graphicType.equals("2")) {
                                if (answer.equals("It's not your turn") || answer.equals("Input error") || answer.equals("Input error, Retry!")) {
                                    sendToClient("No");
                                }
                                else {
                                	sendToClient("Yes");
                                }
                            }
							break;
						case "/chat" : 
							stateGame = null;
							try {
								gameController.sendMessageChat(line, username + " ");
							} catch (IOException e) {
								LOGGER.log(Level.WARNING, "warning", e);
							}
							answer = "Type: /playturn for an action; /chat to send message;  /afk to disconnect from the game";
							break;
						case "/vaticansupport" :
							try {
								answer = toGameHandler(line);
							} catch (IOException e) {
								LOGGER.log(Level.WARNING, "warning", e);
							}
							if (graphicType.equals("2")) {
                                if (answer.equals("It's not your turn") || answer.equals("Input error") || answer.equals("Input error, Retry!")) {
                                    sendToClient("No");
                                }
                                else {
                                    sendToClient("Yes");
                                }
                            }
							break;
						default :
							answer = "Wrong input, Type: /playturn for an action; /chat to send message; /afk to disconnect from the game";
							break;
					}
				}
			}

			if(graphicType.equals("1") || fase == 0){
				sendToClient(answer);
			}
		}
	}

	//check connection is up
	public boolean isNotConnected() {
		boolean result;
		answer = "waiting";
		sendToClient("Are you connected?");
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
            @Override
            public void run() {
            	if(answer.equals("waiting")){
            		isNotConnect.put(true);
            		answer=null;
            	}
            	else{
            		isNotConnect.put(false);
            		answer = null;
            	}
            }
        }, 2000);
		
		result = isNotConnect.get();
		return result;
	}

	public int getFase() {
		return fase;
	}

}
