package it.polimi.ingsw.pc34;

import it.polimi.ingsw.pc34.Model.Game;
import it.polimi.ingsw.pc34.RMI.ServerGameRMI;
import it.polimi.ingsw.pc34.RMI.ServerLogin;
import it.polimi.ingsw.pc34.RMI.ServerLoginImpl;
import it.polimi.ingsw.pc34.RMI.UserLogin;
import it.polimi.ingsw.pc34.Socket.ServerSOC;
import it.polimi.ingsw.pc34.SocketRMICongiunction.ConnectionType;
import it.polimi.ingsw.pc34.SocketRMICongiunction.Lobby;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by trill on 01/06/2017.
 */
public class GameTest{
    @Test
    public void startPeriodTest(){
        try {
            HashMap<String, ConnectionType> usersOfThisGame = new HashMap<>();
            usersOfThisGame.put("Erick", ConnectionType.RMI);
            usersOfThisGame.put("Tommaso", ConnectionType.SOCKET);
            usersOfThisGame.put("PaoloCulo", ConnectionType.RMI);

            Lobby lobby = new Lobby();
            ServerLoginImpl serverLogin = new ServerLoginImpl(lobby);
            ServerSOC serverSoc = new ServerSOC(1337, lobby);
            ServerGameRMI serverGameRMI = new ServerGameRMI(new ArrayList<UserLogin>());
            Game game = new Game(usersOfThisGame, serverGameRMI, serverSoc);
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