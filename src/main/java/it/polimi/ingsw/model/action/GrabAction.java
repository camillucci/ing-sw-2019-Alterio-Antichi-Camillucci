package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * This class represents a specific case of extendable action. It contains the methods relative to an action that can be
 * used to grab an item from the square the player is standing in.
 */
public class GrabAction extends ExtendableAction
{
    public GrabAction() {
        this.visualizable = new Visualizable(null, "grab.png", "grab", "grab");
    }

    @Override
    protected void op()
    {
        this.grab();
    }

    /**
     * Executes the grab action.
     */
    private void grab()
    {
        this.branches = this.ownerPlayer.getCurrentSquare().grab(this.ownerPlayer, this.discardedPowerUps);
    }

    /**
     * Calculates the list of power up cards that can be discarded from the players hand.
     * @return the list of power up cards that can be discarded from the players hand.
     */
    @Override
    public List<PowerUpCard> getDiscardablePowerUps() {
        LinkedHashSet<PowerUpCard> temp = new LinkedHashSet<>();
        Ammo alreadyAdded = new Ammo(0, 0, 0);
        for(PowerUpCard pu : discardedPowerUps)
            alreadyAdded = alreadyAdded.add(pu.colorToAmmo());
        for(WeaponCard wc : ownerPlayer.getCurrentSquare().getWeapons())
            for(PowerUpCard pu : ownerPlayer.getPowerUps())
                if(!discardedPowerUps.contains(pu) && alreadyAdded.add(pu.colorToAmmo()).isLessOrEqualThan(wc.buyCost))
                    temp.add(pu);
        return new ArrayList<>(temp);
    }
}
