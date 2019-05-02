package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ReloadActionTest {

    private Action action = new ReloadAction();
    private Player player = new Player("A", PlayerColor.GREY, null);
    private Ammo ammo1 = new Ammo(0, 0, 0);
    private Ammo ammo2 = new Ammo(4, 4, 4);
    private WeaponCard weaponCard1 = new WeaponCard("B", ammo1, ammo1, () -> Arrays.asList(new FireModalityAction(ammo1, new ArrayList<>())));
    private WeaponCard weaponCard2 = new WeaponCard("C", ammo2, ammo2,() -> Arrays.asList(new FireModalityAction(ammo2, new ArrayList<>()), new FireModalityAction(ammo2, new ArrayList<>())));

    @Test
    void op() {
        action.initialize(player);
        player.addWeapon(weaponCard1);
        player.unloadWeapon(weaponCard1);
        assertEquals(0, player.getLoadedWeapons().size());
        assertEquals(1, player.getUnloadedWeapons().size());
        action.addWeapon(weaponCard1);
        action.doAction();
        assertEquals(1, player.getLoadedWeapons().size());
        assertEquals(0, player.getUnloadedWeapons().size());
        player.unloadWeapon(weaponCard1);
        player.addWeapon(weaponCard2);
        player.unloadWeapon(weaponCard2);
        assertEquals(0, player.getLoadedWeapons().size());
        assertEquals(2, player.getUnloadedWeapons().size());
        action.addWeapon(weaponCard2);
        action.doAction();
        assertEquals(1, player.getLoadedWeapons().size());
        assertEquals(1, player.getUnloadedWeapons().size());
    }
}
