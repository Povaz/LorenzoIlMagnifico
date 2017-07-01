package it.polimi.ingsw.pc34.Model;

import java.util.List;
import java.util.Set;

public abstract class DevelopmentCard {
	private final String name;
	private final String path;
	private final CardType type;
	private final int period;
	private final Set<Reward> costs;
	private final Set<Reward> fastRewards;
	private final List<FamilyMember> actions;

	public DevelopmentCard(String name, String path, CardType type, int period, Set<Reward> costs, Set<Reward> fastRewards,
						   List<FamilyMember> actions){
		this.name = name;
		this.path = path;
		this.type = type;
		this.period = period;
		this.costs = costs;
		this.fastRewards = fastRewards;
		this.actions = actions;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DevelopmentCard that = (DevelopmentCard) o;

		if (period != that.period) return false;
		if (!name.equals(that.name)) return false;
		if (type != that.type) return false;
		if (costs != null ? !costs.equals(that.costs) : that.costs != null) return false;
		if (fastRewards != null ? !fastRewards.equals(that.fastRewards) : that.fastRewards != null) return false;
		return actions != null ? actions.equals(that.actions) : that.actions == null;
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + type.hashCode();
		result = 31 * result + period;
		result = 31 * result + (costs != null ? costs.hashCode() : 0);
		result = 31 * result + (fastRewards != null ? fastRewards.hashCode() : 0);
		result = 31 * result + (actions != null ? actions.hashCode() : 0);
		return result;
	}

	@Override
	public String toString(){
		StringBuilder bld = new StringBuilder();

		bld.append("    Type: " + type.toString() + "\n");
		bld.append("    Name: " + name + "\n");
		bld.append("    Period: " + period + "\n");

		if(costs != null){
			bld.append("    Costs: ");
			for(Reward r: costs){
				bld.append(r.toString() + ";  ");
			}
			bld.append("\n");
		}
		if(fastRewards != null){
			bld.append("    Fast rewards: ");
			for(Reward r: fastRewards){
				bld.append(r.toString() + ";  ");
			}
			bld.append("\n");
		}
		if(actions != null){
			bld.append("    Actions:\n");
			for (FamilyMember g : actions){
				bld.append(g.toString() + "\n");
			}
		}
		return bld.toString();
	}

	public String getName(){
		return name;
	}

	public CardType getType(){
		return type;
	}

	public int getPeriod(){
		return period;
	}

	public Set<Reward> getCosts(){
		return costs;
	}

	public Set<Reward> getFastRewards(){
		return fastRewards;
	}

	public List<FamilyMember> getActions(){
		return actions;
	}

	public String getPath(){
		return path;
	}
}
