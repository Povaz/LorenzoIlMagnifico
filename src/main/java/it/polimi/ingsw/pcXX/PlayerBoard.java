package it.polimi.ingsw.pcXX;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trill on 23/05/2017.
 */
public class PlayerBoard {
    private final PlayerColor color;
    private final Resource coin;
    private final Resource wood;
    private final Resource stone;
    private final Resource servant;
    private final List<FamilyMember> familyMembers;
    private final PersonalBonusTile personalBonusTile;
    private final List<LeaderCard> leaderCards;
    private final TerritorySpot territorySpot;
    private final BuildingSpot buildingSpot;
    private final CharacterSpot characterSpot;
    private final VentureSpot ventureSpot;
    private final Modifier modifier;


    public PlayerBoard(PlayerColor color, int playerOrder, PersonalBonusTile personalBonusTile, List<LeaderCard> leaderCards){
        this.color = color;
        this.coin = new Resource(ResourceType.COIN, 5);
        this.wood = new Resource(ResourceType.WOOD, 2);
        this.stone = new Resource(ResourceType.STONE, 2);
        this.servant = new Resource(ResourceType.SERVANT, 3);
        initializeResources(playerOrder);
        this.familyMembers = initializeFamilyMembers(color);
        this.personalBonusTile = personalBonusTile;
        this.leaderCards = leaderCards;
        this.territorySpot = new TerritorySpot();
        this.buildingSpot = new BuildingSpot();
        this.characterSpot = new CharacterSpot();
        this.ventureSpot = new VentureSpot();
        this.modifier = new Modifier();
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

    private List<FamilyMember> initializeFamilyMembers(PlayerColor color){
        List<FamilyMember> familyMember = new ArrayList<>();
        familyMember.add(new FamilyMember(false, 0, false, color, FamilyColor.WHITE));
        familyMember.add(new FamilyMember(false, 0, false, color, FamilyColor.ORANGE));
        familyMember.add(new FamilyMember(false, 0, false, color, FamilyColor.BLACK));
        familyMember.add(new FamilyMember(false, 0, false, color, FamilyColor.NEUTRAL));
        return familyMember;
    }
}
