package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.PowerUpAction;
import it.polimi.ingsw.model.action.ShootAction;

import java.util.function.Supplier;

public abstract class PowerUpCard {
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

    public abstract void addTo(PowerupSet powerupSet);
    public abstract void removeFrom(PowerupSet powerupSet);

    public PowerUpAction getEffect() { return effectBuilder.get(); }

    public String getName() {
        return name;
    }

    public AmmoColor getColor() {
        return color;
    }
}
