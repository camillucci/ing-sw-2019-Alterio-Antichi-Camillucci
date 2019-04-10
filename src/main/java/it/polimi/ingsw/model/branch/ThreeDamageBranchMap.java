package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.action.*;

import java.util.Arrays;
import java.util.List;

public class ThreeDamageBranchMap extends NoAdrenalineBranchMap
{
    public ThreeDamageBranchMap()
    {
        this.setupBranches(createBranches());
    }

    @Override
    protected List<Branch> createBranches()
    {
        return Arrays.asList(getM3(), getM2G(), getW());
    }
    protected Branch getM2G()
    {
        return new Branch(new MoveAction(2), new GrabAction(), new EndBranchAction());
    }
}
