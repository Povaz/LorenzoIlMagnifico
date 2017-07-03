package it.polimi.ingsw.pc34.SocketRMICongiunction;

import it.polimi.ingsw.pc34.RMI.ServerRMI;
import it.polimi.ingsw.pc34.RMI.UserRMIImpl;
import it.polimi.ingsw.pc34.Socket.ClientSOC;
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
    private UserRMIImpl userLoginRMI;
    private ClientSOC userSoc;

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
        this.userLoginRMI.loginHandler(serverRMI);
    }

    public void startClientSOC() {
        Thread userSoc = new Thread (this.userSoc);
        userSoc.start();
    }

    public static void main (String[] args) throws InputMismatchException, IOException, AlreadyBoundException, NotBoundException {
        Client client;
        boolean connectionChosen = false;
        boolean graphicChosen = false;
        int choose;
        while(!graphicChosen){
        	System.out.println("Which Interface do you want to use? 1. CLI 2. GUI");
        	Scanner inGraphic = new Scanner(System.in);
        	choose = inGraphic.nextInt();
        	switch (choose) {
        		case 1:
        			graphicChosen = true;
        	        choose = 0;
        			break;
	            case 2:
	                //QUI FAR PARTIRE GUI
	            	graphicChosen = true;
	            	connectionChosen = true;
	            	break;
	            default:
	                System.out.println("Input Error");
	        	}
        }
        while (!connectionChosen) {
            try {
                System.out.println("Which Connection Type do you want to use? 1. RMI 2. Socket");
                @SuppressWarnings("resource")
				Scanner inChoose = new Scanner(System.in);
                choose = inChoose.nextInt();

                switch (choose) {
                    case 1:
                        Application.launch(Main.class, args);
                        UserRMIImpl userLoginImpl = new UserRMIImpl();
                        client = new Client(userLoginImpl);
                        client.startClientRMI();
                        connectionChosen = true;
                        break;
                    case 2:
                        ClientSOC userSoc = new ClientSOC(); 
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
