package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Visualizable;

import java.util.List;
import java.util.function.BiConsumer;

public class ShootAction extends Action
{
    public ShootAction(Player ownerPlayer, BiConsumer<Player, List<Square>> shootFunc, Visualizable visualizable)
    {
        super(ownerPlayer);
        this.shootFuncS = shootFunc;
        this.visualizable = visualizable;
    }

    public ShootAction(BiConsumer<Player, List<Player>> shootFunc, Visualizable visualizable, Player ownerPlayer)
    {
        super(ownerPlayer);
        this.shootFuncP = shootFunc;
        this.visualizable = visualizable;
    }
    protected ShootAction(Player ownerPlayer)
    {
        super(ownerPlayer);
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
            this.shootFuncP.accept(this.currentPlayer, this.targetPlayers);
        else
            this.shootFuncS.accept(this.currentPlayer, this.targetSquares);
    }

    @Override
    public void visualize() {
        this.visualizable.visualize();
    }
}
