package it.polimi.ingsw.pcXX;

import it.polimi.ingsw.pcXX.Exception.IllegalNumberOf;
import it.polimi.ingsw.pcXX.Exception.SameChooseErrorException;

import java.util.*;

/**
 * Created by Povaz on 06/06/2017.
 */
public class TerminalInput { //Metodi view del Client

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

    public static void main (String args[]) throws SameChooseErrorException{
        Reward councilPrivilege = new Reward (RewardType.COUNCIL_PRIVILEGE, 2);

        int[] choose = exchangeCouncilPrivilege(councilPrivilege);

        for (int i = 0; i < choose.length; i++) {
            System.out.println(choose[i]);
        }
    }

    public static void manageAction (ActionSpot actionSpot, FamilyMember familyMember) {
        // TODO gestire mappatura delle azioni
    }

    public static Trade chooseTrade (BuildingCard buildingCard) {
        try {
            if (buildingCard.getTrades().size() > 2) {
                throw new IllegalNumberOf(buildingCard);
            }

            while (true) {
                try {
                    System.out.println("Choose between this two possibility: \n    1. " + buildingCard.getTrades().get(0).toString() + "\n    2. " + buildingCard.getTrades().get(1).toString() + "\n");
                    Scanner inChoose = new Scanner(System.in);
                    int choose = inChoose.nextInt();

                    if (choose > 1 || choose < 2) {
                        throw new InputMismatchException();
                    }
                    else {
                        return buildingCard.getTrades().get(choose);
                    }

                } catch (InputMismatchException e) {
                    e.printStackTrace();
                    System.out.println("Incorrect answer");
                }
            }
        }
        catch (IllegalNumberOf e) {
            e.printStackTrace();
            Set<Reward> give = new HashSet<>();
            Set<Reward> take = new HashSet<>();
            Trade nullTrade = new Trade(give, take);
            return nullTrade;
        }
    }
}

