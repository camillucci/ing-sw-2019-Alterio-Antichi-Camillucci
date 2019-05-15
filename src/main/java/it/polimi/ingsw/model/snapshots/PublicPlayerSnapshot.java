package it.polimi.ingsw.model.snapshots;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.cards.WeaponCard;

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
    private final List<String> damage = new ArrayList<>();
    private final List<String> mark = new ArrayList<>();
    private final List<String> unloadedWeapons = new ArrayList<>();
    public final int loadedWeaponsNumber;
    public final int powerUpsNumber;

    public PublicPlayerSnapshot(Player player) {
        this.color = player.color;
        this.name = player.name;
        this.skull = player.getSkull();
        this.blueAmmo = player.getAmmo().blue;
        this.redAmmo = player.getAmmo().red;
        this.yellowAmmo = player.getAmmo().yellow;
        this.finalFrenzy = player.isFinalFrenzy();
        for(PlayerColor damageColor : player.getDamage())
            this.damage.add(damageColor.getName());
        for(PlayerColor markColor : player.getMark())
            this.mark.add(markColor.getName());
        this.loadedWeaponsNumber = player.getLoadedWeapons().size();
        for(WeaponCard weaponCard : player.getUnloadedWeapons())
            unloadedWeapons.add(weaponCard.name);
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
}
