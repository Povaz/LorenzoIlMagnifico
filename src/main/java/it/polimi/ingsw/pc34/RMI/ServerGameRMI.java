package it.polimi.ingsw.pc34.RMI;

import it.polimi.ingsw.pc34.Controller.ActionInput;
import it.polimi.ingsw.pc34.Exception.IllegalNumberOf;
import it.polimi.ingsw.pc34.Exception.SameChooseErrorException;
import it.polimi.ingsw.pc34.Model.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by Povaz on 18/06/2017.
 */
public class ServerGameRMI {
    private GameController gameController;
    private int playerNumber;
    private Player currentPlayer;
    private ActionInputCreated actionInputCreated;
    private ActionInputProducer actionInputProducer;
    private IntegerCreated integerCreated;
    private IntegerProducer integerProducer;
    private FamilyColorCreated familyColorCreated;
    private FamilyColorProducer familyColorProducer;
    private ArrayIntegerCreated arrayIntegerCreated;
    private ArrayIntegerProducer arrayIntegerProducer;

    private ArrayList <UserLogin> playersInThisGame;

    public ServerGameRMI(ArrayList<UserLogin> playersInThisGame) {
        this.playersInThisGame = playersInThisGame;
    }

    public ArrayList<UserLogin> getPlayersInThisGame () {
        return playersInThisGame;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public ActionInputCreated getActionInputCreated() {
        return actionInputCreated;
    }

    public void setActionInputCreated(ActionInputCreated actionInputCreated) {
        this.actionInputCreated = actionInputCreated;
    }

    public ActionInputProducer getActionInputProducer() {
        return actionInputProducer;
    }

    public void setActionInputProducer(ActionInputProducer actionInputProducer) {
        this.actionInputProducer = actionInputProducer;
    }

    public IntegerCreated getIntegerCreated() {
        return integerCreated;
    }

    public void setIntegerCreated(IntegerCreated integerCreated) {
        this.integerCreated = integerCreated;
    }

    public IntegerProducer getIntegerProducer() {
        return integerProducer;
    }

    public void setIntegerProducer(IntegerProducer integerProducer) {
        this.integerProducer = integerProducer;
    }

    public FamilyColorCreated getFamilyColorCreated() {
        return familyColorCreated;
    }

    public void setFamilyColorCreated(FamilyColorCreated familyColorCreated) {
        this.familyColorCreated = familyColorCreated;
    }

    public FamilyColorProducer getFamilyColorProducer() {
        return familyColorProducer;
    }

    public void setFamilyColorProducer(FamilyColorProducer familyColorProducer) {
        this.familyColorProducer = familyColorProducer;
    }

    public void askNumberMinMax(int min, int max) throws RemoteException {
        integerCreated = new IntegerCreated();
        integerProducer = new IntegerProducer(integerCreated, min, max);
        gameController.setIntegerCreated(integerCreated);
    }

    public void askAction(int playerNumber) throws RemoteException {
        actionInputCreated = new ActionInputCreated();
        gameController.setActionInputCreated(actionInputCreated);
        actionInputProducer = new ActionInputProducer(actionInputCreated);
    }

    public void askFamilyColor() throws RemoteException {
        familyColorCreated = new FamilyColorCreated();
        familyColorProducer = new FamilyColorProducer(familyColorCreated);
        gameController.setFamilyColorCreated(familyColorCreated);
    }

    public void askArrayInt() throws RemoteException {
        arrayIntegerCreated = new ArrayIntegerCreated();
        arrayIntegerProducer = new ArrayIntegerProducer(arrayIntegerCreated);
        gameController.setArrayIntegerCreated(arrayIntegerCreated);
    }

    public boolean checkAlreadyPlaced () throws RemoteException {
        return currentPlayer.isPlacedFamilyMember();
    }

    public void createNewAction (UserLogin userLogin) throws RemoteException {
        int choose = 0;
        integerProducer.setChoose(choose);
        integerProducer.start();

        ActionInput actionInput = this.chooseActionInput(playerNumber, userLogin);
        actionInputProducer.setActionInput(actionInput);
        actionInputProducer.start();

        FamilyColor familyColor = this.chooseFamilyColor(userLogin);
        familyColorProducer.setFamilyColor(familyColor);
        familyColorProducer.start();

        String m = "How many Servants do you want to use?";
        int servant = this.chooseSpot(integerProducer.getMin(), integerProducer.getMax(), userLogin, m);
        integerProducer.setChoose(servant);
        integerProducer.start();
    }


    public void skipTurn() throws RemoteException {
        int choose = 4;
        integerProducer.setChoose(choose);
        integerProducer.start();
    }

    public boolean checkCurrentPlayer (UserLogin userLogin) throws RemoteException {
        if (userLogin.getUsername().equals(currentPlayer.getUsername())) {
            return true;
        }
        else {
            return false;
        }
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
            String tower = "Which card? From 0 to 3";
            String market = "Which Spot? 0.COIN(5)  1.SERVANT(5)   2.COIN(2) & MILITARY_POINT(3) 3.COUNCILPRIVILEGE(2)";
            userLogin.sendMessage(m);
            choose = userLogin.setActionChoose();
            switch (choose) {
                case 1:
                    actionInput.setActionType(ActionType.TERRITORY_TOWER);
                    actionInput.setSpot(this.chooseSpot(0, 3, userLogin, tower));
                    correct = true;
                    break;
                case 2:
                    actionInput.setActionType(ActionType.BUILDING_TOWER);
                    actionInput.setSpot(this.chooseSpot(0, 3, userLogin, tower));
                    correct = true;
                    break;
                case 3:
                    actionInput.setActionType(ActionType.CHARACTER_TOWER);
                    userLogin.sendMessage("Which card?");
                    actionInput.setSpot(this.chooseSpot(0, 3, userLogin, tower));
                    correct = true;
                    break;
                case 4:
                    actionInput.setActionType(ActionType.VENTURE_TOWER);
                    userLogin.sendMessage("Which card?");
                    actionInput.setSpot(this.chooseSpot(0, 3, userLogin, tower));
                    correct = true;
                    break;
                case 5:
                    actionInput.setActionType(ActionType.HARVEST);
                    if (playerNumber > 2) {
                        actionInput.setSpot(this.chooseSpot(0, 1, userLogin, ""));
                    } else {
                        actionInput.setSpot(0);
                    }
                    correct = true;
                    break;
                case 6:
                    actionInput.setActionType(ActionType.PRODUCE);
                    if (playerNumber > 2) {
                        actionInput.setSpot(this.chooseSpot(0, 1, userLogin, ""));
                    } else {
                        actionInput.setSpot(0);
                    }
                    correct = true;
                    break;
                case 7:
                    actionInput.setActionType(ActionType.MARKET);
                    if (playerNumber > 3) {
                        actionInput.setSpot(this.chooseSpot(0, 3, userLogin, market));
                    } else {
                        actionInput.setSpot(this.chooseSpot(0, 1, userLogin, market));
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

    public int chooseSpot(int min, int max, UserLogin userLogin, String message) throws RemoteException {
        boolean correct = false;
        int choose = 0;
        while (!correct) {
            userLogin.sendMessage(message);
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

    public FamilyColor chooseFamilyColor (UserLogin userLogin) throws RemoteException {
        FamilyColor familyColor = FamilyColor.NEUTRAL;
        boolean correct = false;
        while (!correct) {
            try {
                String m = "Which FamilyMember do you choose?\n" + "1. " + FamilyColor.WHITE + "\n" + "2. " + FamilyColor.BLACK + "\n" + "3. " + FamilyColor.ORANGE + "\n" + "4. " + FamilyColor.NEUTRAL + "\n";

                int choose = this.chooseSpot(1, 4, userLogin, m);

                switch (choose) {
                    case 1:
                        familyColor = FamilyColor.WHITE;
                        correct = true;
                        break;
                    case 2:
                        familyColor = FamilyColor.BLACK;
                        correct = true;
                        break;
                    case 3:
                        familyColor = FamilyColor.ORANGE;
                        correct = true;
                        break;
                    case 4:
                        familyColor = FamilyColor.NEUTRAL;
                        correct = true;
                        break;
                    default:
                        userLogin.sendMessage("Incorrect Answer");
                }

            } catch (InputMismatchException e) {
                e.printStackTrace();
                userLogin.sendMessage("Incorrect answer");
            }
        }
        return familyColor;
    }

    public int[] chooseCouncilPrivilegeReward (Reward councilPrivilege, UserLogin userLogin) throws RemoteException {
        try {
            if (councilPrivilege.getQuantity() > 5) {
                throw new IllegalNumberOf(councilPrivilege);
            }
            int[] choose = new int[councilPrivilege.getQuantity()];
            for (int i = 0; i < councilPrivilege.getQuantity(); i++) {
                try {
                    String exchange = "1. 1 WOOD 1 Stone   2. 2 SERVANT    3. 2 COIN   4. 2 MILITARY_POINT  5. 1 FAITH_POINT \n" +
                            "Don't choose the sameType reward as before" + "\n";
                    this.chooseSpot(1, 5, userLogin, exchange);

                    boolean contains = false;
                    for (int j = 0; j < i; j++) {
                        if (choose[i] == choose[j]) {
                            contains = true;
                        }
                    }
                    if (contains) {
                        throw new SameChooseErrorException(councilPrivilege);
                    }
                } catch (SameChooseErrorException e) {
                    e.printStackTrace();
                    --i;
                }
            }
            return choose;
        }
        catch (IllegalNumberOf e) {
            e.printStackTrace();
            int [] choose = new int[] {1, 2, 3, 4, 5};
            return choose;
        }
    }

    public void sendMessage(Player player, String message) throws RemoteException {
        for(int i = 0; i < playersInThisGame.size(); i++) {
            if (player.getUsername().equals(playersInThisGame.get(i))) {
                playersInThisGame.get(i).sendMessage(message);
            }
        }
    }


}
