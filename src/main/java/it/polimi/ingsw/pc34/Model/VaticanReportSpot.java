package it.polimi.ingsw.pc34.Model;

import it.polimi.ingsw.pc34.JSONUtility;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VaticanReportSpot {
	Logger LOGGER = Logger.getLogger(VaticanReportSpot.class.getName());

	private final List<Player> reported;
	private final VaticanReportCard vaticanReportCard;
	private final Reward faithPointNeeded;
	private final boolean last;

	public VaticanReportSpot(VaticanReportCard vaticanReportCard, Reward faithPointNeeded, boolean last){
		this.reported = new ArrayList<>();
		this.vaticanReportCard = vaticanReportCard;
		this.faithPointNeeded = faithPointNeeded;
		this.last = last;
	}

	public Set<Reward> calculateVictoryPointFromFaithPoint(Reward faithPoint){
		try{
			return JSONUtility.getFaithRoutePoint(faithPoint);
		} catch(JSONException e){
			Set<Reward> toReturn = new HashSet<>();
			toReturn.add(standardRewardsFromFaithPoints(faithPoint.getQuantity()));
			LOGGER.log(Level.WARNING, "Config.json: Wrong format", e);
			return toReturn;
		} catch(IOException e){
			Set<Reward> toReturn = new HashSet<>();
			toReturn.add(standardRewardsFromFaithPoints(faithPoint.getQuantity()));
			LOGGER.log(Level.WARNING, "Config.json: Incorrect path", e);
			return toReturn;
		}
	}

	private Reward standardRewardsFromFaithPoints(int faith){
		if(faith < 0){
			return new Reward(RewardType.VICTORY_POINT, 0);
		}
		else if(faith <= 5){
			return new Reward(RewardType.VICTORY_POINT, faith);
		}
		else if(faith <= 12){
			return new Reward(RewardType.VICTORY_POINT, 2 * faith - 5);
		}
		else if(faith <= 13){
			return new Reward(RewardType.VICTORY_POINT, 3 * faith - 17);
		}
		else{
			return new Reward(RewardType.VICTORY_POINT, 30);
		}
	}

	public void report(Player player){
		reported.add(player);
		player.getPlayerBoard().getModifier().update(vaticanReportCard);
	}

	@Override
	public String toString(){
		StringBuilder bld = new StringBuilder();
		if(faithPointNeeded != null){
			bld.append("  Faith point needed: " + faithPointNeeded.toString() + ";\n");
		}
		if(vaticanReportCard != null){
			bld.append("  Vatican report card:\n" + vaticanReportCard.toString());
		}
		if(!reported.isEmpty()){
			bld.append("  Players reported:\n");
			for(Player p : reported){
				bld.append("    " + p.getUsername() + p.getColor() + "\n");
			}
		}

		return bld.toString();
	}

	public List<Player> getReported() {
		return reported;
	}

	public VaticanReportCard getVaticanReportCard() {
		return vaticanReportCard;
	}

	public Reward getFaithPointNeeded() {
		return faithPointNeeded;
	}

	public boolean isLast() {
		return last;
	}
}
