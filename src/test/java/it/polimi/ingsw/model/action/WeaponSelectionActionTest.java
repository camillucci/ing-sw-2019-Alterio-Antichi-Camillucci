package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WeaponSelectionActionTest {

    private ExtendableAction action = new WeaponSelectionAction();
    private Player player = new Player("A", PlayerColor.GREY, null);
    private Ammo ammo = new Ammo(0, 0, 0);
    private WeaponCard weaponCard1 = new WeaponCard("B", ammo, ammo, new FireModalityAction(ammo, new ArrayList<>()));
    private WeaponCard weaponCard2 = new WeaponCard("C", ammo, ammo, new FireModalityAction(ammo, new ArrayList<>()), new FireModalityAction(ammo, new ArrayList<>()));

    @Test
    void op() {
        action.initialize(player);
        action.doAction();
        assertEquals(0, action.getBranches().size());
        player.addWeapon(weaponCard1);
        action.doAction();
        assertEquals(1, action.getBranches().size());
        player.addWeapon(weaponCard2);
        action.doAction();
        assertEquals(2, action.getBranches().size());
    }
}
