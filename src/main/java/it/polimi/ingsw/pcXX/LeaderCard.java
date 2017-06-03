package it.polimi.ingsw.pcXX;

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


    public LeaderCard (String name, boolean inHand, Set<Reward> activationRewardRequirement, Map<CardType, Integer> activationCardTypeRequirement) {
        this.name = name;
        this.inHand = inHand;
        this.activationRewardRequirement = activationRewardRequirement;
        this.activationCardTypeRequirement = activationCardTypeRequirement;
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
                leaderCardString += "CardType: " + entry.getKey() + "Count: " + entry.getValue();
            }
        }
        return leaderCardString;
    }
}

