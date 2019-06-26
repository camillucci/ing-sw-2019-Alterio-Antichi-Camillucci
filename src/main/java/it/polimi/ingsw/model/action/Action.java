package it.polimi.ingsw.model.action;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Action
{
    /**
     * Events invoked when doAction() method returns. It notifies subscribers that the action is completed
     */
    public final IEvent<Action, Action> completedActionEvent = new Event<>();

    /**
     * Action that can be done only after this is completed
     */
    protected Action next;

    /**
     * Cost in ammo payed by ownerPlayer after that the action is done
     */
    protected Ammo doActionCost = new Ammo(0,0,0);

    /**
     * Reference to the player this action belongs to
     */
    protected Player ownerPlayer;

    /**
     * List of Squares added as targets by the player
     */
    protected List<Square> targetSquares = new ArrayList<>();
    /**
     * List of Players added as targets by the player
     */
    protected List<Player> targetPlayers = new ArrayList<>();
    /**
     * List of weapons the player wants to use
     */
    protected List<WeaponCard> selectedWeapons = new ArrayList<>();
    /**
     * Power up card selected by player
     */
    protected PowerUpCard selectedPowerUp;
    /**
     * List of power up card the player wants to discard
     */
    protected List<PowerUpCard> discardedPowerUps = new ArrayList<>();
    /**
     * Ammo the player wants to discard
     */
    protected Ammo discardedAmmo;
    /**
     * True iff the Action is ignorable in the actions sequence of a BranchMap
     */
    protected boolean optional = false;
    /**
     * True iff the method doAction can be invoked, according to Adrenaline rules.
     */
    protected boolean canBeDone = true;

    /**
     * Description of the action
     */
    protected Visualizable visualizable;

    public Visualizable getVisualizable(){
        return visualizable;
    }

    public void doAction()
    {
        if(canBeDone && spendAmmo()) {
            this.op();
            ((Event<Action, Action>)completedActionEvent).invoke(this, this);
        }
    }

    public void initialize(Player ownerPlayer)
    {
        this.ownerPlayer = ownerPlayer;
    }

    public void addWeapon(WeaponCard weapon) {
        /* Only for Override */
    }
    public void addTarget(Square target) {
        /* Only for Override */
    }
    public void addTarget(Player target) {
        /* Only for Override */
    }
    public void addPowerUp(PowerUpCard powerUpCard)
    {
        if(getDiscardablePowerUps().contains(powerUpCard))
            this.discardedPowerUps.add(powerUpCard);
    }

    public void use(PowerUpCard powerUp)
    {
        if(getPossiblePowerUps().contains(powerUp))
            this.selectedPowerUp = powerUp;
    }

    public void discard(Ammo ammo) { /* Only for Override */}

    public List<Player> getPossiblePlayers() { return Collections.emptyList(); }
    public List<Square> getPossibleSquares() { return Collections.emptyList(); }
    public List<PowerUpCard> getPossiblePowerUps() { return Collections.emptyList(); }
    public List<PowerUpCard> getDiscardablePowerUps() { return Collections.emptyList(); }
    public List<Ammo> getDiscardableAmmos() { return Collections.emptyList(); }
    public List<WeaponCard> getPossibleWeapons() { return Collections.emptyList(); }
    public Player getOwnerPlayer() { return this.ownerPlayer; }

    public boolean isOptional() { return optional; }

    public Action next() { return next; }

    protected void op() {}

    public void setCanBeDone(boolean val) { this.canBeDone = val; } //Only for Tests

    public boolean canBeDone() { return canBeDone; }

    private boolean spendAmmo()
    {
        Ammo ammo = new Ammo(0, 0, 0);
        for(PowerUpCard pu : discardedPowerUps)
            ammo = ammo.add(pu.colorToAmmo());
        if(discardedAmmo != null)
            doActionCost = discardedAmmo;
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

    public boolean isCompatible(Action action)
    {
        return this == action;
    }
    protected boolean testCompatibilityWith(MoveAction action)
    {
        return false;
    }
    protected boolean testCompatibilityWith(CounterPowerUpAction action)
    {
        return false;
    }
    protected boolean testCompatibilityWith(InTurnPowerUpAction action)
    {
        return false;
    }
    protected boolean testCompatibilityWith(PowerUpAction action)
    {
        return false;
    }
    protected boolean testCompatibilityWith(ReloadAction action)
    {
        return false;
    }
    protected boolean testCompatibilityWith(WeaponSelectionAction action)
    {
        return false;
    }
}
