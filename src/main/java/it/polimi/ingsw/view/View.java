package it.polimi.ingsw.view;

public abstract class View
{
    protected ViewElement curViewElement;
    protected Login login;
    protected ActionHandler actionHandler;

    protected View(){}
    protected View (Login login, ActionHandler actionHandler){
        curViewElement = this.login = login;
        this.actionHandler = actionHandler;
    }
    public Login getLogin() {
        return login;
    }

    public ViewElement getCurViewElement() {return curViewElement;}

    public ActionHandler getActionHandler() {
        return actionHandler;
    }
    public void loginCompleted() {
        this.curViewElement = actionHandler;
    }
}
