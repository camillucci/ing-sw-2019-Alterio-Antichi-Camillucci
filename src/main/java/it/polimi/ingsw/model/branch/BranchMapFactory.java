package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PowerUpCard;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BranchMapFactory
{
    private BranchMapFactory(){}

    public static BranchMap noAdrenaline()
    {
        List<Branch> branches = new ArrayList<>(Arrays.asList(
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(1), new GrabAction()), //PM1G
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(3), new EndBranchAction()), //PM3
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new WeaponSelectionAction()))); //PW
        return new BranchMap(branches);
    }

    public static BranchMap threeDamage()
    {
        List<Branch> branches = new ArrayList<>(Arrays.asList(
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(2), new GrabAction()), //PM2G
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(3), new EndBranchAction()), //PM3
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new WeaponSelectionAction()))); //PW
        return new BranchMap(branches);
    }

    public static BranchMap sixDamage()
    {
        List<Branch> branches = new ArrayList<>(Arrays.asList(
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(3), new EndBranchAction()), //PM3
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(2), new GrabAction()), //PM2G
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(1), new WeaponSelectionAction()))); //PM1W
        return new BranchMap(branches);
    }

    public static BranchMap adrenalineX1()
    {
        List<Branch> branches = new ArrayList<>(Arrays.asList(
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(2), new ReloadAction(), new WeaponSelectionAction()), //PM2RW
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(3), new GrabAction()))); //PM3G
        return new BranchMap(branches);
    }

    public static BranchMap adrenalineX2()
    {
        List<Branch> branches = new ArrayList<>(Arrays.asList(
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(1), new ReloadAction(), new WeaponSelectionAction()), //PM1RW
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(4), new EndBranchAction()), //PM4
                new Branch(new PowerUpAction(PowerUpAction.Type.END_START_MOVE), new MoveAction(2), new GrabAction()))); //PM2G
        //addPowerups(branches, player);
        return new BranchMap(branches);
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
        List<Branch> branches = new ArrayList<>(Arrays.asList(
                new Branch(new ReloadAction(), new EndBranchAction())));
        //addPowerups(branches, player);
        return new BranchMap(branches);
    }

    public static BranchMap PowerUpBranchMap(Player player, PowerUpAction.Type type)
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
