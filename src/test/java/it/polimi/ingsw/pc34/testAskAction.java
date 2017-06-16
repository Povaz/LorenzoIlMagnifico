package it.polimi.ingsw.pc34;

import org.junit.Test;

import it.polimi.ingsw.pc34.Socket.ClientSOC;

public class testAskAction {
	@Test
	public void testAction(){
		// TODO cambia! va in loop
		String message = ClientSOC.askAction();
		System.out.println(message);
	}
}
