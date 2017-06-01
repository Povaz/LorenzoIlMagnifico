package it.polimi.ingsw.pcXX;

public class Floor extends ActionSpot{
	private DevelopmentCard card;
	private final Reward reward;

	public Floor(int value, Reward reward){
		super(true, false, value);
		this.card = null;
		this.reward = reward;
	}

	public DevelopmentCard getCard() {
		return card;
	}

	public void setCard(DevelopmentCard card) {
		this.card = card;
	}

	public Reward getReward() {
		return reward;
	}

	public void reinitialize(){
		super.reinitialize();
		card = null;
	}
}
