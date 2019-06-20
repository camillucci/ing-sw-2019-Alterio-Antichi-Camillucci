package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShootAction extends Action
{
    protected List<Player> damagedPlayers = new ArrayList<>();

    protected ShootFunc shootFunc;
    protected PlayersFilter playersFilter;
    protected SquaresFilter squaresFilter;
    private boolean doesDamage = true;

    protected ShootAction(){}

    public ShootAction(ShootFunc shootFunc, SquaresFilter squaresFilter)
    {
        this(TargetsFilters.noPlayersFilter, squaresFilter, shootFunc);
    }

    public ShootAction(PlayersFilter playersFilter, ShootFunc shootFunc)
    {
        this(playersFilter, TargetsFilters.noSquaresFilter, shootFunc);
    }

    public ShootAction(PlayersFilter playersFilter, SquaresFilter squaresFilter, ShootFunc shootFunc)
    {
        this.shootFunc = shootFunc;
        this.playersFilter = playersFilter;
        this.squaresFilter = squaresFilter;
        this.canBeDone = false;
        this.text = "shoot";
    }

    public ShootAction(PlayersFilter playersFilter, ShootFunc shootFunc, boolean doesDamage)
    {
        this(playersFilter, TargetsFilters.noSquaresFilter, shootFunc);
        this.doesDamage = doesDamage;
    }

    @Override
    protected void op() {
        this.shoot();
        for(Player p: damagedPlayers)
            p.damagedEvent.removeEventHandler((damaged, val) -> damagedEventHandler(damaged));
        if(selectedPowerUp != null) {
            preparePowerUp();
            targetPlayers.clear();
        }
    }

    protected void preparePowerUp() {
        InTurnPowerUpAction tmp = (InTurnPowerUpAction)selectedPowerUp.getEffect();
        tmp.setTargets(damagedPlayers);
        tmp.damagedPlayers = damagedPlayers;
        this.next = tmp;
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

    @Override
    public List<PowerUpCard> getPossiblePowerUps(){
        if(selectedPowerUp != null || !doesDamage)
            return Collections.emptyList();

        return new ArrayList<>(ownerPlayer.getPowerupSet().getInTurnPUs());
    }

    @Override
    public void add(Player target)
    {
        if(this.getPossiblePlayers().contains(target)) {
            targetPlayers.add(target);
            if(doesDamage)
                target.damagedEvent.addEventHandler((damaged, val) -> damagedEventHandler(damaged));
            if(squaresFilter == TargetsFilters.noSquaresFilter || !this.targetSquares.isEmpty())
                this.canBeDone = true;
        }
    }

    @Override
    public void add(Square target) {
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
}
