package it.polimi.ingsw.pcXX.Exception;

/**
 * Created by Povaz on 06/06/2017.
 */
public class IllegalNumberOf extends Exception {
    public IllegalNumberOf(Object obj){
        System.out.println("This Object contains an exceeding number of " + obj.getClass());
    }
}
