package it.polimi.ingsw.pc34.Model;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by trill on 22/05/2017.
 */
public class OrderTest{
    private Player paolo = new Player("Paolo", null, PlayerColor.BLUE);
    private Player tommaso = new Player("Tommaso", null, PlayerColor.GREEN);
    private Player erick = new Player("Erick", null, PlayerColor.YELLOW);
    private Player cugola = new Player("Cugola", null, PlayerColor.PURPLE);
    private Player affetti = new Player("Affetti", null, PlayerColor.RED);

    // Without councilPalaceOrder
    @Test
    public void testRecalculate1(){
        List<Player> players = new ArrayList<>(Arrays.asList(paolo, tommaso, erick, cugola, affetti));
        Order order = new Order(players);
        order.setShown(players);

        List<Player> expected = new ArrayList<>(Arrays.asList(paolo, tommaso, erick, cugola, affetti));

        List<FamilyMember> councilPalaceOrder = new ArrayList<>();
        order.calculateShownOrder(councilPalaceOrder);
        List<Player> calculated = order.getShown();

        assertEquals(expected, calculated);
    }

    // With councilPalaceOrder
    @Test
    public void testRecalculate2(){
        List<Player> players = new ArrayList<>(Arrays.asList(paolo, tommaso, erick, cugola, affetti));
        Order order = new Order(players);
        order.setShown(players);

        List<Player> expected = new ArrayList<>(Arrays.asList(cugola, tommaso, affetti, paolo, erick));

        List<FamilyMember> councilPalaceOrder = new ArrayList<>();
        councilPalaceOrder.add(new FamilyMember(cugola, FamilyColor.NEUTRAL));
        councilPalaceOrder.add(new FamilyMember(tommaso, FamilyColor.NEUTRAL));
        councilPalaceOrder.add(new FamilyMember(affetti, FamilyColor.NEUTRAL));
        order.calculateShownOrder(councilPalaceOrder);
        List<Player> calculated = order.getShown();

        assertEquals(expected, calculated);
    }

    // Without vaticanReport
    @Test
    public void testCalculateRealOrder1(){
        List<Player> players = new ArrayList<>(Arrays.asList(paolo, tommaso, erick, cugola));
        Order order = new Order(players);
        order.setShown(players);

        List<Player> expected = new ArrayList<>(Arrays.asList(paolo, tommaso, erick, cugola,
                paolo, tommaso, erick, cugola, paolo, tommaso, erick, cugola, paolo, tommaso, erick, cugola));

        order.calculateRealOrder();
        List<Player> calculated = order.getReal();

        assertEquals(expected, calculated);
    }

    // With vaticanReport player separated
    @Test
    public void testCalculateRealOrder2(){
        List<Player> players = new ArrayList<>(Arrays.asList(paolo, tommaso, erick, cugola));
        Order order = new Order(players);
        order.setShown(players);
        paolo.getPlayerBoard().getModifier().setJumpFirstRound(true);
        cugola.getPlayerBoard().getModifier().setJumpFirstRound(true);

        List<Player> expected = new ArrayList<>(Arrays.asList(tommaso, erick,
                paolo, tommaso, erick, cugola, paolo, tommaso, erick, cugola, paolo, tommaso, erick, cugola,
                paolo,cugola));

        order.calculateRealOrder();
        List<Player> calculated = order.getReal();

        assertEquals(expected, calculated);
    }

    // With vaticanReport player not separated
    @Test
    public void testCalculateRealOrder3(){
        List<Player> players = new ArrayList<>(Arrays.asList(paolo, tommaso, erick, cugola));
        Order order = new Order(players);
        order.setShown(players);
        tommaso.getPlayerBoard().getModifier().setJumpFirstRound(true);
        erick.getPlayerBoard().getModifier().setJumpFirstRound(true);

        List<Player> expected = new ArrayList<>(Arrays.asList(paolo, cugola,
                paolo, tommaso, erick, cugola, paolo, tommaso, erick, cugola, paolo, tommaso, erick, cugola,
                tommaso, erick));

        order.calculateRealOrder();
        List<Player> calculated = order.getReal();

        assertEquals(expected, calculated);
    }

    // Without duplicates
    @Test
    public void testRemoveBottomDuplicates1(){
        List<Player> expected = new ArrayList<>(Arrays.asList(paolo, tommaso, erick, cugola, affetti));
        Order order = new Order(expected);

        List<Player> calculated = new ArrayList<>(Arrays.asList(paolo, tommaso, erick, cugola, affetti));
        order.removeBottomDuplicates(calculated);

        assertEquals(expected, calculated);
    }

    // With duplicates
    @Test
    public void testRemoveBottomDuplicates2(){
        List<Player> expected = new ArrayList<>(Arrays.asList(paolo, tommaso, erick, cugola, affetti));
        Order order = new Order(expected);

        List<Player> calculated = new ArrayList<>(Arrays.asList(paolo, tommaso, paolo, erick, cugola, cugola, affetti, tommaso));
        order.removeBottomDuplicates(calculated);

        assertEquals(expected, calculated);
    }
}
