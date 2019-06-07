package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.FireModalityAction;
import it.polimi.ingsw.model.cards.WeaponCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private GameBoard gameBoard = new GameBoard(3, 0);
    private Player player = new Player("A", PlayerColor.YELLOW, gameBoard);
    private static final int N = 10;
    private static final int MAX_AMMO = 3;
    private static final int MAX_POWER_UPS = 3;
    private static final int MAX_POWER_UPS_RESPAWN = 4;
    private static final int MAX_DAMAGES = 12;

    @Test
    void addRed() {
        for(int i = 0; i < N; i++) {
            assertEquals(Math.min(i + 1, MAX_AMMO), player.getAmmo().red);
            player.addRed(1);
        }
    }

    @Test
    void addBlue() {
        for(int i = 0; i < N; i++) {
            assertEquals(Math.min(i + 1, MAX_AMMO), player.getAmmo().blue);
            player.addBlue(1);
        }
    }

    @Test
    void addYellow() {
        for(int i = 0; i < N; i++) {
            assertEquals(Math.min(i + 1, MAX_AMMO), player.getAmmo().yellow);
            player.addYellow(1);
        }
    }

    @Test
    void addPowerUpCardAndRemove() {
        for(int i = 0; i < N; i++) {
            assertEquals(Math.min(i, MAX_POWER_UPS), player.getPowerUps().size());
            player.addPowerUpCard();
        }
        for(int i = MAX_POWER_UPS - 1; i >= 0; i--)
            player.removePowerUpCard(player.getPowerUps().get(i));
    }

    @Test
    void addPowerUpCardRespawnAndRemove() {
        for(int i = 0; i < N; i++) {
            assertEquals(Math.min(i, MAX_POWER_UPS_RESPAWN), player.getPowerUps().size());
            player.addPowerUpCardRespawn();
        }
        for(int i = MAX_POWER_UPS_RESPAWN - 1; i >= 0; i--)
            player.removePowerUpCard(player.getPowerUps().get(i));
    }

    @Test
    void addDamages() {
        Player player2 = new Player("B", PlayerColor.VIOLET, gameBoard);
        Player player3 = new Player("C", PlayerColor.GREEN, gameBoard);

        for(int i = 0; i < N * 2; i++) {
            assertEquals(Math.min(i * 3, MAX_DAMAGES), player.getDamage().size());
            player.addDamage(player2, 1);
            assertEquals(Math.min(i * 3 + 1, MAX_DAMAGES), player.getDamage().size());
            player.addDamage(player3, 2);
        }
    }

    @Test
    void addMark() {
        Player player2 = new Player("B", PlayerColor.VIOLET, gameBoard);
        Player player3 = new Player("C", PlayerColor.GREEN, gameBoard);

        assertEquals(0, player3.getMark().size());
        player3.addMark(player, 1);
        assertEquals(1, player3.getMark().size());
        player3.addMark(player2, 2);
        assertEquals(3, player3.getMark().size());
        player3.addMark(player, 1);
        assertEquals(4, player3.getMark().size());
        player3.addMark(player2, 2);
        assertEquals(5, player3.getMark().size());
        player3.addMark(player, 1);
        assertEquals(6, player3.getMark().size());
        player3.addMark(player2, 2);
        assertEquals(6, player3.getMark().size());

        assertEquals(0, player3.getDamage().size());
        player3.addDamage(player, 1);
        assertEquals(4, player3.getDamage().size());

    }

    @Test
    void addUnloadReloadRemoveGetWeapon() {
        final Supplier<List<FireModalityAction>> listSupplier = () -> Collections.singletonList(new FireModalityAction(null, new ArrayList<>()));
        WeaponCard weaponCard1 = new WeaponCard("B", null, null, listSupplier);
        WeaponCard weaponCard2 =  new WeaponCard("B", null, null, listSupplier);
        assertEquals(0, player.getLoadedWeapons().size());
        assertEquals(0, player.getUnloadedWeapons().size());
        assertEquals(0, player.getWeapons().size());
        player.addWeapon(weaponCard1);
        assertEquals(1, player.getLoadedWeapons().size());
        assertEquals(0, player.getUnloadedWeapons().size());
        assertEquals(1, player.getWeapons().size());
        player.unloadWeapon(weaponCard1);
        assertEquals(0, player.getLoadedWeapons().size());
        assertEquals(1, player.getUnloadedWeapons().size());
        assertEquals(1, player.getWeapons().size());
        player.addWeapon(weaponCard2);
        assertEquals(1, player.getLoadedWeapons().size());
        assertEquals(1, player.getUnloadedWeapons().size());
        assertEquals(2, player.getWeapons().size());
        player.unloadWeapon(weaponCard2);
        assertEquals(0, player.getLoadedWeapons().size());
        assertEquals(2, player.getUnloadedWeapons().size());
        assertEquals(2, player.getWeapons().size());
        player.reloadWeapon(weaponCard1);
        assertEquals(1, player.getLoadedWeapons().size());
        assertEquals(1, player.getUnloadedWeapons().size());
        assertEquals(2, player.getWeapons().size());
        player.removeWeapon(weaponCard1);
        assertEquals(0, player.getLoadedWeapons().size());
        assertEquals(1, player.getUnloadedWeapons().size());
        assertEquals(1, player.getWeapons().size());
        player.removeWeapon(weaponCard2);
        assertEquals(0, player.getLoadedWeapons().size());
        assertEquals(0, player.getUnloadedWeapons().size());
        assertEquals(0, player.getWeapons().size());
    }

    @Test
    void addPoints() {
        player.addPoints(N);
        assertEquals(N, player.getPoints());
    }

    @Test
    void getClone() {
        Player cloned = new Player(player);
        Player player2 = new Player("B", PlayerColor.VIOLET, gameBoard);
        WeaponCard weaponCard =  new WeaponCard("B", null, null, () -> Collections.singletonList(new FireModalityAction(null, new ArrayList<>())));

        assertNotEquals(player, cloned);

        assertEquals(player.getDamage(), cloned.getDamage());
        cloned.addDamage(player2, 1);
        assertNotEquals(player.getDamage(), cloned.getDamage());

        assertEquals(player.getMark(), cloned.getMark());
        cloned.addMark(player2, 1);
        assertNotEquals(player.getMark(), cloned.getMark());

        assertEquals(player.getLoadedWeapons(), cloned.getLoadedWeapons());
        cloned.addWeapon(weaponCard);
        assertNotEquals(player.getLoadedWeapons(), cloned.getLoadedWeapons());

        assertEquals(player.getUnloadedWeapons(), cloned.getUnloadedWeapons());
        cloned.unloadWeapon(weaponCard);
        assertNotEquals(player.getUnloadedWeapons(), cloned.getUnloadedWeapons());

        assertEquals(player.getPowerUps(), cloned.getPowerUps());
        cloned.addPowerUpCard();
        assertNotEquals(player.getPowerUps(), cloned.getPowerUps());
    }
}
