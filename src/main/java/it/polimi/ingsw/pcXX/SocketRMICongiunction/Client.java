package it.polimi.ingsw.pcXX.SocketRMICongiunction;

import it.polimi.ingsw.pcXX.RMI.UserLoginImpl;

import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Povaz on 10/06/2017.
 */

public class Client {
    private UserLoginImpl userLoginRMI;
    // ClientSocket

    public Client (UserLoginImpl userLoginRMI) {
        this.userLoginRMI = userLoginRMI;
    }

    public Client (/*ClientSocket*/) {
        //ClientSocket
    }

    public void startClientRMI() {
        Thread userLoginRMI = new Thread (this.userLoginRMI);
        userLoginRMI.start();
    }

    public void startClientSocket () {
        //start clientSocket
    }

    public static void main (String[] args) throws RemoteException, InputMismatchException {
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
                        //ClientSocket
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
