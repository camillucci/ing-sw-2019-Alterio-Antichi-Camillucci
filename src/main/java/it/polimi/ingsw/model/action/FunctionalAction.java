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
     * @param visualizable description of the action
     */
    public FunctionalAction(Ammo doActionCost,  boolean isOptional,  Consumer<Action> doActionMethod, Visualizable visualizable)
    {
        this.opMethod = doActionMethod;
        this.doActionCost = doActionCost;
        this.optional = isOptional;
        this.visualizable = visualizable;
    }

    public FunctionalAction(Ammo doActionCost, Consumer<Action> doActionMethod, Visualizable visualizable)
    {
        this(doActionCost, false, doActionMethod, visualizable);
    }

    public FunctionalAction(Consumer<Action> doActionMethod, Visualizable visualizable)
    {
        this(new Ammo(0,0,0), doActionMethod, visualizable);
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
