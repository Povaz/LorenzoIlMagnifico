package it.polimi.ingsw.pc34.Model;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Povaz on 03/06/2017.
 */
public class PermanentLeaderCard extends LeaderCard {
    private final boolean copyOtherCard;

    private final int neutralFamilyMemberModifier;
    private final int coloredFamilyMemberModifier;
    private final boolean doubleFastRewardDevelopmentCard;
    private final boolean placeInBusyActionSpot;

    private final boolean permanentDice;
    private final int permanentDiceValue;

    private final boolean notSatisfyMilitaryPointForTerritory;
    private final boolean notPayTollBusyTower;

    private final List<Reward> bonusRewardChurchSupport;
    private Map<CardType, List<List<Reward>>> costDiscountDevelopmentCard;

    public PermanentLeaderCard(String name, Set<Reward> activationRewardCost, Map<CardType, Integer> activationCardCost,
                                int neutralFamilyMemberModifier, int coloredFamilyMemberModifier, boolean doubleFastRewardDevelopmentCard,
                                boolean placeInBusyActionSpot, List<Reward> bonusRewardChurchSupport, boolean permanentDice,
                                int permanentDiceValue, Map<CardType, List<List<Reward>>> costDiscountDevelopmentCard,
                                boolean notSatisfyMilitaryPointForTerritory, boolean notPayTollBusyTower, boolean copyOtherCard){
        super (name, activationRewardCost, activationCardCost);
        this.neutralFamilyMemberModifier = neutralFamilyMemberModifier;
        this.coloredFamilyMemberModifier = coloredFamilyMemberModifier;
        this.doubleFastRewardDevelopmentCard = doubleFastRewardDevelopmentCard;
        this.placeInBusyActionSpot = placeInBusyActionSpot;
        this.bonusRewardChurchSupport = bonusRewardChurchSupport;
        this.permanentDice = permanentDice;
        this.permanentDiceValue = permanentDiceValue;
        this.costDiscountDevelopmentCard = costDiscountDevelopmentCard;
        this.notSatisfyMilitaryPointForTerritory = notSatisfyMilitaryPointForTerritory;
        this.notPayTollBusyTower = notPayTollBusyTower;
        this.copyOtherCard = copyOtherCard;
    }

    @Override
    public String toString () {
        String permanentLeaderCardString = super.toString();

        if (neutralFamilyMemberModifier == 0) {
            permanentLeaderCardString += "Neutral Family Member Modifier: " + neutralFamilyMemberModifier + "\n";
        }

        if (coloredFamilyMemberModifier == 0) {
            permanentLeaderCardString += "Colored Family Member Modifier: " + coloredFamilyMemberModifier + "\n";
        }

        if (doubleFastRewardDevelopmentCard) {
            permanentLeaderCardString += "Double Fast Reward Development Card: " + doubleFastRewardDevelopmentCard + "\n";
        }

        if (placeInBusyActionSpot) {
            permanentLeaderCardString += "Place in busy Action Spot: " + placeInBusyActionSpot + "\n";
        }

        if (bonusRewardChurchSupport != null) {
            permanentLeaderCardString += "Bonus Reward Church Support: \n";
            for (Reward r: bonusRewardChurchSupport) {
                permanentLeaderCardString += "  Rewards: " + r.toString() + "\n";
            }
        }

        if (permanentDice) {
            permanentLeaderCardString += "Permanent Dice Value: " + permanentDiceValue + "\n";
        }

        /* TODO TODOTODO risistema
        if (costDiscountDevelopmentCard != null) {
            permanentLeaderCardString += "Cost Discount Development Card: \n";
            for (Reward r: costDiscountDevelopmentCard){
                permanentLeaderCardString += "  Discount: " + r.toString() + "\n";
            }
        }*/

        if (notSatisfyMilitaryPointForTerritory) {
            permanentLeaderCardString += "Not Satisfy Military Point for Territory: " + notSatisfyMilitaryPointForTerritory + "\n";
        }

        if (notPayTollBusyTower) {
            permanentLeaderCardString += "Not Pay toll Busy Tower: " + notPayTollBusyTower + "\n";
        }

        if (copyOtherCard) {
            permanentLeaderCardString += "Copy other Card: " + copyOtherCard + "\n";
        }

        return permanentLeaderCardString;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PermanentLeaderCard that = (PermanentLeaderCard) o;

        if (copyOtherCard != that.copyOtherCard) return false;
        if (neutralFamilyMemberModifier != that.neutralFamilyMemberModifier) return false;
        if (coloredFamilyMemberModifier != that.coloredFamilyMemberModifier) return false;
        if (doubleFastRewardDevelopmentCard != that.doubleFastRewardDevelopmentCard) return false;
        if (placeInBusyActionSpot != that.placeInBusyActionSpot) return false;
        if (permanentDice != that.permanentDice) return false;
        if (permanentDiceValue != that.permanentDiceValue) return false;
        if (notSatisfyMilitaryPointForTerritory != that.notSatisfyMilitaryPointForTerritory) return false;
        if (notPayTollBusyTower != that.notPayTollBusyTower) return false;
        if (!bonusRewardChurchSupport.equals(that.bonusRewardChurchSupport)) return false;
        return costDiscountDevelopmentCard.equals(that.costDiscountDevelopmentCard);
    }

    @Override
    public int hashCode(){
        int result = super.hashCode();
        result = 31 * result + (copyOtherCard ? 1 : 0);
        result = 31 * result + neutralFamilyMemberModifier;
        result = 31 * result + coloredFamilyMemberModifier;
        result = 31 * result + (doubleFastRewardDevelopmentCard ? 1 : 0);
        result = 31 * result + (placeInBusyActionSpot ? 1 : 0);
        result = 31 * result + (permanentDice ? 1 : 0);
        result = 31 * result + permanentDiceValue;
        result = 31 * result + (notSatisfyMilitaryPointForTerritory ? 1 : 0);
        result = 31 * result + (notPayTollBusyTower ? 1 : 0);
        result = 31 * result + bonusRewardChurchSupport.hashCode();
        result = 31 * result + costDiscountDevelopmentCard.hashCode();
        return result;
    }

    public boolean isCopyOtherCard() {
        return copyOtherCard;
    }

    public int getNeutralFamilyMemberModifier() {
        return neutralFamilyMemberModifier;
    }

    public int getColoredFamilyMemberModifier() {
        return coloredFamilyMemberModifier;
    }

    public boolean isDoubleFastRewardDevelopmentCard() {
        return doubleFastRewardDevelopmentCard;
    }

    public boolean isPlaceInBusyActionSpot() {
        return placeInBusyActionSpot;
    }

    public boolean isPermanentDice() {
        return permanentDice;
    }

    public int getPermanentDiceValue() {
        return permanentDiceValue;
    }

    public boolean isNotSatisfyMilitaryPointForTerritory() {
        return notSatisfyMilitaryPointForTerritory;
    }

    public boolean isNotPayTollBusyTower() {
        return notPayTollBusyTower;
    }

    public List<Reward> getBonusRewardChurchSupport() {
        return bonusRewardChurchSupport;
    }

    public Map<CardType, List<List<Reward>>> getCostDiscountDevelopmentCard() {
        return costDiscountDevelopmentCard;
    }
}
