package it.polimi.ingsw.pcXX;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by trill on 30/05/2017.
 */
public class Game{
    private final int PERIOD_NUMBER = 3;
    private final int CARD_FOR_TOWER = 4;
    private final List<String> usernames;
    private final int playerNumber;
    private final Board board;
    private final List<Player> players;
    private int[] territoryCard;
    private int[] buildingCard;
    private int[] characterCard;
    private int[] ventureCard;

    public Game(ArrayList<String> usernames){
        this.usernames = usernames;
        this.playerNumber = usernames.size();
        this.board = new Board(playerNumber);
        this.players = initializePlayers();
    }

    private List<Player> initializePlayers(){
        List<Player> players = new ArrayList<>();
        for(int i = 0; i < playerNumber; i++){
            PlayerColor playerColor = PlayerColor.fromInt(i + 1);
            int position = board.getOrder().getPositionOrder(playerColor);
            players.add(new Player(usernames.get(i), playerColor, position, null, null));
        }

        return players;
    }

    private List<PlayerColor> calculateOrder(){
        List<PlayerColor> order = new ArrayList<>();
        Random rand = new Random();
        List<Integer> orderInt= new ArrayList<>();
        int i = 0;
        while(i < playerNumber){
            int randInt = rand.nextInt(playerNumber) + 1;
            if(!orderInt.contains(randInt)){
                orderInt.add(randInt);
                i++;
            }
        }
        for(i = 0; i < orderInt.size(); i++){
            order.add(PlayerColor.fromInt(orderInt.get(i)));
        }
        return order;
    }
}
