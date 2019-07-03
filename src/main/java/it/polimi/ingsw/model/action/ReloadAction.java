package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * This class is a specific case of an action class. It contains all the generic methods relative to an action
 * that can be executed by the current turn's player.
 */
public class ReloadAction extends Action
{
    /**
     * Constructor that sets the 3 key parameters of the class with default values
     */
    public ReloadAction()
    {
        this.optional = true;
        this.canBeDone = false;
        this.visualizable = new Visualizable("reload a weapon", "reload");
    }

    @Override
    protected void op()
    {
        for(WeaponCard wc : this.selectedWeapons)
            this.ownerPlayer.reloadWeapon(wc);
    }

    @Override
    public void addWeapon(WeaponCard weapon)
    {
        if(getPossibleWeapons().contains(weapon)) {
            this.selectedWeapons.add(weapon);
            this.doActionCost = this.doActionCost.add(weapon.reloadCost);
            if(ownerPlayer.getAmmo().isGreaterOrEqual(doActionCost))
                this.canBeDone = true;
        }
    }

    @Override
    public void addPowerUp(PowerUpCard powerUpCard)
    {
        if(getDiscardablePowerUps().contains(powerUpCard)) {
            this.discardedPowerUps.add(powerUpCard);
            this.doActionCost = this.doActionCost.sub(powerUpCard.colorToAmmo());
            if(ownerPlayer.getAmmo().isGreaterOrEqual(doActionCost))
                this.canBeDone = true;
        }
    }

    @Override
    public List<WeaponCard> getPossibleWeapons() {
        Ammo powerUpAmmo = new Ammo(0, 0, 0);
        for(PowerUpCard pu : ownerPlayer.getPowerUps())
            powerUpAmmo = powerUpAmmo.add(pu.colorToAmmo());
        for(PowerUpCard pu : discardedPowerUps)
            powerUpAmmo = powerUpAmmo.sub(pu.colorToAmmo());
        List<WeaponCard> temp = new ArrayList<>();
        for(WeaponCard weapon : ownerPlayer.getUnloadedWeapons())
            if(!selectedWeapons.contains(weapon) && ownerPlayer.getAmmo().add(powerUpAmmo).isGreaterOrEqual(doActionCost.add(weapon.reloadCost)))
                temp.add(weapon);
        return temp;
    }

    @Override
    public List<PowerUpCard> getDiscardablePowerUps() {
        List<PowerUpCard> temp = new ArrayList<>();
        for(PowerUpCard pu : ownerPlayer.getPowerUps())
            if(!discardedPowerUps.contains(pu) && pu.colorToAmmo().isLessOrEqualThan(doActionCost))
                temp.add(pu);
        return temp;
    }

    /**
     * Calls the method used to compare the compatibility between this action and the input action
     * @param action Action this class is going to be compared to
     * @return True if the input action is compatible with this class, false otherwise
     */
    @Override
    public boolean isCompatible(Action action) {
        return action.testCompatibilityWith(this);
    }

    /**
     * Returns a boolean that represents whether the input is compatible with this class
     * @param action Input action that's going to be compared with this class
     * @return Always true, given that the input parameter is always required to be a ReloadAction
     */
    @Override
    protected boolean testCompatibilityWith(ReloadAction action) {
        return true;
    }
}
