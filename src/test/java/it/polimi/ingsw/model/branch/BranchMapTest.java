package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.action.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.branch.BranchTestUtilities.testEquality;
import static org.junit.jupiter.api.Assertions.*;

class BranchMapTest {
    private BranchMap curBranchMap;
    private boolean eventTriggered = false;
    Player p = new Player("p", PlayerColor.BLUE,new GameBoard(3,10));

    @Test
    void getPossibleActions()
    {
        curBranchMap = new NoAdrenalineBranchMap(p); // = {M1G, M3, W }
        ArrayList<Action> expectedActions = new ArrayList<>();
        expectedActions.add(new MoveAction(p, 1)); //M1
        expectedActions.add(new MoveAction(p, 3)); //M3
        expectedActions.add(new GrabAction(p)); //G
        expectedActions.add(new WeaponSelectionAction(p)); //W
        expectedActions.add(new RollBackAction(p)); //R
        expectedActions.add(new EndBranchAction(p)); //EndBranch
        assertTrue(testEquality(curBranchMap.getPossibleActions(), expectedActions));
    }

    @Test
    void eventsTest()
    {
        // Rollback event
        eventTriggered = false;
        curBranchMap = new NoAdrenalineBranchMap(p);
        curBranchMap.rollbackEvent.addEventHandler((a,b)->this.eventTriggered = true);
        curBranchMap.getPossibleActions().stream().filter(a->a instanceof RollBackAction).forEach(a->a.doAction());
        assertTrue(eventTriggered);

        // EndBranch event
        eventTriggered = false;
        curBranchMap = new NoAdrenalineBranchMap(p);
        curBranchMap.endOfBranchMapReachedEvent.addEventHandler((a,b)->this.eventTriggered = true);
        curBranchMap.getPossibleActions().stream().filter(a->a instanceof EndBranchAction).forEach(a->a.doAction());
        assertTrue(eventTriggered);

        // NewActions event
        eventTriggered = false;
        curBranchMap = new AdrenalineX1BranchMap(p);
        curBranchMap.newActionsEvent.addEventHandler(this::checkNewActionsEvent);
        curBranchMap.newActionsEvent.addEventHandler((a,b)->this.eventTriggered=true);
        MoveAction M3 = new MoveAction(p, 3);
        curBranchMap.getPossibleActions().stream().filter(a->a.isCompatible(M3) && a instanceof MoveAction).forEach(a->a.doAction());
        assertTrue(eventTriggered);
    }

    void checkNewActionsEvent(BranchMap branchMap, List<Action> newActions)
    {
        // branchMap should be in the initial state of AdrenalineX1BranchMap = {M2RS, M3G } -> {G} after that M3 is done
        ArrayList<Action> expectedActions = new ArrayList<>();
        expectedActions.add(new GrabAction(p));
        assertTrue(testEquality(newActions, expectedActions));
        expectedActions.add(new MoveAction(p, 2));
        assertFalse(testEquality(newActions, expectedActions));
    }
}