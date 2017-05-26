package it.polimi.ingsw.pcXX.RMI;

import org.json.JSONException;

import java.io.IOException;
import java.rmi.NotBoundException;
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
    private boolean logged;
    private int choose;

    public UserLoginImpl () throws RemoteException {
        this.username = "Null";
        this.password = "Null";
        this.response = false;
        this.logged = false;
        this.choose = 1;
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

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public int getChoose() {
        return choose;
    }

    public void setChoose(int choose) {
        this.choose = choose;
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

    public void login (ServerLogin serverLogin) throws RemoteException, JSONException, IOException{
        this.insertData();
        this.setLogged(serverLogin.controlUser(this));
        if (!this.isLogged()) {
            System.out.println("Login Failed: incorrect Username or Password");
        }
        else {
            System.out.println("Login successfull");
        }
    }

    public void registration (ServerLogin serverLogin) throws RemoteException, JSONException, IOException {
        while (!this.getResponse()) {
            this.insertData();
            this.response = serverLogin.saveUser(this);
            if (!this.response) { System.out.println("Registration Failed: Username already taken"); }
            else {
                System.out.println("Registration successful");
            }
        }
        this.setResponse(false);
        return;
    }

    public void logout(ServerLogin serverLogin) throws RemoteException {
        while (!this.getResponse()) {
            this.response = serverLogin.logoutUser(this);
            if (!this.response) {
                System.out.println("Logout Failed");
            }
        }
        this.setResponse(false);
        return;
    }

    public static void main (String[] args) throws RemoteException, NotBoundException, JSONException, IOException{
        Registry registry = LocateRegistry.getRegistry(8000);
        ServerLogin serverLogin = (ServerLogin) registry.lookup("serverLogin");
        UserLoginImpl userLogin = new UserLoginImpl();

        while (true) {
            System.out.println("1. Login/Logout; 2. SignUp; 3. Quit     -   Logged: " + userLogin.isLogged());
            Scanner inChoose = new Scanner(System.in);
            userLogin.setChoose(inChoose.nextInt());

            switch (userLogin.getChoose()) {
                case 1:
                    if (!userLogin.isLogged()) {
                        userLogin.login(serverLogin);
                    }
                    else {
                        userLogin.logout(serverLogin);
                    }
                    break;
                case 2:
                    if(!userLogin.isLogged()) {
                        userLogin.registration(serverLogin);
                    }
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Uncorrect answer");
            }
        }
    }
}

