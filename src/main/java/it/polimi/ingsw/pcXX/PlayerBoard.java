package it.polimi.ingsw.pcXX;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trill on 23/05/2017.
 */
public class PlayerBoard {
    private final PlayerColor color;
    private final Counter counter;
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
        this.counter = new Counter(playerOrder);
        this.familyMembers = initializeFamilyMembers(player);
        this.personalBonusTile = personalBonusTile;
        this.leaderCards = leaderCards;
        this.territorySpot = new TerritorySpot();
        this.buildingSpot = new BuildingSpot();
        this.characterSpot = new CharacterSpot();
        this.ventureSpot = new VentureSpot();
        this.modifier = new Modifier();
    }

    private List<FamilyMember> initializeFamilyMembers(Player player){
        List<FamilyMember> familyMember = new ArrayList<>();
        familyMember.add(new FamilyMember(false, 0, false, player, FamilyColor.WHITE));
        familyMember.add(new FamilyMember(false, 0, false, player, FamilyColor.ORANGE));
        familyMember.add(new FamilyMember(false, 0, false, player, FamilyColor.BLACK));
        familyMember.add(new FamilyMember(false, 0, false, player, FamilyColor.NEUTRAL));
        return familyMember;
    }


    public boolean harvest(int value){
        for(DevelopmentCard dC : territorySpot.getCards()){
            TerritoryCard tc = (TerritoryCard) dC;
            if(value >= tc.getDiceHarvestAction()){
                counter.add(tc.getEarnings());
            }
        }
        return true;
    }

    public boolean produce(int value){

        return false;
    }

    public PlayerColor getColor() {
        return color;
    }

    public Counter getCounter() {
        return counter;
    }

    public List<FamilyMember> getFamilyMembers() {
        return familyMembers;
    }

    public PersonalBonusTile getPersonalBonusTile() {
        return personalBonusTile;
    }

    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public TerritorySpot getTerritorySpot() {
        return territorySpot;
    }

    public BuildingSpot getBuildingSpot() {
        return buildingSpot;
    }

    public CharacterSpot getCharacterSpot() {
        return characterSpot;
    }

    public VentureSpot getVentureSpot() {
        return ventureSpot;
    }

    public Modifier getModifier() {
        return modifier;
    }
}
