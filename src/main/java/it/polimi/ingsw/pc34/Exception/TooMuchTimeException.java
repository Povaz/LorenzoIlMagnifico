package it.polimi.ingsw.pc34.Exception;

/**
 * Created by trill on 06/06/2017.
 */
public class TooMuchTimeException extends Exception{
    public TooMuchTimeException(Object object){
        System.out.println("Timout for action ended in " + object.getClass());
    }
}