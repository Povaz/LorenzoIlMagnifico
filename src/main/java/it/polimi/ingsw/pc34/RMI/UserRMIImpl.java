package it.polimi.ingsw.pc34.RMI;

import it.polimi.ingsw.pc34.View.GUI.BoardView;
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
    private String gameState; //The state of a player in Server: /playturn; /chat; /vaticansupport
    private boolean GUI; //Boolean variable that tells if a User is CLI (false) or GUI (true)
    //SynchronizedString and SynchronizedBoardView used to communicate with GUI
    private SynchronizedString chatIn;
    private SynchronizedString chatOut;
    private SynchronizedString messageByGUI;
    private SynchronizedString messageForGUI;
    private SynchronizedString messageToChangeWindow;
    private SynchronizedString messageInfo;
    private SynchronizedBoardView boardView;
    private boolean logged; //Boolean variabile that tells if a User is currently logged
    private boolean startingGame; //Boolean variable that tells if a User is currently in Game

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

    //Setter and Getter: begin

    public void setSynchronizedMessageByGUI(SynchronizedString messageByGUI) {
        this.messageByGUI = messageByGUI;
    }

    public void setSynchronizedMessageForGUI(SynchronizedString messageForGUI) {
        this.messageForGUI = messageForGUI;
    }

    public void setSynchronizedMessageToChangeWindow(SynchronizedString messageToChangeWindow) {this.messageToChangeWindow = messageToChangeWindow; }

    public void setSynchronizedBoardView (SynchronizedBoardView boardView) {this.boardView = boardView;}

    public void setSynchronizedMessageInfo (SynchronizedString messageInfo) {this.messageInfo = messageInfo; }

    public void setChatIn(SynchronizedString chatIn) {
        this.chatIn = chatIn;
    }

    public void setChatOut(SynchronizedString chatOut) {
        this.chatOut = chatOut;
    }

    @Override
    public void setMessageForGUI(String messageForGUI) throws RemoteException {this.messageForGUI.put(messageForGUI); }

    @Override
    public void setMessageToChangeWindow (String messageToChangeWindow) throws RemoteException {this.messageToChangeWindow.put(messageToChangeWindow);}

    @Override
    public void setMessageByGUI(String messageByGUI) throws RemoteException {this.messageByGUI.put(messageByGUI);}

    @Override
    public String getMessageByGUI () throws RemoteException { return this.messageByGUI.get();}

    @Override
    public void setBoardView (BoardView boardView) {this.boardView.put(boardView);}

    @Override
    public void setMessageInfo (String messageInfo) {this.messageInfo.put(messageInfo);}

    @Override
    public void setMessageChatIn (String messageChatIn) {this.chatIn.put(messageChatIn);}

    @Override
    public void setMessageChatOut (String messageChatOut) {this.chatOut.put(messageChatOut);}


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

    //Setter and getter: end

    @Override
    public void sendMessage (String message) throws RemoteException { //Receives message from Server, evaluating them when necessary
        switch (message) {
            case "":
                return;
            case "The game is starting":
                this.setStartingGame(true);
                if (!isGUI()) {
                    System.out.println(message + ". Press any key to start!");
                }
                break;
            case "Reconnected":
                this.setStartingGame(true);
                if (!isGUI()) {
                    System.out.println(message + ". Press any key to start!");
                }
                break;
            default:
                if (!isGUI()) {
                    System.out.println(message);
                }
        }
    }

    private void insertData() { //For a RMI CLI User
        System.out.print("Insert your Username: ");
        Scanner inUsername = new Scanner(System.in);
        this.setUsername(inUsername.nextLine());
        System.out.print("Insert your Password: ");
        Scanner inPassword = new Scanner(System.in);
        this.setKeyword(inPassword.nextLine());
    }

    private void insertDataGUI() { //For a RMI GUI User
        this.setUsername(messageByGUI.get());
        this.setKeyword(messageByGUI.get());
    }

    private void login(ServerRMI serverRMI) throws JSONException, IOException{ //Login procedure for RMI CLI Users
        this.insertData();
        this.setLogged(serverRMI.loginServer(this));
    }

    private void loginGUI(ServerRMI serverRMI) throws JSONException, IOException{ //Login procedure for RMI GUI Users
        this.insertDataGUI();
        this.setLogged(serverRMI.loginServer(this));
    }

    private void registration(ServerRMI serverRMI) throws JSONException, IOException { //Registration procedure for RMI CLI Users
        this.insertData();
        serverRMI.registrationServer(this);
    }

    private void registrationGUI(ServerRMI serverRMI) throws JSONException, IOException { //Registration procedure for RMI GUI Users
        this.insertDataGUI();
        serverRMI.registrationServer(this);
    }

    private void logout(ServerRMI serverRMI) throws RemoteException { //Logout procedure for RMI CLI/GUI Users
        this.setLogged(serverRMI.logoutServer(this));
    }

    public void loginHandler (ServerRMI serverRMI) throws IOException { //It manages the Login phase for RMI CLI Users
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

    public void gameHandler (ServerRMI serverRMI) throws IOException { //It manages the gaming phase for RMI CLI Users
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
                System.out.println("ConcurrentModificationException");
                this.loginHandler(serverRMI);
            }
        }
        this.loginHandler(serverRMI);
    }

    public void loginHandlerGUI (ServerRMI serverRMI) throws IOException { //It manages the login phase for RMI GUI Users
        String choose;
        while (!startingGame) {
            try {
                choose = messageByGUI.get();

                switch (choose) {
                    case "/login":
                        if (this.isLogged()) {
                            //GUI User shouldn't be able to get here
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
                            //GUI User shouldn't be able to get here
                        }
                        break;
                    case "/registration":
                        if (!this.isLogged()) {
                            this.registrationGUI(serverRMI);
                        } else {
                            //GUI User shouldn't be able to get here
                        }
                        break;
                    default:
                        //GUI User shouldn't be able to get here
                        break;
                }
            } catch (InputMismatchException e) {
                //GUI User shouldn't be able to get here
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.gameHandlerGUI(serverRMI);
    }


    public void gameHandlerGUI (ServerRMI serverRMI) throws IOException { //It manages the gaming phase for RMI GUI Users
        String choose;
        messageToChangeWindow.put("/game"); //It requests the opening of the Gaming Windows
        messageByGUI.get(); //Waits for the Gaming Windows

        serverRMI.sendInput("/update", this); //Requests for an update of the GUI

        while (logged) {
            try {
                choose = messageByGUI.get(); //For an a GUI-Input
                System.out.println(choose);
                serverRMI.sendInput(choose, this);
            }
            catch (InputMismatchException e) {
                //GUI User shouldn't be able to get here
            }
            catch (ConcurrentModificationException e) {
                this.loginHandlerGUI(serverRMI);
            }
        }

        String message = messageInfo.get();
        messageToChangeWindow.put("/login");
        messageInfo.put(message);
        this.loginHandlerGUI(serverRMI);
    }
}

