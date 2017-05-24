package it.polimi.ingsw.pcXX.RMI;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Created by Povaz on 24/05/2017.
 */
public class UserLoginImpl extends UnicastRemoteObject implements UserLogin{
    private String username;
    private String password;
    private boolean response;
    private int choose;

    public static final Object stop = null;

    public UserLoginImpl () throws RemoteException {
        this.username = "Null";
        this.password = "Null";
        this.response = false;
        this.choose = 1;
    }

    public int getChoose() {
        return choose;
    }

    public void setChoose(int choose) {
        this.choose = choose;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setResponse (boolean response) throws RemoteException {
        this.response = response;
    }

    public boolean getResponse () {
        return response;
    }

    public synchronized void login (ServerLogin serverLogin) throws InterruptedException, RemoteException{
        while (!this.getResponse()) {
            System.out.println("1. Login or 2. SignUp: ");
            Scanner inChoose = new Scanner(System.in);

            while (true) {
                try {
                    this.setChoose(inChoose.nextInt());
                    if ((this.getChoose() != 1) && (this.getChoose() != 2)) {
                        throw new Exception();
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Wrong number: 1. Login or 2. SignUp");
                }
            }

            System.out.println("Insert your Username: ");
            Scanner inUsername = new Scanner(System.in);
            this.setUsername(inUsername.nextLine());
            System.out.println("Insert your Password: ");
            Scanner inPassword = new Scanner(System.in);
            this.setPassword(inPassword.nextLine());

            if (this.getChoose() == 1) {
                serverLogin.controlUser(this);
            } else {
                serverLogin.saveUser(this);
            }
            wait();
        }
        serverLogin.printUsers();
    }

    public static void main (String[] args) throws RemoteException, NotBoundException, InterruptedException{
        Registry registry = LocateRegistry.getRegistry();
        ServerLogin serverLogin = (ServerLogin) registry.lookup("serverLogin");

        System.out.println("Connected");

        UserLoginImpl userLogin = new UserLoginImpl();

        userLogin.login(serverLogin);
    }
}
