package it.polimi.ingsw.model.snapshots;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.WeaponCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PublicPlayerSnapshot implements Serializable {

    public final PlayerColor color;
    public final String name;
    public final int skull;
    public final int blueAmmo;
    public final int redAmmo;
    public final int yellowAmmo;
    public final boolean finalFrenzy;
    public final List<PlayerColor> damage;
    public final List<PlayerColor> mark;
    public final int loadedWeaponsNumber;
    public final List<String> unloadedWeapons = new ArrayList<>();
    public final int powerUpsNumber;

    public PublicPlayerSnapshot(Player player) {
        this.color = player.color;
        this.name = player.name;
        this.skull = player.getSkull();
        this.blueAmmo = player.getBlueAmmo();
        this.redAmmo = player.getRedAmmo();
        this.yellowAmmo = player.getYellowAmmo();
        this.finalFrenzy = player.isFinalFrenzy();
        this.damage = player.getDamage();
        this.mark = player.getMark();
        this.loadedWeaponsNumber = player.getLoadedWeapons().size();
        for(WeaponCard weaponCard : player.getUnloadedWeapons())
            unloadedWeapons.add(weaponCard.name);
        this.powerUpsNumber = player.getPowerUps().size();
    }
}
