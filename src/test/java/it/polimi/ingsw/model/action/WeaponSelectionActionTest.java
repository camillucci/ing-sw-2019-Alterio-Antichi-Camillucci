package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.cards.ShootFunc;
import it.polimi.ingsw.model.cards.WeaponCard;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class WeaponSelectionActionTest {

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
        return new WeaponCard("B", ammo, ammo, () -> Collections.singletonList(
                new FireModalityAction(ammo, new Branch(new ShootAction((shooter, players, squares) -> Collections.emptyList(), (shooter, players, squares) -> Collections.emptyList(), s), new EndBranchAction()))));
    }
}
