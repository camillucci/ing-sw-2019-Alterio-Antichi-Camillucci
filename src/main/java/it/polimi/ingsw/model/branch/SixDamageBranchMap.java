package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.List;

public class SixDamageBranchMap extends ThreeDamageBranchMap
{
    public SixDamageBranchMap()
    {
        this.setupBranches(createBranches());
    }

    @Override
    protected List<Branch> createBranches()
    {
        ArrayList<Branch> branches = new ArrayList<>();
        branches.add(getM3());
        branches.add(getM2G());
        branches.add(getM1W());
        return branches;
    }

    private Branch getM1W()
    {
        return new Branch(new MoveAction(1), new WeaponSelectionAction());
    }
}
