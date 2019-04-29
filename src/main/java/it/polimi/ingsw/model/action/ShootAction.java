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
    protected ShootFunc shootFunc = (a, b, c) -> {};
    protected PlayersFilter playersFilter;
    protected SquaresFilter squaresFilter;

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
        List <PowerUpCard> temp = new ArrayList<>();
        if(!this.targetPlayers.isEmpty())
            for(PowerUpCard powerUpCard : ownerPlayer.getPowerUps())
                if(powerUpCard.name.equals("Targeting Scope") && ownerPlayer.getTotalAmmo() >= 1)
                    temp.add(powerUpCard);
        return temp;
    }

    @Override
    public void addPowerUp(PowerUpCard powerUp)
    {
        if(this.getPossiblePowerUps().contains(powerUp)) {
            Player target = this.targetPlayers.get(targetPlayers.size() - 1); // last added
            this.doActionCost = doActionCost.add(powerUp.cost);
            this.shootFunc = this.shootFunc.andThen((player, players, squares) -> Effects.damage(ownerPlayer, Collections.singletonList(target), Collections.singletonList(1)));
        }
    }
}
