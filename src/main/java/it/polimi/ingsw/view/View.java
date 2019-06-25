package it.polimi.ingsw.view;

public abstract class View
{
    protected ViewElement curViewElement;
    protected Login login;
    protected ActionHandler actionHandler;

    protected void buildView(Login login, ActionHandler actionHandler){
        curViewElement = this.login = login;
        this.actionHandler = actionHandler;
        login.loginCompletedEvent.addEventHandler((a, snapshot) -> {
            this.curViewElement = actionHandler;
            actionHandler.onModelChanged(snapshot);
        });
    }

    public Login getLogin() {
        return login;
    }

    public ViewElement getCurViewElement() {return curViewElement;}

    public ActionHandler getActionHandler() {
        return actionHandler;
    }
}
