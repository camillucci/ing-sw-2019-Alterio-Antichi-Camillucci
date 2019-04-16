package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ShootAction extends Action
{
    protected BiConsumer<Player, List<Player>> shootFuncP = (a,b) -> {};
    protected BiConsumer<Player, List<Square>> shootFuncS = (a,b) -> {};
    protected Function<Player,List<Square>> possibleTargetsFuncS;
    protected BiFunction<Player, List<Player>, List<Player>> possibleTargetsFuncP;

    protected ShootAction(){}

    public ShootAction(BiConsumer<Player, List<Square>> shootFunc, Function<Player,List<Square>> possibleTargetsFunc) //  void shootFunc(Player,List<Square>), List<Square> possibleTargetsFunc(Player)
    {
        this.possibleTargetsFuncS = possibleTargetsFunc;
        this.shootFuncS = shootFunc;
    }

    public ShootAction(BiFunction<Player, List<Player>, List<Player>> possibleTargetsFunc, BiConsumer<Player, List<Player>> shootFunc)
    {
        this.possibleTargetsFuncP = possibleTargetsFunc;
        this.shootFuncP = shootFunc;
    }

    @Override
    protected void op() {
        this.shoot();
    }

    protected void shoot()
    {
        this.shootFuncP.accept(this.ownerPlayer, this.targetPlayers);
        this.shootFuncS.accept(this.ownerPlayer, this.targetSquares);
    }

    @Override
    public List<Player> getPossiblePlayers() {
        return this.possibleTargetsFuncP.apply(ownerPlayer, new ArrayList<>(this.targetPlayers));
    }

    @Override
    public List<Square> getPossibleSquares() {
        return this.possibleTargetsFuncS.apply(ownerPlayer);
    }
}
