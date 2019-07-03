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
     * Events invoked when a new Action is created inside an Action
     */
    public final IEvent<Action, Action> createdActionEvent = new Event<>();

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
    protected Ammo discardedAmmo = null;

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

    public Visualizable getVisualizable() {
        return visualizable;
    }

    /**
     * Executes the action if the variable canBeDone is true and the cost of the action is affordable by the player.
     * In case the action is executed, the completedAction event is invoked.
     */
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

    /**
     * Checks whether the weapon gotten as input in contained in the list of the selectable weapons. If it is, the
     * weapon is added to the selectedWeapons list.
     * @param weapon Reference to the added weapon
     */
    public void addWeapon(WeaponCard weapon) {
        if(getPossibleWeapons().contains(weapon))
            this.selectedWeapons.add(weapon);
    }

    /**
     * Checks whether the square gotten as input in contained in the list of the squares that can be selected as targets.
     * If it is, the square is added to the targetSquares list.
     * @param target Reference to the added square
     */
    public void addTarget(Square target) {
        if(getPossibleSquares().contains(target))
            this.targetSquares.add(target);
    }

    /**
     * Checks whether the player gotten as input in contained in the list of the players who can be selected as targets.
     * If it is, the player is added to the targetPlayers list.
     * @param target Reference to the added square
     */
    public void addTarget(Player target) {
        if(getPossiblePlayers().contains(target))
            this.targetPlayers.add(target);
    }

    /**
     * Checks whether the power up gotten as input in contained in the list of the selectable power up cards. If it is,
     * the power up is added to the discardedPowerUps list.
     * @param powerUpCard Reference to the discarded power up
     */
    public void addPowerUp(PowerUpCard powerUpCard) {
        if(getDiscardablePowerUps().contains(powerUpCard))
            this.discardedPowerUps.add(powerUpCard);
    }

    /**
     * Checks whether the power up gotten as input in contained in the list of the usable power up cards. If it is,
     * the power up is added to the selectedPowerUp list.
     * @param powerUp Reference to the power up being used in the action
     */
    public void use(PowerUpCard powerUp) {
        if(getPossiblePowerUps().contains(powerUp))
            this.selectedPowerUp = powerUp;
    }

    /**
     * Checks whether the ammo gotten as input in contained in the list of the selectable ammo cards. If it is,
     * the ammo is added to the discardedAmmos list.
     * @param ammo Reference to the discarded ammo
     */
    public void discard(Ammo ammo) {
        if(getDiscardableAmmos().contains(ammo))
            this.discardedAmmo = ammo;
    }

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

    /**
     * This method is called when an action is executed. It checks whether the player has enough ammo to pay for the
     * cost of the action. It they do, the action can be executed and the return value is true.
     * @return True when the player can pay for the action cost, false otherwise.
     */
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

    /**
     * Getter that returns a boolean parameter that represents whether the input action is compatible with this action.
     * @param action Action this class is going to be compared to
     * @return A parameter that represents whether the input is equal to this action.
     */
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

    public boolean isInvalid() {
        return !canBeDone && getPossiblePlayers().isEmpty() && getPossibleSquares().isEmpty() && getPossiblePowerUps().isEmpty()
                && getDiscardablePowerUps().isEmpty() && getDiscardableAmmos().isEmpty() && getPossibleWeapons().isEmpty();
    }
}
