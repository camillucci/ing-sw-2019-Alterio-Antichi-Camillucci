package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.generics.Visualizable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a specific case of action. It contains all the methods and info relative to an action used to
 * move a player from one square to another.
 */
public class MoveAction extends Action
{
    /**
     * Integer that indicates the minimum amount of squares the player has to move
     */
    private int minDistance;

    /**
     * Integer that represents the maximum amount of squares the player can choose to move
     */
    private int maxDistance;

    /**
     * Constructor that assigns the input parameter to its global correspondent and sets some of the other parameters
     * using default values.
     * @param maxDistance Integer that represents the maximum amount of squares the player can choose to move
     */
    public MoveAction(int maxDistance)
    {
        this.minDistance = -1;
        this.maxDistance = maxDistance;
        this.optional = true;
        this.canBeDone = false;
        this.visualizable = new Visualizable(null, "m" + maxDistance + ".png", "move of " + maxDistance, "Move");
    }

    /**
     * Constructor that assigns the input parameters to their global correspondents.
     * @param minDistance Integer that indicates the minimum amount of squares the player has to move
     * @param maxDistance Integer that represents the maximum amount of squares the player can choose to move
     */
    public MoveAction(int minDistance, int maxDistance)
    {
        this(maxDistance);
        this.minDistance = minDistance;
        this.optional = false;
    }

    /**
     * Executes the move action by calling the move method
     */
    @Override
    protected void op()
    {
        this.move();
    }

    /**
     * Private method that moves the player from the square they currently are in to the square decided as a target.
     */
    private void move()
    {
        ownerPlayer.getCurrentSquare().removePlayer(ownerPlayer);
        ownerPlayer.setCurrentSquare(targetSquares.get(0));
        ownerPlayer.getCurrentSquare().addPlayer(ownerPlayer);
    }

    /**
     * Adds the input square to the targets list (in this class the target list is always going to have a maximum of
     * 1 element). Then sets the canBeDone variable to true, indicating that the action can be executed.
     * @param target Reference to the square the player using the action is going to move to
     */
    @Override
    public void addTarget(Square target)
    {
        if(this.getPossibleSquares().contains(target)) {
            this.targetSquares.add(target);
            this.canBeDone = true;
        }
    }

    /**
     * Based on minDistance and maxDistance values, the method calculates which are the legal squares the player can
     * move to. First all the squares that have a distance of maxDistance or less from the player are added to the
     * list. Then every square that is too close to the player is removed.
     * @return List of squares that the player can move to, calculated based on minDistance and maxDistance values.
     */
    @Override
    public List<Square> getPossibleSquares() {
        if(!targetSquares.isEmpty())
            return Collections.emptyList();
        if(minDistance == -1)
            return ownerPlayer.gameBoard.getSquares(ownerPlayer, this.maxDistance);
        List<Square> temp = new ArrayList<>(ownerPlayer.gameBoard.getSquares(ownerPlayer, this.maxDistance));
        temp.removeAll(ownerPlayer.gameBoard.getSquares(ownerPlayer, this.minDistance - 1));
        return temp;
    }

    /**
     * Getter that returns a boolean parameter that represents whether the input action is compatible with this action.
     * @param action Action this class is going to be compared to
     * @return A parameter that represents whether the input is equal to this action.
     */
    @Override
    public boolean isCompatible(Action action) {
        return action.testCompatibilityWith(this);
    }

    /**
     * Determines whether the input action is compatible with this class by checking bo minDistance's values and
     * comparing their maxDistance's values. Returns true if this class has minDistance and maxDistance values
     * between the minDistance and maxDistance values of the input parameter.
     */
    @Override
    public boolean testCompatibilityWith(MoveAction action)
    {
        if(action.minDistance == -1 || this.minDistance == -1)
            return this.maxDistance <= action.maxDistance;
        return this.minDistance >= action.minDistance &&  this.maxDistance <= action.maxDistance;
    }
}
