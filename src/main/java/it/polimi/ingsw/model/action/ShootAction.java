package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.weapons.PlayersFilter;
import it.polimi.ingsw.model.weapons.ShootFunc;
import it.polimi.ingsw.model.weapons.SquaresFilter;

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


    /*
    @Override
    public List<Player> getPossiblePlayers() {
        if(possibleTargetFuncM == null)
            return this.playersFilter.apply(ownerPlayer, new ArrayList<>(this.targetPlayers));
        List<Pair<Player, Square>> tempPair = new ArrayList<>();
        for(int i = 0; i < targetPlayers.size(); i++)
            tempPair.add(new Pair<>(targetPlayers.get(i), targetSquares.get(i)));
        List<Player> temp = new ArrayList<>();
        for(Pair<Player, Square> pair : possibleTargetFuncM.apply(ownerPlayer, tempPair))
            temp.add(pair.getLeft());
        return temp;
    }

    @Override
    public List<Square> getPossibleSquares() {
        if(possibleTargetFuncM == null)
            return this.squaresFilter.apply(ownerPlayer, new ArrayList<>(this.targetSquares));
        List<Pair<Player, Square>> tempPair = new ArrayList<>();
        for(int i = 0; i < targetPlayers.size(); i++)
            tempPair.add(new Pair<>(targetPlayers.get(i), targetSquares.get(i)));
        List<Square> temp = new ArrayList<>();
        for(Pair<Player, Square> pair : possibleTargetFuncM.apply(ownerPlayer, tempPair))
            temp.add(pair.getRight());
        return temp;
    }

     */
}
