package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.branch.Branch;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShootActionTest {

    private Player p = new Player("p", PlayerColor.BLUE, new GameBoard(3,10));
    private boolean shooted = false;

    @Test
    void op()
    {
        //TODO
        /*
        shooted = false;
        Action shootAction = new ShootAction();
        shootAction.addTarget(p);
        shootAction.initializeAction();
        shootAction.doAction(p);
        assertTrue(shooted);
         */
    }

    private void shoot(Player shooter, List<Player> targets)
    {
        assertTrue(targets.get(0).equals(p));
        this.shooted = true;
    }
}