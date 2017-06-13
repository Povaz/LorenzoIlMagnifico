package it.polimi.ingsw.pc34.Model;

public enum CardType {
	TERRITORY, BUILDING, CHARACTER, VENTURE, ANY;

	public boolean sameType(ActionType action){
		switch(this){
			case TERRITORY:
				if(action == ActionType.TERRITORY_TOWER || action == ActionType.ANY_TOWER){
					return true;
				}
				return false;
			case BUILDING:
				if(action == ActionType.BUILDING_TOWER || action == ActionType.ANY_TOWER){
					return true;
				}
				return false;
			case CHARACTER:
				if(action == ActionType.CHARACTER_TOWER || action == ActionType.ANY_TOWER){
					return true;
				}
				return false;
			case VENTURE:
				if(action == ActionType.VENTURE_TOWER || action == ActionType.ANY_TOWER){
					return true;
				}
				return false;
			default:
				return false;
		}
	}
}
