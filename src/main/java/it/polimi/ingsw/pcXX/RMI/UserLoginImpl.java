package it.polimi.ingsw.pcXX.RMI;

import it.polimi.ingsw.pcXX.SocketRMICongiunction.ConnectionType;
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

public class UserLoginImpl extends UnicastRemoteObject implements UserLogin, Runnable {
    private String username;
    private String keyword;
    private boolean logged;
    private int choose;

    public UserLoginImpl() throws RemoteException {
        this.username = "Null";
        this.keyword = "Null";
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
    public String getKeyword() {
        return keyword;
    }

    private void setKeyword(String keyword) {
        this.keyword = keyword;
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
        System.out.print("Insert your Password: ");
        Scanner inPassword = new Scanner(System.in);
        this.setKeyword(inPassword.nextLine());
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

    public void run () {
        try {
            Registry registry = LocateRegistry.getRegistry(8000);
            ServerLogin serverLogin = (ServerLogin) registry.lookup("serverLogin");

            while (true) {
                try {
                    System.out.println("1. Login/Logout; 2. SignUp; 3. Quit     -   Logged: " + this.isLogged());
                    Scanner inChoose = new Scanner(System.in);
                    this.setChoose(inChoose.nextInt());

                    switch (this.getChoose()) {
                        case 1:
                            if (!this.isLogged()) {
                                this.login(serverLogin);
                            } else {
                                this.logout(serverLogin);
                            }
                            break;
                        case 2:
                            if (!this.isLogged()) {
                                this.registration(serverLogin);
                            } else {
                                System.out.println("You have to log out if you want to register another user");
                            }
                            break;
                        case 3:
                            if (this.isLogged()) {
                                this.logout(serverLogin);
                            }
                            break;
                        case 4:
                            this.printUsers(serverLogin);
                            break;
                        default:
                            System.out.println("Incorrect answer");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("InputError: Retry");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (this.getChoose() == 3) {
                    System.exit(0);
                }
            }
        }
        catch (NotBoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

