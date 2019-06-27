package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.cards.WeaponCard;

/**
 * This class represents a specific case of action. It contains the method and info relative to an action used to
 * close a branch.
 */
public class EndBranchAction extends Action {

    /**
     * String that represents the weapon that's going to be unloaded by this action.
     */
    private String weaponName = "";

    public EndBranchAction() {
        this.visualizable = new Visualizable("end the move", "end");
    }

    /**
     * Constructor that assigns the input parameter to its global correspondence
     * @param weaponName String that represents the name of the weapon that's going to be unloaded by this action.
     */
    public EndBranchAction(String weaponName) {
        this();
        this.weaponName = weaponName;
    }

    /**
     * Checks whether the player has a weapon that matches the name with the global parameter. It that's the case, the
     * weapon is unloaded.
     */
    @Override
    protected void op() {
        for(WeaponCard weaponCard : ownerPlayer.getLoadedWeapons())
            if(weaponCard.name.equals(weaponName)) {
                ownerPlayer.unloadWeapon(weaponCard);
                return;
            }
    }
}
