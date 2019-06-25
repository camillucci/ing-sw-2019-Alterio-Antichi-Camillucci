package it.polimi.ingsw.model.snapshots;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.List;

public class PrivatePlayerSnapshot extends PublicPlayerSnapshot {

    private final List<String> loadedWeapons = new ArrayList<>();
    private final List<String> loadedWeaponsCost = new ArrayList<>();
    private final List<String> powerUps = new ArrayList<>();

    protected PrivatePlayerSnapshot(Player player) {
        super(player);
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
