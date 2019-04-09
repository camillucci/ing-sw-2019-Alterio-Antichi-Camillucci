package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.SelectionAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.List;

public class WeaponCard implements Visualizable {

    protected String name;
    protected int blueBuyCost;
    protected int yellowBuyCost;
    protected int redBuyCost;
    protected int blueReloadCost;
    protected int redReloadCost;
    protected int yellowReloadCost;

    protected List<SelectionAction> fireModalities = new ArrayList<>();

    public WeaponCard(List<SelectionAction> fireModalities)
    {
        this.fireModalities = fireModalities;
    }

    public List<Branch> getFireModalities(Player shooter)
    {
        ArrayList<Branch> ret = new ArrayList<>();
        for(SelectionAction a : fireModalities)
            ret.add(new Branch(a));
        return ret;
    }

    @Override
    public void visualize() {

    }
}
