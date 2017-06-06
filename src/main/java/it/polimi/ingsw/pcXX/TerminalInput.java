package it.polimi.ingsw.pcXX;

import com.sun.org.apache.regexp.internal.RE;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Created by Povaz on 06/06/2017.
 */
public class TerminalInput { //Metodi view del Client

    public static int[] exchangeCouncilPrivilege(Reward councilPrivilege) throws SameChooseErrorException {
        try {
            if (councilPrivilege.getQuantity() > 5) {
                throw new IllegalCouncilPrivilegeQuantity();
            }
            int[] choose = new int[councilPrivilege.getQuantity()];
            for (int i = 0; i < councilPrivilege.getQuantity() - 1; i++) {
                try {
                    System.out.println("1. 1 WOOD 1 Stone   2. 2 SERVANT    3. 2 COIN   4. 2 MILITARY_POINT  5. 1 FAITH_POINT \n" +
                            "Don't choose the same reward as before");
                    Scanner inChoose = new Scanner(System.in);
                    choose[i] = inChoose.nextInt();

                    boolean contains = false;
                    for (int j = 0; j < i; j++) {
                        if (choose[i] == choose[j]) {
                            contains = true;
                        }
                    }

                    if (contains) {
                        throw new SameChooseErrorException();
                    }
                } catch (SameChooseErrorException e) {
                    i--;
                    System.out.println("You cannot chose the same reward twice");
                }
            }
            return choose;
        }
        catch (IllegalCouncilPrivilegeQuantity e) {
            int [] choose = new int[] {1, 2, 3, 4, 5};
            return choose;
        }
    }

    public static void main (String args[]) throws SameChooseErrorException{
        Reward councilPrivilege = new Reward (RewardType.COUNCIL_PRIVILEGE, 7);

        int[] choose = exchangeCouncilPrivilege(councilPrivilege);

        for (int i = 0; i < choose.length; i++) {
            System.out.println(choose[i]);
        }
    }
}
