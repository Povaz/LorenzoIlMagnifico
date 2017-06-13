package it.polimi.ingsw.pc34.Model;

public enum PlayerColor {
	YELLOW, RED, BLUE, GREEN, PURPLE;

	public static PlayerColor fromInt(int number){
		switch(number){
			case 1:
				return YELLOW;
			case 2:
				return RED;
			case 3:
				return BLUE;
			case 4:
				return GREEN;
			case 5:
				return PURPLE;
		}
		throw new IllegalArgumentException();
	}
}
