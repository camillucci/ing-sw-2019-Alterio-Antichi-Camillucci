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
    protected int redBuyCost;
    protected int yellowBuyCost;
    protected int blueReloadCost;
    protected int redReloadCost;
    protected int yellowReloadCost;

    protected List<SelectionAction> fireModalities = new ArrayList<>();

    public WeaponCard(String name, int[] ammoCosts, List<SelectionAction> fireModalities)
    {
        this.name = name;
        this.fireModalities = new ArrayList<>(fireModalities);
        this.blueBuyCost = ammoCosts[0];
        this.redBuyCost = ammoCosts[1];
        this.yellowBuyCost = ammoCosts[2];
        this.blueReloadCost = ammoCosts[3];
        this.redReloadCost = ammoCosts[4];
        this.yellowReloadCost = ammoCosts[5];
    }
    public WeaponCard(String name, int[] ammoCosts, SelectionAction first)
    {
        this(name, ammoCosts, Collections.singletonList(first));
    }

    public WeaponCard(String name, int[] ammoCosts, SelectionAction first, SelectionAction second)
    {
        this(name, ammoCosts, Arrays.asList(first,second));
    }

    public WeaponCard(String name, int[] ammoCosts, SelectionAction first, SelectionAction second, SelectionAction third) {
        this(name, ammoCosts, Arrays.asList(first, second, third));
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
        //TODO
    }
}
