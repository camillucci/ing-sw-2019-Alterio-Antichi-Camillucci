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

    protected ShootAction(){}

    public ShootAction(ShootFunc shootFunc, SquaresFilter squaresFilter)
    {
        this(noPlayersFilter, squaresFilter, shootFunc);
    }

    public ShootAction(PlayersFilter playersFilter, ShootFunc shootFunc)
    {
        this(playersFilter, noSquaresFilter, shootFunc);
    }

    public ShootAction(PlayersFilter playersFilter, SquaresFilter squaresFilter, ShootFunc shootFunc)
    {
        this.shootFunc = shootFunc;
        this.playersFilter = playersFilter;
        this.squaresFilter = squaresFilter;
        this.canBeDone = false;
    }

    @Override
    protected void op() {
        this.shoot();
        for(Player p: damagedPlayers)
            p.damagedEvent.removeEventHandler((damaged, val) -> damagedEventHandler(damaged));
        if(selectedPowerUp != null)
            preparePowerUp();
    }

    protected void preparePowerUp()
    {
        SupportPowerUpAction tmp = (SupportPowerUpAction)selectedPowerUp.getEffect();
        tmp.setTargets(damagedPlayers);
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
        if(selectedPowerUp != null)
            return Collections.emptyList();

        List <PowerUpCard> temp = new ArrayList<>();
        if(!this.damagedPlayers.isEmpty())
            temp.addAll(ownerPlayer.getPowerupSet().getInTurnPUs());
        return temp;
    }

    @Override
    public void addTarget(Player target)
    {
        if(this.getPossiblePlayers().contains(target)) {
            targetPlayers.add(target);
            target.damagedEvent.addEventHandler((damaged, val) -> damagedEventHandler(damaged));
            if(squaresFilter == noSquaresFilter || !this.targetSquares.isEmpty())
                this.canBeDone = true;
        }
    }

    @Override
    public void addTarget(Square target) {
        if(this.getPossibleSquares().contains(target)) {
            this.targetSquares.add(target);
            if(playersFilter == noPlayersFilter || !this.targetPlayers.isEmpty())
                this.canBeDone = true;
        }
    }

    private void damagedEventHandler(Player damaged)
    {
        if(!damagedPlayers.contains(damaged))
            this.damagedPlayers.add(damaged);
    }

    private static PlayersFilter noPlayersFilter = (shooter, players, squares) -> Collections.emptyList();
    private static SquaresFilter noSquaresFilter = (shooter, players, squares) -> Collections.emptyList();
}
