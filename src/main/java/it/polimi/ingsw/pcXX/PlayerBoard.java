package it.polimi.ingsw.pcXX;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by trill on 23/05/2017.
 */
public class PlayerBoard {
    private final PlayerColor color;
    private final Resource coin;
    private final Resource wood;
    private final Resource stone;
    private final Resource servant;
    private final Point militaryPoint;
    private final Point faltihPoint;
    private final Point victoryPoint;
    private final List<FamilyMember> familyMembers;
    private final PersonalBonusTile personalBonusTile;
    private final List<LeaderCard> leaderCards;
    private final TerritorySpot territorySpot;
    private final BuildingSpot buildingSpot;
    private final CharacterSpot characterSpot;
    private final VentureSpot ventureSpot;
    private final Modifier modifier;


    public PlayerBoard(Player player, int playerOrder, PersonalBonusTile personalBonusTile, List<LeaderCard> leaderCards){
        this.color = player.getColor();
        this.coin = new Resource(ResourceType.COIN, 5);
        this.wood = new Resource(ResourceType.WOOD, 2);
        this.stone = new Resource(ResourceType.STONE, 2);
        this.servant = new Resource(ResourceType.SERVANT, 3);
        initializeResources(playerOrder);
        this.militaryPoint = new Point(PointType.MILITARY_POINT, 0);
        this.faltihPoint = new Point(PointType.FAITH_POINT, 0);
        this.victoryPoint = new Point(PointType.VICTORY_POINT, 0);
        this.familyMembers = initializeFamilyMembers(player);
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

    private List<FamilyMember> initializeFamilyMembers(Player player){
        List<FamilyMember> familyMember = new ArrayList<>();
        familyMember.add(new FamilyMember(false, 0, false, player, FamilyColor.WHITE));
        familyMember.add(new FamilyMember(false, 0, false, player, FamilyColor.ORANGE));
        familyMember.add(new FamilyMember(false, 0, false, player, FamilyColor.BLACK));
        familyMember.add(new FamilyMember(false, 0, false, player, FamilyColor.NEUTRAL));
        return familyMember;
    }

    public void give(Set<Reward> rewards){
        for(Reward r : rewards){
            if(r instanceof Resource){
                giveResource((Resource) r);
            }
            else if(r instanceof Point){
                givePoint((Point) r);
            }
            else if(r instanceof CouncilPrivilege){
                giveCouncilPrivilege((CouncilPrivilege) r);
            }
        }
    }

    private void giveResource(Resource resource){
        switch(resource.getType()){
            case WOOD:
                wood.addQuantity(resource.getQuantity());
                break;
            case STONE:
                stone.addQuantity(resource.getQuantity());
                break;
            case SERVANT:
                servant.addQuantity(resource.getQuantity());
                break;
            case COIN:
                coin.addQuantity(resource.getQuantity());
                break;
        }
    }

    private void givePoint(Point point){
        switch(point.getType()){
            case MILITARY_POINT:
                militaryPoint.addQuantity(point.getQuantity());
                break;
            case FAITH_POINT:
                faltihPoint.addQuantity(point.getQuantity());
                break;
            case VICTORY_POINT:
                victoryPoint.addQuantity(point.getQuantity());
                break;
        }
    }

    private void giveCouncilPrivilege(CouncilPrivilege councilPrivilege){
        // TODO
    }

    public boolean harvest(int value){
        for(DevelopmentCard dC : territorySpot.getCards()){
            TerritoryCard tc = (TerritoryCard) dC;
            if(value >= tc.getDiceHarvestAction()){
                give(tc.getEarnings());
            }
        }
        return true;
    }

    public boolean produce(int value){
        // TODO
        return false;
    }
}
