package it.polimi.ingsw.pc34.RMI;

import it.polimi.ingsw.pc34.Controller.BooleanCreated;
import org.json.JSONException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Povaz on 24/05/2017.
 **/

public class UserRMIImpl extends UnicastRemoteObject implements UserRMI {
    private String username;
    private String keyword;
    private boolean logged;
    private String choose;
    private BooleanCreated gameIsStarting = new BooleanCreated();

    public UserRMIImpl() throws RemoteException {
        this.username = "Null";
        this.keyword = "Null";
        this.logged = false;
        this.choose = "null";
    }

    @Override
    public boolean equals (Object obj) {
        if (this == obj) return true;
        if (obj == null ||  getClass() != obj.getClass()) return false;

        UserRMIImpl that = (UserRMIImpl) obj;

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

    @Override
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    private String getChoose() {
        return choose;
    }

    private void setChoose(String choose) {
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

    private void login(ServerRMI serverRMI) throws JSONException, IOException{
        this.insertData();
        this.setLogged(serverRMI.loginServer(this));
    }

    private void registration(ServerRMI serverRMI) throws JSONException, IOException {
        this.insertData();
        serverRMI.registrationServer(this);
    }

    private void logout(ServerRMI serverRMI) throws RemoteException {
        this.setLogged(serverRMI.logoutServer(this));
    }

    private void printUsers(ServerRMI serverRMI) throws RemoteException {
        serverRMI.printLoggedUsers();
    }

    public void loginHandler (ServerRMI serverRMI) throws IOException {
        boolean correct = false;
        System.out.println("Insert: /login to login, /registration to registrate a new user or /exit to close to application");
        while (!correct) {
            try {
                Scanner inChoose = new Scanner(System.in);
                this.setChoose(inChoose.nextLine());

                switch (this.getChoose()) {
                    case "/login":
                        this.login(serverRMI);
                        if (this.isLogged()) {
                            correct = true;
                        }
                        break;
                    case "/registration":
                        if (!this.isLogged()) {
                            this.registration(serverRMI);
                        } else {
                            System.out.println("You have to log out if you want to register another user");
                        }
                        break;
                    case "/exit":
                        System.exit(0);
                        break;
                    case "/print":
                        this.printUsers(serverRMI); //TODO ELIMINARE
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

    public void gameHandler (ServerRMI serverRMI) throws IOException {
        while (logged) {
            try {
                System.out.println("Type: /playTurn for an Action; /chat to send message; /stampinfo to stamp info  \n");
                Scanner inChoose = new Scanner (System.in);
                String choose = inChoose.nextLine();
                serverRMI.sendInput(choose, this);
            }
            catch (InputMismatchException e) {
                System.out.println("InputError: Retry");
            }
        }
        this.loginHandler(serverRMI);
    }

    @Override
    public void startGameHandler() throws IOException {
        gameIsStarting.put(true);
    }

    public BooleanCreated getGameIsStarting () {
        return gameIsStarting;
    }
}

