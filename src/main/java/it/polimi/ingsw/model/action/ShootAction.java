package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Visualizable;

import java.util.List;
import java.util.function.BiConsumer;

public class ShootAction extends Action
{
    public ShootAction(BiConsumer<Player, List<Square>> shootFunc, Visualizable visualizable)
    {
        this.shootFuncS = shootFunc;
        this.visualizable = visualizable;
    }

    public ShootAction(Visualizable visualizable, BiConsumer<Player, List<Player>> shootFunc)
    {
        this.shootFuncP = shootFunc;
        this.visualizable = visualizable;
    }
    protected ShootAction()
    {

    }

    protected BiConsumer<Player, List<Player>> shootFuncP;
    protected BiConsumer<Player, List<Square>> shootFuncS;
    private Visualizable visualizable;

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
    public void visualize() {
        this.visualizable.visualize();
    }
}
