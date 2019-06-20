package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.action.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static it.polimi.ingsw.model.branch.BranchTestUtilities.testEquality;
import static org.junit.jupiter.api.Assertions.*;

class BranchMapTest {
    private BranchMap curBranchMap;
    private boolean eventTriggered = false;
    private Player p = new Player("p", PlayerColor.BLUE,new GameBoard(3, 10));

    @Test
    void getPossibleActions()
    {
        curBranchMap = BranchMapFactory.noAdrenaline(); // = {PM1G, PM3, PW, R }
        assertTrue(testEquality(curBranchMap.getPossibleActions(), BranchTestUtilities.noAdrenalinePossibleActions()));
        curBranchMap = BranchMapFactory.adrenalineX2();
        assertEquals(9, curBranchMap.getPossibleActions().size());
    }

    @Test
    void eventsTest()
    {
        GameBoard gameBoard = new GameBoard(5, 11);
        p.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p.getCurrentSquare().addPlayer(p);

        // Rollback event
        eventTriggered = false;
        curBranchMap = BranchMapFactory.threeDamage();
        curBranchMap.rollbackEvent.addEventHandler((a,b)->this.eventTriggered = true);
        curBranchMap.getPossibleActions().stream().filter(a->a instanceof RollBackAction).forEach(a->{a.initialize(p);a.doAction();});
        assertTrue(eventTriggered);

        // EndBranch event
        eventTriggered = false;
        curBranchMap = BranchMapFactory.sixDamage();
        curBranchMap.endOfBranchMapReachedEvent.addEventHandler((a,b)->this.eventTriggered = true);
        curBranchMap.getPossibleActions().stream().filter(a->a instanceof EndBranchAction).forEach(a->{a.initialize(p);a.doAction();});
        assertTrue(eventTriggered);

        // NewActions event
        eventTriggered = false;
        curBranchMap = BranchMapFactory.adrenalineX1();
        curBranchMap.newActionsEvent.addEventHandler(this::checkNewActionsEvent);
        curBranchMap.newActionsEvent.addEventHandler((a,b)->this.eventTriggered=true);
        MoveAction M3 = new MoveAction(3);
        M3.initialize(p);
        M3.add(p.getCurrentSquare());
        curBranchMap.getPossibleActions().stream().filter(a->a.isCompatible(M3) && a instanceof MoveAction).forEach(
                a->{a.initialize(p); a.add(p.getCurrentSquare()); a.doAction();});
        assertTrue(eventTriggered);
    }

    void checkNewActionsEvent(BranchMap branchMap, List<Action> newActions)
    {
        // branchMap should be in the initial state of AdrenalineX1BranchMap = {M2RS, M3G, R } -> {G, R} after that M3 is done
        assertTrue(testEquality(newActions, new GrabAction(), new RollBackAction()));
    }

    @Test
    void invalidState() {
        curBranchMap = BranchMapFactory.noAdrenaline();
        assertEquals(7, curBranchMap.getPossibleActions().size());
        ((Event<BranchMap, EndBranchAction>)curBranchMap.endOfBranchMapReachedEvent).invoke(null, null);
        assertEquals(0, curBranchMap.getPossibleActions().size());
    }
}
