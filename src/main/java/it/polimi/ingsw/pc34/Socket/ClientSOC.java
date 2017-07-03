package it.polimi.ingsw.pc34.Socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

//if client decides to use socket as comunication tool, ClientSoc will be launched
public class ClientSOC implements Runnable {
	private final String ip = "127.0.0.1";
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
			//create GUI here!!!
		}
		
		//2 thread, 1 for input and 1 for output
		//output
		ClientOutputHandler coh = new ClientOutputHandler (socketServer);
		Thread output = new Thread(coh);
		output.start();
		
		//input
		ClientInputHandler cih = null;
		try {
			cih = new ClientInputHandler (socketServer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread input = new Thread(cih);
		input.start();
	}
	
}