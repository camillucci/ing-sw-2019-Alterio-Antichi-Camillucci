package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.cards.WeaponCard;

public class EndBranchAction extends Action {

    private String weaponName = "";

    public EndBranchAction() {
        this.visualizable = new Visualizable("end the move", "end");
    }

    public EndBranchAction(String weaponName) {
        this();
        this.weaponName = weaponName;
    }

    @Override
    protected void op() {
        for(WeaponCard weaponCard : ownerPlayer.getLoadedWeapons())
            if(weaponCard.name.equals(weaponName)) {
                ownerPlayer.unloadWeapon(weaponCard);
                return;
            }
    }
}
