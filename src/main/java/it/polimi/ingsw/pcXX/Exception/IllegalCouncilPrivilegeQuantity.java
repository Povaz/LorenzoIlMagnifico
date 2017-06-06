package it.polimi.ingsw.pcXX.Exception;

/**
 * Created by Povaz on 06/06/2017.
 */
public class IllegalCouncilPrivilegeQuantity extends Exception {
    public IllegalCouncilPrivilegeQuantity (){
        System.out.println("Council privilege with Quantity > 5 is not allowed");
    }
}
