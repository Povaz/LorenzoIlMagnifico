package it.polimi.ingsw.pc34.Model;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Povaz on 03/06/2017.
 */
public class ImmediateLeaderCard extends LeaderCard {
    private boolean activated;
    private final Set<Reward> reward;

    private final boolean changeColoredFamilyMamberValue;
    private final int newValueColoredFamilyMember;

    private final List<FamilyMember> actions;

    public ImmediateLeaderCard (String name, Set<Reward> activationRewardCost, Map<CardType, Integer> activationCardTypeRequirement,
                                Set<Reward> reward, boolean changeColoredFamilyMamberValue, int newValueColoredFamilyMember,
                                List<FamilyMember> actions){
        super(name, activationRewardCost, activationCardTypeRequirement);
        this.activated = false;
        this.reward = reward;
        this.changeColoredFamilyMamberValue = changeColoredFamilyMamberValue;
        this.newValueColoredFamilyMember = newValueColoredFamilyMember;
        this.actions = actions;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ImmediateLeaderCard that = (ImmediateLeaderCard) o;

        if (activated != that.activated) return false;
        if (reward != null ? !reward.equals(that.reward) : that.reward != null) return false;
        if (changeColoredFamilyMamberValue != that.changeColoredFamilyMamberValue) return false;
        if (newValueColoredFamilyMember != that.newValueColoredFamilyMember) return false;
        return actions != null ? !actions.equals(that.actions) : that.actions != null;
    }

    @Override
    public String toString () {
        String immediateLeaderCardString = super.toString();
        immediateLeaderCardString += "Activated: " + activated + "\n";
        if (reward != null) {
            immediateLeaderCardString += "Reward: \n";
            for (Reward r: reward) {
                immediateLeaderCardString += "  " + r.toString() + "\n";
            }
        }
        if (changeColoredFamilyMamberValue) {
            immediateLeaderCardString += "Change Color Family Member Value: " + changeColoredFamilyMamberValue + "\n";
            immediateLeaderCardString += "New Color Family Member Value: " + newValueColoredFamilyMember + "\n";
        }
        if (actions != null) {
            immediateLeaderCardString += "Actions: \n";
            for (FamilyMember g: actions) {
                immediateLeaderCardString += "  " + g.toString() + "\n";
            }
        }
        return immediateLeaderCardString;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (activated ? 1 : 0);
        result = 31 * result + (reward != null ? reward.hashCode() : 0);
        result = 31 * result + (changeColoredFamilyMamberValue ? 1 : 0);
        result = 31 * result + newValueColoredFamilyMember;
        result = 31 * result + (actions != null ? actions.hashCode() : 0);
        return result;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Set<Reward> getReward() {
        return reward;
    }

    public boolean isChangeColoredFamilyMamberValue() {
        return changeColoredFamilyMamberValue;
    }

    public int getNewValueColoredFamilyMember() {
        return newValueColoredFamilyMember;
    }

    public List<FamilyMember> getActions() {
        return actions;
    }
}