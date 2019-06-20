package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
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
                if(isEqual(action, curActions.get(i)))
                {
                    curActions.remove(i);
                    break;
                }
        return  curActions.isEmpty();
    }

    public static boolean isEqual(Action a, Action b)
    {
        if(a instanceof MoveAction && b instanceof MoveAction)
            return a.isCompatible(b) && b.isCompatible(a);
        return a.getClass().equals(b.getClass());
    }
    public static Action search(List<Action> actions, Action key)
    {
        for(Action a : actions)
            if(isEqual(a, key))
                return a;
        return null;
    }
    public static void searchAndDo(List<Action> actions, Action action)
    {
        actions.stream().filter(a->BranchTestUtilities.isEqual(a, action)).forEach(Action::doAction);
    }
    public static boolean testEquality(List<Action> curActions, Action ... expectedActions)
    {
        return testEquality(curActions, Arrays.asList(expectedActions));
    }

    public static ArrayList<Action> noAdrenalinePossibleActions()
    {
        return new ArrayList<>(Arrays.asList(
                new PowerUpAction(), //P
                new MoveAction(1), //M1
                new MoveAction(3), //M3
                new GrabAction(), //G
                new WeaponSelectionAction(), //W
                new RollBackAction(), //R
                new EndBranchAction())); //EndBranch
    }

    public static ArrayList<Action> threeDamagePossibleActions()
    {
        return new ArrayList<>(Arrays.asList(
                new PowerUpAction(), //P
                new MoveAction(2), //M2
                new GrabAction(), //G
                new MoveAction(3), //M3
                new WeaponSelectionAction(), //W
                new RollBackAction(), //R
                new EndBranchAction())); //EndBranch
    }

    public static ArrayList<Action> reloadEndTurnPossibleActions()
    {
        return new ArrayList<>(Arrays.asList(
                new PowerUpAction(), //P
                new ReloadAction(), //R
                new RollBackAction(), //R
                new EndBranchAction())); //EndBranch
    }
}
