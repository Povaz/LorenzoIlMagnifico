package it.polimi.ingsw.pc34.Socket;

import java.rmi.RemoteException;

import it.polimi.ingsw.pc34.Controller.ActionInput;
import it.polimi.ingsw.pc34.Model.ActionType;
import it.polimi.ingsw.pc34.Model.FamilyColor;
import it.polimi.ingsw.pc34.Model.GameController;
import it.polimi.ingsw.pc34.RMI.ActionInputCreated;
import it.polimi.ingsw.pc34.RMI.ActionInputProducer;
import it.polimi.ingsw.pc34.RMI.FamilyColorCreated;
import it.polimi.ingsw.pc34.RMI.FamilyColorProducer;
import it.polimi.ingsw.pc34.RMI.IntegerCreated;
import it.polimi.ingsw.pc34.RMI.IntegerProducer;
import it.polimi.ingsw.pc34.RMI.UserLogin;

public class GameFlow {
	
	private boolean start;
	private boolean action;
	private boolean familyMember;
	private boolean servants;
	private boolean drawLeader;
	private boolean activateLeader;
	private String actionSpot;
	
	private int playersNumber;
	private ActionInputCreated actionInputCreated;
    private ActionInputProducer actionInputProducer;
    private IntegerCreated integerCreated;
    private IntegerProducer integerProducer;
    private FamilyColorCreated familyColorCreated;
    private FamilyColorProducer familyColorProducer;
	private GameController gameController;
	ActionInput actionInput;
	
	public GameFlow() {
		start = true;
		action = false;
		drawLeader = false;
		activateLeader = false;
		familyMember = false;
	}
	
	public void setController(GameController gameController) {
		this.gameController = gameController;
	}
	
	public void setPlayerNumber(int playersNumber){
		this.playersNumber = playersNumber;
	}
	
	public void askNumber(int min, int max) {
        integerCreated = new IntegerCreated();
        integerProducer = new IntegerProducer(integerCreated);
        gameController.setIntegerCreated(integerCreated);
    }
	
	public void askAction(){
        actionInputCreated = new ActionInputCreated();
        gameController.setActionInputCreated(actionInputCreated);
        actionInputProducer = new ActionInputProducer(actionInputCreated);
    }
	
	public void askFamilyColor(){
        familyColorCreated = new FamilyColorCreated();
        familyColorProducer = new FamilyColorProducer(familyColorCreated);
        gameController.setFamilyColorCreated(familyColorCreated);
    }
	
	public void askNumberMinMax(int min, int max) throws RemoteException {
        integerCreated = new IntegerCreated();
        integerProducer = new IntegerProducer(integerCreated, min, max);
        gameController.setIntegerCreated(integerCreated);
    }
	
	public boolean checkNumber (int min, int max, String decision){
        int dec;
        try{
        	dec = Integer.parseInt(decision);
        }
		catch(NumberFormatException nfe){
			return false;
		}
        
		if (dec >= min && dec <= max) {
            return true;
        }
        else {
            return false;
        }
	}
	
	public void createNewAction (int choose){
        integerProducer.setChoose(choose);
        integerProducer.start();
        /*
        ActionInput actionInput = this.chooseActionInput(playerNumber, userLogin);
        actionInputProducer.setActionInput(actionInput);
        actionInputProducer.start();

        FamilyColor familyColor = this.chooseFamilyColor(userLogin);
        familyColorProducer.setFamilyColor(familyColor);
        familyColorProducer.start();

        String m = "How many Servants do you want to use?";
        int servant = this.chooseSpot(integerProducer.getMin(), integerProducer.getMax(), userLogin, m);
        integerProducer.setChoose(servant);
        integerProducer.start();
        */
    }
	
	public String flow (String asked){
		if (asked.equals("/skip")){
			
			return "/skip non ancora implementato";
		}
		else if (asked.equals("/chat")){
			
			return "/chat non ancora implementato";
		}
		else if (asked.equals("/stampinfo")){
			
			return "/stampinfo non ancora implementato";
		}
		else{
			if(start) {
				if(asked.equals("/action")){
					start = false;
					action = true;
					integerProducer.setChoose(0);
			        integerProducer.start();
					return "Which ActionSpot do you choose? Choose a number : 1. TERRITORY TOWER 2. BUILDING TOWER 3. CHARACTER TOWER 4. VENTURE TOWER 5. HARVEST 6. PRODUCE 7. MARKET 8. COUNCILPALACE";
				}
			}
			else if(action){
				if(actionSpot == null && checkNumber(1, 8, asked)){
					actionInput = new ActionInput();
					actionSpot = asked;
					switch(actionSpot) {
						case "1":
							actionInput.setActionType(ActionType.TERRITORY_TOWER);
							return "Which card? From 0 to 3";
						case "2":
							actionInput.setActionType(ActionType.BUILDING_TOWER);
							return "Which card? From 0 to 3";
						case "3":
							actionInput.setActionType(ActionType.CHARACTER_TOWER);
							return "Which card? From 0 to 3";
						case "4":
							actionInput.setActionType(ActionType.VENTURE_TOWER);
							return "Which card? From 0 to 3";
						case "5":
							actionInput.setActionType(ActionType.HARVEST);
							return "Which spot? 0 or 1";
						case "6":
							actionInput.setActionType(ActionType.PRODUCE);
							return "Which spot? 0 or 1";
						case "7":
							actionInput.setActionType(ActionType.MARKET);
							return "Which Spot? 0.COIN(5)  1.SERVANT(5)   2.COIN(2) & MILITARY_POINT(3) 3.COUNCILPRIVILEGE(2)";
						case "8":
							actionInput.setActionType(ActionType.COUNCIL_PALACE);
							actionInput.setSpot(0);
							actionInputProducer.setActionInput(actionInput);
							actionInputProducer.start();
							return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
					}
				}
				else if(actionSpot!=null){
					switch(actionSpot){ 
						case "1":
							if(checkNumber(0, 3, asked)){
								actionInput.setSpot(Integer.parseInt(asked));
								actionInputProducer.setActionInput(actionInput);
								action = false;
								familyMember = true;
								actionInputProducer.start();
								return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
							}
						case "2":
							if(checkNumber(0, 3, asked)){
								actionInput.setSpot(Integer.parseInt(asked));
								actionInputProducer.setActionInput(actionInput);
								action = false;
								familyMember = true;
								actionInputProducer.start();
								return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
							}
						case "3":
							if(checkNumber(0, 3, asked)){
								actionInput.setSpot(Integer.parseInt(asked));
								actionInputProducer.setActionInput(actionInput);
								action = false;
								familyMember = true;
								actionInputProducer.start();
								return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
							}
						case "4":
							if(checkNumber(0, 3, asked)){
								actionInput.setSpot(Integer.parseInt(asked));
								actionInputProducer.setActionInput(actionInput);
								action = false;
								familyMember = true;
								actionInputProducer.start();
								return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
							}
						case "5":
							if (playersNumber > 2 && checkNumber(0, 1, asked)) {
								actionInput.setSpot(Integer.parseInt(asked));
								actionInputProducer.setActionInput(actionInput);
								action = false;
								familyMember = true;
								actionInputProducer.start();
								return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
		                    } else if(playersNumber == 2 && checkNumber(0, 0, asked)){
		                        actionInput.setSpot(0);
		                        actionInputProducer.setActionInput(actionInput);
								action = false;
								familyMember = true;
								actionInputProducer.start();
								return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
		                    }
		                    else{
		                    	return "retry";
		                    }
						case "6":
							if (playersNumber > 2 && checkNumber(0, 1, asked)) {
								actionInput.setSpot(Integer.parseInt(asked));
								actionInputProducer.setActionInput(actionInput);
								action = false;
								familyMember = true;
								actionInputProducer.start();
								return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
		                    } else if(playersNumber == 2 && checkNumber(0, 0, asked)){
		                        actionInput.setSpot(0);
		                        actionInputProducer.setActionInput(actionInput);
		                        action = false;
								familyMember = true;
								actionInputProducer.start();
								return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
		                    }
		                    else{
		                    	return "retry";
		                    }
						case "7":
							if (playersNumber > 3 && checkNumber(0, 3, asked)) {
								actionInput.setSpot(Integer.parseInt(asked));
								actionInputProducer.setActionInput(actionInput);
								action = false;
								familyMember = true;
								actionInputProducer.start();
								return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
		                    } else if(playersNumber <= 3 && checkNumber(0, 1, asked)){
		                        actionInput.setSpot(0);
		                        actionInputProducer.setActionInput(actionInput);
		                        action = false;
								familyMember = true;
								actionInputProducer.start();
								return "Which FamilyMember do you choose? 1. " + FamilyColor.WHITE + "  " + "2. " + FamilyColor.BLACK + "  " + "3. " + FamilyColor.ORANGE + "  " + "4. " + FamilyColor.NEUTRAL;
		                    }
		                    else{
		                    	return "retry";
		                    }
					}
				}
				else{
					return "Retry";
				}
				
			}
			else if(familyMember){
				if (checkNumber(0, 4, asked)){
					switch (asked){
						case "0" :
							familyColorProducer.setFamilyColor(FamilyColor.WHITE);
							familyColorProducer.start();
							familyMember = false;
							servants = true;
							return "How many Servants do you want to use?";
						case "1" :
							familyColorProducer.setFamilyColor(FamilyColor.WHITE);
							familyColorProducer.start();
							familyMember = false;
							servants = true;
							return "How many Servants do you want to use?";
						case "2" :
							familyColorProducer.setFamilyColor(FamilyColor.WHITE);
							familyColorProducer.start();
							familyMember = false;
							servants = true;
							return "How many Servants do you want to use?";
						case "3" :
							familyColorProducer.setFamilyColor(FamilyColor.WHITE);
							familyColorProducer.start();
							familyMember = false;
							servants = true;
							return "How many Servants do you want to use?";
						case "4" :
							familyColorProducer.setFamilyColor(FamilyColor.WHITE);
							familyColorProducer.start();
							familyMember = false;
							servants = true;
							return "How many Servants do you want to use?";
					}
				}
				else{
					return "retry";
				}
			}
			else if(servants){
				if (checkNumber(integerProducer.getMin(), integerProducer.getMax(), asked)){
					integerProducer.setChoose(Integer.parseInt(asked));
			        integerProducer.start();
			        servants = false;
			        start = true;
			        return "Type: /action for an Action;  /skip to skip this turn  /drawleadercard to use a LeaderCard  /activateleadercard to activate a Leader Card  /chat to send message;  /stampinfo to stamp info";
				}
				else{
					return "retry";
				}
			}
			return asked;
		}
		
	}

}
