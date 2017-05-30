package it.polimi.ingsw.pcXX.RMI;

import org.json.JSONException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Povaz on 24/05/2017.
 **/
public class UserLoginImpl extends UnicastRemoteObject implements UserLogin{
    private String username;
    private String password;
    private boolean logged;
    private int choose;

    private UserLoginImpl() throws RemoteException {
        this.username = "Null";
        this.password = "Null";
        this.logged = false;
        this.choose = 1;
    }

    @Override
    public String getUsername() throws RemoteException {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    private boolean isLogged() throws RemoteException {
        return logged;
    }

    private void setLogged(boolean logged) {
        this.logged = logged;
    }

    private int getChoose() {
        return choose;
    }

    private void setChoose(int choose) {
        this.choose = choose;
    }

    @Override
    public void sendMessage (String message) throws RemoteException {
        System.out.println(message);
    }

    private void insertData() {
        System.out.print("Insert your Username: ");
        Scanner inUsername = new Scanner(System.in);
        this.setUsername(inUsername.nextLine());
        System.out.println("\n");
        System.out.print("Insert your Password: ");
        Scanner inPassword = new Scanner(System.in);
        this.setPassword(inPassword.nextLine());
        System.out.println("\n");
    }

    private void login(ServerLogin serverLogin) throws JSONException, IOException{
        this.insertData();
        this.setLogged(serverLogin.loginServer(this));
    }

    private void registration(ServerLogin serverLogin) throws JSONException, IOException {
        this.insertData();
        serverLogin.registrationServer(this);
    }

    private void logout(ServerLogin serverLogin) throws RemoteException {
        this.setLogged(serverLogin.logoutServer(this));
    }

    private void printUsers(ServerLogin serverLogin) throws RemoteException {
        serverLogin.printLoggedUsers();
    }

    public static void main (String[] args) throws NotBoundException, JSONException, IOException {
        Registry registry = LocateRegistry.getRegistry(8000);
        ServerLogin serverLogin = (ServerLogin) registry.lookup("serverLogin");
        UserLoginImpl userLogin = new UserLoginImpl();

        while (true) {
            try {
                System.out.println("1. Login/Logout; 2. SignUp; 3. Quit     -   Logged: " + userLogin.isLogged());
                Scanner inChoose = new Scanner(System.in);
                userLogin.setChoose(inChoose.nextInt());

                switch (userLogin.getChoose()) {
                    case 1:
                        if (!userLogin.isLogged()) {
                            userLogin.login(serverLogin);
                        } else {
                            userLogin.logout(serverLogin);
                        }
                        break;
                    case 2:
                        if (!userLogin.isLogged()) {
                            userLogin.registration(serverLogin);
                        } else {
                            System.out.println("You have to log out if you want to register another user");
                        }
                        break;
                    case 3:
                        if (userLogin.isLogged()) {
                            userLogin.logout(serverLogin);
                        }
                        System.exit(0);
                        break;
                    case 4:
                        userLogin.printUsers(serverLogin);
                        break;
                    default:
                        System.out.println("Incorrect answer");
                }
            }
            catch (InputMismatchException e) {
                System.out.println("InputError: Retry");
            }
        }
    }
}

