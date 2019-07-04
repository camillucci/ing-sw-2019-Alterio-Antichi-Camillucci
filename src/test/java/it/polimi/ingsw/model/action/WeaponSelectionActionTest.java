package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.cards.CardsFactory;
import it.polimi.ingsw.model.cards.ShootFunc;
import it.polimi.ingsw.model.cards.WeaponCard;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static it.polimi.ingsw.model.AmmoColor.BLUE;
import static it.polimi.ingsw.model.AmmoColor.RED;
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
                new FireModalityAction(ammo, "", "", new Branch(new ShootAction((shooter, players, squares) -> Collections.emptyList(), (shooter, players, squares) -> Collections.emptyList(), s), new EndBranchAction()))));
    }

    @Test
    void getDiscardablePowerUps() {
        GameBoard gameBoard = new GameBoard(5, 1);
        Player player = new Player("A", PlayerColor.VIOLET, gameBoard);
        gameBoard.setPlayers(Collections.singletonList(player));
        player.addBlue(2);
        player.addRed(2);
        player.addYellow(2);
        player.addPowerUpCard();
        if(player.getPowerUps().get(0).color == BLUE)
            player.addWeapon(CardsFactory.getWeapons().get(2));
        else if(player.getPowerUps().get(0).color == RED)
            player.addWeapon(CardsFactory.getWeapons().get(0));
        else
            player.addWeapon(CardsFactory.getWeapons().get(1));
        Action action = new WeaponSelectionAction();
        action.initialize(player);
        assertEquals(1, action.getDiscardablePowerUps().size());
        action.addPowerUp(player.getPowerUps().get(0));
        assertEquals(0, action.getDiscardablePowerUps().size());
    }
}
