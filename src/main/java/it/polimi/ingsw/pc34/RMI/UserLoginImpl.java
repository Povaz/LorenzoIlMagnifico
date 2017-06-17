package it.polimi.ingsw.pc34.RMI;

import it.polimi.ingsw.pc34.Controller.ActionInput;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Server;
import it.polimi.ingsw.pc34.View.TerminalInput;
import org.json.JSONException;

import javax.swing.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Povaz on 24/05/2017.
 **/

public class UserLoginImpl extends UnicastRemoteObject implements UserLogin {
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
    public boolean equals (Object obj) {
        if (this == obj) return true;
        if (obj == null ||  getClass() != obj.getClass()) return false;

        UserLoginImpl that = (UserLoginImpl) obj;

        if (username != that.username) return false;
        return keyword == that.keyword;
    }

    @Override
    public String toString () {
       String userLoginString = "";
       userLoginString += "Username: " + this.username + "\n";
       userLoginString += "Password: " + this.keyword + "\n";
       return userLoginString;
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

    public void loginHandler (ServerLogin serverLogin) throws IOException {
        boolean correct = false;
        while (!correct) {
            try {
                System.out.println("1. Login; 2. SignUp; 3. Quit     -   Logged: " + this.isLogged());
                Scanner inChoose = new Scanner(System.in);
                this.setChoose(inChoose.nextInt());

                switch (this.getChoose()) {
                    case 1:
                        this.login(serverLogin);
                        if (this.isLogged()) {
                            correct = true;
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
                        System.exit(0);
                        break;
                    case 4:
                        this.printUsers(serverLogin); //TODO ELIMINARE
                        break;
                    default:
                        System.out.println("Incorrect answer");
                }
            } catch (InputMismatchException e) {
                System.out.println("InputError: Retry");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void gameHandler (ServerLogin serverLogin) throws RemoteException {
        while (true) {
            try {
                System.out.println("Type: /action for an Action; \n/skip to skip this turn \n/drawleadercard to use a LeaderCard\n/activateleadercard to activate a Leader Card\n" +
                        "/chat to send message; \n/stampinfo to stamp info");
                Scanner inChoose = new Scanner (System.in);
                String choose = inChoose.nextLine();
                serverLogin.sendInput(choose, this);
            }
            catch (InputMismatchException e) {
                System.out.println("InputError: Retry");
            }
        }
    }

    @Override
    public int setActionChoose () throws RemoteException {
        int chooseAction = 0;
        boolean correct = false;
        while (!correct) {
            try {
                Scanner inChoose = new Scanner(System.in);
                chooseAction = inChoose.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input");
                continue;
            }
            correct = true;
        }
        return chooseAction;
    }


}

