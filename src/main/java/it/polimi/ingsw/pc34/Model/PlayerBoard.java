package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.Exception.TooMuchTimeException;
import it.polimi.ingsw.pc34.View.TerminalInput;

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
    private final TerritorySpot territorySpot;
    private final BuildingSpot buildingSpot;
    private final CharacterSpot characterSpot;
    private final VentureSpot ventureSpot;
    private final Modifier modifier;
    private final List<LeaderCard> leaderCardsInHand;
    private final List<ImmediateLeaderCard> immediateLeaderCardsPositionated;
    private final List<PermanentLeaderCard> permanentLeaderCardsPositionated;

    public PlayerBoard(Player player, PersonalBonusTile personalBonusTile, List<LeaderCard> leaderCards){
        this.player = player;
        this.color = player.getColor();
        this.counter = new Counter();
        this.familyMembers = initializeFamilyMembers(player);
        this.personalBonusTile = personalBonusTile;
        this.territorySpot = new TerritorySpot();
        this.buildingSpot = new BuildingSpot();
        this.characterSpot = new CharacterSpot();
        this.ventureSpot = new VentureSpot();
        this.modifier = new Modifier();
        this.leaderCardsInHand = leaderCards;
        this.immediateLeaderCardsPositionated = new ArrayList<>();
        this.permanentLeaderCardsPositionated = new ArrayList<>();
    }

    private List<FamilyMember> initializeFamilyMembers(Player player){
        List<FamilyMember> familyMember = new ArrayList<>();
        familyMember.add(new FamilyMember(player, FamilyColor.WHITE));
        familyMember.add(new FamilyMember(player, FamilyColor.ORANGE));
        familyMember.add(new FamilyMember(player, FamilyColor.BLACK));
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
        for(ImmediateLeaderCard iLC : immediateLeaderCardsPositionated){
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

    private void earnVictoryPointFromRewards(){
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

    private void loseVictoryPointFromMilitaryPoint(){
        counter.subtract(new Reward(RewardType.VICTORY_POINT, counter.getMilitaryPoint().getQuantity()));
    }

    private void loseVictoryPointFromBuildingCost(){
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

    private void loseVictoryPointFromVictoryPoint(){
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

    public FamilyMember getViewFamilyMember() throws TooMuchTimeException{
        FamilyColor familyColor = TerminalInput.chooseFamilyMemberColor();
        for(FamilyMember fM : familyMembers){
            if(fM.getColor() == familyColor){
                fM.setServantUsed(TerminalInput.askNumberOfServant());
                return fM;
            }
        }
        return null;
    }

    public void changeLeaderCardInReward() throws TooMuchTimeException{
        if(leaderCardsInHand.size() > 0){
            int index = TerminalInput.askWhichCardChange(leaderCardsInHand);
            counter.sum(leaderCardsInHand.get(index).getChangedRewards());
            counter.round();
            leaderCardsInHand.remove(index);
        }
    }

    public void activeImmediateLeaderCard() throws TooMuchTimeException{
        if(immediateLeaderCardsPositionated.size() > 0){
            int index = TerminalInput.askWhichCardActivate(immediateLeaderCardsPositionated);

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

    public List<ImmediateLeaderCard> getImmediateLeaderCardsPositionated() {
        return immediateLeaderCardsPositionated;
    }

    public List<PermanentLeaderCard> getPermanentLeaderCardsPositionated() {
        return permanentLeaderCardsPositionated;
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

    @Override
    public String toString (){
        String string = "";
        string += "username: " + player.getUsername() + "\n";
        string += "color: " + color + "\n";
        string += "\npersonalBonusTile:\n" + getPersonalBonusTile().toString() + "\n";
        string += "\nleader card: DA FARE!!!\n\n";
        string += "territorySpot: \n" + territorySpot.toString();
        string += "buildingSpot: \n" + buildingSpot.toString();
        string += "characterSpot: \n" + characterSpot.toString();
        string += "ventureSpot: \n" + ventureSpot.toString();
        string += "\n" + counter.toString() + "\n";
        string += "familyMembers:\n";
        for(FamilyMember fM : familyMembers){
            string += fM.toString() + "\n";
        }
        return string;
    }
}
