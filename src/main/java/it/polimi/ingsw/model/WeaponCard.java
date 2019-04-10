package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.SelectionAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    public WeaponCard(String name, List<SelectionAction> fireModalities)
    {
        this.name = name;
        this.fireModalities = new ArrayList<>(fireModalities);
    }
    public WeaponCard(String name, SelectionAction first){this(name, Collections.singletonList(first));}
    public WeaponCard(String name, SelectionAction first, SelectionAction second)
    {
        this(name, Arrays.asList(first,second));
    }
    public WeaponCard(String name, SelectionAction first, SelectionAction second, SelectionAction third) {
        this(name, Arrays.asList(first, second, third));
    }
    public List<Branch> getFireModalities()
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
