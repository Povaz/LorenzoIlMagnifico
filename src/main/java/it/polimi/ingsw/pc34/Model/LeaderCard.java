package it.polimi.ingsw.pc34.Model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by trill on 24/05/2017.
 */
public abstract class LeaderCard {
    private final String name;
    private final String path;
    private final Set<Reward> activationRewardCost;
    private final Map<CardType, Integer> activationCardCost;
    private final Set<Reward> changedRewards;

    public LeaderCard (String name, String path, Set<Reward> activationRewardCost, Map<CardType, Integer> activationCardCost) {
        this.name = name;
        this.path = path;
        this.activationRewardCost = activationRewardCost;
        this.activationCardCost = activationCardCost;
        this.changedRewards = new HashSet<>();
        this.changedRewards.add(new Reward(RewardType.COUNCIL_PRIVILEGE, 1));
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeaderCard that = (LeaderCard) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (activationRewardCost != null ? !activationRewardCost.equals(that.activationRewardCost) : that.activationRewardCost != null)
            return false;
        if (activationCardCost != null ? !activationCardCost.equals(that.activationCardCost) : that.activationCardCost != null)
            return false;
        return changedRewards != null ? changedRewards.equals(that.changedRewards) : that.changedRewards == null;
    }

    @Override
    public int hashCode(){
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (activationRewardCost != null ? activationRewardCost.hashCode() : 0);
        result = 31 * result + (activationCardCost != null ? activationCardCost.hashCode() : 0);
        result = 31 * result + (changedRewards != null ? changedRewards.hashCode() : 0);
        return result;
    }

    @Override
    public String toString () {
        StringBuilder bld = new StringBuilder();

        bld.append("  Name: " + name + "\n");

        if(activationRewardCost != null){
            bld.append("  Rewards Requirement:  ");
            for(Reward r: activationRewardCost){
                bld.append(r.toString() + "; ");
            }
            bld.append("\n");
        }

        if(activationCardCost != null){
            bld.append("  Cards Requirement:  ");
            for(CardType cT : activationCardCost.keySet()){
                bld.append(activationCardCost.get(cT) + " " + cT.toString() + "; ");
            }
            bld.append("\n");
        }
        return bld.toString();
    }

    public String getPath(){
        return path;
    }
}

