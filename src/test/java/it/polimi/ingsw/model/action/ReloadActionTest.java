package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.CardsFactory;
import it.polimi.ingsw.model.cards.WeaponCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static it.polimi.ingsw.model.AmmoColor.*;
import static org.junit.jupiter.api.Assertions.*;

class ReloadActionTest {

    private Action action;
    private GameBoard gameBoard;
    private Player player;
    private Ammo ammo1;
    private Ammo ammo2;
    private WeaponCard weaponCard1;
    private WeaponCard weaponCard2;

    @BeforeEach
    void setUp() {
        action = new ReloadAction();
        gameBoard = new GameBoard(7, 10);
        player = new Player("A", PlayerColor.GREY, gameBoard);
        gameBoard.setPlayers(Collections.singletonList(player));
        ammo1 = new Ammo(0, 0, 0);
        ammo2 = new Ammo(4, 4, 4);
        weaponCard1 = new WeaponCard("B", ammo1, ammo1, () -> Collections.singletonList(new FireModalityAction(ammo1, new ArrayList<>())));
        weaponCard2 = new WeaponCard("C", ammo2, ammo2,() -> Arrays.asList(new FireModalityAction(ammo2, new ArrayList<>()), new FireModalityAction(ammo2, new ArrayList<>())));
    }

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

    @Test
    void getDiscardablePowerUps() {
        action.initialize(player);
        player.addPowerUpCard();
        player.addBlue(2);
        player.addRed(2);
        player.addYellow(2);
        if(player.getPowerUps().get(0).color == BLUE)
            player.addWeapon(CardsFactory.getWeapons().get(1));
        else if(player.getPowerUps().get(0).color == RED)
            player.addWeapon(CardsFactory.getWeapons().get(8));
        else
            player.addWeapon(CardsFactory.getWeapons().get(15));
        player.unloadWeapon(player.getWeapons().get(0));
        action.addWeapon(player.getWeapons().get(0));
        assertEquals(0, player.getLoadedWeapons().size());
        assertEquals(1, player.getUnloadedWeapons().size());
        assertEquals(1, action.getDiscardablePowerUps().size());
        action.addDiscarded(player.getPowerUps().get(0));
        assertEquals(0, action.getDiscardablePowerUps().size());
        action.doAction();
        assertEquals(1, player.getLoadedWeapons().size());
        assertEquals(0, player.getUnloadedWeapons().size());
        assertEquals(0, player.getPowerUps().size());
    }
}
