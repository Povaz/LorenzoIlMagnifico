package it.polimi.ingsw.pc34.Socket;

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
	
	public ClientSOC(int graphicType) throws UnknownHostException, IOException{
		socketServer = new Socket (ip, port);
		this.graphicType = graphicType;
	}

	public void run() {
		System.out.println("Connection established");
		System.out.println("");
		
		if(graphicType==2){
			Application.launch(Main.class);
		}
		
		//2 thread, 1 for input and 1 for output
		//output
		ClientOutputHandler coh = new ClientOutputHandler (socketServer);
		if(graphicType==1){
				Thread output = new Thread(coh);
				output.start();
		}
		
		//input
		ClientInputHandler cih = null;
		try {
			cih = new ClientInputHandler (socketServer, graphicType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread input = new Thread(cih);
		input.start();
	}
	
}