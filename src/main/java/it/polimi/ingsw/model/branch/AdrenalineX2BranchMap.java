package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.List;

public class AdrenalineX2BranchMap extends BranchMap
{
    public AdrenalineX2BranchMap(Player ownerPlayer)
    {
        super(ownerPlayer);
        this.setupBranches(createBranches());
    }

    private List<Branch> createBranches()
    {
        ArrayList<Branch> ret = new ArrayList<>();
        ret.add(getM1RW());
        ret.add(new Branch(new MoveAction(ownerPlayer,4), new EndBranchAction(ownerPlayer)));
        ret.add(getM2G());
        return ret;
    }

    private Branch getM1RW()
    {
        ArrayList<Action> actions =  new ArrayList<>();
        actions.add(new MoveAction(this.ownerPlayer, 1));
        actions.add(new ReloadSelectionAction(ownerPlayer));
        return new Branch(actions, new WeaponSelectionAction(ownerPlayer));
    }

    private Branch getM2G()
    {
        ArrayList<Action> actions =  new ArrayList<>();
        actions.add(new MoveAction(ownerPlayer, 2));
        actions.add(new GrabAction(ownerPlayer));
        return new Branch(actions, new EndBranchAction(ownerPlayer));
    }
}
