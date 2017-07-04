package it.polimi.ingsw.pc34.SocketRMICongiunction;

import it.polimi.ingsw.pc34.RMI.ServerRMI;
import it.polimi.ingsw.pc34.RMI.ServerRMIImpl;
import it.polimi.ingsw.pc34.RMI.SynchronizedString;
import it.polimi.ingsw.pc34.RMI.UserRMIImpl;
import it.polimi.ingsw.pc34.Socket.ClientSOC;
import it.polimi.ingsw.pc34.View.GUI.LaunchGUI;
import it.polimi.ingsw.pc34.View.GUI.Main;
import javafx.application.Application;

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
    private SynchronizedString messageForGUI;

    public Client (UserRMIImpl userLoginRMI) {
        this.userLoginRMI = userLoginRMI;
    }

    public Client (ClientSOC userSoc) {
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

        if (this.getUserLoginRMI().isGUI()) {
            messageForGUI = new SynchronizedString();
            (new Thread(new LaunchGUI())).start();
            while(guiReference == null){}
            System.out.println(guiReference);
            guiReference.setServerComunication(messageForGUI);

            this.getUserLoginRMI().setSynchronizedMessageForGUI(messageForGUI);
            this.getUserLoginRMI().loginHandlerGUI(serverRMI);
        }
        else {
            this.getUserLoginRMI().loginHandler(serverRMI);
        }
    }

    public void startClientSOC() {
        Thread userSoc = new Thread (this.userSoc);
        userSoc.start();
    }

    public static void main (String[] args) throws InputMismatchException, IOException, AlreadyBoundException, NotBoundException {
        Client client;
        boolean connectionChosen = false;
        int graphicChosen = 0;
        int choose;
        System.out.println("Which Interface do you want to use? 1. CLI 2. GUI");
        while(graphicChosen!=1 && graphicChosen!=2){
        	try {	
        		@SuppressWarnings("resource")
				Scanner inGraphic = new Scanner(System.in);
	        	graphicChosen = inGraphic.nextInt();

	        	if(graphicChosen!=1 && graphicChosen!=2){
	        		System.out.println("Input Error");
	        	}
	        }
	        catch (InputMismatchException e) {
	            System.out.println("Input Error");
	        }
        }
        while (!connectionChosen) {
            try {
                System.out.println("Which Connection Type do you want to use? 1. RMI 2. Socket");
                @SuppressWarnings("resource")
				Scanner inChoose = new Scanner(System.in);
                choose = inChoose.nextInt();

                //TODO costruttore che dà in ingresso graphicChosen
                switch (choose) {
                    case 1:
                        UserRMIImpl userRMI;
                        if (graphicChosen == 1) {
                            userRMI = new UserRMIImpl(false);
                        }
                        else {
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
            }
            catch (InputMismatchException e) {
                System.out.println("Input Error");
            }
        }
    }
}
