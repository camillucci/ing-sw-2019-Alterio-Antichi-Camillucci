package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class ShootAction extends Action
{
    public ShootAction(Player ownerPlayer, BiConsumer<Player, ArrayList<Square>> shootFunc, Visualizable visualizable)
    {
        super(ownerPlayer);
        this.shootFunc = shootFunc;
        this.visualizable = visualizable;
    }

    private BiConsumer<Player, ArrayList<Square>> shootFunc;
    private Visualizable visualizable;

    @Override
    public void op() {
        this.shoot();
    }

    public void shoot()
    {
        this.shootFunc.accept(this.currentPlayer, this.targetSquares);
    }

    @Override
    public void visualize() {
        this.visualizable.visualize();
    }
}
