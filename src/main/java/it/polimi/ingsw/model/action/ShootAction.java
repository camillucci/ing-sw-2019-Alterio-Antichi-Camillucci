package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PowerUpCard;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.weapons.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShootAction extends Action
{
    protected List<Player> damagedPlayers = new ArrayList<>();

    protected ShootFunc shootFunc = (a, b, c) -> {};
    protected PlayersFilter playersFilter = (shooter, players) -> Collections.emptyList();
    protected SquaresFilter squaresFilter = (shooter, squares) -> Collections.emptyList();

    protected ShootAction(){}

    public ShootAction(ShootFunc shootFunc, SquaresFilter squaresFilter)
    {
        this(null, squaresFilter, shootFunc);
    }

    public ShootAction(PlayersFilter playersFilter, ShootFunc shootFunc)
    {
        this(playersFilter, null, shootFunc);
    }

    public ShootAction(PlayersFilter playersFilter, SquaresFilter squaresFilter, ShootFunc shootFunc)
    {
        this.shootFunc = shootFunc;
        this.playersFilter = playersFilter;
        this.squaresFilter = squaresFilter;
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
        return this.playersFilter.apply(ownerPlayer, targetPlayers);
    }

    @Override
    public List<Square> getPossibleSquares()
    {
        return this.squaresFilter.apply(ownerPlayer, targetSquares);
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
        }
    }

    private void damagedEventHandler(Player damaged)
    {
        if(!damagedPlayers.contains(damaged))
            this.damagedPlayers.add(damaged);
    }
}
