package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.ExtendableAction;

import java.util.Arrays;
import java.util.List;

public class BranchTestUtilities
{
    public static boolean testEquality(List<Action> curActions, List<Action> expectedActions)
    {
        if(expectedActions.size() != curActions.size())
            return false;

        for(Action action : expectedActions)
            for(int i=0; i < curActions.size(); i++)
                if( (curActions.get(i).isCompatible(action) && action.isCompatible(curActions.get(i))) || (action instanceof ExtendableAction && curActions.get(i) instanceof  ExtendableAction))
                {
                    curActions.remove(i);
                    break;
                }
        return  curActions.isEmpty();
    }

    public static boolean testEquality(List<Action> curActions, Action ... expectedActions)
    {
        return testEquality(curActions, Arrays.asList(expectedActions));
    }

}
