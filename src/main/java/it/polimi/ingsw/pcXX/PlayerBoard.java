package it.polimi.ingsw.pcXX;

/**
 * Created by trill on 23/05/2017.
 */
public class PlayerBoard {
    private final PlayerColor color;
    private final Resource coin;
    private final Resource wood;
    private final Resource stone;
    private final Resource servant;

    public PlayerBoard(PlayerColor color, int order){
        this.color = color;
        this.coin = new Resource(ResourceType.COIN, 3);
        this.wood = new Resource(ResourceType.WOOD, 3);
        this.stone = new Resource(ResourceType.STONE, 3);
        this.servant = new Resource(ResourceType.SERVANT, 3);
        initializeResources(order);
    }

    private void initializeResources(int order){
        if(order == 2)
            coin.addQuantity(1);
        if(order == 3)
            coin.addQuantity(2);
        if(order == 4)
            coin.addQuantity(3);
        if(order == 5)
            coin.addQuantity(4);
    }
}
