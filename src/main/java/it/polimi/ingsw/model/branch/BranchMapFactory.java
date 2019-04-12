package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.action.*;

public class BranchMapFactory
{
    private BranchMapFactory(){}
    public static BranchMap noAdrenaline()
    {
        return new BranchMap(
                new Branch(new MoveAction(1), new GrabAction(), new EndBranchAction()), //M1G
                new Branch(new MoveAction(3), new EndBranchAction()), //M3
                new Branch(new WeaponSelectionAction()) //W
        );
    }
    public static BranchMap threeDamage()
    {
        return new BranchMap(
                new Branch(new MoveAction(2), new GrabAction(), new EndBranchAction()), //M2G
                new Branch(new MoveAction(3), new EndBranchAction()), //M3
                new Branch(new WeaponSelectionAction())); //W
    }
    public static BranchMap sixDamage()
    {
        return new BranchMap(
                new Branch(new MoveAction(3), new EndBranchAction()), //M3
                new Branch(new MoveAction(2), new GrabAction(), new EndBranchAction()), //M2G
                new Branch(new MoveAction(1), new WeaponSelectionAction())); //M1W
    }
    public static BranchMap adrenalineX1()
    {
        return new BranchMap(
                new Branch(new MoveAction(2), new ReloadAction(), new WeaponSelectionAction()), //M2RW
                new Branch(new MoveAction(3), new GrabAction(), new EndBranchAction())); //M3G
    }
    public static BranchMap adrenalineX2()
    {
        return new BranchMap(
                new Branch(new MoveAction(1), new ReloadAction(), new WeaponSelectionAction()), //M!RW
                new Branch(new MoveAction(4), new EndBranchAction()), //M4
                new Branch(new MoveAction(2), new GrabAction(), new EndBranchAction())); //M2G
    }
    public static BranchMap reloadEndTurn()
    {
        return new BranchMap(new Branch(new ReloadAction(), new EndBranchAction()));
    }
}
