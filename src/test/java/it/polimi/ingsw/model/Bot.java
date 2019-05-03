package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.branch.BranchMap;
import it.polimi.ingsw.model.branch.BranchMapFactory;
import it.polimi.ingsw.model.branch.BranchTestUtilities;
import it.polimi.ingsw.model.weapons.ShootFunc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Bot
{

    Player player = new Player("A", PlayerColor.GREY, null);
    private WeaponCard newWeaponCard(ShootFunc s)
    {
        Ammo ammo = new Ammo(0, 0, 0);
        return new WeaponCard("B", ammo, ammo, () -> Arrays.asList(
                new FireModalityAction(ammo, new Branch(new ShootAction((shooter, players) -> Collections.emptyList(), (shooter, squares) -> Collections.emptyList(), s), new EndBranchAction()))));
    }

    public void playNoAdrenaline(ActionsProvider provider) // add 3 or 4 damage to shooter
    {
        WeaponCard weaponCard1 = newWeaponCard((shooter, players, squares) -> shooter.addDamage(player, 3));
        WeaponCard weaponCard2 = newWeaponCard((shooter, players, squares) -> shooter.addDamage(player,4));
        provider.getPlayer().addWeapon(weaponCard1);
        provider.getPlayer().addWeapon(weaponCard2);

        assertTrue(BranchTestUtilities.testEquality(BranchTestUtilities.noAdrenalinePossibleActions(), provider.getActions()));
        // branchmap = { PM1G, PM3, PW, R } do = W

        for(Action a : provider.getActions())
            if(a instanceof WeaponSelectionAction)
                a.doAction();


        // possible = { W1, W2, R } do = W1
        List<Action> tmp = provider.getActions();
        assertTrue(BranchTestUtilities.testEquality(tmp, new ExtendableAction(), new ExtendableAction(), new RollBackAction()));

        provider.getActions().get(1).doAction();

        // possible = {F1, R} do = F1
        tmp = provider.getActions();
        assertTrue(BranchTestUtilities.testEquality(tmp, new FireModalityAction(null,null,null), new RollBackAction()));
        provider.getActions().get(0).doAction();

        // possible = {S1, R} do = S1
        tmp = provider.getActions();
        assertTrue( BranchTestUtilities.testEquality(tmp, new ShootAction(null,null,null), new RollBackAction()));
        provider.getActions().get(0).doAction();

        assertTrue(provider.getPlayer().getDamage().size() >= 3);

        // possible = {E, R} do = E
        tmp = provider.getActions();
        assertTrue( BranchTestUtilities.testEquality(tmp, new EndBranchAction(), new RollBackAction()));
        provider.getActions().get(0).doAction();
    }

    public void playThreeDamage(ActionsProvider provider)
    {
        assertTrue(BranchTestUtilities.testEquality(BranchTestUtilities.threeDamagePossibleActions(), provider.getActions()));
        // todo logic, to end the turn I choose EndBranchAction right now
        BranchTestUtilities.searchAndDo(provider.getActions(), new EndBranchAction());
    }

    public void playSpawnBranchMap(ActionsProvider provider)
    {
        assertTrue(provider.getActions().size() == 2);
        provider.getActions().get(0).doAction();
        provider.getActions().get(0).doAction();
        provider.getActions().get(0).doAction();
    }
}