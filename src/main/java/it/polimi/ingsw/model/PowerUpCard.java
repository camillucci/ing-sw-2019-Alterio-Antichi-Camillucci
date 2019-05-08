package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.PowerUpAction;

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

    public PowerUpAction getEffect() {
        return effectBuilder.get();
    }

    public abstract void addTo(PowerupSet powerupSet);

    public abstract void removeFrom(PowerupSet powerupSet);

    public String getName() {
        return name.concat(" " + color.getName());
    }

    public Ammo colorToAmmo() {
        if(color == AmmoColor.BLUE)
            return new Ammo(1, 0, 0);
        if(color == AmmoColor.RED)
            return new Ammo(0, 1, 1);
        return new Ammo(0, 0, 1);
    }
}
