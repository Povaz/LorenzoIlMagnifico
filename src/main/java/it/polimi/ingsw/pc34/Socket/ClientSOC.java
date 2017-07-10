package it.polimi.ingsw.pc34.Socket;

import it.polimi.ingsw.pc34.RMI.SynchronizedBoardView;
import it.polimi.ingsw.pc34.RMI.SynchronizedString;
import it.polimi.ingsw.pc34.View.GUI.BoardView;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;

//if client decides to use socket as comunication tool, ClientSoc will be launched
public class ClientSOC implements Runnable {
	private final String ip = System.getProperty("myapplication.ip");
	private final int port = 1337;
	private static Socket socketServer;
	private final int graphicType;
	private ClientOutputHandler cliOutHan;
	private ClientInputHandler cliInHan;
	
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
    private boolean startingGame; //Boolean variable that tells if a User is currently in Game 
    
    boolean youCanSend;
    
	public ClientSOC(int graphicType) throws UnknownHostException, IOException{
		socketServer = new Socket (ip, port);
		this.graphicType = graphicType;
		youCanSend = true;
	}

	//set and get
	
	public void setBoardView (BoardView boardView) {this.boardView.put(boardView);}

	public void setStartingGame(boolean value){
		startingGame = value;
	}
	
	public ClientOutputHandler getClientOutputHandler(){
		return cliOutHan;
	}
	
	public ClientInputHandler getClientInputHandler(){
		return cliInHan;
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

    private void setUsername(String username) {
        this.username = username;
    }
	
	private void setKeyword(String keyword) {
        this.keyword = keyword;
    }
	
    //called only if client is cli
	public void run() {
		System.out.println("Connection established");
		System.out.println("");
		
		//2 thread, 1 for input and 1 for output
		//output
		String messageGraphicType = "" + graphicType;
		cliOutHan = new ClientOutputHandler (socketServer, this, graphicType);
		try {
			cliInHan = new ClientInputHandler (socketServer, this, graphicType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			ClientOutputHandler.sendToServer(messageGraphicType);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Thread output = new Thread(cliOutHan);
		output.start();
		setYouCanSend(true);
		//input
		Thread input = new Thread(cliInHan);
		input.start();
		
	}

	//login path for gui
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
			startGame = new Thread(cliInHan);
			startGame.start();
		}
    }
	
	//send username and password
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
	
	//register path for gui
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

    //start instead of run for gui
    public void loginHandlerGUI() throws IOException {
		System.out.println("Connection established");
		System.out.println("");
		cliOutHan = new ClientOutputHandler (socketServer, this, graphicType);
		cliInHan = new ClientInputHandler (socketServer, this, graphicType);
		String messageGraphicType = "" + graphicType; 
		ClientOutputHandler.sendToServer(messageGraphicType);
		String message = getSynchronizedMessageByGUI().get();
		ClientOutputHandler.sendToServer(message);
		String choose;
	    while (!startingGame) {
            choose = messageByGUI.get();

            switch (choose) {
                case "/login":
                    this.loginGUI();
                    break;
                case "/logout":
                	getSynchronizedMessageForGUI().put("Not available on Socket");
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
	    outputGUI = new Thread(cliOutHan);
	    outputGUI.start();
		
	}
	
}