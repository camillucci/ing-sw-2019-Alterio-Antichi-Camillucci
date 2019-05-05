package it.polimi.ingsw.model.snapshots;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PowerUpCard;
import it.polimi.ingsw.model.WeaponCard;

import java.util.ArrayList;
import java.util.List;

public class PrivatePlayerSnapshot extends PublicPlayerSnapshot {

    public final List<String> loadedWeapons = new ArrayList<>();
    public final List<String> powerUps = new ArrayList<>();

    protected PrivatePlayerSnapshot(Player player) {
        super(player);
        for(WeaponCard weaponCard : player.getLoadedWeapons())
            loadedWeapons.add(weaponCard.name);
        for(PowerUpCard powerUpCard : player.getPowerUps())
            powerUps.add(powerUpCard.getName());
    }
}
