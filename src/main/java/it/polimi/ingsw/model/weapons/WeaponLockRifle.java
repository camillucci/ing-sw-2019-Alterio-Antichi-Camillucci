package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.branch.Branch;

import java.util.List;

public class WeaponLockRifle extends WeaponCard {

    public WeaponLockRifle () {

        this.name = "Lock Rifle";
        this.color = AmmoColor.BLUE;
        this.blueBuyCost = 1;
        this.redBuyCost = 0;
        this.yellowBuyCost = 0;
        this.blueReloadCost = 2;
        this.redReloadCost = 0;
        this.yellowReloadCost = 0;
        this.fireModalities = null; //TODO
    }

    protected void buildFireModalities(Player shooter) {

        //TODO
        //TODO fireModalities.add(new SelectionBasicEffect);
        //TODO fireModalities.add(new SelectionSecondLock);
        //TODO le selection action sono una classe diversa per ogni modalità di ogni arma?
    }

    @Override
    public List<Branch> getBranches(Player branchesOwner) {
        return null; //TODO
    }

    @Override
    public void visualize() {
        //TODO

    }
}
