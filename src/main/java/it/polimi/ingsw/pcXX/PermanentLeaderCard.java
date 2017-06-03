package it.polimi.ingsw.pcXX;

import java.util.Map;
import java.util.Set;

/**
 * Created by Povaz on 03/06/2017.
 */
public class PermanentLeaderCard extends LeaderCard {
    private final int neutralFamilyMemberModifier;
    private final int coloredFamilyMemberModifier;
    private final boolean doubleFastRewardDevelopmentCard;
    private final boolean placeInBusyActionSpot;
    private final Set<Reward> bonusRewardChurchSupport;
    private final boolean permanentDice;
    private final int permanentDiceValue;
    private final Set<Reward> costDiscountDevelopmentCard;
    private final boolean notSatisfyMilitaryPointForTerritory;
    private final boolean notPayTollBusyTower;
    private final boolean copyOtherCard;

    public PermanentLeaderCard (String name, boolean inHand, Set<Reward> activationRewardRequirement, Map<CardType, Integer> activationCardTypeRequirement,
                                int neutralFamilyMemberModifier, int coloredFamilyMemberModifier, boolean doubleFastRewardDevelopmentCard,
                                boolean placeInBusyActionSpot, Set<Reward> bonusRewardChurchSupport, boolean permanentDice,
                                int permanentDiceValue, Set<Reward> costDiscountDevelopmentCard, boolean notSatisfyMilitaryPointForTerritory,
                                boolean notPayTollBusyTower, boolean copyOtherCard) {
        super (name, inHand, activationRewardRequirement, activationCardTypeRequirement);
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
                permanentLeaderCardString += "Rewards: " + r.toString();
            }
        }

        if (permanentDice) {
            permanentLeaderCardString += "Permanent Dice Value: " + permanentDiceValue + "\n";
        }

        if (costDiscountDevelopmentCard != null) {
            permanentLeaderCardString += "Cost Discount Development Card: \n";
            for (Reward r: costDiscountDevelopmentCard) {
                permanentLeaderCardString += "Discount: " + r.toString();
            }
        }

        if (notSatisfyMilitaryPointForTerritory) {
            permanentLeaderCardString += "Not Satisfy Military Point for Territory: " + notSatisfyMilitaryPointForTerritory;
        }

        if (notPayTollBusyTower) {
            permanentLeaderCardString += "Not Pay toll Busy Tower: " + notPayTollBusyTower;
        }

        if (copyOtherCard) {
            permanentLeaderCardString += "Copy other Card: " + copyOtherCard;
        }

        return permanentLeaderCardString;
    }

    @Override
    public int hashCode () {
        int result = super.hashCode();
        result = 31 * result + neutralFamilyMemberModifier;
        result = 31 * result + coloredFamilyMemberModifier;
        result = 31 * result + (doubleFastRewardDevelopmentCard ? 1 : 0);
        result = 31 * result + (placeInBusyActionSpot ? 1 : 0);
        result = 31 * result + (bonusRewardChurchSupport != null ? bonusRewardChurchSupport.hashCode() : 0);
        result = 31 * result + (permanentDice ? 1 : 0);
        result = 31 * result + permanentDiceValue;
        result = 31 * result + (costDiscountDevelopmentCard != null ? costDiscountDevelopmentCard.hashCode() : 0);
        result = 31 * result + (notSatisfyMilitaryPointForTerritory ? 1 : 0);
        result = 31 * result + (notPayTollBusyTower ? 1 : 0);
        result = 31 * result + (copyOtherCard ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PermanentLeaderCard that = (PermanentLeaderCard) o;

        if (neutralFamilyMemberModifier != that.neutralFamilyMemberModifier) return false;
        if (coloredFamilyMemberModifier != that.coloredFamilyMemberModifier) return false;
        if (doubleFastRewardDevelopmentCard != that.doubleFastRewardDevelopmentCard) return false;
        if (placeInBusyActionSpot != that.placeInBusyActionSpot) return false;
        if (bonusRewardChurchSupport != null ? !bonusRewardChurchSupport.equals(that.bonusRewardChurchSupport) : that.bonusRewardChurchSupport != null) return false;
        if (permanentDice != that.permanentDice) return false;
        if (permanentDiceValue != that.permanentDiceValue) return false;
        if (costDiscountDevelopmentCard != null ? !costDiscountDevelopmentCard.equals(that.costDiscountDevelopmentCard) : that.costDiscountDevelopmentCard != null) return false;
        if (notSatisfyMilitaryPointForTerritory != that.notSatisfyMilitaryPointForTerritory) return false;
        if (notPayTollBusyTower != that.notPayTollBusyTower) return false;
        return copyOtherCard != that.copyOtherCard;
    }
}
