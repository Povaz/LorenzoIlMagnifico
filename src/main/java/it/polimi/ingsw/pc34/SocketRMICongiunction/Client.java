package it.polimi.ingsw.pc34.SocketRMICongiunction;

import com.sun.org.apache.regexp.internal.RE;
import it.polimi.ingsw.pc34.RMI.ServerLogin;
import it.polimi.ingsw.pc34.RMI.UserLoginImpl;
import it.polimi.ingsw.pc34.Socket.ClientSOC;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Povaz on 10/06/2017.
 */

public class Client {
    private UserLoginImpl userLoginRMI;
    private ClientSOC userSoc;

    public Client (UserLoginImpl userLoginRMI) {
        this.userLoginRMI = userLoginRMI;
    }

    public Client (ClientSOC userSoc) {
        this.userSoc = userSoc;
    }

    public UserLoginImpl getUserLoginRMI() {
        return userLoginRMI;
    }

    public ClientSOC getUserSoc() {
        return userSoc;
    }
    
    public void startClientRMI() throws IOException, AlreadyBoundException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(8000);
        ServerLogin serverLogin = (ServerLogin) registry.lookup("serverLogin");

        this.userLoginRMI.loginHandler(serverLogin);
        userLoginRMI.sendMessage("Waiting for the Game to Start");

        this.userLoginRMI.gameHandler(serverLogin);
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
                        UserLoginImpl userLoginImpl = new UserLoginImpl();
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
