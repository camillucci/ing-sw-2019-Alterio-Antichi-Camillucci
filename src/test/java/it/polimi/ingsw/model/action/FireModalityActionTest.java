package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.CardsFactory;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static it.polimi.ingsw.model.AmmoColor.*;
import static org.junit.jupiter.api.Assertions.*;

class FireModalityActionTest {

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
        Action action = player.getWeapons().get(0).fireBuilder.get().get(1);
        action.initialize(player);
        assertEquals(1, action.getDiscardablePowerUps().size());
        action.add(player.getPowerUps().get(0));
        assertEquals(0, action.getDiscardablePowerUps().size());
    }
}
