package it.polimi.ingsw.pc34.Socket;

import java.rmi.RemoteException;

import it.polimi.ingsw.pc34.RMI.UserLogin;

public class GameFlow {
	
	private boolean start;
	private boolean action;
	private boolean drawLeader;
	private boolean activateLeader;
	private String decision;
	
	public GameFlow(){
		start = true;
		action = false;
		drawLeader = false;
		activateLeader = false;
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
	
	//QUANDO CREA GAME FLOW MANDA PRIMO MESSAGGIO
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
			if(start){
				if(asked.equals("/action")){
					start = false;
					action = true;
					return "Which ActionSpot do you choose? Type /action and then choose a number : 1. TERRITORY TOWER 2. BUILDING TOWER 3. CHARACTER TOWER 4. VENTURE TOWER 5. HARVEST 6. PRODUCE 7. MARKET 8. COUNCILPALACE";
				}
			}
			else if(action){
				if(decision == null && checkNumber(1, 8, asked)){
					decision = asked;
					switch(decision){ 
						case "1":
							return "Which card?";
						case "2":
							return "Which card?";
						case "3":
							return "Which card?";
						case "4":
							return "Which card?";
						case "5":
							return "Which spot?";
						case "6":
							return "Which spot?";
						case "7":
							return "Which Spot? 0.COIN(5)  1.SERVANT(5)   2.COIN(2) & MILITARY_POINT(3) 3.COUNCILPRIVILEGE(2)";
						case "8":
							break;
					}
				}
				
			}
			
			
			return asked;
		}
		
	}
}
