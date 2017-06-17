package it.polimi.ingsw.pc34.RMI;


import it.polimi.ingsw.pc34.Controller.ActionInput;
import it.polimi.ingsw.pc34.JSONUtility;
import it.polimi.ingsw.pc34.Model.ActionType;
import it.polimi.ingsw.pc34.Model.GameController;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.NotificationType;

import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;
import it.polimi.ingsw.pc34.View.TerminalInput;
import org.json.JSONException;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import sun.rmi.server.UnicastServerRef;

import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by Povaz on 24/05/2017.
 **/
public class ServerLoginImpl extends UnicastRemoteObject implements ServerLogin{
    private ArrayList<UserLogin> usersLoggedRMI;
    private Lobby lobby;

    private GameController gameController;
    private String currentPlayer;
    private int playerNumber;
    private ActionInputCreated actionInputCreated;
    private ActionInputProducer actionInputProducer;
    private IntegerCreated integerCreated;
    private IntegerProducer integerProducer;

    public ServerLoginImpl (Lobby lobby) throws RemoteException {
        this.usersLoggedRMI = new ArrayList<>();
        this.lobby = lobby;
        this.lobby.setServerRMI(this);
        this.currentPlayer = "null";
    }

    public void setGameController (GameController gameController) {
        this.gameController = gameController;
    }

    public void setCurrentPlayer (String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    private boolean searchUserLogged (UserLogin userLogin) throws RemoteException {
        Set<String> usernames = lobby.getUsers().keySet();
        for (String username : usernames) {
            if ((userLogin.getUsername().equals(username))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean loginServer (UserLogin userLogin) throws JSONException, IOException {
        if (!searchUserLogged(userLogin)) {
            if (JSONUtility.checkLogin(userLogin.getUsername(), userLogin.getKeyword())) {
                this.usersLoggedRMI.add(userLogin);

                lobby.getUsers().put(userLogin.getUsername(), ConnectionType.RMI);
                userLogin.sendMessage("Login Successful");
                lobby.notifyAllUsers(NotificationType.USERLOGIN, userLogin.getUsername());

                if (lobby.getUsers().size() == 2) {
                	userLogin.sendMessage("Timer Started");
                    lobby.inizializeTimer();
                    lobby.startTimer();
                }

                if (lobby.getUsers().size() == 5) {
                    lobby.stopTimer();
                    System.out.println("Start Game"); //TODO CREARE TIMER PERSONALIZZABILE PER FARLO SCATTARE IMMEDIATAMENTE
                }

                return true;
            }
            userLogin.sendMessage("Incorrect Username or password");
            return false;
        }
        userLogin.sendMessage("User already logged");
        return false;
    }

    @Override
    public void registrationServer (UserLogin userLogin) throws JSONException, IOException {
        if (JSONUtility.checkRegister(userLogin.getUsername(), userLogin.getKeyword())) {
            userLogin.sendMessage("Registration Successful");
        }
        else {
            userLogin.sendMessage("Registration Failed: Username already taken");
        }
    }

    @Override
    public boolean logoutServer (UserLogin userLogin) throws RemoteException {  //TODO IMPLEMENTAZIONE NUOVA
        Set<String> usernames = lobby.getUsers().keySet();
        for (String user : usernames) {
            if ((userLogin.getUsername().equals(user))) {
                usersLoggedRMI.remove(userLogin);

                lobby.removeUser(user);

                lobby.notifyAllUsers(NotificationType.USERLOGOUT, userLogin.getUsername());
                userLogin.sendMessage("Logout successful");

                if (lobby.getUsers().size() == 1) {
                    System.out.println(userLogin.getUsername() + "left the room");
                    lobby.stopTimer();
                }

                return false;
            }
        }
        userLogin.sendMessage("Logout Failed");
        return true;
    }

    @Override
    public void printLoggedUsers () throws RemoteException{ // TODO DA ELIMINARE ALLA FINE
        Set<String> users = lobby.getUsers().keySet();
        for (String user: users) {
            System.out.println(user);
        }
    }

    public void notifyRMIPlayers (String m) throws RemoteException {
        for (UserLogin user: usersLoggedRMI) {
            user.sendMessage(m);
        }
    }

    public boolean checkCurrentPlayer (UserLogin userLogin) throws RemoteException {
        if (userLogin.getUsername().equals(currentPlayer)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void sendInput (String input, UserLogin userLogin) throws RemoteException{
        switch (input) {
            case "/skip":
                userLogin.sendMessage("Not Implemented yet");
                break;
            case "/action" :
                if (checkCurrentPlayer(userLogin)) {
                    this.createNewAction(userLogin);
                }
                else {
                    userLogin.sendMessage("Just wait mate, it's not your turn, you mad?");
                }
                break;
            case "/drawleadercard":
                userLogin.sendMessage("Not Implemented yet");
                break;
            case "/activateleadercard":
                userLogin.sendMessage("Not Implemented yet");
                break;
            case "/chat":
                userLogin.sendMessage("Not implemented yet");
                break;
            case "/stampinfo":
                userLogin.sendMessage("Not implemented yet");
                break;
            default: userLogin.sendMessage("Command Unknown");
        }
    }

    public void createNewAction (UserLogin userLogin) throws RemoteException {
        int choose = 0;
        integerProducer.setChoose(choose);
        integerProducer.start();

        ActionInput actionInput = this.chooseActionInput(playerNumber, userLogin);
        actionInputProducer.setActionInput(actionInput);
        actionInputProducer.start();
    }

    public void askAction(int playerNumber) throws RemoteException {
        this.playerNumber = playerNumber;

        actionInputCreated = new ActionInputCreated();
        gameController.setActionInputCreated(actionInputCreated);
        actionInputProducer = new ActionInputProducer(actionInputCreated);
    }

    private ActionInput chooseActionInput (int playerNumber, UserLogin userLogin) throws RemoteException {
        ActionInput actionInput = new ActionInput();
        boolean correct = false;
        int choose;
        while (!correct) {
            String m = "Which ActionSpot do you choose? Type /action and then choose a number\n" + "1. " + "TERRYTORY TOWER" + "\n"
                    + "2. " + "BUILDING TOWER" + "\n" + "3. " + "CHARACTER TOWER" + "\n" + "4. "
                    + "VENTURE TOWER" + "\n" + "5. " + "HARVEST" + "\n" + "6. " + "PRODUCE"
                    + "\n" + "7. " + "MARKET" + "\n" + "8. " + "COUNCILPALACE" + "\n";
            userLogin.sendMessage(m);
            choose = userLogin.setActionChoose();
            switch (choose) {
                case 1:
                    actionInput.setActionType(ActionType.TERRITORY_TOWER);
                    userLogin.sendMessage("Which card?");
                    actionInput.setSpot(this.askSpot(0, 3, userLogin));
                    correct = true;
                    break;
                case 2:
                    actionInput.setActionType(ActionType.BUILDING_TOWER);
                    userLogin.sendMessage("Which card?");
                    actionInput.setSpot(this.askSpot(0, 3, userLogin));
                    break;
                case 3:
                    actionInput.setActionType(ActionType.CHARACTER_TOWER);
                    userLogin.sendMessage("Which card?");
                    actionInput.setSpot(this.askSpot(0, 3, userLogin));
                    correct = true;
                    break;
                case 4:
                    actionInput.setActionType(ActionType.VENTURE_TOWER);
                    userLogin.sendMessage("Which card?");
                    actionInput.setSpot(this.askSpot(0, 3, userLogin));
                    correct = true;
                    break;
                case 5:
                    actionInput.setActionType(ActionType.HARVEST);
                    if (playerNumber > 2) {
                        actionInput.setSpot(this.askSpot(0, 1, userLogin));
                    } else {
                        actionInput.setSpot(0);
                    }
                    correct = true;
                    break;
                case 6:
                    actionInput.setActionType(ActionType.PRODUCE);
                    if (playerNumber > 2) {
                        actionInput.setSpot(this.askSpot(0, 1, userLogin));
                    } else {
                        actionInput.setSpot(0);
                    }
                    correct = true;
                    break;
                case 7:
                    actionInput.setActionType(ActionType.MARKET);
                    userLogin.sendMessage("Which Spot? 0.COIN(5)  1.SERVANT(5)   2.COIN(2) & MILITARY_POINT(3) 3.COUNCILPRIVILEGE(2)");
                    if (playerNumber > 3) {
                        actionInput.setSpot(this.askSpot(0, 3, userLogin));
                    } else {
                        actionInput.setSpot(this.askSpot(0, 1, userLogin));
                    }
                    correct = true;
                    break;
                case 8:
                    actionInput.setActionType(ActionType.COUNCIL_PALACE);
                    actionInput.setSpot(0);
                    correct = true;
                    break;
                default:
                    System.out.println("Incorrect Answer");
                    break;
            }
        }
        return actionInput;
    }

    public void askNumber(int min, int max, String currentPlayer) throws RemoteException {
        this.currentPlayer = currentPlayer;

        integerCreated = new IntegerCreated();
        integerProducer = new IntegerProducer(integerCreated);
        gameController.setIntegerCreated(integerCreated);
    }

    public int askSpot (int min, int max, UserLogin userLogin) throws RemoteException {
        boolean correct = false;
        int choose = 0;
        while (!correct) {
            choose = userLogin.setActionChoose();
            if (choose >= min && choose <= max) {
                correct = true;
            }
            else {
                userLogin.sendMessage("Invalid Answer");
            }
        }
        return choose;
    }
}
