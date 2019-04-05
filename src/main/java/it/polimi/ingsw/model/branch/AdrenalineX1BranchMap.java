package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.List;

public class AdrenalineX1BranchMap extends BranchMap
{
    public AdrenalineX1BranchMap(Player ownerPlayer)
    {
        super(ownerPlayer);
        this.setupBranches(createBranches());
    }

    private List<Branch> createBranches()
    {
        ArrayList<Branch> ret = new ArrayList<>();
        ret.add(getM2RW());
        ret.add(getM3G());
        return ret;
    }

    private Branch getM2RW()
    {
        ArrayList<Action> actions =  new ArrayList<>();
        actions.add(new MoveAction(this.ownerPlayer, 2));
        actions.add(new ReloadSelectionAction(ownerPlayer));
        return new Branch(actions, new WeaponSelectionAction(ownerPlayer));
    }

    private Branch getM3G()
    {
        ArrayList<Action> actions =  new ArrayList<>();
        actions.add(new MoveAction(ownerPlayer, 3));
        actions.add(new GrabAction(ownerPlayer));
        return new Branch(actions, new EndBranchAction(ownerPlayer));
    }
}
