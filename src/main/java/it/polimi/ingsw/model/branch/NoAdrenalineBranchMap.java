package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.action.*;

import java.util.Arrays;
import java.util.List;

public class NoAdrenalineBranchMap extends BranchMap
{
    public NoAdrenalineBranchMap()
    {
        this.setupBranches(createBranches());
    }

    protected List<Branch> createBranches()
    {
        return Arrays.asList(getM1G(), getM3(), getW());
    }
    protected Branch getM1G()
    {
        return new Branch(new MoveAction(1), new GrabAction(), new EndBranchAction());
    }

    protected Branch getM3()
    {
        return new Branch(new MoveAction(3), new EndBranchAction());
    }

    protected Branch getW()
    {
        return new Branch(new WeaponSelectionAction());
    }
}
