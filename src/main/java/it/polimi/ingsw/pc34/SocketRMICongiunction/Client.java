package it.polimi.ingsw.pc34.SocketRMICongiunction;

import it.polimi.ingsw.pc34.Controller.BooleanCreated;
import it.polimi.ingsw.pc34.RMI.ServerRMI;
import it.polimi.ingsw.pc34.RMI.UserRMIImpl;
import it.polimi.ingsw.pc34.Socket.ClientSOC;

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
        userLoginRMI.sendMessage("Waiting for the Game to Start");
        userLoginRMI.getGameIsStarting().get();
        userLoginRMI.gameHandler(serverRMI);
    }

    public void startClientSOC() {
        Thread userSoc = new Thread (this.userSoc);
        userSoc.start();
    }

    public static void main (String[] args) throws InputMismatchException, IOException, AlreadyBoundException, NotBoundException {
        Client client;
        boolean correct = false;
        while (!correct) {
            try {
                System.out.println("Which Connection Type do you want to use? 1. RMI 2. Socket");

                Scanner inChoose = new Scanner(System.in);
                int choose = inChoose.nextInt();

                switch (choose) {
                    case 1:
                        UserRMIImpl userLoginImpl = new UserRMIImpl();
                        client = new Client(userLoginImpl);
                        client.startClientRMI();
                        correct = true;
                        break;
                    case 2:
                        ClientSOC userSoc = new ClientSOC(); 
                        client = new Client(userSoc);
                        client.startClientSOC();
                        correct = true;
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
