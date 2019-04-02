package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Branch
{
    public Event BranchActionCompletedEvent = new Event();
    public Branch(List<Action> actions, ExtAction endAction) {
        this.actions = new ArrayList<>(actions); // forse ci sarebbe da copiare anche ogni action dell'arraylist
        this.endAction = endAction;
        for (Action a : this.actions)
            a.CompletedActionEvent.addEventHandler(this::OnActionComplete);
        endAction.CompletedActionEvent.addEventHandler(this::OnActionComplete);
    }
    public Branch(ExtAction endAction)
    {
        this(new ArrayList<Action>(),endAction);
    }
    private void OnActionComplete(Object Sender, Object args)
    {
        BranchActionCompletedEvent.invoke(Sender, args);
    }
    public Action getCurAction()
    {
        if(!actions.isEmpty())
            return this.actions.get(0);
        return endAction;
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
