package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import it.polimi.ingsw.model.branch.BranchTestUtilities;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpActionTest {

    private GameBoard gameBoard;
    private Player p1;
    private Player p2;

    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard(5, 10);
        p1 = new Player("p", PlayerColor.BLUE, gameBoard);
        p2 = new Player("p", PlayerColor.BLUE, gameBoard);
    }

    @Test
    void shootNewton()
    {
        PowerUpCard newton = CardsFactory.getPowerUps().get(1);
        p1.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p1.getCurrentSquare().addPlayer(p1);
        p2.setCurrentSquare(gameBoard.getSquares().get(5));
        p2.getCurrentSquare().addPlayer(p2);
        gameBoard.setPlayers(Arrays.asList(p1, p2));
        Action powerUpAction = newton.getEffect();
        powerUpAction.initialize(p1);
        powerUpAction.addTarget(p2);
        powerUpAction.addTarget(gameBoard.getSquares().get(4));
        powerUpAction.doAction();
        assertEquals(0, p2.getDamage().size());
        assertEquals(p1.gameBoard.getSquares().get(4), p2.getCurrentSquare());
    }

    @Test
    void shootTeleporter()
    {
        PowerUpCard teleporter = CardsFactory.getPowerUps().get(3);
        p1.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p1.getCurrentSquare().addPlayer(p1);
        Action powerUpAction = teleporter.getEffect();
        powerUpAction.initialize(p1);
        powerUpAction.addTarget(p1);
        powerUpAction.addTarget(gameBoard.getSquares().get(6));
        powerUpAction.doAction();
        assertEquals(p1.gameBoard.getSquares().get(6), p1.getCurrentSquare());
    }

    @Test
    void shoot3()
    {
        Match match;
        Bot bot = new Bot();
        Player p;
        do{
            match = new Match(Arrays.asList("A", "B", "C"), Arrays.asList(PlayerColor.BLUE,PlayerColor.GREY, PlayerColor.GREEN),8, 10);
            match.start();
            match.getPlayer().addPowerUpCard();
            match.getPlayer().addPowerUpCard();
            for(Player p2: match.getPlayers()) {
                bot.playSpawnBranchMap(match);
                bot.playEmptyTurn(match);
            }
        }while((p = match.getPlayer()).getPowerupSet().getEndStartPUs().size() != 2);

        assertEquals(2, match.getPlayer().getPowerupSet().getEndStartPUs().size());
        Action a = BranchTestUtilities.search(match.getActions(), new PowerUpAction());

        List<PowerUpCard> tmp = a.getPossiblePowerUps();
        assertTrue(tmp.size() > 0);
        a.usePowerUp(a.getPossiblePowerUps().get(0));
        a.doAction();

        a = BranchTestUtilities.search(match.getActions(), new PowerUpAction());
        a.usePowerUp(a.getPossiblePowerUps().get(0));
        a.doAction();
        assertNotNull(BranchTestUtilities.search(match.getActions(), new PowerUpAction()));

        a = BranchTestUtilities.search(match.getActions(), new PowerUpAction());
        a.usePowerUp(a.getPossiblePowerUps().get(0));
        a.doAction();
        assertNotNull(BranchTestUtilities.search(match.getActions(), new PowerUpAction()));

        a = BranchTestUtilities.search(match.getActions(), new PowerUpAction());
        assertTrue(a.getPossiblePowerUps().isEmpty());
        bot.playNoAdrenaline(match);
        bot.playThreeDamage(match);
    }
}
