package it.polimi.ingsw.snapshots;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all info relative to the current turn's player that clients are going to need. It implements
 * Serializable in order to be able to be sent to the client.
 */
public class PrivatePlayerSnapshot extends PublicPlayerSnapshot {

    /**
     * List of strings which represent the name of all loaded weapons current player has
     */
    private final List<String> loadedWeapons = new ArrayList<>();

    /**
     * List of strings which represent all the costs associated to the loaded weapons current player has
     */
    private final List<String> loadedWeaponsCost = new ArrayList<>();

    /**
     * List of strings which represent all the powerup cards current player has in hand
     */
    private final List<String> powerUps = new ArrayList<>();

    /**
     * Constructor. It collects all the info relative to current player that clients are going to need.
     * @param player Players from which the infos are going to be selected.
     * @param finalFrenzy It say if the match has reached the Final Frenzy mode.
     */
    protected PrivatePlayerSnapshot(Player player, boolean finalFrenzy) {
        super(player, finalFrenzy);
        for(WeaponCard weaponCard : player.getLoadedWeapons()) {
            loadedWeapons.add(weaponCard.name);
            loadedWeaponsCost.add(weaponCard.buyCostToString(false));
        }
        for(PowerUpCard powerUpCard : player.getPowerUps())
            powerUps.add(powerUpCard.getName());
    }

    public List<String> getLoadedWeapons() {
        return loadedWeapons;
    }

    public List<String> getPowerUps() {
        return powerUps;
    }

    public List<String> getLoadedWeaponsCost() {
        return loadedWeaponsCost;
    }
}
