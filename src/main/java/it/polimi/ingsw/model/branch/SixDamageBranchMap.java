package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.List;

public class SixDamageBranchMap extends ThreeDamageBranchMap
{
    public SixDamageBranchMap(Player ownerPlayer)
    {
        super(ownerPlayer);
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
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(new MoveAction(this.ownerPlayer, 1));
        return new Branch(actions, new WeaponSelectionAction(this.ownerPlayer));
    }
}
