package it.polimi.ingsw.pc34.Model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by trill on 24/05/2017.
 */
public abstract class LeaderCard {
    private final String name;
    private final boolean inHand;
    private final Set<Reward> activationRewardCost;
    private final Map<CardType, Integer> activationCardCost;
    private final Set<Reward> changedRewards;

    public LeaderCard (String name, Set<Reward> activationRewardCost, Map<CardType, Integer> activationCardCost) {
        this.name = name;
        this.inHand = true;
        this.activationRewardCost = activationRewardCost;
        this.activationCardCost = activationCardCost;
        this.changedRewards = new HashSet<>();
        this.changedRewards.add(new Reward(RewardType.COUNCIL_PRIVILEGE, 1));
    }

    public String getName() {
        return name;
    }

    public boolean isInHand() {
        return inHand;
    }

    public Set<Reward> getActivationRewardCost() {
        return activationRewardCost;
    }

    public Map<CardType, Integer> getActivationCardCost() {
        return activationCardCost;
    }

    public Set<Reward> getChangedRewards(){
        return changedRewards;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (inHand ? 1 : 0);
        result = 31 * result + (activationRewardCost != null ? activationRewardCost.hashCode() : 0);
        result = 31 * result + (activationCardCost != null ? activationCardCost.hashCode() : 0);
        return result;
    }

    @Override
    public String toString () {
        String leaderCardString = "";

        leaderCardString += "Name: " + name + "\n";
        leaderCardString += "Hand: " + inHand + "\n";

        if (activationRewardCost != null) {
            leaderCardString += "Rewards Requirement: \n";
            for (Reward r: activationRewardCost) {
                leaderCardString += "  " + r.toString() + "\n";
            }
        }

        if (activationCardCost != null) {
            leaderCardString += "Cards Requirement: \n";
            for (Map.Entry <CardType, Integer> entry : activationCardCost.entrySet()) {
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
        if (activationRewardCost != null ? !activationRewardCost.equals(that.activationRewardCost) : that.activationRewardCost != null) return false;
        return activationCardCost != null ? !activationCardCost.equals(that.activationCardCost) : that.activationCardCost != null;
    }
}

