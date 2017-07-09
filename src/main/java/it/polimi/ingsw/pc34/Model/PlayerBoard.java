package it.polimi.ingsw.pc34.Model;

import java.util.*;

/**
 * Created by trill on 23/05/2017.
 */
public class PlayerBoard {
    private final Player player;
    private final PlayerColor color;
    private Counter counter;
    private final List<FamilyMember> familyMembers;
    private PersonalBonusTile personalBonusTile;
    private final TerritorySpot territorySpot;
    private final BuildingSpot buildingSpot;
    private final CharacterSpot characterSpot;
    private final VentureSpot ventureSpot;
    private final Modifier modifier;
    private final List<LeaderCard> leaderCardsInHand;
    private final List<ImmediateLeaderCard> immediateLeaderCardsPositioned;
    private final List<PermanentLeaderCard> permanentLeaderCardsPositioned;

    public PlayerBoard(Player player){
        this.player = player;
        this.color = player.getColor();
        this.counter = new Counter();
        this.familyMembers = initializeFamilyMembers(player);
        this.personalBonusTile = null;
        this.territorySpot = new TerritorySpot();
        this.buildingSpot = new BuildingSpot();
        this.characterSpot = new CharacterSpot();
        this.ventureSpot = new VentureSpot();
        this.modifier = new Modifier();
        this.leaderCardsInHand = new ArrayList<>();
        this.immediateLeaderCardsPositioned = new ArrayList<>();
        this.permanentLeaderCardsPositioned = new ArrayList<>();

    }

    private List<FamilyMember> initializeFamilyMembers(Player player){
        List<FamilyMember> familyMember = new ArrayList<>();
        familyMember.add(new FamilyMember(player, FamilyColor.BLACK));
        familyMember.add(new FamilyMember(player, FamilyColor.WHITE));
        familyMember.add(new FamilyMember(player, FamilyColor.ORANGE));
        familyMember.add(new FamilyMember(player, FamilyColor.NEUTRAL));
        return familyMember;
    }

    public void reinitializeFamilyMembers(List<Dice> dices){
        for(FamilyMember fM : familyMembers){
            fM.setUsed(false);
            FamilyColor color = fM.getColor();
            for(Dice d : dices){
                if(color == d.getColor()){
                    if(modifier.isPermanentDice()){
                        fM.setValue(modifier.getPermanentDiceValue());
                    }
                    else{
                        fM.setValue(d.getValue());
                    }
                }
            }
        }
    }

    public void reinitializeLeaderCards(){
        for(ImmediateLeaderCard iLC : immediateLeaderCardsPositioned){
            iLC.setActivated(false);
        }
    }

    public void earnFinalVictoryPoint(){
        if(!modifier.isNotEarnVictoryPointFromTerritory()){
            counter.sum(territorySpot.estimateVictoryPoint());
        }
        counter.sum(buildingSpot.estimateVictoryPoint());
        if(!modifier.isNotEarnVictoryPointFromCharacter()){
            counter.sum(characterSpot.estimateVictoryPoint());
        }
        if(!modifier.isNotEarnVictoryPointFromVenture()){
            counter.sum(ventureSpot.estimateVictoryPoint());
            counter.sum(earnVictoryPointFromVentureCards());
        }
        earnVictoryPointFromRewards();
        if(modifier.isLoseVictoryPointFromMilitaryPoint()){
            loseVictoryPointFromMilitaryPoint();
        }
        if(modifier.isLoseVictoryPointFromBuildingCost()){
            loseVictoryPointFromBuildingCost();
        }
        counter.round();
        if(modifier.isLoseVictoryPointFromVictoryPoint()){
            loseVictoryPointFromVictoryPoint();
        }
    }

    Reward earnVictoryPointFromVentureCards(){
        Reward victoryPoint = new Reward(RewardType.VICTORY_POINT, 0);
        for(DevelopmentCard dC : ventureSpot.getCards()){
            VentureCard vC = (VentureCard) dC;
            victoryPoint.sumQuantity(vC.getVictoryPointEarned());
        }
        return victoryPoint;
    }

    void earnVictoryPointFromRewards(){
        int sumRewards = 0;
        sumRewards += counter.getCoin().getQuantity();
        sumRewards += counter.getWood().getQuantity();
        sumRewards += counter.getStone().getQuantity();
        sumRewards += counter.getServant().getQuantity();
        int numberVictoryPoints = sumRewards / 5;
        counter.sum(new Reward(RewardType.VICTORY_POINT, numberVictoryPoints));
        // togli i punti se ha la scomunica
        if(modifier.isLoseVictoryPointFromResource()){
            counter.subtract(new Reward(RewardType.VICTORY_POINT, sumRewards));
        }
    }

    void loseVictoryPointFromMilitaryPoint(){
        counter.subtract(new Reward(RewardType.VICTORY_POINT, counter.getMilitaryPoint().getQuantity()));
    }

    void loseVictoryPointFromBuildingCost(){
        int sumCosts = 0;
        List<DevelopmentCard> developmentCards = buildingSpot.getCards();
        for(DevelopmentCard card : developmentCards){
            if(card.getCosts() != null){
                for(Reward r : card.getCosts()){
                   if(r.getType() == RewardType.WOOD || r.getType() == RewardType.STONE){
                       sumCosts += r.getQuantity();
                   }
                }
            }
        }
        counter.subtract(new Reward(RewardType.VICTORY_POINT, sumCosts));
    }

    void loseVictoryPointFromVictoryPoint(){
        int victoryPointToLose = counter.getVictoryPoint().getQuantity() / 5;
        counter.subtract(new Reward(RewardType.VICTORY_POINT, victoryPointToLose));
    }

    public CardSpot getCardSpot(CardType cardType){
        switch(cardType){
            case TERRITORY:
                return getTerritorySpot();
            case BUILDING:
                return getBuildingSpot();
            case CHARACTER:
                return getCharacterSpot();
            case VENTURE:
                return getVentureSpot();
            default:
                throw new IllegalArgumentException("developmentCard type incorrect");
        }
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

    public List<LeaderCard> getLeaderCardsInHand() {
        return leaderCardsInHand;
    }

    public List<ImmediateLeaderCard> getImmediateLeaderCardsPositioned() {
        return immediateLeaderCardsPositioned;
    }

    public List<PermanentLeaderCard> getPermanentLeaderCardsPositioned() {
        return permanentLeaderCardsPositioned;
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

    public void setPersonalBonusTile(PersonalBonusTile personalBonusTile){
        this.personalBonusTile = personalBonusTile;
    }

    @Override
    public String toString (){
        StringBuilder bld = new StringBuilder();
        bld.append("Username: " + player.getUsername() + "\n");
        bld.append("color: " + color + "\n\n\n");
        if(personalBonusTile != null){
            bld.append("Personal bonus tile:\n" + personalBonusTile.toString() + "\n\n");
        }
        if(!leaderCardsInHand.isEmpty()){
            bld.append("Leader cards in hand:\n");
            for(LeaderCard c : leaderCardsInHand){
                bld.append(c.toString() + "\n");
            }
            bld.append("\n");
        }
        if(!permanentLeaderCardsPositioned.isEmpty()){
            bld.append("Permanent leader cards positionated:\n");
            for(PermanentLeaderCard p : permanentLeaderCardsPositioned){
                bld.append(p.toString() + "\n");
            }
            bld.append("\n");
        }
        if(!immediateLeaderCardsPositioned.isEmpty()){
            bld.append("Immediate leader cards positionated:\n");
            for(ImmediateLeaderCard i : immediateLeaderCardsPositioned){
                bld.append(i.toString() + "\n");
            }
            bld.append("\n");
        }
        if(!territorySpot.getCards().isEmpty()){
            bld.append("Territory spot:\n" + territorySpot.toString() + "\n\n");
        }
        if(!buildingSpot.getCards().isEmpty()){
            bld.append("Building spot:\n" + buildingSpot.toString() + "\n\n");
        }
        if(!characterSpot.getCards().isEmpty()){
            bld.append("Character spot:\n" + characterSpot.toString() + "\n\n");
        }
        if(!ventureSpot.getCards().isEmpty()){
            bld.append("Venture spot:\n" + ventureSpot.toString() + "\n\n");
        }
        bld.append(counter.toString() + "\n\n");
        bld.append("Family members:\n");
        for(FamilyMember fM : familyMembers){
            bld.append(fM.toString() + "\n");
        }
        return bld.toString();
    }
}
