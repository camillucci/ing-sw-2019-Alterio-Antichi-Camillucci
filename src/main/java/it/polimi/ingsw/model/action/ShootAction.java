package it.polimi.ingsw.model.action;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.generics.Visualizable;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to represent a particular case of actions, therefore it extends Action and provides all
 * the info necessary to define the targets and effects of a player shooting.
 */
public class ShootAction extends Action
{
    /**
     *
     */
    protected List<Player> damagedPlayers = new ArrayList<>();

    protected ShootFunc shootFunc;
    protected PlayersFilter playersFilter;
    protected SquaresFilter squaresFilter;

    /**
     * Boolean that represents whether the action damages other players.
     */
    private boolean doesDamage = true;

    protected ShootAction(){}

    public ShootAction(PlayersFilter playersFilter, SquaresFilter squaresFilter, ShootFunc shootFunc)
    {
        this.shootFunc = shootFunc;
        this.playersFilter = playersFilter;
        this.squaresFilter = squaresFilter;
        this.canBeDone = false;
        this.visualizable = new Visualizable("shoot", "shoot");
    }

    public ShootAction(ShootFunc shootFunc, SquaresFilter squaresFilter)
    {
        this(TargetsFilters.noPlayersFilter, squaresFilter, shootFunc);
    }

    public ShootAction(PlayersFilter playersFilter, ShootFunc shootFunc)
    {
        this(playersFilter, TargetsFilters.noSquaresFilter, shootFunc);
    }

    public ShootAction(PlayersFilter playersFilter, ShootFunc shootFunc, boolean doesDamage)
    {
        this(playersFilter, TargetsFilters.noSquaresFilter, shootFunc);
        this.doesDamage = doesDamage;
    }

    public ShootAction(PlayersFilter playersFilter, SquaresFilter squaresFilter, ShootFunc shootFunc, Visualizable visualizable)
    {
        this(playersFilter, squaresFilter, shootFunc);
        this.visualizable = visualizable;
    }

    public ShootAction(ShootFunc shootFunc, SquaresFilter squaresFilter, Visualizable visualizable)
    {
        this(TargetsFilters.noPlayersFilter, squaresFilter, shootFunc);
        this.visualizable = visualizable;
    }

    public ShootAction(PlayersFilter playersFilter, ShootFunc shootFunc, Visualizable visualizable)
    {
        this(playersFilter, TargetsFilters.noSquaresFilter, shootFunc);
        this.visualizable = visualizable;
    }

    @Override
    protected void op() {
        this.shoot();
        for(Player p: damagedPlayers)
            p.damagedEvent.removeEventHandler((damaged, val) -> damagedEventHandler(damaged));
        if(!damagedPlayers.isEmpty())
            preparePowerUp();
    }

    protected void shoot()
    {
        this.shootFunc.accept(ownerPlayer, targetPlayers, targetSquares);
    }

    @Override
    public List<Player> getPossiblePlayers()
    {
        return this.playersFilter.apply(ownerPlayer, targetPlayers, targetSquares);
    }

    @Override
    public List<Square> getPossibleSquares()
    {
        return this.squaresFilter.apply(ownerPlayer, targetPlayers, targetSquares);
    }

    /**
     * Adds the selected (from the user) player to the targets the weapon is going to shoot to. To do so, it first
     * checks whether the player is a legal target.
     */
    @Override
    public void addTarget(Player target)
    {
        if(this.getPossiblePlayers().contains(target)) {
            targetPlayers.add(target);
            if(doesDamage)
                target.damagedEvent.addEventHandler((damaged, val) -> damagedEventHandler(damaged));
            if(squaresFilter == TargetsFilters.noSquaresFilter || !this.targetSquares.isEmpty())
                this.canBeDone = true;
        }
    }

    /**
     * Adds the selected (from the user) square to the targets the weapon is going to shoot to. To do so, it first
     * checks whether the square is a legal target.
     */
    @Override
    public void addTarget(Square target) {
        if(this.getPossibleSquares().contains(target)) {
            this.targetSquares.add(target);
            if(playersFilter == TargetsFilters.noPlayersFilter || !this.targetPlayers.isEmpty())
                this.canBeDone = true;
        }
    }

    private void damagedEventHandler(Player damaged)
    {
        if(!damagedPlayers.contains(damaged))
            this.damagedPlayers.add(damaged);
    }

    protected void preparePowerUp() {
        List<PowerUpCard> temp = new ArrayList<>(ownerPlayer.getPowerupSet().getInTurnPUs());
        if(!temp.isEmpty()) {
            InTurnPowerUpAction tmp = new InTurnPowerUpAction();
            tmp.initialize(ownerPlayer);
            tmp.setTargets(damagedPlayers);
            ((Event<Action, Action>)createdActionEvent).invoke(this, tmp);
            this.next = tmp;
        }
    }
}
