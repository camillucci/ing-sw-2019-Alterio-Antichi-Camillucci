package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.PowerUpAction;

import java.util.function.Supplier;

public class PowerUpCard {

    //public final PlayersFilter playersFilter;
    //public final SquaresFilter squaresFilter;
    //public final ShootFunc shootFunc;
    public final String name;
    public final AmmoColor color;
    public final Ammo cost;
    private Supplier<PowerUpAction> effectBuilder;

    public PowerUpCard(String name, AmmoColor color, Ammo cost, Supplier<PowerUpAction> effect)
    {
        this.name = name;
        this.color = color;
        this.cost = cost;
        this.effectBuilder = effect;
    }

    public PowerUpAction getEffect() {
        return effectBuilder.get();
    }
}
