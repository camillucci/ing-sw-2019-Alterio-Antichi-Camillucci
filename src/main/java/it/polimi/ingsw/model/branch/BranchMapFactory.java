package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PowerUpCard;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.List;

public class BranchMapFactory
{
    private BranchMapFactory(){}
    public static BranchMap noAdrenaline()
    {
        return new BranchMap(
                new Branch(new MoveAction(1), new GrabAction()), //M1G
                new Branch(new MoveAction(3), new EndBranchAction()), //M3
                new Branch(new WeaponSelectionAction()) //W
        );
    }

    public static BranchMap threeDamage()
    {
        return new BranchMap(
                new Branch(new MoveAction(2), new GrabAction()), //M2G
                new Branch(new MoveAction(3), new EndBranchAction()), //M3
                new Branch(new WeaponSelectionAction())); //W
    }

    public static BranchMap sixDamage()
    {
        return new BranchMap(
                new Branch(new MoveAction(3), new EndBranchAction()), //M3
                new Branch(new MoveAction(2), new GrabAction()), //M2G
                new Branch(new MoveAction(1), new WeaponSelectionAction())); //M1W
    }

    public static BranchMap adrenalineX1()
    {
        return new BranchMap(
                new Branch(new MoveAction(2), new ReloadAction(), new WeaponSelectionAction()), //M2RW
                new Branch(new MoveAction(3), new GrabAction())); //M3G
    }

    public static BranchMap adrenalineX2()
    {
        return new BranchMap(
                new Branch(new MoveAction(1), new ReloadAction(), new WeaponSelectionAction()), //M!RW
                new Branch(new MoveAction(4), new EndBranchAction()), //M4
                new Branch(new MoveAction(2), new GrabAction())); //M2G
    }

    public static BranchMap spawnBranchMap(Player spawnPlayer)
    {
        List<Branch> branches = new ArrayList<>();
        for(PowerUpCard pu : spawnPlayer.getPowerUps())
            branches.add(new Branch(new Action(a->
            {
                Player p = a.getOwnerPlayer();
                p.removePowerUpCard(pu);
                p.gameBoard.getSpawnAndShopSquare(pu.getColor()).addPlayer(p);
            }), new EndBranchAction()));
        return new BranchMap(new Branch(new ExtendableAction(branches)));
    }

    public static BranchMap reloadEndTurn()
    {
        return new BranchMap(new Branch(new ReloadAction(), new EndBranchAction()));
    }
}
