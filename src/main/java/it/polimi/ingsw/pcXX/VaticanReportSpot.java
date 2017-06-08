package it.polimi.ingsw.pcXX;

import java.util.ArrayList;
import java.util.List;

public class VaticanReportSpot {
	private final List<Player> reported;
	private final VaticanReportCard vaticanReportCard;

	public VaticanReportSpot(VaticanReportCard vaticanReportCard){
		this.reported = new ArrayList<>();
		this.vaticanReportCard = vaticanReportCard;
	}
}
