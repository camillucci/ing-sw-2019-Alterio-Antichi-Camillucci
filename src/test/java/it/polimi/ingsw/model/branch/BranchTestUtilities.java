package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.action.Action;

import java.util.List;

public class BranchTestUtilities
{
    public static boolean testEquality(List<Action> curActions, List<Action> expectedActions)
    {
        if(expectedActions.size() != curActions.size())
            return false;

        for(Action action : expectedActions)
            for(int i=0; i < curActions.size(); i++)
                if(curActions.get(i).isCompatible(action) && action.isCompatible(curActions.get(i)))
                {
                    curActions.remove(i);
                    break;
                }
        return  curActions.isEmpty();
    }

}
