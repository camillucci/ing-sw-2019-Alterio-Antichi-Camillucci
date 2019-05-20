package it.polimi.ingsw.view;

public abstract class View
{
    private ViewElement curViewElement;
    public final Login login;
    public final ActionHandler actionHandler;
    public View (Login login, ActionHandler actionHandler){
        curViewElement = this.login = login;
        this.actionHandler = actionHandler;
    }

    public ViewElement getCurViewElement() {return curViewElement;}
}
