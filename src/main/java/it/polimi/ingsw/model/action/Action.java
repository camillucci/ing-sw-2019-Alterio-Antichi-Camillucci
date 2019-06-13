package it.polimi.ingsw.model.action;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class Action
{
    /**
     * Events invoked when doAction() method returns. It notifies subscribers that the action is completed
     */
    public final IEvent<Action, Action> completedActionEvent = new Event<>();

    /**
     * Action that can be done only after that this is completed
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
     * Consumer that represents the effectively way the action handle targets.
     * If opMethod != a -> {} then is invoked in doAction() method.
     */
    private Consumer opMethod = a -> { };
    /**
     * Textual description of the action
     */
    protected String text;

    protected Action() {}

    protected Action(String text) {
        this.text = text;
    }

    /**
     * @param doActionCost Cost of the action
     * @param isOptional true iff isOptional == true
     * @param doActionMethod invoked when doAction() is invoked
     * @param text textual description of the action
     */
    public Action(Ammo doActionCost,  boolean isOptional,  Consumer<Action> doActionMethod, String text)
    {
        this.opMethod = doActionMethod;
        this.doActionCost = doActionCost;
        this.optional = isOptional;
        this.text = text;
    }

    public Action(Ammo doActionCost, Consumer<Action> doActionMethod, String text)
    {
        this(doActionCost, false, doActionMethod, text);
    }

    public Action(Consumer<Action> doActionMethod, String text)
    {
        this(new Ammo(0,0,0), doActionMethod, text);
    }

    public void doAction()
    {
        if(canBeDone && spendAmmo()) {
            this.op();
            ((Event<Action, Action>)completedActionEvent).invoke(this, this);
        }
    }

    /**
     * Sets the player which this actions belongs to
     * @param ownerPlayer
     */
    public void initialize(Player ownerPlayer)
    {
        this.ownerPlayer = ownerPlayer;
    }

    public void addWeapon(WeaponCard weapon) {
        //Only for Override
    }

    public void addTarget(Square target) {
        //Only for Override
    }

    public void addTarget(Player target) {
        //Only for Override
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

    public void addDiscardedAmmo(Ammo ammo) {
        //Only for Override
    }

    public List<Player> getPossiblePlayers() { return Collections.emptyList(); }
    public List<Square> getPossibleSquares() { return Collections.emptyList(); }
    public List<PowerUpCard> getPossiblePowerUps() { return Collections.emptyList(); }
    public List<PowerUpCard> getDiscardablePowerUps() { return Collections.emptyList(); }
    public List<Ammo> getDiscardableAmmos() { return Collections.emptyList(); }
    public List<WeaponCard> getPossibleWeapons() { return Collections.emptyList(); }

    public Player getOwnerPlayer() { return this.ownerPlayer; }

    public boolean isCompatible(Action action)
    {
        return action.getClass().isInstance(this);
    }

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

    public boolean isOptional() { return optional; }

    public Action next() { return next; }

    protected void op() { this.opMethod.accept(this); }

    public void setCanBeDone(boolean val) { this.canBeDone = val; } //Only for Tests

    public String getText() {
        return text;
    }
}
