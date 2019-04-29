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

    public static BranchMap noAdrenaline(Player player)
    {
        List<Branch> branches = new ArrayList<>(Arrays.asList(
                new Branch(new MoveAction(1), new GrabAction()), //M1G
                new Branch(new MoveAction(3), new EndBranchAction()), //M3
                new Branch(new WeaponSelectionAction()))); //W
        addPowerups(branches, player);
        return new BranchMap(branches);
    }

    public static BranchMap threeDamage(Player player)
    {
        List<Branch> branches = new ArrayList<>(Arrays.asList(
                new Branch(new MoveAction(2), new GrabAction()), //M2G
                new Branch(new MoveAction(3), new EndBranchAction()), //M3
                new Branch(new WeaponSelectionAction()))); //W
        addPowerups(branches, player);
        return new BranchMap(branches);
    }

    public static BranchMap sixDamage(Player player)
    {
        List<Branch> branches = new ArrayList<>(Arrays.asList(
                new Branch(new MoveAction(3), new EndBranchAction()), //M3
                new Branch(new MoveAction(2), new GrabAction()), //M2G
                new Branch(new MoveAction(1), new WeaponSelectionAction()))); //M1W
        addPowerups(branches, player);
        return new BranchMap(branches);
    }

    public static BranchMap adrenalineX1(Player player)
    {
        List<Branch> branches = new ArrayList<>(Arrays.asList(
                new Branch(new MoveAction(2), new ReloadAction(), new WeaponSelectionAction()), //M2RW
                new Branch(new MoveAction(3), new GrabAction()))); //M3G
        addPowerups(branches, player);
        return new BranchMap(branches);
    }

    public static BranchMap adrenalineX2(Player player)
    {
        List<Branch> branches = new ArrayList<>(Arrays.asList(
                new Branch(new MoveAction(1), new ReloadAction(), new WeaponSelectionAction()), //M!RW
                new Branch(new MoveAction(4), new EndBranchAction()), //M4
                new Branch(new MoveAction(2), new GrabAction()))); //M2G
        addPowerups(branches, player);
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

    public static BranchMap reloadEndTurn(Player player)
    {
        List<Branch> branches = new ArrayList<>(Arrays.asList(
                new Branch(new ReloadAction(), new EndBranchAction())));
        addPowerups(branches, player);
        return new BranchMap(branches);
    }

    private static List<Branch> addPowerups(List<Branch> branches, Player player)
    {
        for(PowerUpCard powerUpCard : player.getPowerUps())
            branches.addAll(powerUpCard.getBranches());
        return branches;
    }
}
