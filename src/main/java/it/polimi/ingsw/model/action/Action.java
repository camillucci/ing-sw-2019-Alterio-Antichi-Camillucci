package it.polimi.ingsw.model.action;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class Action
{
    public final Event<Action, Action> completedActionEvent = new Event<>();
    protected Action next;
    protected Ammo doActionCost = new Ammo(0,0,0);
    protected Player ownerPlayer;
    protected List<Square> targetSquares = new ArrayList<>();
    protected List<Player> targetPlayers = new ArrayList<>();
    protected List<WeaponCard> selectedWeapons = new ArrayList<>();
    protected PowerUpCard selectedPowerUp;
    protected List<PowerUpCard> discardedPowerUps = new ArrayList<>();
    protected boolean optional = false;
    protected boolean canBeDone = true;
    
    private Consumer opMethod = a -> { };

    protected Action() {}

    public Action(Consumer<Action> doActionMethod, boolean isOptional)
    {
        this.opMethod = doActionMethod;
        this.optional = isOptional;
    }

    public Action(Consumer<Action> doActionMethod)
    {
        this(doActionMethod, false);
    }

    public void doAction()
    {
        if(canBeDone && spendAmmo()) {
            this.op();
            completedActionEvent.invoke(this, this);
        }
    }

    public void initialize(Player ownerPlayer)
    {
        this.ownerPlayer = ownerPlayer;
    }

    public void addWeapon(WeaponCard weapon)
    {
        this.selectedWeapons.add(weapon);
    }
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
    public void usePowerUp(PowerUpCard powerUp)
    {
        if(getPossiblePowerUps().contains(powerUp))
            this.selectedPowerUp = powerUp;
    }
    public void addDiscarded(PowerUpCard powerUpCard)
    {
        if(getDiscardablePowerUps().contains(powerUpCard))
            this.discardedPowerUps.add(powerUpCard);
    }

    public List<Player> getPossiblePlayers() { return Collections.emptyList(); }
    public List<Square> getPossibleSquares() { return Collections.emptyList(); }
    public List<PowerUpCard> getPossiblePowerUps() { return Collections.emptyList(); }
    public List<PowerUpCard> getDiscardablePowerUps() { return Collections.emptyList();}

    public Player getOwnerPlayer() { return this.ownerPlayer; }
    public boolean isCompatible(Action action)
    {
        return action.getClass().isInstance(this);
    }

    private boolean spendAmmo()
    {
        Ammo ammo = new Ammo(0, 0, 0);
        for(PowerUpCard pu : discardedPowerUps)
            ammo = ammo.add(pu.colorToAmmo());
        if(ownerPlayer.getAmmo().isGreaterOrEqual(this.doActionCost))
        {
            this.ownerPlayer.addBlue(-(doActionCost.sub(ammo).blue));
            this.ownerPlayer.addRed(-(doActionCost.sub(ammo).red));
            this.ownerPlayer.addYellow(-(doActionCost.sub(ammo).yellow));
            for(PowerUpCard pu : discardedPowerUps) {
                ownerPlayer.gameBoard.powerupDeck.addDiscarded(pu);
                ownerPlayer.removePowerUpCard(pu);
            }
            return true;
        }
        return false;
    }

    public void setDoActionCost(Ammo ammo) { this.doActionCost = ammo; }
    public boolean isOptional() { return optional; }
    public Action next() { return next; }
    protected void op() {
        this.opMethod.accept(this);
    }
    public void setCanBeDone(boolean val) { this.canBeDone = val; } //Only for Tests
}
