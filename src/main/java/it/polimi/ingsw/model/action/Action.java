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
    protected Ammo doActionCost = new Ammo(0,0,0);
    protected Player ownerPlayer;
    protected ArrayList<Square> targetSquares = new ArrayList<>();
    protected ArrayList<Player> targetPlayers = new ArrayList<>();
    protected ArrayList<WeaponCard> selectedWeapons = new ArrayList<>();
    protected ArrayList<PowerUpCard> selectedPowerUps = new ArrayList<>();
    protected boolean optional = false;
    private Consumer opMethod = a -> { };

    protected Action(){}

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
        if(spendAmmo()) {
            this.op();
            completedActionEvent.invoke(this, this);
        }
    }

    public void initialize(Player ownerPlayer)
    {
        this.ownerPlayer = ownerPlayer;
        /*
        this.targetSquares = new ArrayList<>();
        this.targetPlayers = new ArrayList<>();
        this.selectedWeapons = new ArrayList<>();
        this.selectedPowerUps = new ArrayList<>();
        */
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
    public void addPowerUp(PowerUpCard powerUp)
    {
        if(getPossiblePowerUps().contains(powerUp))
            this.selectedPowerUps.add(powerUp);
    }
    public List<Player> getPossiblePlayers(){return Collections.emptyList();}
    public List<Square> getPossibleSquares(){return Collections.emptyList();}
    public List<PowerUpCard> getPossiblePowerUps(){return Collections.emptyList();}
    public Player getOwnerPlayer(){ return this.ownerPlayer; }

    public boolean isCompatible(Action action)
    {
        return action.getClass().isInstance(this);
    }
    private boolean spendAmmo()
    {
        if(Ammo.getAmmo(this.ownerPlayer).isGreaterOrEqual(this.doActionCost))
        {
            this.ownerPlayer.addBlue(-doActionCost.blue);
            this.ownerPlayer.addRed(-doActionCost.red);
            this.ownerPlayer.addYellow(-doActionCost.yellow);

            return true;
        }
        return false;
    }

    public boolean isOptional(){return optional;}
    protected void op() {
        this.opMethod.accept(this);
    }
}
