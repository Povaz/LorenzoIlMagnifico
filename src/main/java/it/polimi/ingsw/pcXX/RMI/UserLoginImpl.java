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

    @Override
    public String getUsername() throws RemoteException {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getResponse () {
        return response;
    }

    public void setResponse (boolean response) {
        this.response = response;
    }

    public void insertData () {
        System.out.println("Insert your Username: ");
        Scanner inUsername = new Scanner(System.in);
        this.setUsername(inUsername.nextLine());
        System.out.println("Insert your Password: ");
        Scanner inPassword = new Scanner(System.in);
        this.setPassword(inPassword.nextLine());
        return;
    }

    public void login (ServerLogin serverLogin) throws RemoteException{
        while (!this.getResponse()) {
            this.insertData();
            this.response = serverLogin.controlUser(this);
            if (!this.response) { System.out.println("Login Failed: incorrect Username or Password"); }
        }
        this.setResponse(false);
        return;
    }

    public void registration (ServerLogin serverLogin) throws RemoteException {
        while (!this.getResponse()) {
            this.insertData();
            this.response = serverLogin.saveUser(this);
            if (!this.response) { System.out.println("Registration Failed: Username already taken"); }
        }
        this.setResponse(false);
        return;
    }

    public void remove (ServerLogin serverLogin) throws RemoteException {
        while (!this.getResponse()) {
            this.insertData();
            this.response = serverLogin.deleteUser(this);
            if (!this.response) {
                System.out.println("Deletion failed: username or password incorrect");
            }
        }
        this.setResponse(false);
        return;
    }

    public static void main (String[] args) throws RemoteException, NotBoundException{
        Registry registry = LocateRegistry.getRegistry();
        ServerLogin serverLogin = (ServerLogin) registry.lookup("serverLogin");
        UserLoginImpl userLogin = new UserLoginImpl();

        while (true) {
            System.out.println("1. Login; 2. SignUp; 3. Remove; 4. List of Users; 5. Quit");
            Scanner inChoose = new Scanner(System.in);
            userLogin.setChoose(inChoose.nextInt());

            switch (userLogin.getChoose()) {
                case 1:
                    userLogin.login(serverLogin);
                    break;
                case 2:
                    userLogin.registration(serverLogin);
                    break;
                case 3:
                    userLogin.remove(serverLogin);
                    break;
                case 4:
                    serverLogin.printUsers();
                    break;
                case 5: System.exit(0);
                default: System.out.println("Uncorrect answer");
            }
        }
    }
}
