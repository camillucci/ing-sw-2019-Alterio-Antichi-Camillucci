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
    public void op2()
    {
    }
}
