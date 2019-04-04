package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.SelectionAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.List;

public class WeaponMachineGun extends WeaponCard {

    private String name;
    private AmmoColor color;
    private int blueCost;
    private int yellowCost;
    private int redCost;
    private int blueReloadCost;
    private int redReloadCost;
    private int yellowReloadCost;
    private List<SelectionAction> fireModalities = new ArrayList<>();

    public void WeaponMachineGun () {

        blueCost = 1;
        redCost = 0;
        yellowCost = 0;
        blueReloadCost = 1;
        redReloadCost = 1;
        yellowReloadCost = 0;
        fireModalities = null;
    }


    protected void buildFireModalities(Player shooter) {

    }

    @Override
    public List<Branch> getBranches(Player branchesOwner) {
        return null;
    }

    @Override
    public void visualize() {

    }
}