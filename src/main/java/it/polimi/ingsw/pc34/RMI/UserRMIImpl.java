package it.polimi.ingsw.pc34.RMI;

import it.polimi.ingsw.pc34.Controller.BooleanCreated;
import org.json.JSONException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ConcurrentModificationException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Povaz on 24/05/2017.
 **/

public class UserRMIImpl extends UnicastRemoteObject implements UserRMI {
    private String username;
    private String keyword;
    private boolean logged;
    private boolean startingGame;

    public UserRMIImpl() throws RemoteException {
        this.username = "Null";
        this.keyword = "Null";
        this.logged = false;
        this.startingGame = false;
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

    public boolean isStartingGame() {
        return startingGame;
    }

    public void setStartingGame(boolean startingGame) {
        this.startingGame = startingGame;
    }

    @Override
    public void sendMessage (String message) throws RemoteException {
        switch (message) {
            case "The game is starting":
                this.setStartingGame(true);
                System.out.println("Starting game:" + startingGame);
                System.out.println("Logged: " + logged);
                System.out.println(message + ". Press any key to start!");
                break;
            case "Reconnected":
                this.setStartingGame(true);
                this.setLogged(true);
                System.out.println("Starting game:" + startingGame);
                System.out.println("Logged: " + logged);
                System.out.println(message + ". Press any key to start!");
                break;
            default:
                System.out.println(message);
        }
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
        String choose;
        while (!startingGame) {
            try {
                System.out.println("Insert: /login to login, /logout to logout /registration to registrate a new user or /exit to close to application      - Logged: " + this.isLogged());
                Scanner inChoose = new Scanner(System.in);
                choose = inChoose.nextLine();

                switch (choose) {
                    case "/login":
                        if (this.isLogged()) {
                            System.out.println("You're already logged");
                        }
                        else {
                            this.login(serverRMI);
                        }
                        break;
                    case "/logout":
                        if (this.isLogged()) {
                            this.logout(serverRMI);
                        }
                        else {
                            System.out.println("You're not logged yet");
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
                        System.out.println(startingGame);
                        if (startingGame) {
                            startingGame = false;
                            this.gameHandler(serverRMI);
                        }
                        else {
                            System.out.println("Incorrect answer");
                        }
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
            catch (ConcurrentModificationException e) {
                this.loginHandler(serverRMI);
            }
        }
        this.loginHandler(serverRMI);
    }
}

