package it.polimi.ingsw.pc34;

import it.polimi.ingsw.pc34.Model.Game;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by trill on 01/06/2017.
 */
public class GameTest{
    @Test
    public void startPeriodTest(){
        try {
            List<String> usernames = new ArrayList<>(Arrays.asList("EriK", "Paolo", "Tom"));
            Game game = new Game(usernames);
            //game.startPeriod();

            System.out.println(Arrays.toString(game.getTerritoryCard()));
            System.out.println(Arrays.toString(game.getBuildingCard()));
            System.out.println(Arrays.toString(game.getCharacterCard()));
            System.out.println(Arrays.toString(game.getVentureCard()));
        } catch(Exception e){
            e.printStackTrace();
            assertTrue(false);
        }
    }
}