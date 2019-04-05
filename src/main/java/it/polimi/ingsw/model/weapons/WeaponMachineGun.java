package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.branch.Branch;

import java.util.List;

public class WeaponMachineGun extends WeaponCard {

    public WeaponMachineGun() {

        this.name = "Machine Gun";
        this.color = AmmoColor.BLUE;
        this.blueBuyCost = 1;
        this.redBuyCost = 0;
        this.yellowBuyCost = 0;
        this.blueReloadCost = 1;
        this.redReloadCost = 1;
        this.yellowReloadCost = 0;
        this.fireModalities = null; //TODO
    }

    protected void buildFireModalities(Player shooter) {
        //TODO
    }

    @Override
    public List<Branch> getBranches(Player branchesOwner) {
        return null;
    } //TODO

    @Override
    public void visualize() {
        //TODO
    }
}