package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Branch
{
    public Branch(List<Action> actions, ExtAction endAction)
    {
        this.actions = new ArrayList<>(actions); // forse ci sarebbe da copiare anche ogni action dell'arraylist
        this.endAction = endAction;
    }
    public Action getCurAction()
    {
        if(actions.isEmpty())
            return this.actions.get(0);
        return endAction;
    }
    public void addActionCompletedSubscriber(ActionCompletedSubscriber sub)
    {
        for(Action a : this.actions)
            a.addCompletedActionSubscriber(sub);
    }
    public boolean goNext()
    {
        if(actions.isEmpty())
            return false;
        this.actions.remove(0);
        return true;
    }
    public ExtAction getEndAction()
    {
        return this.endAction;
    }
    private ArrayList<Action> actions;
    private ExtAction endAction;
}
