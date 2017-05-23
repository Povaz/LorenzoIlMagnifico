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
    /*
    private final FamilyMember[] familyMember;
    private final PersonalBonusTile personalBonusTile;
    private final Modifier modifier;
    private final LeaderCard[] leaderCard;
    private final TerritorySpot territorySpot;
    private final BuildingSpot buildingSpot;
    private final CharacterSpot characterSpot;
    private final VentureSpot ventureSpot;
    */

    public PlayerBoard(PlayerColor color, int playerOrder){
        this.color = color;
        this.coin = new Resource(ResourceType.COIN, 5);
        this.wood = new Resource(ResourceType.WOOD, 2);
        this.stone = new Resource(ResourceType.STONE, 2);
        this.servant = new Resource(ResourceType.SERVANT, 3);
        initializeResources(playerOrder);
    }

    private void initializeResources(int playerOrder){
    	if(playerOrder == 1)
            coin.setQuantity(5);
    	if(playerOrder == 2)
    		coin.setQuantity(6);
        if(playerOrder == 3)
        	coin.setQuantity(7);
        if(playerOrder == 4)
        	coin.setQuantity(8);
        if(playerOrder == 5)
            coin.setQuantity(9);
    }
}
