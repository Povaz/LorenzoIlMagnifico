package it.polimi.ingsw.pc34.RMI;

import it.polimi.ingsw.pc34.SocketRMICongiunction.Client;
import it.polimi.ingsw.pc34.View.GUI.BoardView;
import org.json.JSONException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ConcurrentModificationException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static it.polimi.ingsw.pc34.SocketRMICongiunction.Client.guiReference;

/**
 * Created by Povaz on 24/05/2017.
 **/

public class UserRMIImpl extends UnicastRemoteObject implements UserRMI {
    private String username;
    private String keyword;
    private String gameState;
    private boolean GUI;
    private SynchronizedString messageByGUI;
    private SynchronizedString messageForGUI;
    private SynchronizedString messageToChangeWindow;
    private SynchronizedBoardView boardView;
    private boolean logged;
    private boolean startingGame;

    public UserRMIImpl(boolean GUI) throws RemoteException {
        this.username = "Null";
        this.keyword = "Null";
        this.GUI = GUI;
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

    public void setSynchronizedMessageByGUI(SynchronizedString messageByGUI) {
        this.messageByGUI = messageByGUI;
    }

    public void setSynchronizedMessageForGUI(SynchronizedString messageForGUI) {
        this.messageForGUI = messageForGUI;
    }

    public void setSynchronizedMessageToChangeWindow(SynchronizedString messageToChangeWindow) {this.messageToChangeWindow = messageToChangeWindow; }

    public void setSynchronizedBoardView (SynchronizedBoardView boardView) {this.boardView = boardView;}

    @Override
    public void setMessageForGUI(String messageForGUI) {this.messageForGUI.put(messageForGUI); }

    @Override
    public void setMessageToChangeWindow (String messageToChangeWindow) {this.messageToChangeWindow.put(messageToChangeWindow);}

    @Override
    public void setMessageByGUI(String messageByGUI) {this.messageByGUI.put(messageByGUI);}

    @Override
    public void setBoardView (BoardView boardView) {this.boardView.put(boardView);}

    @Override
    public boolean isGUI() throws RemoteException {
        return GUI;
    }

    public void setGUI(boolean GUI) {
        this.GUI = GUI;
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

    @Override
    public void setStartingGame(boolean startingGame) {
        this.startingGame = startingGame;
    }

    @Override
    public String getGameState() {
        return gameState;
    }
    @Override
    public void setGameState(String gameState) {
        this.gameState = gameState;
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

    private void insertDataGUI() {
        this.setUsername(messageByGUI.get());
        this.setKeyword(messageByGUI.get());
    }

    private void login(ServerRMI serverRMI) throws JSONException, IOException{
        this.insertData();
        this.setLogged(serverRMI.loginServer(this));
    }

    private void loginGUI(ServerRMI serverRMI) throws JSONException, IOException{
        this.insertDataGUI();
        this.setLogged(serverRMI.loginServer(this));
    }

    private void registration(ServerRMI serverRMI) throws JSONException, IOException {
        this.insertData();
        serverRMI.registrationServer(this);
    }

    private void registrationGUI(ServerRMI serverRMI) throws JSONException, IOException {
        this.insertDataGUI();
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
                       break;
                }
            } catch (InputMismatchException e) {
                System.out.println("InputError: Retry");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.gameHandler(serverRMI);
    }

    public void gameHandler (ServerRMI serverRMI) throws IOException {
        System.out.println("Type: /playturn for an Action; /chat to send message;\n");
        while (logged) {
            try {
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

    public void loginHandlerGUI (ServerRMI serverRMI) throws IOException {
        String choose;
        while (!startingGame) {
            try {
                choose = messageByGUI.get();

                switch (choose) {
                    case "/login":
                        if (this.isLogged()) {
                            System.out.println("You're already logged");
                        }
                        else {
                            this.loginGUI(serverRMI);
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
                            this.registrationGUI(serverRMI);
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
                        //System.out.println("Incorrect Answer");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("InputError: Retry");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.gameHandlerGUI(serverRMI);
    }


    public void gameHandlerGUI (ServerRMI serverRMI) throws IOException {
        String choose;
        messageToChangeWindow.put("/game");
        while (logged) {
            try {
                choose = messageByGUI.get();
                System.out.println(choose);
                serverRMI.sendInput(choose, this);
            }
            catch (InputMismatchException e) {
                System.out.println("InputError: Retry");
            }
            catch (ConcurrentModificationException e) {
                this.loginHandlerGUI(serverRMI);
            }
        }
        this.loginHandlerGUI(serverRMI);
    }
}

