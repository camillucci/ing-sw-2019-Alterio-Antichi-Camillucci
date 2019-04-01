package it.polimi.ingsw.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Branch
{
    public Branch(ArrayList<Action> actions, ExtAction EndAction)
    {
        this.actions = new ArrayList<>(actions); // forse ci sarebbe da copiare anche ogni action dell'arraylist
        this.endAction = EndAction;
    }
    public Action GetCurAction()
    {
        if(actions.isEmpty())
            return this.actions.get(0);
        return endAction;
    }
    public void AddActionCompletedSubscriber(ActionCompletedSubscriber sub)
    {
        for(Action a : this.actions)
            a.addCompletedActionSubscriber(sub);
    }
    public boolean GoNext()
    {
        if(actions.isEmpty())
            return false;
        this.actions.remove(0);
        return true;
    }
    public ExtAction GetEndAction()
    {
        return this.endAction;
    }
    private ArrayList<Action> actions;
    private ExtAction endAction;
}
