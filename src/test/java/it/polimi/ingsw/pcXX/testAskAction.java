package it.polimi.ingsw.pcXX;

import org.junit.Test;

import it.polimi.ingsw.pcXX.Socket.Client;

public class testAskAction {
	@Test
	public void testAction(){
		String message = Client.askAction();
		System.out.println(message);
	}
}
