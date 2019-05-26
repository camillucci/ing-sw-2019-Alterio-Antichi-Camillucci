package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.cards.CardsFactory;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.WeaponCard;
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
        gameBoard = new GameBoard(5, 0);
        p1 = new Player("p", PlayerColor.BLUE, gameBoard);
        p2 = new Player("p", PlayerColor.BLUE, gameBoard);
    }

    @Test
    void shootNewton()
    {
        PowerUpCard newton = CardsFactory.getPowerUps().get(1);
        PowerUpCard teleporter = CardsFactory.getPowerUps().get(3);
        p1.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p1.getCurrentSquare().addPlayer(p1);
        p1.addPowerUpCard(newton);
        p1.addPowerUpCard(teleporter);
        p2.setCurrentSquare(gameBoard.getSquares().get(5));
        p2.getCurrentSquare().addPlayer(p2);
        gameBoard.setPlayers(Arrays.asList(p1, p2));
        Action powerUpAction = newton.getEffect();
        powerUpAction.initialize(p1);
        assertEquals(1, powerUpAction.getPossiblePlayers().size());
        assertEquals(0, powerUpAction.getPossibleSquares().size());
        powerUpAction.addTarget(p2);
        assertEquals(0, powerUpAction.getPossiblePlayers().size());
        assertEquals(4, powerUpAction.getPossibleSquares().size());
        powerUpAction.addTarget(gameBoard.getSquares().get(4));
        assertEquals(0, powerUpAction.getPossiblePlayers().size());
        assertEquals(0, powerUpAction.getPossibleSquares().size());
        powerUpAction.usePowerUp(teleporter);
        powerUpAction.doAction();
        assertEquals(0, p2.getDamage().size());
        assertEquals(p1.gameBoard.getSquares().get(4), p2.getCurrentSquare());
    }

    @Test
    void shootTeleporter()
    {
        PowerUpCard teleporter = CardsFactory.getPowerUps().get(3);
        PowerUpCard newton = CardsFactory.getPowerUps().get(1);
        p1.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p1.getCurrentSquare().addPlayer(p1);
        p1.addPowerUpCard(newton);
        p1.addPowerUpCard(teleporter);
        Action powerUpAction = teleporter.getEffect();
        powerUpAction.initialize(p1);
        assertEquals(0, powerUpAction.getPossiblePlayers().size());
        assertEquals(9, powerUpAction.getPossibleSquares().size());
        powerUpAction.addTarget(gameBoard.getSquares().get(6));
        assertEquals(0, powerUpAction.getPossiblePlayers().size());
        assertEquals(0, powerUpAction.getPossibleSquares().size());
        powerUpAction.usePowerUp(newton);
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
        //a.doAction();

        a = BranchTestUtilities.search(match.getActions(), new PowerUpAction());
        a.usePowerUp(a.getPossiblePowerUps().get(0));
        //a.doAction();
        assertNotNull(BranchTestUtilities.search(match.getActions(), new PowerUpAction()));

        a = BranchTestUtilities.search(match.getActions(), new PowerUpAction());
        a.usePowerUp(a.getPossiblePowerUps().get(0));
        //a.doAction();
        assertNotNull(BranchTestUtilities.search(match.getActions(), new PowerUpAction()));

        a = BranchTestUtilities.search(match.getActions(), new PowerUpAction());
        //assertTrue(a.getPossiblePowerUps().isEmpty());
        bot.playNoAdrenaline(match);
        bot.playThreeDamage(match);
    }

    @Test
    void shootTargetingScope() {
        PowerUpCard targetingScope1 = CardsFactory.getPowerUps().get(0);
        PowerUpCard targetingScope2 = CardsFactory.getPowerUps().get(4);
        WeaponCard zx2 = CardsFactory.getWeapons().get(16);
        p1.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p1.getCurrentSquare().addPlayer(p1);
        p1.addWeapon(zx2);
        p1.addPowerUpCard(targetingScope1);
        p1.addPowerUpCard(targetingScope2);
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p2.getCurrentSquare().addPlayer(p2);
        Branch branch = p1.getWeapons().get(0).getFireModalitysBranch(0).get(0);
        Action shootAction = branch.getCompatibleActions().get(0);
        shootAction.initialize(p1);
        assertEquals(1, shootAction.getPossiblePlayers().size());
        assertEquals(0, shootAction.getPossibleSquares().size());
        assertEquals(2, shootAction.getPossiblePowerUps().size());
        shootAction.addTarget(p2);
        assertEquals(0, shootAction.getPossiblePlayers().size());
        assertEquals(0, shootAction.getPossibleSquares().size());
        assertEquals(2, shootAction.getPossiblePowerUps().size());
        shootAction.usePowerUp(targetingScope1);
        assertEquals(0, shootAction.getPossiblePlayers().size());
        assertEquals(0, shootAction.getPossibleSquares().size());
        assertEquals(0, shootAction.getPossiblePowerUps().size());
        shootAction.doAction();
        assertEquals(1, p2.getDamage().size());
        assertEquals(2, p2.getMark().size());
        branch.goNext(shootAction);
        assertTrue(branch.getCompatibleActions().get(0) instanceof SupportPowerUpAction);
        SupportPowerUpAction supportPowerUpAction = (SupportPowerUpAction) branch.getCompatibleActions().get(0);
        supportPowerUpAction.initialize(p1);
        assertEquals(1, supportPowerUpAction.getPossiblePlayers().size());
        supportPowerUpAction.addTarget(p2);
        assertEquals(0, supportPowerUpAction.getPossiblePlayers().size());
        supportPowerUpAction.usePowerUp(targetingScope2);
        supportPowerUpAction.doAction();
        assertEquals(2, p2.getDamage().size());
        assertEquals(2, p2.getMark().size());
        branch.goNext(supportPowerUpAction);
        assertTrue(branch.getCompatibleActions().get(0) instanceof SupportPowerUpAction);
        supportPowerUpAction = (SupportPowerUpAction) branch.getCompatibleActions().get(0);
        supportPowerUpAction.initialize(p1);
        assertEquals(1, supportPowerUpAction.getPossiblePlayers().size());
        supportPowerUpAction.addTarget(p2);
        assertEquals(0, supportPowerUpAction.getPossiblePlayers().size());
        supportPowerUpAction.doAction();
        assertEquals(3, p2.getDamage().size());
        assertEquals(2, p2.getMark().size());
        branch.goNext(shootAction);
        assertTrue(branch.getCompatibleActions().get(0) instanceof EndBranchAction);
    }
}
