package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PowerUpSet;
import it.polimi.ingsw.model.action.PowerUpAction;

import java.util.function.Supplier;

/**
 * This class contains all the generic methods and information common between all different types of power up cards.
 */
public abstract class PowerUpCard {

    /**
     * Name that distinguishes the card from the others present in the game
     */
    public final String name;

    /**
     * Color of the ammo the card produces by being discarded
     */
    public final AmmoColor color;
    private Supplier<PowerUpAction> effectBuilder;

    /**
     * Constructor. It assigns all the input parameters to their global correspondences
     * @param name Name that distinguishes the card from the others present in the game
     * @param color Color of the ammo the card produces by being discarded
     * @param effect //todo
     */
    public PowerUpCard(String name, AmmoColor color, Supplier<PowerUpAction> effect)
    {
        this.name = name;
        this.color = color;
        this.effectBuilder = effect;
    }

    public PowerUpAction getEffect() {
        return effectBuilder.get();
    }

    /**
     * This method calls an add method from the power up set of cards the card needs to added to.
     * @param powerupSet List of power up cards
     */
    public abstract void addTo(PowerUpSet powerupSet);

    /**
     * This method calls an add method from the power up set of cards the card needs to removed from.
     * @param powerupSet List of power up cards
     */
    public abstract void removeFrom(PowerUpSet powerupSet);

    public String getName() {
        return name.concat(" " + color.getName());
    }

    /**
     * Calculates which color of ammo the card produces when discarded
     * @return The color of ammo associated with the card
     */
    public Ammo colorToAmmo() {
        if(color == AmmoColor.BLUE)
            return new Ammo(1, 0, 0);
        if(color == AmmoColor.RED)
            return new Ammo(0, 1, 0);
        return new Ammo(0, 0, 1);
    }
}
