package it.polimi.ingsw.pcXX;

import org.junit.Test;

import it.polimi.ingsw.pcXX.Socket.ClientSOC;

public class testAskAction {
	@Test
	public void testAction(){
		String message = ClientSOC.askAction();
		System.out.println(message);
	}
}
