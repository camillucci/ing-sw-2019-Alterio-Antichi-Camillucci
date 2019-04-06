package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.List;

public class NoAdrenalineBranchMap extends BranchMap
{
    public NoAdrenalineBranchMap(Player ownerPlayer)
    {
        super(ownerPlayer);
        this.setupBranches(createBranches());
    }

    protected List<Branch> createBranches()
    {
        ArrayList<Branch> branches = new ArrayList<>();
        branches.add(getM1G());
        branches.add(getW());
        branches.add(getM3());
        return branches;
    }
    protected Branch getM1G()
    {
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(new MoveAction(this.ownerPlayer, 1));
        actions.add(new GrabAction(this.ownerPlayer));
        return new Branch(actions, new EndBranchAction(this.ownerPlayer));
    }

    protected Branch getM3()
    {
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(new MoveAction(this.ownerPlayer, 3));
        return new Branch(actions, new EndBranchAction(this.ownerPlayer));
    }

    protected Branch getW()
    {
        return new Branch(new WeaponSelectionAction(this.ownerPlayer));
    }
}