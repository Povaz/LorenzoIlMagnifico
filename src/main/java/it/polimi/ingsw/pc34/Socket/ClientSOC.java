package it.polimi.ingsw.pc34.Socket;

import it.polimi.ingsw.pc34.RMI.SynchronizedBoardView;
import it.polimi.ingsw.pc34.RMI.SynchronizedString;
import it.polimi.ingsw.pc34.View.GUI.LaunchGUI;
import it.polimi.ingsw.pc34.View.GUI.Main;
import javafx.application.Application;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

//if client decides to use socket as comunication tool, ClientSoc will be launched
public class ClientSOC implements Runnable {
	private final String ip = System.getProperty("myapplication.ip");
	private final int port = 1337;
	private static Socket socketServer;
	private final int graphicType;
	
	private SynchronizedString messageByGUI;
    private SynchronizedString messageForGUI;
    private SynchronizedString messageToChangeWindow;
    private SynchronizedString messageInfo;
    private SynchronizedString chatOut;
    private SynchronizedString chatIn;
    private SynchronizedBoardView boardView;
	
	public ClientSOC(int graphicType) throws UnknownHostException, IOException{
		socketServer = new Socket (ip, port);
		this.graphicType = graphicType;
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
	
	public void setSynchronizedMessageByGUI(SynchronizedString messageByGUI) {
        this.messageByGUI = messageByGUI;
    }
	
    public void setSynchronizedMessageForGUI(SynchronizedString messageForGUI) {
        this.messageForGUI = messageForGUI;
    }

    public void setSynchronizedMessageToChangeWindow(SynchronizedString messageToChangeWindow) {this.messageToChangeWindow = messageToChangeWindow; }

    public void setSynchronizedBoardView (SynchronizedBoardView boardView) {this.boardView = boardView;}

    public void setSynchronizedMessageInfo (SynchronizedString messageInfo) {this.messageInfo = messageInfo; }

    public void setChatIn(SynchronizedString chatIn) {
        this.chatIn = chatIn;
    }

    public void setChatOut(SynchronizedString chatOut) {
        this.chatOut = chatOut;
    }
	
	@SuppressWarnings("restriction")
	public void run() {
		System.out.println("Connection established");
		System.out.println("");
		
		if(graphicType==2){
			Application.launch(Main.class);
		}
		
		//2 thread, 1 for input and 1 for output
		//output
		String messageGraphicType = "" + graphicType;
		ClientOutputHandler coh = new ClientOutputHandler (socketServer, this, graphicType);
		Thread output = new Thread(coh);
		output.start();
		
		//input
		ClientInputHandler cih = null;
		try {
			cih = new ClientInputHandler (socketServer, this, graphicType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread input = new Thread(cih);
		input.start();
		try {
			ClientOutputHandler.sendToServer(messageGraphicType);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}