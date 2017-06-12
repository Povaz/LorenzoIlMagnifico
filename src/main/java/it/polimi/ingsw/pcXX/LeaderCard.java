package it.polimi.ingsw.pcXX;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by trill on 24/05/2017.
 */
public abstract class LeaderCard {
    private final String name;
    private final boolean inHand;
    private final Set<Reward> activationRewardRequirement;
    private final Map<CardType, Integer> activationCardTypeRequirement;
    private final Set<Reward> changedRewards;

    public LeaderCard (String name, Set<Reward> activationRewardRequirement, Map<CardType, Integer> activationCardTypeRequirement) {
        this.name = name;
        this.inHand = true;
        this.activationRewardRequirement = activationRewardRequirement;
        this.activationCardTypeRequirement = activationCardTypeRequirement;
        this.changedRewards = new HashSet<>();
        this.changedRewards.add(new Reward(RewardType.COUNCIL_PRIVILEGE, 1));
    }

    public String getName() {
        return name;
    }

    public boolean isInHand() {
        return inHand;
    }

    public Set<Reward> getActivationRewardRequirement() {
        return activationRewardRequirement;
    }

    public Map<CardType, Integer> getActivationCardTypeRequirement() {
        return activationCardTypeRequirement;
    }

    public Set<Reward> getChangedRewards() {
        return changedRewards;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (inHand ? 1 : 0);
        result = 31 * result + (activationRewardRequirement != null ? activationRewardRequirement.hashCode() : 0);
        result = 31 * result + (activationCardTypeRequirement != null ? activationCardTypeRequirement.hashCode() : 0);
        return result;
    }

    @Override
    public String toString () {
        String leaderCardString = "";

        leaderCardString += "Name: " + name + "\n";
        leaderCardString += "Hand: " + inHand + "\n";

        if (activationRewardRequirement != null) {
            leaderCardString += "Rewards Requirement: \n";
            for (Reward r: activationRewardRequirement) {
                leaderCardString += "  " + r.toString() + "\n";
            }
        }

        if (activationCardTypeRequirement != null) {
            leaderCardString += "Cards Requirement: \n";
            for (Map.Entry <CardType, Integer> entry : activationCardTypeRequirement.entrySet()) {
                leaderCardString += "   CardType: " + entry.getKey() + "\n" + " Count: " + entry.getValue() + "\n";
            }
        }
        return leaderCardString;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        LeaderCard that = (LeaderCard) o;

        if (name != that.name) return false;
        if (inHand != that.inHand) return false;
        if (activationRewardRequirement != null ? !activationRewardRequirement.equals(that.activationRewardRequirement) : that.activationRewardRequirement != null) return false;
        return activationCardTypeRequirement != null ? !activationCardTypeRequirement.equals(that.activationCardTypeRequirement) : that.activationCardTypeRequirement != null;
    }
}

