package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class is used to generate default branch maps based on what point of the game we are in  and what board state
 * there currently is.
 */
public class BranchMapFactory
{
    private BranchMapFactory(){}

    /**
     * Generates a default branch map for when the current turn's player is no adrenaline (this method is called
     * based on the amount of damage the player has).
     * @return The default generated branch map for when a player has no adrenaline
     */
    public static BranchMap noAdrenaline()
    {
        return new BranchMap(new ArrayList<>(Arrays.asList(
                new Branch(new PowerUpAction(), new MoveAction(1), new GrabAction()), //PM1G
                new Branch(new PowerUpAction(), new MoveAction(3), new EndBranchAction()), //PM3
                new Branch(new PowerUpAction(), new WeaponSelectionAction())))); //PW
    }

    /**
     * Generates a default branch map for when the current turn's player has between 3 and 5 drops of damage
     * @return The default generated branch map for when a player has between 3 and 5 drops of damage
     */
    public static BranchMap threeDamage()
    {
        return new BranchMap(new ArrayList<>(Arrays.asList(
                new Branch(new PowerUpAction(), new MoveAction(2), new GrabAction()), //PM2G
                new Branch(new PowerUpAction(), new MoveAction(3), new EndBranchAction()), //PM3
                new Branch(new PowerUpAction(), new WeaponSelectionAction())))); //PW
    }

    /**
     * Generates a default branch map for when the current turn's player has 6 or more drops of damage
     * @return The default generated branch map for when a player has between 6 or more drops of damage
     */
    public static BranchMap sixDamage()
    {
        return new BranchMap(new ArrayList<>(Arrays.asList(
                new Branch(new PowerUpAction(), new MoveAction(3), new EndBranchAction()), //PM3
                new Branch(new PowerUpAction(), new MoveAction(2), new GrabAction()), //PM2G
                new Branch(new PowerUpAction(), new MoveAction(1), new WeaponSelectionAction())))); //PM1W
    }

    /**
     * Generates a default branch map for when the current turn's player has the adrenaline and has the restrictions
     * based on turn order (this method is called only if the last part of the game has been reached).
     * @return The default generated branch map for when a player has a the adrenaline and has restrictions based on
     * turn order
     */
    public static BranchMap adrenalineX1()
    {
        return new BranchMap(new ArrayList<>(Arrays.asList(
                new Branch(new PowerUpAction(), new MoveAction(2), new ReloadAction(), new WeaponSelectionAction()), //PM2RW
                new Branch(new PowerUpAction(), new MoveAction(3), new GrabAction())))); //PM3G
    }

    /**
     * Generates a default branch map for when the current turn's player has the adrenaline and has no restrictions
     * based on turn order (this method is called only if the last part of the game has been reached).
     * @return The default generated branch map for when a player has a the adrenaline and has no restrictions based on
     * turn order
     */
    public static BranchMap adrenalineX2()
    {
        return new BranchMap(new ArrayList<>(Arrays.asList(
                new Branch(new PowerUpAction(), new MoveAction(1), new ReloadAction(), new WeaponSelectionAction()), //PM1RW
                new Branch(new PowerUpAction(), new MoveAction(4), new EndBranchAction()), //PM4
                new Branch(new PowerUpAction(), new MoveAction(2), new GrabAction())))); //PM2G
    }

    /**
     * Generates a branch map for when the player has died and needs to respawn. It does so by creating branches based
     * on the power up cards the player has in hand and the one they draw for the spawn.
     * @param spawnPlayer Player who needs to respawn
     * @return The generated branch map relative to the respawn of the player
     */
    public static BranchMap spawnBranchMap(Player spawnPlayer)
    {
        List<Branch> branches = new ArrayList<>();
        for(PowerUpCard pu : spawnPlayer.getPowerUps())
            branches.add(new Branch(new FunctionalAction(a ->
            {
                Player p = a.getOwnerPlayer();
                p.removePowerUpCard(pu);
                p.setCurrentSquare(p.gameBoard.getSpawnAndShopSquare(pu.color));
                p.getCurrentSquare().addPlayer(p);
            }, new Visualizable("/powerup/" + pu.getName().replace(" ", "_") + ".png","discard " + pu.getName(), "Spawn")), new EndBranchAction()));
        return new BranchMap(branches);
    }

    /**
     * Generates a default branch map for when the current turn's player reaches the end of their turn and has to
     * decide which weapons to reload
     * @return The generated branch map created based on the weapons available to the current turn's player
     */
    public static BranchMap reloadEndTurn()
    {
        return new BranchMap(new ArrayList<>(Collections.singletonList(
                new Branch(new PowerUpAction(), new ReloadAction(), new EndBranchAction())))); //PR
    }

    /**
     * Generates a default branch map for a player (who's not on turn) gets damaged by another player. The branch map
     * is generated by taking into account how many counterAttack power up cards the player in question has.
     * @return The default generated branch map relative to the possible counterAttack power up cards a player can use
     * when they are damaged by another player.
     */
    public static BranchMap counterAttackBranchMap()
    {
        return new BranchMap(new Branch(new CounterPowerUpAction(), new EndBranchAction()));
    }
}
