package it.polimi.ingsw.pcXX;

import java.util.*;

/**
 * Created by trill on 23/05/2017.
 */
public class PlayerBoard {
    private final Player player;
    private final PlayerColor color;
    private Counter counter;
    private final List<FamilyMember> familyMembers;
    private final PersonalBonusTile personalBonusTile;
    private final List<LeaderCard> leaderCards;
    private final TerritorySpot territorySpot;
    private final BuildingSpot buildingSpot;
    private final CharacterSpot characterSpot;
    private final VentureSpot ventureSpot;
    private final Modifier modifier;


    public PlayerBoard(Player player, int playerOrder, PersonalBonusTile personalBonusTile, List<LeaderCard> leaderCards){
        this.player = player;
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
        familyMember.add(new FamilyMember(player, FamilyColor.WHITE));
        familyMember.add(new FamilyMember(player, FamilyColor.ORANGE));
        familyMember.add(new FamilyMember(player, FamilyColor.BLACK));
        familyMember.add(new FamilyMember(player, FamilyColor.NEUTRAL));
        return familyMember;
    }

    public boolean harvest(int value) throws TooMuchTimeException{
        Counter newCounter = new Counter(counter);

        if(personalBonusTile.getHarvestRewards() != null) {
            if(value >= personalBonusTile.getDiceHarvest()){
                newCounter.sum(personalBonusTile.getHarvestRewards());
            }
        }
        for(DevelopmentCard dC : territorySpot.getCards()){
            TerritoryCard tc = (TerritoryCard) dC;
            if(value >= tc.getDiceHarvestAction()){
                if(tc.getEarnings() != null){
                    newCounter.sum(tc.getEarnings());
                }
            }
        }
        if(!newCounter.check()){
            return false;
        }
        newCounter.round();
        counter = newCounter;
        return true;
    }

    public boolean produce(int value) throws TooMuchTimeException{
        Counter copyForCosts = new Counter(counter);
        Counter newCounter = new Counter(counter);

        if(personalBonusTile.getProductionRewards() != null) {
            if(value >= personalBonusTile.getDiceProduction()){
                newCounter.sum(personalBonusTile.getProductionRewards());
            }
        }
        for(DevelopmentCard dC : buildingSpot.getCards()){
            BuildingCard bC = (BuildingCard) dC;
            if(value >= bC.getDiceProductionAction()){
                if(bC.getEarnings() != null){
                    newCounter.sum(bC.getEarnings());
                }
                if(bC.getRewardForCard() != null){
                    newCounter.sum(convertRewardForReward(bC.getRewardForReward()));
                }
                if(bC.getRewardForReward() != null){
                    newCounter.sum(covertRewardForCard(bC.getRewardForCard()));
                }
                if(bC.getTrades() != null){

                    System.out.println("Seleziona scambio:");
                    Scanner input = new Scanner(System.in);
                    int scelta = input.nextInt();

                    if(scelta != -1){
                        try {
                            Trade trade = bC.getTrades().get(scelta);
                            copyForCosts.subtract(trade.getGive());
                            newCounter.subtract(trade.getGive());
                            newCounter.sum(trade.getTake());
                        } catch (IndexOutOfBoundsException e) {
                            return false;
                        }
                    }
                }
            }
        }
        if(!copyForCosts.check()){
            return false;
        }
        if(!newCounter.check()){
            return false;
        }
        newCounter.round();
        counter = newCounter;
        return true;
    }

    public boolean buyCard(Floor floor) throws TooMuchTimeException{
        Counter newCounter = new Counter(counter);
        DevelopmentCard developmentCard = floor.getCard();

        if(!payTowerTax(newCounter, floor)){
            return false;
        }

        earnTowerReward(newCounter, floor);

        earnCardFastReward(newCounter, developmentCard);

        if(!developmentCard.isPlaceable(newCounter, this)){
            return false;
        }

        if(!developmentCard.canBuyCard(newCounter)){
            return false;
        }

        if(!newCounter.check()){
            return false;
        }

        developmentCard.place(this);
        newCounter.round();
        counter = newCounter;


        return true;
    }

    private boolean payTowerTax(Counter newCounter, Floor floor){
        if(floor.getTower().isOccupied()){
            newCounter.subtract(floor.getTower().getOccupiedTax());
        }
        if(!newCounter.check()){
            return false;
        }
        return true;
    }

    private void earnTowerReward(Counter newCounter, Floor floor) throws TooMuchTimeException{
        if(floor.getRewards() != null){
            newCounter.sum(floor.getRewards());
        }
    }

    private void earnCardFastReward(Counter newCounter, DevelopmentCard developmentCard) throws TooMuchTimeException{
        if(developmentCard.getFastRewards() != null){
            newCounter.sum(developmentCard.getFastRewards());
        }
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

    public Player getPlayer() {
        return player;
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

    public void setCounter(Counter counter) {
        this.counter = counter;
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
