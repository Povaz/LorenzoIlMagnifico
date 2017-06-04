package it.polimi.ingsw.pcXX;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        if(value >= personalBonusTile.getDiceHarvest()){
            counter.sum(personalBonusTile.getHarvestRewards());
        }
        for(DevelopmentCard dC : territorySpot.getCards()){
            TerritoryCard tc = (TerritoryCard) dC;
            if(value >= tc.getDiceHarvestAction()){
                if(tc.getEarnings() != null){
                    counter.sum(tc.getEarnings());
                }
            }
        }
        return true;
    }

    public boolean produce(int value){
        Counter copyForCosts = new Counter(counter);
        Counter counterMod = new Counter();

        if(value >= personalBonusTile.getDiceProduction()){
            counterMod.sum(personalBonusTile.getProductionRewards());
        }
        for(DevelopmentCard dC : buildingSpot.getCards()){
            BuildingCard bC = (BuildingCard) dC;
            if(value >= bC.getDiceProductionAction()){
                if(bC.getEarnings() != null){
                    counterMod.sum(bC.getEarnings());
                }
                if(bC.getRewardForCard() != null){
                    counterMod.sum(convertRewardForReward(bC.getRewardForReward()));
                }
                if(bC.getRewardForReward() != null){
                    covertRewardForCard(bC.getRewardForCard());
                }
                if(bC.getTrades() != null){
                    for(Trade t : bC.getTrades()){
                        copyForCosts.subtract(t.getGive());
                        counterMod.subtract(t.getGive());
                        counterMod.sum(t.getTake());
                    }
                }
            }
        }
        if(!copyForCosts.check()){
            return false;
        }
        counter.sum(counterMod);
        return true;
    }

    public boolean buyCard(int value){
        /*
        TODO: controlla 3 monete, controlla costo + risorse rapide, compra carta;
         */


        return false;
    }

    private Set<Reward> convertRewardForReward(RewardForReward rewardForReward){
        Reward owned = rewardForReward.getOwned();
        Reward currentReward = counter.giveSameReward(owned);
        int multiplier = currentReward.getQuantity() / owned.getQuantity();
        Set<Reward> earned = new HashSet<>(rewardForReward.getEarned());
        for(Reward r : earned){
            r.multiplyQuantity(multiplier);
        }
        return earned;
    }

    private Set<Reward> covertRewardForCard(RewardForCard rewardForCard){
        CardType cardType = rewardForCard.getCardTypeOwned();
        int multiplier = getCardSpot(cardType).getCards().size();
        Set<Reward> earned = new HashSet<>(rewardForCard.getEarned());
        for(Reward r : earned){
            r.multiplyQuantity(multiplier);
        }
        return earned;
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

    public CardSpot getCardSpot(CardType cardType){
        if(cardType == CardType.TERRITORY){
            return getTerritorySpot();
        }
        if(cardType == CardType.BUILDING){
            return getBuildingSpot();
        }
        if(cardType == CardType.CHARACTER){
            return getCharacterSpot();
        }
        if(cardType == CardType.VENTURE){
            return getVentureSpot();
        }
        throw new IllegalArgumentException();
    }
}
