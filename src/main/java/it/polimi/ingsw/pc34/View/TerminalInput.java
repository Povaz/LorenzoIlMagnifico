package it.polimi.ingsw.pc34.View;

import it.polimi.ingsw.pc34.Controller.ActionInput;
import it.polimi.ingsw.pc34.Exception.IllegalNumberOf;
import it.polimi.ingsw.pc34.Exception.SameChooseErrorException;
import it.polimi.ingsw.pc34.Model.*;
import org.json.JSONException;

import java.io.IOException;
import java.util.*;

/**
 * Created by Povaz on 06/06/2017.
 */
public class TerminalInput { //Metodi view: richieste ai Client TODO DOVRANNO ESSERE SPOSTATI SOLAMENTE SU CLIENT SIDE

    public static boolean doYouWantToSkip () {
        System.out.println("Do you want to skip your action?\n  0. No\n  1. Yes");
        int skipAction = askNumber(0,1);
        if (skipAction == 0) {
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
                                "Don't choose the sameType reward as before" + "\n");
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

    public static FamilyColor chooseFamilyMemberColorNotNeutral () throws InputMismatchException {
        FamilyColor familyColor = FamilyColor.NEUTRAL;
        boolean correct = false;
        while (!correct) {
            try {
                System.out.println("Which FamilyMember do you choose?\n" + "1. " + FamilyColor.WHITE + "\n" + "2. " + FamilyColor.BLACK + "\n" + "3. " + FamilyColor.ORANGE + "\n");
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

    public static int askWhichCardChange(List<LeaderCard> leaderCardsInHand){
        System.out.println("Scegli la carta da convertire: ");
        for(int i = 0; i < leaderCardsInHand.size(); i++){
            System.out.println(i + ".");
            System.out.println(leaderCardsInHand.get(i).toString());
        }
        return askNumber(0, leaderCardsInHand.size() - 1);
    }

    public static int askWhichCardPlace(List<LeaderCard> leaderCardsInHand){
        System.out.println("Scegli la carta da piazzare: ");
        for(int i = 0; i < leaderCardsInHand.size(); i++){
            System.out.println(i + ".");
            System.out.println(leaderCardsInHand.get(i).toString());
        }
        return askNumber(0, leaderCardsInHand.size() - 1);
    }

    public static int askWhichCardActivate(List<ImmediateLeaderCard> immediateLeaderCardsPositionated){
        System.out.println("Scegli la carta da piazzare: ");
        for(int i = 0; i < immediateLeaderCardsPositionated.size(); i++){
            System.out.println(i + ".");
            System.out.println(immediateLeaderCardsPositionated.get(i).toString());
        }
        return askNumber(0, immediateLeaderCardsPositionated.size() - 1);
    }

    public static int askNumberOfServant() {
        System.out.println("How many Servant do you want to use?");
        return askNumber(0, 7);
    }

    public static boolean wantToSupportVatican() {
        System.out.print("Do you choose to support Vatican? \n0. No\n1. Yes");
        int supportVatican = askNumber(0,1);
        if(supportVatican == 0) {
            return false;
        }
        else{
            return true;
        }
    }


    public static ActionInput chooseAction(int playerNumber, int choose) {
        ActionInput actionInput = new ActionInput();
        boolean correct = false;
        while(!correct) {
            try {
                switch (choose) {
                    case 1:
                        actionInput.setActionType(ActionType.TERRITORY_TOWER);
                        System.out.println("Which card?");
                        actionInput.setSpot(askNumber(0, 3));
                        correct = true;
                        break;
                    case 2:
                        actionInput.setActionType(ActionType.BUILDING_TOWER);
                        System.out.println("Which card?");
                        actionInput.setSpot(askNumber(0, 3));
                        correct = true;
                        break;
                    case 3:
                        actionInput.setActionType(ActionType.CHARACTER_TOWER);
                        System.out.println("Which card?");
                        actionInput.setSpot(askNumber(0, 3));
                        correct = true;
                        break;
                    case 4:
                        actionInput.setActionType(ActionType.VENTURE_TOWER);
                        System.out.println("Which card?");
                        actionInput.setSpot(askNumber(0, 3));
                        correct = true;
                        break;
                    case 5:
                        actionInput.setActionType(ActionType.HARVEST);
                        if(playerNumber > 2){
                            actionInput.setSpot(askNumber(0, 1));
                        }
                        else{
                            actionInput.setSpot(0);
                        }
                        correct = true;
                        break;
                    case 6:
                        actionInput.setActionType(ActionType.PRODUCE);
                        if(playerNumber > 2){
                            actionInput.setSpot(askNumber(0, 1));
                        }
                        else{
                            actionInput.setSpot(0);
                        }
                        correct = true;
                        break;
                    case 7:
                        actionInput.setActionType(ActionType.MARKET);
                        System.out.println("Which Spot? 0.COIN(5)  1.SERVANT(5)   2.COIN(2) & MILITARY_POINT(3) 3.COUNCILPRIVILEGE(2)");
                        if(playerNumber > 3){
                            actionInput.setSpot(askNumber(0, 3));
                        }
                        else {
                            actionInput.setSpot(askNumber(0, 1));
                        }
                        correct = true;
                        break;
                    case 8:
                        actionInput.setActionType(ActionType.COUNCIL_PALACE);
                        actionInput.setSpot(0);
                        correct = true;
                        break;
                    case -1:
                        boolean skip = doYouWantToSkip();
                        if (skip) {
                            return null;
                        }
                        else {
                            break;
                        }
                            default:
                        System.out.println("Incorrect Answer");

                }
            } catch (InputMismatchException e) {
                e.printStackTrace();
                System.out.println("Incorrect Answer");
            }
        }
        return actionInput;
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

    public static int askWhichDiscount(List<List<Reward>> discounts){
        System.out.println("Which Discount do you want to choose?\n");
        for (int i = 0; i < discounts.size(); i++) {
            System.out.println(i + ". " + discounts.get(i).toString() + "\n");
        }
        return askNumber(0, discounts.size() - 1);
    }

    public static boolean wantToPayWithMilitaryPoint(Set<Reward> costs, Reward militaryPointNeeded, Reward militaryPointPrice){
        System.out.println("Do you want to pay with Military Points?\n  0. No\n  1. Yes");


        System.out.println("Costs: ");
        for (Reward reward: costs) {
            System.out.println("    " + reward);
        }
        System.out.println("Military Point Needed: " + militaryPointNeeded.toString() + " ");
        System.out.println("MilitaryPointPrice: " + militaryPointPrice.toString() + " ");

        int payWithMilitaryPoint = askNumber(0,1);
        if (payWithMilitaryPoint == 0) {
            return false;
        }
        else {
            return true;
        }
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

        /* ActionInput action = chooseAction(4);

        System.out.println(action.toString());*/

        /* System.out.println(askNumberOfServant());
        System.out.println(askVaticanSupport()); */

        /* CharacterCard characterCard = (CharacterCard) JSONUtility.getCard(1, 1, CardType.CHARACTER);
        CostDiscount costDiscount = askWhichDiscount(characterCard);
        System.out.println(costDiscount.toString()); */

        Set<Reward> reward = new HashSet<>();
        reward.add(new Reward(RewardType.COIN, 2));
        reward.add(new Reward(RewardType.STONE, 2));


        boolean militaryPoint = wantToPayWithMilitaryPoint(reward, new Reward (RewardType.MILITARY_POINT, 5), new Reward (RewardType.MILITARY_POINT, 2));
        System.out.println(militaryPoint);
    }
}

