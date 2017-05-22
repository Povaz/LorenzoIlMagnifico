package it.polimi.ingsw.pcXX;

public class Floor extends ActionSpot{
	private DevelopmentCard card;
	private final Reward reward;

	public Floor(int value, Reward reward){
		super(true, false, value);
		this.card = null;
		this.reward = reward;
	}
}
