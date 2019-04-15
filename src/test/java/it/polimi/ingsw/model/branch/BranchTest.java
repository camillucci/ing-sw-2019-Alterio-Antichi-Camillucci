package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.action.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.branch.BranchTestUtilities.testEquality;
import static org.junit.jupiter.api.Assertions.*;

class BranchTest {
    private Player p = new Player("p1", PlayerColor.BLUE,new GameBoard(3,10));
    private Branch curBranch;
    private List<Action> curBranchActions;
    private boolean eventTriggered = false;

    private void getM3GM2GW()
    {
        curBranchActions = new ArrayList<>();
        curBranchActions.add(new MoveAction(3));
        curBranchActions.add(new GrabAction());
        curBranchActions.add(new MoveAction(2));
        curBranchActions.add(new GrabAction());
        WeaponSelectionAction wsa = new WeaponSelectionAction();
        curBranch= new Branch(curBranchActions, wsa);
        curBranchActions.add(wsa);
    }

    private void getRM2RM3G()
    {
        curBranchActions = new ArrayList<>();
        curBranchActions.add(new ReloadAction());
        curBranchActions.add(new MoveAction(2));
        curBranchActions.add(new ReloadAction());
        curBranchActions.add(new MoveAction(3));
        curBranchActions.add(new GrabAction());
        curBranch = new Branch(curBranchActions, new EndBranchAction());
        curBranchActions.add(new EndBranchAction());
    }
    private void getM2RW()
    {
        curBranchActions = new ArrayList<>();
        curBranchActions.add(new MoveAction(2));
        curBranchActions.add(new ReloadAction());
        WeaponSelectionAction wsa = new WeaponSelectionAction();
        curBranch = new Branch(curBranchActions, wsa);
        curBranchActions.add(wsa);
    }
    @Test
    void getCompatibleActions()
    {
        getM3GM2GW();
        List<Action> compatibleActions;
        ArrayList<Action> tmp = new ArrayList<>();
        tmp.add(curBranchActions.get(0));
        tmp.add(curBranchActions.get(1));
        assertTrue(testEquality(curBranch.getCompatibleActions(), tmp)); // Possible actions: M3 and G
        tmp.clear();
        tmp.add(new MoveAction(3));
        tmp.add(new MoveAction(3));
        assertFalse(testEquality(curBranch.getCompatibleActions(), tmp));
    }

    @Test
    void goNext()
    {
        getM3GM2GW();
        ArrayList<Action> tmp = new ArrayList<>();

        tmp.add(curBranchActions.get(0));
        tmp.add(curBranchActions.get(1));
        assertTrue(testEquality(curBranch.getCompatibleActions(), tmp));

        tmp.clear();
        curBranch.goNext(curBranchActions.get(1));
        tmp.add(curBranchActions.get(2));
        tmp.add(curBranchActions.get(3));
        assertTrue(testEquality(curBranch.getCompatibleActions(), tmp));

        tmp.clear();
        curBranch.goNext(curBranchActions.get(2));
        tmp.add(curBranchActions.get(3));
        assertTrue(testEquality(curBranch.getCompatibleActions(), tmp));

        tmp.clear();
        curBranch.goNext(curBranchActions.get(3));
        tmp.add(curBranchActions.get(4));
        assertTrue(testEquality(curBranch.getCompatibleActions(), tmp));
    }

    @Test
    void goNext2()
    {
        getM2RW(); // M2 and R both optional -> compatibleActions() = { M2, R, W }
        ArrayList<Action> tmp = new ArrayList<>();
        tmp.add(new MoveAction(2));
        tmp.add(new ReloadAction());
        tmp.add(new WeaponSelectionAction());
        assertTrue(testEquality(curBranch.getCompatibleActions(), tmp));
    }

    @Test
    void goNext3()
    {
        getRM2RM3G();
        ArrayList<Action> tmp = new ArrayList<>();
        tmp.add(new ReloadAction());
        tmp.add(new MoveAction(2));
        tmp.add(new ReloadAction());
        tmp.add(new MoveAction(3));
        tmp.add(new GrabAction());
        assertTrue(testEquality(curBranch.getCompatibleActions(), tmp));

    }

    @Test
    void isInvalidBranch()
    {
        getM3GM2GW();
        Action incompatibleAction = new MoveAction(32);
        curBranch.goNext(incompatibleAction);
        assertTrue(curBranch.isInvalidBranch());

        getM3GM2GW();
        curBranch.goNext(new MoveAction(3)); //M3
        assertFalse(curBranch.isInvalidBranch());
        curBranch.goNext(new MoveAction(1));
        assertTrue(curBranch.isInvalidBranch());

        getM3GM2GW();
        curBranch.goNext(curBranchActions.get(1));
        curBranch.goNext(new ReloadAction());
        assertTrue(curBranch.isInvalidBranch());

        // trying to "overflow" curBranch

        getM3GM2GW();
        curBranch.goNext(curBranchActions.get(1));
        curBranch.goNext(curBranchActions.get(3));
        curBranch.goNext(curBranchActions.get(4)); // Can't move next when final action is reached -> isInvalidBranch() returns true
        assertTrue(curBranch.isInvalidBranch());

        //let a = M3GM2GW, and b = GM1GW
        //then b is compatible with a

        getM3GM2GW();
        curBranch.goNext(new GrabAction());
        curBranch.goNext(new MoveAction(1));
        curBranch.goNext(new GrabAction());
        assertFalse(curBranch.isInvalidBranch());
        assertTrue(curBranch.getCompatibleActions().size() == 1); // only WeaponSelectionAction left
        assertTrue(curBranch.getCompatibleActions().get(0) instanceof WeaponSelectionAction);
    }

    @Test
    void eventsTest()
    {
        // Action completed
        eventTriggered = false;
        getM3GM2GW();
        curBranch.actionCompletedEvent.addEventHandler((a,b)->this.eventTriggered=true);
        curBranch.getCompatibleActions().get(0).initialize(p);
        curBranch.getCompatibleActions().get(0).doAction();
        assertTrue(eventTriggered);

        // ExtendibleAction completed
        eventTriggered = false;
        Branch branch = new Branch(new WeaponSelectionAction());
        branch.extActionCompletedEvent.addEventHandler((a,b)->this.eventTriggered=true);
        curBranch.getCompatibleActions().get(0).initialize(p);
        curBranch.getCompatibleActions().get(0).doAction();
        assertTrue(eventTriggered);

        // EndBranchAction completed
        eventTriggered = false;
        branch = new Branch(new MoveAction(3), new EndBranchAction());
        branch.endBranchEvent.addEventHandler((a,b)->this.eventTriggered=true);
        curBranch.getCompatibleActions().get(0).initialize(p);
        curBranch.getCompatibleActions().get(0).doAction();
        curBranch.getCompatibleActions().get(0).initialize(p);
        curBranch.getCompatibleActions().get(0).doAction();
        assertTrue(eventTriggered);
    }
}
