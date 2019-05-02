package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.branch.BranchMap;
import it.polimi.ingsw.model.branch.BranchMapFactory;
import it.polimi.ingsw.model.branch.BranchTestUtilities;
import it.polimi.ingsw.model.weapons.ShootFunc;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeaponSelectionActionTest {

    boolean shoot1 = false;
    boolean shoot2 = false;
    boolean triggered;
    private ExtendableAction action = new WeaponSelectionAction();
    private Player player = new Player("A", PlayerColor.GREY, null);
    private Ammo ammo = new Ammo(0, 0, 0);
    @Test
    void op() {
        action.initialize(player);
        action.doAction();
        assertEquals(0, action.getBranches().size());
        player.addWeapon(newWeaponCard(null));
        action.doAction();
        assertEquals(1, action.getBranches().size());
        player.addWeapon(newWeaponCard(null));
        action.doAction();
        assertEquals(2, action.getBranches().size());
    }

    WeaponCard newWeaponCard(ShootFunc s)
    {
       return new WeaponCard("B", ammo, ammo, () -> Arrays.asList(
                new FireModalityAction(ammo, new Branch(new ShootAction((shooter, players) -> Collections.emptyList(), (shooter, squares) -> Collections.emptyList(), s), new EndBranchAction()))));
    }

    @Test
    void op2()
    {
        WeaponCard weaponCard1 = newWeaponCard((shooter, players, squares) -> this.shoot1 = true);
        WeaponCard weaponCard2 = newWeaponCard((shooter, players, squares) -> this.shoot2 = true);
        player.addWeapon(weaponCard1);
        player.addWeapon(weaponCard2);
        BranchMap branchMap = BranchMapFactory.noAdrenaline(player);
        branchMap.endOfBranchMapReachedEvent.addEventHandler((a,b)->triggered=true);

        // branchmap = { M1G, M3, W, R } do = W

        for(Action a : branchMap.getPossibleActions())
            if(a instanceof WeaponSelectionAction)
            {
                a.initialize(player);
                a.doAction();
            }


        // possible = { W1, W2, R } do = W1

        List<Action> tmp = branchMap.getPossibleActions();
        assertTrue(BranchTestUtilities.testEquality(tmp, new ExtendableAction(), new ExtendableAction(), new RollBackAction()));

        branchMap.getPossibleActions().get(1).initialize(player);
        branchMap.getPossibleActions().get(1).doAction();

        // possible = {F1, R} do = F1
        tmp = branchMap.getPossibleActions();
        assertTrue(BranchTestUtilities.testEquality(tmp, new FireModalityAction(null,null,null), new RollBackAction()));
        branchMap.getPossibleActions().get(0).initialize(player);
        branchMap.getPossibleActions().get(0).doAction();

        // possible = {S1, R} do = S1
        tmp = branchMap.getPossibleActions();
        assertTrue( BranchTestUtilities.testEquality(tmp, new ShootAction(null,null,null), new RollBackAction()));
        branchMap.getPossibleActions().get(0).initialize(player);
        branchMap.getPossibleActions().get(0).doAction();

        assertTrue(shoot1 || shoot2);

        // possible = {E, R} do = E
        tmp = branchMap.getPossibleActions();
        assertTrue( BranchTestUtilities.testEquality(tmp, new EndBranchAction(), new RollBackAction()));
        branchMap.getPossibleActions().get(0).initialize(player);
        branchMap.getPossibleActions().get(0).doAction();

        // possible = {}
        tmp = branchMap.getPossibleActions();
        assertTrue(branchMap.getPossibleActions().isEmpty());
        assertTrue(triggered);
    }
}
