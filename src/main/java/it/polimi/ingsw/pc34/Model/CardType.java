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

	public ActionType toActionType(){
		switch(this){
			case TERRITORY:
				return ActionType.TERRITORY_TOWER;
			case BUILDING:
				return ActionType.BUILDING_TOWER;
			case CHARACTER:
				return ActionType.CHARACTER_TOWER;
			case VENTURE:
				return ActionType.VENTURE_TOWER;
			default:
				return null;
		}
	}
}
