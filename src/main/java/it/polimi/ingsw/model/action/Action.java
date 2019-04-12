package it.polimi.ingsw.model.action;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Action
{
    public final Event<Action, Action> completedActionEvent = new Event<>();
    protected Player ownerPlayer;
    protected ArrayList<Square> targetSquares = new ArrayList<>();
    protected ArrayList<Player> targetPlayers = new ArrayList<>();
    protected ArrayList<WeaponCard> selectedWeapons = new ArrayList<>();
    protected ArrayList<PowerUpCard> selectedPowerUp = new ArrayList<>();
    protected boolean optional = false;
    protected Ammo doActionCost = new Ammo(0,0,0);

    public void doAction()
    {
        if(!spendAmmo())
            return;
        this.op();
        completedActionEvent.invoke(this, this);
    }
    public List<Player> getPossiblePlayers(){return Collections.emptyList();}
    public List<Square> getPossibleSquares(){return Collections.emptyList();}
    public boolean isOptional(){return optional;}
    public void addTarget(Square target)
    {
        if(this.getPossibleSquares().contains(target))
            this.targetSquares.add(target);
    }
    public void addTarget(Player target)
    {
        if(this.getPossiblePlayers().contains(target))
            this.targetPlayers.add(target);
    }
    public void addWeapon(WeaponCard weapon)
    {
        this.selectedWeapons.add(weapon);
    }
    public void addPowerUp(PowerUpCard powerUp)
    {
        this.selectedPowerUp.add(powerUp);
    }
    public boolean isCompatible(Action action)
    {
        return action.getClass().isInstance(this);
    }
    public void initializeAction(Player ownerPlayer)
    {
        this.ownerPlayer = ownerPlayer;
    }
    private boolean spendAmmo()
    {
        if(this.ownerPlayer.getBlueAmmo() >= this.doActionCost.blue && this.ownerPlayer.getRedAmmo() >= this.doActionCost.red && this.ownerPlayer.getYellowAmmo() >= this.doActionCost.yellow)
        {
            this.ownerPlayer.addBlue(-doActionCost.blue);
            this.ownerPlayer.addRed(-doActionCost.red);
            this.ownerPlayer.addYellow(-doActionCost.yellow);

            return true;
        }
        return false;
    }
    protected void op() {

    }
}
