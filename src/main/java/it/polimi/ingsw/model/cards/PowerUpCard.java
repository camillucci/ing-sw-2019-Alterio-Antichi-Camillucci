package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PowerUpSet;
import it.polimi.ingsw.model.action.PowerUpAction;

import java.util.function.Supplier;

public abstract class PowerUpCard {

    public final String name;
    public final AmmoColor color;
    private Supplier<PowerUpAction> effectBuilder;

    public PowerUpCard(String name, AmmoColor color, Supplier<PowerUpAction> effect)
    {
        this.name = name;
        this.color = color;
        this.effectBuilder = effect;
    }

    public PowerUpAction getEffect() {
        return effectBuilder.get();
    }

    public abstract void addTo(PowerUpSet powerupSet);

    public abstract void removeFrom(PowerUpSet powerupSet);

    public String getName() {
        return name.concat(" " + color.getName());
    }

    public Ammo colorToAmmo() {
        if(color == AmmoColor.BLUE)
            return new Ammo(1, 0, 0);
        if(color == AmmoColor.RED)
            return new Ammo(0, 1, 0);
        return new Ammo(0, 0, 1);
    }
}
