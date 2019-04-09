package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.EndBranchAction;
import it.polimi.ingsw.model.action.ReloadSelectionAction;

import java.util.Collections;

public class ReloadEndTurnBranchMap extends BranchMap
{
    public ReloadEndTurnBranchMap(Player ownerPlayer)
    {
        super(ownerPlayer, Collections.singletonList(new Branch(new ReloadSelectionAction(ownerPlayer), new EndBranchAction(ownerPlayer))));
    }
}
