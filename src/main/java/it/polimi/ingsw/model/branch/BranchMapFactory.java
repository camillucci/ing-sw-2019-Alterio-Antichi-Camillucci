package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PowerUpCard;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BranchMapFactory
{
    private BranchMapFactory(){}

    public static BranchMap noAdrenaline()
    {
        return new BranchMap(new ArrayList<>(Arrays.asList(
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(1), new GrabAction()), //PM1G
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(3), new EndBranchAction()), //PM3
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new WeaponSelectionAction())))); //PW
    }

    public static BranchMap threeDamage()
    {
        return new BranchMap(new ArrayList<>(Arrays.asList(
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(2), new GrabAction()), //PM2G
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(3), new EndBranchAction()), //PM3
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new WeaponSelectionAction())))); //PW
    }

    public static BranchMap sixDamage()
    {
        return new BranchMap(new ArrayList<>(Arrays.asList(
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(3), new EndBranchAction()), //PM3
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(2), new GrabAction()), //PM2G
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(1), new WeaponSelectionAction())))); //PM1W
    }

    public static BranchMap adrenalineX1()
    {
        return new BranchMap(new ArrayList<>(Arrays.asList(
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(2), new ReloadAction(), new WeaponSelectionAction()), //PM2RW
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(3), new GrabAction())))); //PM3G
    }

    public static BranchMap adrenalineX2()
    {
        return new BranchMap(new ArrayList<>(Arrays.asList(
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(1), new ReloadAction(), new WeaponSelectionAction()), //PM1RW
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(4), new EndBranchAction()), //PM4
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(2), new GrabAction())))); //PM2G
    }

    public static BranchMap spawnBranchMap(Player spawnPlayer)
    {
        List<Branch> branches = new ArrayList<>();
        for(PowerUpCard pu : spawnPlayer.getPowerUps())
            branches.add(new Branch(new Action(a ->
            {
                Player p = a.getOwnerPlayer();
                p.removePowerUpCard(pu);
                p.gameBoard.getSpawnAndShopSquare(pu.color).addPlayer(p);
            }), new EndBranchAction()));
        return new BranchMap(new Branch(new ExtendableAction(branches)));
    }

    public static BranchMap reloadEndTurn()
    {
        return new BranchMap(new ArrayList<>(Collections.singletonList(
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new ReloadAction(), new EndBranchAction())))); //PR
    }

    public static BranchMap powerUpBranchMap(PowerUpAction.Type type)
    {
        return new BranchMap(new Branch(new PowerUpAction(type), new EndBranchAction()));
    }

    /*
    private static List<Branch> addPowerups(List<Branch> branches, Player player)
    {
        for(PowerUpCard powerUpCard : player.getPowerUps())
            branches.addAll(powerUpCard.getEffect());
        return branches;
    }

     */
}
