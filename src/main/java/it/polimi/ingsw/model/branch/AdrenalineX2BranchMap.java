package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.action.*;

import java.util.Arrays;
import java.util.List;

public class AdrenalineX2BranchMap extends BranchMap
{
    public AdrenalineX2BranchMap()
    {
        this.setupBranches(createBranches());
    }

    private List<Branch> createBranches()
    {
        return Arrays.asList(getM1RW(), getM4(), getM2G());
    }

    private Branch getM1RW()
    {
        return new Branch(new MoveAction(1), new ReloadSelectionAction(), new WeaponSelectionAction());
    }
    private Branch getM4()
    {
        return new Branch(new MoveAction(4), new EndBranchAction());
    }

    private Branch getM2G()
    {
        return new Branch(new MoveAction(2), new GrabAction(), new EndBranchAction());
    }
}
