package it.polimi.ingsw.pc34.Socket;

import it.polimi.ingsw.pc34.RMI.ServerRMI; 
import it.polimi.ingsw.pc34.RMI.SynchronizedBoardView;
import it.polimi.ingsw.pc34.RMI.SynchronizedString;
import it.polimi.ingsw.pc34.View.GUI.BoardView;
import it.polimi.ingsw.pc34.View.GUI.LaunchGUI;
import it.polimi.ingsw.pc34.View.GUI.Main;
import javafx.application.Application;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException; 
import java.util.InputMismatchException; 
 
import org.json.JSONException; 

//if client decides to use socket as comunication tool, ClientSoc will be launched
public class ClientSOC implements Runnable {
	private final String ip = System.getProperty("myapplication.ip");
	private final int port = 1337;
	private static Socket socketServer;
	private final int graphicType;
	private ClientOutputHandler coh;
	private ClientInputHandler cih;
	
	private SynchronizedString messageByGUI;
    private SynchronizedString messageForGUI;
    private SynchronizedString messageToChangeWindow;
    private SynchronizedString messageInfo;
    private SynchronizedString chatIn;
    private SynchronizedBoardView boardView;
	
    private Thread startGame;
    private Thread outputGUI;  
    private String username; 
    private String keyword; 
    private boolean logged; //Boolean variabile that tells if a User is currently logged 
    private boolean startingGame; //Boolean variable that tells if a User is currently in Game 
    
    boolean youCanSend;
    
	public ClientSOC(int graphicType) throws UnknownHostException, IOException{
		socketServer = new Socket (ip, port);
		this.graphicType = graphicType;
		youCanSend = true;
	}
	
	public void setBoardView (BoardView boardView) {this.boardView.put(boardView);}

	public void setStartingGame(boolean value){
		startingGame = value;
	}
	
	public ClientOutputHandler getClientOutputHandler(){
		return coh;
	}
	
	public ClientInputHandler getClientInputHandler(){
		return cih;
	}
	
	public void setYouCanSend(boolean value){
		youCanSend = value;
	}
	
	public SynchronizedString getChatIn(){
		return chatIn;
	}
	
	public boolean getYouCanSend(){
		return youCanSend;
	}
	
	public int getGraphicType(){
		return graphicType;
	}
	
	public SynchronizedString getSynchronizedMessageByGUI() {
        return messageByGUI;
    }
	
	public SynchronizedString getSynchronizedMessageForGUI() {
        return messageForGUI;
    }
	
	public SynchronizedString getSynchronizedMessageInfo() {
        return messageInfo;
    }
	
	public void setSynchronizedMessageByGUI(SynchronizedString messageByGUI) {
        this.messageByGUI = messageByGUI;
    }
	
    public void setSynchronizedMessageForGUI(SynchronizedString messageForGUI) {
        this.messageForGUI = messageForGUI;
    }

    public void setSynchronizedMessageToChangeWindow(SynchronizedString messageToChangeWindow) {this.messageToChangeWindow = messageToChangeWindow; }

    public SynchronizedString getSynchronizedMessageToChangeWindow() {return messageToChangeWindow; }
    
    public void setSynchronizedBoardView (SynchronizedBoardView boardView) {this.boardView = boardView;}

    public void setSynchronizedMessageInfo (SynchronizedString messageInfo) {this.messageInfo = messageInfo; }

    public void setChatIn(SynchronizedString chatIn) {
        this.chatIn = chatIn;
    }

	public void run() {
		if(graphicType==1){
			System.out.println("Connection established");
			System.out.println("");
			
			//2 thread, 1 for input and 1 for output
			//output
			String messageGraphicType = "" + graphicType;
			coh = new ClientOutputHandler (socketServer, this, graphicType);
			try {
				cih = new ClientInputHandler (socketServer, this, graphicType);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				ClientOutputHandler.sendToServer(messageGraphicType);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		Thread output = new Thread(coh);
		output.start();
		setYouCanSend(true);
		//input
		Thread input = new Thread(cih);
		input.start();
		
	}

	private boolean isLogged() {
        return logged;
    }
	
	private void setUsername(String username) {
        this.username = username;
    }
	
	private void setKeyword(String keyword) {
        this.keyword = keyword;
    }
	
	private void loginGUI() throws StreamCorruptedException{ //Login procedure for GUI User
		try {
			ClientOutputHandler.sendToServer("/login");
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
			ClientInputHandler.receiveFromServer();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.insertDataGUI();
		String resultLogin = null;
		while(resultLogin==null || resultLogin.equals("You can send!")){
			try {
				resultLogin = ClientInputHandler.receiveFromServer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		getSynchronizedMessageForGUI().put(resultLogin);
		if(resultLogin.equals("Login successful")){
			startGame = new Thread(cih);
			startGame.start();
		}
    }
	
	private void insertDataGUI() { //For a RMI GUI User
        this.setUsername(messageByGUI.get());
        try {
			ClientOutputHandler.sendToServer(username);
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
			ClientInputHandler.receiveFromServer();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        this.setKeyword(messageByGUI.get());
        try {
			ClientOutputHandler.sendToServer(keyword);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
    public void setLogged(boolean logged) {
        this.logged = logged;
    }
    
    private void registrationGUI() { //Registration procedure for RMI GUI Users
    	try {
			ClientOutputHandler.sendToServer("/registration");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	try {
			ClientInputHandler.receiveFromServer();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	this.insertDataGUI();
    	String resultRegister = null;
		try {
			resultRegister = ClientInputHandler.receiveFromServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		getSynchronizedMessageForGUI().put(resultRegister);
    }

	private void logout() throws IOException {
		try {
			ClientOutputHandler.sendToServer("/logout");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String resultLogout = null;
		try {
			resultLogout = ClientInputHandler.receiveFromServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		getSynchronizedMessageForGUI().put(resultLogout);
		getSynchronizedMessageToChangeWindow().put("/login");
		String message = getSynchronizedMessageByGUI().get();
		ClientOutputHandler.sendToServer(message);
		startGame.interrupt();
		cih = new ClientInputHandler (socketServer, this, graphicType);
	}
	
	public void loginHandlerGUI() throws IOException {
		System.out.println("Connection established");
		System.out.println("");
		coh = new ClientOutputHandler (socketServer, this, graphicType);
		cih = new ClientInputHandler (socketServer, this, graphicType);
		String messageGraphicType = "" + graphicType; 
		ClientOutputHandler.sendToServer(messageGraphicType);
		String message = getSynchronizedMessageByGUI().get();
		ClientOutputHandler.sendToServer(message);
		String ok = ClientInputHandler.receiveFromServer();
		String choose;
	    while (!startingGame) {
            choose = messageByGUI.get();

            switch (choose) {
                case "/login":
                    this.loginGUI();
                    break;
                case "/logout":
                    this.logout();
                    break;
                case "/registration":
                    this.registrationGUI();
                    break;
                default:
                    //GUI User shouldn't be able to get here
                	System.out.println("Stato non valido");
                    break;
            }
	    } 
	    System.out.println("Uscita starting game!");
	    outputGUI = new Thread(coh);
	    outputGUI.start();
		
	}
	
}