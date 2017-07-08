package it.polimi.ingsw.pc34.SocketRMICongiunction;

import it.polimi.ingsw.pc34.RMI.ServerRMI;
import it.polimi.ingsw.pc34.RMI.SynchronizedBoardView;
import it.polimi.ingsw.pc34.RMI.SynchronizedString;
import it.polimi.ingsw.pc34.RMI.UserRMIImpl;
import it.polimi.ingsw.pc34.Socket.ClientSOC;
import it.polimi.ingsw.pc34.View.GUI.LaunchGUI;
import it.polimi.ingsw.pc34.View.GUI.Main;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Povaz on 10/06/2017.
 */

public class Client {
	public static Main guiReference = null;
	private UserRMIImpl userLoginRMI;
	private ClientSOC userSoc;
	private SynchronizedString messageByGUI;
	private SynchronizedString messageForGUI;
	private SynchronizedString messageToChangeWindow;
	private SynchronizedString messageInfo;
	private SynchronizedString messageChat;
	private SynchronizedBoardView boardForGUI;

	public Client(UserRMIImpl userLoginRMI) {
		this.userLoginRMI = userLoginRMI;
	}

	public Client(ClientSOC userSoc) {
		this.userSoc = userSoc;
	}

	public UserRMIImpl getUserLoginRMI() {
		return userLoginRMI;
	}

	public ClientSOC getUserSoc() {
		return userSoc;
	}

	public void startClientRMI() throws IOException, AlreadyBoundException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(8000);
		ServerRMI serverRMI = (ServerRMI) registry.lookup("serverRMI");

		if (this.getUserLoginRMI().isGUI()){
			Thread mainGui = new Thread(new LaunchGUI());
			mainGui.start();

			// wait until guiReference is initialized in MainGUI
			while(guiReference == null){
				System.out.println("While guiReference");
			}

			System.out.println(guiReference);

			// wait until guiReference.getBoardViewFromServer() is initialized in MainGUI
			while(guiReference.getBoardViewFromServer() == null){
				System.out.println("While BoardView");
			}

			System.out.println("After Board View");

			messageForGUI = guiReference.getFromServerToGui();
			messageByGUI = guiReference.getFromGuiToServer();
			messageToChangeWindow = guiReference.getOpenWindow();
			messageInfo = guiReference.getInfoFromServer();
			messageChat = guiReference.getChatFromServer();
			boardForGUI = guiReference.getBoardViewFromServer();

			this.getUserLoginRMI().setSynchronizedMessageToChangeWindow(messageToChangeWindow);
			this.getUserLoginRMI().setSynchronizedMessageForGUI(messageForGUI);
			this.getUserLoginRMI().setSynchronizedMessageByGUI(messageByGUI);
			this.getUserLoginRMI().setSynchronizedMessageInfo(messageInfo);
			this.getUserLoginRMI().setMessageChat(messageChat);
			this.getUserLoginRMI().setSynchronizedBoardView(boardForGUI);
			this.getUserLoginRMI().loginHandlerGUI(serverRMI);
		} else {
			this.getUserLoginRMI().loginHandler(serverRMI);
		}
	}

	public void startClientSOC() throws IOException {
		if (this.getUserSoc().getGraphicType() == 2) {
			Thread mainGui = new Thread(new LaunchGUI());
			mainGui.start();

			// wait until guiReference is initialized in MainGUI
			while(guiReference == null){
				System.out.println("While guiReference");
			}

			System.out.println(guiReference);

			// wait until guiReference.getBoardViewFromServer() is initialized in MainGUI
			while(guiReference.getBoardViewFromServer() == null){
				System.out.print("While guiReference");
			}

			messageForGUI = guiReference.getFromServerToGui();
			messageByGUI = guiReference.getFromGuiToServer();
			messageToChangeWindow = guiReference.getOpenWindow();
			messageInfo = guiReference.getInfoFromServer();
			messageChat = guiReference.getChatFromServer();// TODO controlla
			boardForGUI = guiReference.getBoardViewFromServer();

			this.getUserSoc().setSynchronizedMessageToChangeWindow(messageToChangeWindow);
			this.getUserSoc().setSynchronizedMessageForGUI(messageForGUI);
			this.getUserSoc().setSynchronizedMessageByGUI(messageByGUI);
			this.getUserSoc().setSynchronizedMessageInfo(messageInfo);
			this.getUserSoc().setChatIn(messageChat);
			this.getUserSoc().setSynchronizedBoardView(boardForGUI);
			System.out.println("si!"); 
		    this.getUserSoc().loginHandlerGUI(); 
		}
		else{ 
		      Thread userSoc = new Thread(this.userSoc); 
		      userSoc.start(); 
		} 
	}

	public static void main(String[] args)
			throws InputMismatchException, IOException, AlreadyBoundException, NotBoundException {
		Client client;
		boolean connectionChosen = false;
		int graphicChosen = 0;
		int choose;
		System.out.println("Which Interface do you want to use? 1. CLI 2. GUI");
		while (graphicChosen != 1 && graphicChosen != 2) {
			try {
				@SuppressWarnings("resource")
				Scanner inGraphic = new Scanner(System.in);
				graphicChosen = inGraphic.nextInt();

				if (graphicChosen != 1 && graphicChosen != 2) {
					System.out.println("Input Error");
				}
			} catch (InputMismatchException e) {
				System.out.println("Input Error");
			}
		}
		while (!connectionChosen) {
			try {
				System.out.println("Which Connection Type do you want to use? 1. RMI 2. Socket");
				@SuppressWarnings("resource")
				Scanner inChoose = new Scanner(System.in);
				choose = inChoose.nextInt();

				// TODO costruttore che dà in ingresso graphicChosen
				switch (choose) {
				case 1:
					UserRMIImpl userRMI;
					if (graphicChosen == 1) {
						userRMI = new UserRMIImpl(false);
					} else {
						userRMI = new UserRMIImpl(true);
					}
					client = new Client(userRMI);
					client.startClientRMI();
					connectionChosen = true;
					break;
				case 2:
					ClientSOC userSoc = new ClientSOC(graphicChosen);
					client = new Client(userSoc);
					client.startClientSOC();
					connectionChosen = true;
					break;
				default:
					System.out.println("Input Error");
				}
			} catch (InputMismatchException e) {
				System.out.println("Input Error");
			}
		}
	}
}
