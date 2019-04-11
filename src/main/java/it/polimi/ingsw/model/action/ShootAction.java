package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ShootAction extends Action
{
    protected BiConsumer<Player, List<Player>> shootFuncP;
    protected BiConsumer<Player, List<Square>> shootFuncS;
    protected Function<Player,List<Square>> possibleTargetsFuncS;
    protected Function<Player, List<Player>> possibleTargetsFuncP;

    public ShootAction(BiConsumer<Player, List<Square>> shootFunc, Function<Player,List<Square>> possibleTargetsFunc) //  void shootFunc(Player,List<Square>), List<Square> possibleTargetsFunc(Player)
    {
        this.possibleTargetsFuncS = possibleTargetsFunc;
        this.shootFuncS = shootFunc;
    }
    public ShootAction(Function<Player, List<Player>> possibleTargetsFunc, BiConsumer<Player, List<Player>> shootFunc)
    {
        this.possibleTargetsFuncP = possibleTargetsFunc;
        this.shootFuncP = shootFunc;
    }

    protected ShootAction()
    {

    }

    @Override
    protected void op() {
        this.shoot();
    }

    private void shoot()
    {
        if(shootFuncP != null)
            this.shootFuncP.accept(this.ownerPlayer, this.targetPlayers);
        else
            this.shootFuncS.accept(this.ownerPlayer, this.targetSquares);
    }

    @Override
    public List<Player> getPossiblePlayers() {
        return this.possibleTargetsFuncP != null ? this.possibleTargetsFuncP.apply(ownerPlayer) : Collections.emptyList();
    }

    @Override
    public List<Square> getPossibleSquares() {
        return this.possibleTargetsFuncS != null ? this.possibleTargetsFuncS.apply(ownerPlayer) : Collections.emptyList();
    }

    @Override
    public void visualize() {

    }
}
