package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.SelectionAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.List;

public class WeaponLockRifle extends WeaponCard {

    private String name;
    private AmmoColor color;
    private int blueCost;
    private int yellowCost;
    private int redCost;
    private int blueReloadCost;
    private int redReloadCost;
    private int yellowReloadCost;
    private List<SelectionAction> fireModalities = new ArrayList<>();

    public void WeaponLockRifle () {

        blueCost = 1;
        redCost = 0;
        yellowCost = 0;
        blueReloadCost = 2;
        redReloadCost = 0;
        yellowReloadCost = 0;
    }


    protected void buildFireModalities(Player shooter) {

        //fireModalities.add(new SelectionBasicEffect);
        //fireModalities.add(new SelectionSecondLock);
        //le selection action sono una classe diversa per ogni modalità di ogni arma?
    }

    @Override
    public List<Branch> getBranches(Player branchesOwner) {
        return null;
    }

    @Override
    public void visualize() {

    }
}
