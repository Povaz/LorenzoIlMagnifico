package it.polimi.ingsw.pcXX;

import it.polimi.ingsw.pcXX.Exception.IllegalNumberOf;
import it.polimi.ingsw.pcXX.Exception.SameChooseErrorException;
import org.json.JSONException;

import java.io.IOException;
import java.util.*;

/**
 * Created by Povaz on 06/06/2017.
 */
public class TerminalInput { //Metodi view: richieste ai Client

    public static boolean doYouWantToSkip () {
        System.out.println("Do you want to skip your turn? 0. No 1. Yes \n");
        int skipTurn = askNumber(0,1);
        if (skipTurn == 0) {
            return false;
        }
        else {
            return true;
        }
    }


    public static int[] exchangeCouncilPrivilege(Reward councilPrivilege) {
        try {
            if (councilPrivilege.getQuantity() > 5) {
                throw new IllegalNumberOf(councilPrivilege);
            }
            int[] choose = new int[councilPrivilege.getQuantity()];
            for (int i = 0; i < councilPrivilege.getQuantity(); i++) {
                try {
                    try {
                        System.out.println("1. 1 WOOD 1 Stone   2. 2 SERVANT    3. 2 COIN   4. 2 MILITARY_POINT  5. 1 FAITH_POINT \n" +
                                "Don't choose the same reward as before" + "\n");
                        Scanner inChoose = new Scanner(System.in);
                        choose[i] = inChoose.nextInt();

                        if (choose[i] < 1 || choose[i] > 5) {
                            throw new InputMismatchException();
                        }
                        else {
                            boolean contains = false;
                            for (int j = 0; j < i; j++) {
                                if (choose[i] == choose[j]) {
                                    contains = true;
                                }
                            }

                            if (contains) {
                                throw new SameChooseErrorException(councilPrivilege);
                            }
                        }
                    }
                    catch (InputMismatchException e) {
                        e.printStackTrace();
                        --i;
                        System.out.println("Incorrect answer");
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

    public static FamilyColor chooseFamilyMemberColor () throws InputMismatchException {
        FamilyColor familyColor = FamilyColor.NEUTRAL;
        boolean correct = false;
        while (!correct) {
            try {
                System.out.println("Which FamilyMember do you choose?\n" + "1. " + FamilyColor.WHITE + "\n" + "2. " + FamilyColor.BLACK + "\n" + "3. " + FamilyColor.ORANGE + "\n" + "4. " + FamilyColor.NEUTRAL + "\n");
                Scanner inChoose = new Scanner(System.in);
                int choose = inChoose.nextInt();

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
                        System.out.println("Incorrect Answer");
                }

            } catch (InputMismatchException e) {
                e.printStackTrace();
                System.out.println("Incorrect answer");
            }
        }
        return familyColor;
    }

    synchronized public static int askNumber(int min, int max){
        int number;
        Scanner insertNumber;
        while(true){
            insertNumber = new Scanner (System.in);
            try {
                number = insertNumber.nextInt();
                if(number >= min && number <= max){
                    return number;
                }
                System.out.println("Number invalid. Retry");
            }
            catch (InputMismatchException e) {
                System.out.println("InputError. Retry with another input :");
            }
        }
    }

    public static Reward askNumberOfServant() {
        System.out.println("How many Servant do you want to use?");
        return new Reward(RewardType.SERVANT, askNumber(0, 7));
    }

    public static int askVaticanSupport() {
        System.out.print("Do you choose to support Vatican? \n0. No\n1. Yes");
        return askNumber(0,1);
    }


    public static Action chooseAction(int playerNumber) {
        Action action = new Action();
        boolean correct = false;
        while(!correct) {
            try {
                System.out.println("Which ActionSpot do you choose?\n" + "1. " + ActionType.TERRITORY_TOWER + "\n"
                        + "2. " + ActionType.BUILDING_TOWER + "\n" + "3. " + ActionType.CHARACTER_TOWER + "\n" + "4. "
                        + ActionType.VENTURE_TOWER + "\n" + "5. " + ActionType.HARVEST + "\n" + "6. " + ActionType.PRODUCE
                        + "\n" + "7. " + ActionType.MARKET + "\n" + "8. " + ActionType.COUNCIL_PALACE + "\n");
                Scanner inChoose = new Scanner(System.in);
                int choose = inChoose.nextInt();

                switch (choose) {
                    case 1:
                        action.setActionType(ActionType.TERRITORY_TOWER);
                        System.out.println("Which card?");
                        action.setSpot(askNumber(0, 3));
                        correct = true;
                        break;
                    case 2:
                        action.setActionType(ActionType.BUILDING_TOWER);
                        System.out.println("Which card?");
                        action.setSpot(askNumber(0, 3));
                        correct = true;
                        break;
                    case 3:
                        action.setActionType(ActionType.CHARACTER_TOWER);
                        System.out.println("Which card?");
                        action.setSpot(askNumber(0, 3));
                        correct = true;
                        break;
                    case 4:
                        action.setActionType(ActionType.VENTURE_TOWER);
                        System.out.println("Which card?");
                        action.setSpot(askNumber(0, 3));
                        correct = true;
                        break;
                    case 5:
                        action.setActionType(ActionType.HARVEST);
                        if(playerNumber > 2){
                            action.setSpot(askNumber(0, 1));
                        }
                        else{
                            action.setSpot(0);
                        }
                        correct = true;
                        break;
                    case 6:
                        action.setActionType(ActionType.PRODUCE);
                        if(playerNumber > 2){
                            action.setSpot(askNumber(0, 1));
                        }
                        else{
                            action.setSpot(0);
                        }
                        correct = true;
                        break;
                    case 7:
                        action.setActionType(ActionType.MARKET);
                        System.out.println("Which Spot? 0.COIN(5)  1.SERVANT(5)   2.COIN(2) & MILITARY_POINT(3) 3.COUNCILPRIVILEGE(2)");
                        if(playerNumber > 3){
                            action.setSpot(askNumber(0, 3));
                        }
                        else {
                            action.setSpot(askNumber(0, 1));
                        }
                        correct = true;
                        break;
                    case 8:
                        action.setActionType(ActionType.COUNCIL_PALACE);
                        action.setSpot(0);
                        correct = true;
                        break;
                    default:
                        System.out.println("Incorrect Answer");

                }
            } catch (InputMismatchException e) {
                e.printStackTrace();
                System.out.println("Incorrect Answer");
            }
        }
        return action;
    }


    public static Trade chooseTrade (BuildingCard buildingCard) {
        while (true) {
            try {
                System.out.println("Choose between this " + buildingCard.getTrades().size() + " possibilities: \n");
                System.out.println("    -1. No trades");
                for (int i = 0; i < buildingCard.getTrades().size(); i++) {
                    System.out.println("    " + i + ". " + buildingCard.getTrades().get(i).toString());
                }

                Scanner inChoose = new Scanner(System.in);
                int choose = inChoose.nextInt();

                if(choose == -1){
                    return null;
                }
                else if(choose >= 0 && choose < buildingCard.getTrades().size()) {
                    return buildingCard.getTrades().get(choose);
                }
                else {
                    System.out.println("Incorrect answer");
                }

            } catch (InputMismatchException e) {
                e.printStackTrace();
                System.out.println("Incorrect answer");
            }
        }
    }

    public static CostDiscount askWhichDiscount (CharacterCard characterCard) {
        System.out.println("Which Discount do you want to choose?\n");
        for (int i = 0; i < characterCard.getDiscounts().size(); i++) {
            System.out.println(i + ". " + characterCard.getDiscounts().get(i).toString() + "\n");
        }
        return characterCard.getDiscounts().get(askNumber(0, characterCard.getDiscounts().size() - 1));
    }

    public static boolean howDoWantPayVentureCard(Set<Reward> costs, Reward militaryPointNeeded, Reward militaryPointPrice){
        System.out.println("COSTS:\n" + costs);
        System.out.println("\nPOINT NEEDED:\n" + militaryPointNeeded);
        System.out.println("POINT PRICE:\n" + militaryPointPrice);
        System.out.println("\nWANT TO PAY WITH MILITARY POINTS?");
        Scanner input = new Scanner(System.in);
        return input.nextBoolean();
    }

    public static void main (String args[]) throws SameChooseErrorException, IOException, JSONException{
        /* Reward councilPrivilege = new Reward (RewardType.COUNCIL_PRIVILEGE, 2);

        int[] choose = exchangeCouncilPrivilege(councilPrivilege);

        for (int i = 0; i < choose.length; i++) {
            System.out.println(choose[i]);
        }*/

        /* DevelopmentCard buildingCard = JSONUtility.getCard(1, 4, CardType.BUILDING);

        Trade trade = chooseTrade((BuildingCard) buildingCard);

        System.out.println(trade.toString()); */

        /* System.out.println(chooseFamilyMemberColor()); */

        /* Action action = chooseAction(4);

        System.out.println(action.toString()); */

        /* System.out.println(askNumberOfServant());
        System.out.println(askVaticanSupport()); */

        CharacterCard characterCard = (CharacterCard) JSONUtility.getCard(1, 1, CardType.CHARACTER);
        CostDiscount costDiscount = askWhichDiscount(characterCard);
        System.out.println(costDiscount.toString());

    }
}

