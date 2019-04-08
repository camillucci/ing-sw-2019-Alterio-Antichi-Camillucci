package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.action.EndBranchAction;
import it.polimi.ingsw.model.action.SelectionAction;
import it.polimi.ingsw.model.action.ShootAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.List;

public class WeaponLockRifle extends WeaponCard {

    public WeaponLockRifle () {

        this.name = "Lock Rifle";
        this.blueBuyCost = 1;
        this.redBuyCost = 0;
        this.yellowBuyCost = 0;
        this.blueReloadCost = 2;
        this.redReloadCost = 0;
        this.yellowReloadCost = 0;
        this.fireModalities = new ArrayList<>();
    }

    protected void buildFireModalities(Player shooter) {
        // L'arma ha 2 possibilità: basic effect o (basic effect + optionalEffect) -> 2 SelectionAction
        ShootAction s1 = new ShootAction(this::basicEffect, /*Qua si dovrebbe mandare il metodo che fa visualizzare i bersagli compatibili */null,shooter);
        SelectionAction fireModality1 = new SelectionAction(shooter, new Branch(s1, new EndBranchAction(shooter)), ()-> System.out.println("Press 1 to Primary fire: Set 2 damage to player and 1 Mark to another"));

        ShootAction s2 = new ShootAction(this::BasicAndOptionalEffect,/*Qua si dovrebbe mandare il metodo che fa visualizzare i bersagli compatibili */ null,shooter);
        SelectionAction fireModality2 = new SelectionAction(shooter, new Branch(s2, new EndBranchAction(shooter)), ()-> System.out.println("Press 2 to Primary and Optional fire: Set 2 damage to player and 1 Mark to another"));

        this.fireModalities.add(fireModality1);
        this.fireModalities.add(fireModality2);
    }

    private void basicEffect(Player shooter, List<Player> targets) // la signature di questo metodo deve essere uguale a quella del BiConsumer della ShootAction
    {
        Player target = targets.get(0);
        //target.setDamage(shooter, 2);
        //target.setMarks(shooter, 1);
    }
    private void BasicAndOptionalEffect(Player shooter, List<Player> targets)
    {
        basicEffect(shooter, targets);
        Player secondTarget = targets.get(1);
        //secondTarget.setMarks(shooter, 1);
    }

    @Override
    public void visualize() {
        //TODO

    }
}
