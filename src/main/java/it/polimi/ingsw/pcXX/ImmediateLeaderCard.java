package it.polimi.ingsw.pcXX;

import java.util.Map;
import java.util.Set;

/**
 * Created by Povaz on 03/06/2017.
 */
public class ImmediateLeaderCard extends LeaderCard {
    private /*final*/ boolean activated;
    private /*final*/ Set<Reward> reward;
    private /*final*/ boolean changeColoredFamilyMamberValue;
    private /*final*/ int newValueColoredFamilyMember;
    private /*final*/ Set<GhostFamilyMember> actions;

    private ImmediateLeaderCard(){
        super(null, null, null);
    }

    /*public ImmediateLeaderCard (String name, boolean inHand, Set<Reward> activationRewardRequirement, Map<CardType, Integer> activationCardTypeRequirement,
                                boolean activated, Set<Reward> reward, boolean changeColoredFamilyMamberValue, int newValueColoredFamilyMember,
                                Set<GhostFamilyMember> actions) {
        super(name, inHand, activationRewardRequirement, activationCardTypeRequirement);
        this.activated = activated;
        this.reward = reward;
        this.changeColoredFamilyMamberValue = changeColoredFamilyMamberValue;
        this.newValueColoredFamilyMember = newValueColoredFamilyMember;
        this.actions = actions;
    }*/

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
            for (GhostFamilyMember g: actions) {
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

}