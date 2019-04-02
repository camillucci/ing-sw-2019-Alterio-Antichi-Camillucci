package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class ShootAction extends Action
{
    public ShootAction(Player ownerPlayer, BiConsumer<Player, ArrayList<Square>> shootFunc, Visualizable visualizable)
    {
        super(ownerPlayer);
        this.shootFuncS = shootFunc;
        this.visualizable = visualizable;
    }

    public ShootAction(BiConsumer<Player, ArrayList<Player>> shootFunc, Visualizable visualizable, Player ownerPlayer)
    {
        super(ownerPlayer);
        this.shootFuncP = shootFunc;
        this.visualizable = visualizable;
    }

    private BiConsumer<Player, ArrayList<Player>> shootFuncP;
    private BiConsumer<Player, ArrayList<Square>> shootFuncS;
    private Visualizable visualizable;

    @Override
    public void op() {
        this.shoot();
    }

    public void shoot()
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
