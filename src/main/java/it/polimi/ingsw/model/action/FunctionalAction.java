package it.polimi.ingsw.model.action;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class FunctionalAction extends Action
{
    private Consumer<Action> opMethod;

    /**
     * @param doActionCost Cost of the action
     * @param isOptional true iff isOptional == true
     * @param doActionMethod invoked when doAction() is invoked
     * @param text textual description of the action
     */
    public FunctionalAction(Ammo doActionCost,  boolean isOptional,  Consumer<Action> doActionMethod, String text)
    {
        this.opMethod = doActionMethod;
        this.doActionCost = doActionCost;
        this.optional = isOptional;
        this.text = text;
    }

    public FunctionalAction(Ammo doActionCost, Consumer<Action> doActionMethod, String text)
    {
        this(doActionCost, false, doActionMethod, text);
    }

    public FunctionalAction(Consumer<Action> doActionMethod, String text)
    {
        this(new Ammo(0,0,0), doActionMethod, text);
    }

    @Override
    protected void op() {
        opMethod.accept(this);
    }

    @Override
    public boolean isCompatible(Action action)
    {
        return action == this;
    }

}
