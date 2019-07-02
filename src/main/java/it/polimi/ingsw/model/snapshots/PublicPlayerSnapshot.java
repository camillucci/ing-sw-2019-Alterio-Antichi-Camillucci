package it.polimi.ingsw.model.snapshots;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains info relative to a player (who is not the current player) that clients are going to need.
 * All the info present in this class is accessible even by the other players.
 * It implements Serializable in order to be able to be sent to the client.
 */
public class PublicPlayerSnapshot implements Serializable {

    /**
     * String that represents the color associated with the player
     */
    public final String color;

    /**
     * String that represents the name associated with the player
     */
    public final String name;

    /**
     * String that represents the number of deaths of the player
     */
    public final int skull;

    /**
     * String that represents the amount of blue ammo the player has available
     */
    public final int blueAmmo;

    /**
     * String that represents the amount of red ammo the player has available
     */
    public final int redAmmo;

    /**
     * String that represents the amount of yellow ammo the player has available
     */
    public final int yellowAmmo;

    /**
     * Boolean that represents whether the player has access to the bonuses granted by the final frenzy condition
     */
    public final int finalFrenzy;

    /**
     * List of strings that represents the amount of damage the player currently has. Every string is a color, which
     * associates the damage with another player
     */
    private final List<String> damage = new ArrayList<>();

    /**
     * List of strings that represents the amount of marks the player currently has. Every string is a color, which
     * associates the mark with another player
     */
    private final List<String> mark = new ArrayList<>();

    /**
     * List of strings which represents, by name, all the unloaded weapons the player has.
     */
    private final List<String> unloadedWeapons = new ArrayList<>();

    /**
     * List of strings which represent the costs associated with every unloaded weapon the player has.
     */
    private final List<String> unloadedWeaponsCost = new ArrayList<>();

    /**
     * Integer used to represent the amount of loaded weapons that the player has.
     */
    public final int loadedWeaponsNumber;

    /**
     * Integer used to represent the amount of powerup cards that the player has.
     */
    public final int powerUpsNumber;

    /**
     * Constructor. It collects all the info relative to the player that is visible by the other players.
     * @param player Players from which the infos are going to be selected.
     * @param finalFrenzy It say if the match has reached the Final Frenzy mode.
     */
    public PublicPlayerSnapshot(Player player, boolean finalFrenzy) {
        this.color = player.color.getName();
        this.name = player.name;
        this.skull = player.getSkull();
        this.blueAmmo = player.getAmmo().blue;
        this.redAmmo = player.getAmmo().red;
        this.yellowAmmo = player.getAmmo().yellow;
        if(!finalFrenzy)
            this.finalFrenzy = 1;
        else if(!player.isFinalFrenzy())
            this.finalFrenzy = 2;
        else
            this.finalFrenzy = 3;
        for(PlayerColor damageColor : player.getDamage())
            this.damage.add(damageColor.getName());
        for(PlayerColor markColor : player.getMark())
            this.mark.add(markColor.getName());
        this.loadedWeaponsNumber = player.getLoadedWeapons().size();
        for(WeaponCard weaponCard : player.getUnloadedWeapons()) {
            unloadedWeapons.add(weaponCard.name);
            unloadedWeaponsCost.add(weaponCard.buyCostToString(false));
        }
        this.powerUpsNumber = player.getPowerUps().size();
    }

    public List<String> getDamage() {
        return damage;
    }

    public List<String> getMark() {
        return mark;
    }

    public List<String> getUnloadedWeapons() {
        return unloadedWeapons;
    }

    public List<String> getUnloadedWeaponsCost() {
        return unloadedWeaponsCost;
    }
}
