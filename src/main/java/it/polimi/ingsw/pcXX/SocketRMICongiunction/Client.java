package it.polimi.ingsw.pcXX.SocketRMICongiunction;

import it.polimi.ingsw.pcXX.RMI.UserLoginImpl;
import it.polimi.ingsw.pcXX.Socket.ClientSOC;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
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

    public void startClientRMI() {
        Thread userLoginRMI = new Thread (this.userLoginRMI);
        userLoginRMI.start();
    }

    public void startClientSOC() {
        Thread userSoc = new Thread (this.userSoc);
        userSoc.start();
    }

    public static void main (String[] args) throws InputMismatchException, UnknownHostException, IOException {
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
