package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.ActionsProvider;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.action.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
    }

    @Test
    void eventsTest()
    {
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
        curBranchMap.getPossibleActions().stream().filter(a->a.isCompatible(M3) && a instanceof MoveAction).forEach(a->{a.initialize(p);a.doAction();});
        assertTrue(eventTriggered);
    }

    void checkNewActionsEvent(BranchMap branchMap, List<Action> newActions)
    {
        // branchMap should be in the initial state of AdrenalineX1BranchMap = {M2RS, M3G, R } -> {G, R} after that M3 is done
        assertTrue(testEquality(newActions, new GrabAction(), new RollBackAction()));
    }

    public void TestNoAdrenaline(ActionsProvider provider)
    {

    }
}
