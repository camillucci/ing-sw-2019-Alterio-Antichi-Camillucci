package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.branch.BranchTestUtilities;
import it.polimi.ingsw.model.cards.ShootFunc;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Bot
{
    private Player player = new Player("A", PlayerColor.GREY, null);
    private WeaponCard newWeaponCard(ShootFunc s)
    {
        Ammo ammo = new Ammo(0, 0, 0);
        return new WeaponCard("B", ammo, ammo, () -> Collections.singletonList(
                new FireModalityAction(ammo, "", "", new Branch(new ShootAction((shooter, players, squares) -> Collections.emptyList(), (shooter, players, squares) -> Collections.emptyList(), s), new EndBranchAction()))));
    }

    public void playNoAdrenaline(ActionsProvider provider) // addTarget 3 or 4 damage to shooter
    {
        WeaponCard weaponCard1 = newWeaponCard((shooter, players, squares) -> shooter.addDamage(player, 3));
        WeaponCard weaponCard2 = newWeaponCard((shooter, players, squares) -> shooter.addDamage(player, 4));
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
        assertTrue(BranchTestUtilities.testEquality(tmp, new FireModalityAction(null, "", "",null, null), new RollBackAction()));
        provider.getActions().get(0).doAction();

        // possible = {S1, R} do = S1
        tmp = provider.getActions();
        assertTrue( BranchTestUtilities.testEquality(tmp, new ShootAction(null,null,null), new RollBackAction()));
        provider.getActions().get(0).setCanBeDone(true);
        provider.getActions().get(0).doAction();

        assertTrue(provider.getPlayer().getDamage().size() >= 3);

        // possible = {E, R} do = E
        tmp = provider.getActions();
        //assertTrue( BranchTestUtilities.testEquality(tmp, new EndBranchAction(), new RollBackAction()));
        provider.getActions().get(0).doAction();
    }

    public void playThreeDamage(ActionsProvider provider)
    {
        //assertTrue(BranchTestUtilities.testEquality(BranchTestUtilities.threeDamagePossibleActions(), provider.getActions()));
        BranchTestUtilities.searchAndDo(provider.getActions(), new EndBranchAction());
    }

    public void reloadEndTurn(ActionsProvider provider)
    {
        //assertTrue(BranchTestUtilities.testEquality(BranchTestUtilities.reloadEndTurnPossibleActions(), provider.getActions()));
        BranchTestUtilities.searchAndDo(provider.getActions(), new EndBranchAction());
    }

    public void playSpawnBranchMap(ActionsProvider provider)
    {
        assertEquals(3, provider.getActions().size());
        provider.getActions().get(0).doAction();
        provider.getActions().get(0).doAction();
    }

    public void playEmptyTurn(ActionsProvider provider)
    {
        assertEquals(7, provider.getActions().size());
        provider.getActions().get(4).doAction(); //End first move
        provider.getActions().get(4).doAction(); //End Second move
        provider.getActions().get(2).doAction(); //End turn
    }
}
