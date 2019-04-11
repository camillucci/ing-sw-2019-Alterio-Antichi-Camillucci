package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.action.*;

import java.util.Arrays;
import java.util.List;

public class AdrenalineX1BranchMap extends BranchMap
{
    public AdrenalineX1BranchMap()
    {
        this.setupBranches(createBranches());
    }

    private List<Branch> createBranches()
    {
        return Arrays.asList(getM2RW(), getM3G());
    }

    private Branch getM2RW()
    {
        return new Branch(new MoveAction(2), new ReloadAction(), new WeaponSelectionAction());
    }

    private Branch getM3G()
    {
        return new Branch(new MoveAction(3), new GrabAction(), new EndBranchAction());
    }
}
