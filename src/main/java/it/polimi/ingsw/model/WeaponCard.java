package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.SelectionAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.List;

public abstract class WeaponCard implements Branchable, Visualizable {

    private String name;
    private AmmoColor color;
    private int blueCost;
    private int yellowCost;
    private int redCost;
    private List<SelectionAction> fireModalities = new ArrayList<>();

    public List<Branch> getFireModalities(Player shooter)
    {
        buildFireModalities(shooter);
        ArrayList<Branch> ret = new ArrayList<>();
        for(SelectionAction a : fireModalities)
            ret.add(new Branch(a));
        return ret;

        //da spostare in ogni singola arma?
    }

    protected abstract void buildFireModalities(Player shooter);

}
