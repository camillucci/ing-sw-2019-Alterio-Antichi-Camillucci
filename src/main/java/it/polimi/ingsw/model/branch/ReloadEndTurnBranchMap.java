package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.EndBranchAction;
import it.polimi.ingsw.model.action.ReloadAction;

import java.util.Collections;

public class ReloadEndTurnBranchMap extends BranchMap
{
    public ReloadEndTurnBranchMap(Player ownerPlayer)
    {
        super(Collections.singletonList(new Branch(new ReloadAction(), new EndBranchAction())));
    }
}
