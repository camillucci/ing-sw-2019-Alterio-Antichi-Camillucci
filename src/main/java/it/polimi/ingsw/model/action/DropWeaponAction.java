package it.polimi.ingsw.model.action;

import it.polimi.ingsw.generics.Visualizable;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a specific case of action. It contains all the methods and info relative to an action used to
 * drop a weapon into a shop after grabbing the fourth weapon from the shop
 */
public class DropWeaponAction extends Action
{
    private final Player player;

    public DropWeaponAction(Player player) {
        this.player = player;
        canBeDone = false;
        this.visualizable = new Visualizable("drop", "Drop");
    }

    @Override
    protected void op() {
        getOwnerPlayer().getCurrentSquare().addWeapon(selectedWeapons.get(0));
        getOwnerPlayer().removeWeapon(selectedWeapons.get(0));
    }

    @Override
    public List<WeaponCard> getPossibleWeapons() {
        if(!selectedWeapons.isEmpty())
            return Collections.emptyList();

        List<WeaponCard> ret = new ArrayList<>();
        ret.addAll(player.getLoadedWeapons());
        ret.addAll(player.getUnloadedWeapons());
        return ret;
    }

    @Override
    public void addWeapon(WeaponCard weapon) {
        super.addWeapon(weapon);
        canBeDone = true;
    }
}
