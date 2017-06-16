package it.polimi.ingsw.pc34.RMI;

import it.polimi.ingsw.pc34.Controller.ActionInput;
import it.polimi.ingsw.pc34.Model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Set;

/**
 * Created by Povaz on 16/06/2017.
 */
public interface UserGame extends Remote {
    /*boolean doYouWantToSkip();
    int[] exchangeCouncilPrivilege(Reward councilPrivilege);
    FamilyColor chooseFamilyColor() throws InputMismatchException;
    FamilyColor chooseFamilyMemberColor() throws InputMismatchException;
    int askNumber(int min, int max);
    int askWhichCardChange (List<LeaderCard> leaderCardsInHands);
    int askWhichCardPlace (List<LeaderCard> leaderCardsInHands);
    int askWhichCardActivate (List<ImmediateLeaderCard> immediateLeaderCardsPositioned);
    int askNumberOfServant();
    boolean wantToSupportVatican();*/
    /*Trade chooseTrade(BuildingCard buildingCard);
    int askWhichDiscount (List<List<Reward>> discounts);
    boolean wantToPayWithMilitaryPoint(Set<Reward> costs, Reward militaryPointNeeded, Reward militaryPointPrice);*/
}
