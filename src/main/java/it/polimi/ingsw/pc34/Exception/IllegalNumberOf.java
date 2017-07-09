package it.polimi.ingsw.pc34.Exception;

/**
 * Created by Povaz on 06/06/2017.
 */
@SuppressWarnings("serial")
public class IllegalNumberOf extends Exception {
    public IllegalNumberOf(Object obj){
        System.out.println("This Object contains an exceeding number of " + obj.getClass());
    }
}
