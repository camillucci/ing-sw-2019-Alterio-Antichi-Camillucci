package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.List;

public class ThreeDamageBranchMap extends NoAdrenalineBranchMap
{
    public ThreeDamageBranchMap(Player ownerPlayer)
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
        branches.add(getW());
        return branches;
    }
    protected Branch getM2G()
    {
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(new MoveAction(this.ownerPlayer, 2));
        actions.add(new GrabAction(this.ownerPlayer));
        return new Branch(actions, new EndBranchAction(this.ownerPlayer));
    }
}
