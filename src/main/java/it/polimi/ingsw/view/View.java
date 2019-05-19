package it.polimi.ingsw.view;

public abstract class View
{
    private ViewElement curViewElement;
    public final Login login;
    public View (Login login){
        curViewElement = this.login = login;
    }

    public ViewElement getCurViewElement() {return curViewElement;}
}
