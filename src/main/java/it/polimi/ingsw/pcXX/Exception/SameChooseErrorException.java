package it.polimi.ingsw.pcXX.Exception;

/**
 * Created by Povaz on 06/06/2017.
 */
public class SameChooseErrorException extends Exception{
    public SameChooseErrorException (Object obj) {
        System.out.println("You cannot choose the same " + obj.getClass() + " more than once");
    }
}
