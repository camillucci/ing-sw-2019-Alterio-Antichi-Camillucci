package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.snapshots.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to represent sudo-actions on client's side. This way, the client can communicate to the server
 * without having full access to the model (in fact the infos available through this class are limited to the ones
 * that the client has necessary access to).
 */
public class RemoteAction implements Serializable
{
    protected transient PrivatePlayerSnapshot ownerPlayer;

    /**
     * Used to distinguish this action from all the others currently present in the list the user is choosing from
     */
    public final int index;
    public final Visualizable visualizable;

    /**
     * Class which contains all the targets available in the current board state relative to this action.
     */
    private Data data;

    /**
     * Constructor. Assigns an index relative to the action and sets the visualizable parameter
     * @param index Is used to distinguish this actions from the others in the list the user is going to choose from
     * @param visualizable //todo
     */
    public RemoteAction(int index, Visualizable visualizable){
        this.index = index;
        this.visualizable = visualizable;
    }

    /**
     * Sets the ownerPlayer parameter
     * @param player Snapshot of the character relative to the current player
     */
    public void initialize(PrivatePlayerSnapshot player)
    {
        this.ownerPlayer = player;
    }

    /**
     * Returns data parameter
     * @return Contains all the possible targets the user can choose. All the targets contained here are relative to
     * this action, which means the user has to select the action to access the data parameter.
     */
    public Data getData(){return data;}

    /**
     * Changes the data parameter according to how the board state has changed.
     * @param data Contains all the possible targets the user can choose. All the targets contained here are relative to
     * this action, which means the user has to select the action to access the data parameter.
     */
    public void updateData(Data data){this.data = data;}

    public Command<RemoteActionsHandler> choose()
    {
        return new Command<>(actionHandler -> actionHandler.chooseAction(index));
    }

    public Command<RemoteActionsHandler> addTargetPlayer(String target)
    {
        return new Command<>(actionHandler -> actionHandler.addTargetPlayer(data.possiblePlayers.indexOf(target)));
    }
    public Command<RemoteActionsHandler> addTargetSquare(String target)
    {
        return new Command<>(actionHandler -> actionHandler.addTargetSquare(data.possibleSquares.indexOf(target)));
    }
    public Command<RemoteActionsHandler> usePowerUp(String powerUp)
    {
        return new Command<>(actionHandler -> actionHandler.addPowerUp(data.possiblePowerUps.indexOf(powerUp)));
    }
    public Command<RemoteActionsHandler> addDiscardable(String powerUp)
    {
        return new Command<>(actionHandler -> actionHandler.addDiscardedPowerUp(data.discardablePowerUps.indexOf(powerUp)));
    }
    public Command<RemoteActionsHandler> askActionData()
    {
        return new Command<>(RemoteActionsHandler::askActionData);
    }
    public Command<RemoteActionsHandler> addWeapon(String weapon) {
        return new Command<>(actionHandler -> actionHandler.addWeapon(data.possibleWeapons.indexOf(weapon)));
    }
    public Command<RemoteActionsHandler> addDiscardableAmmo(String ammo) throws IOException
    {
        return new Command<>(actionHandler -> actionHandler.addDiscardedAmmo(data.discardableAmmos.indexOf(ammo)));
    }
    public Command<RemoteActionsHandler> doAction()
    {
        return new Command<>(RemoteActionsHandler::doAction);
    }


    /**
     * This class contains all the possible targets available to the user when they choose to use this action
     */
    public static class Data implements Serializable
    {
        /**
         * List of players the user can target with the selected action
         */
        private final List<String> possiblePlayers;

        /**
         * List of squares the user can target with the selected action
         */
        private final List<String> possibleSquares;

        /**
         * List of powerups the user can target with the selected action
         */
        private final List<String> possiblePowerUps;

        /**
         * List of powerups the user can use to discard as a way to pay a cost
         */
        private final List<String> discardablePowerUps;

        /**
         * List of ammos the user can use to discard as a way to pay a cost
         */
        private final List<String> discardableAmmos;

        /**
         * List of loaded weapons the user can choose to fire with after choosing a "shooting" action.
         */
        private final List<String> possibleWeapons;

        /**
         * Boolean parameter which represents whether the action can be concluded. (Initially this parameter is always
         * false)
         */
        public final boolean canBeDone;

        public List<String> getPossibleSquares() {
            return possibleSquares;
        }

        public List<String> getPossiblePowerUps() {
            return possiblePowerUps;
        }

        public List<String> getDiscardablePowerUps() {
            return discardablePowerUps;
        }

        public List<String> getDiscardableAmmos() {
            return discardableAmmos;
        }

        public List<String> getPossibleWeapons() {
            return possibleWeapons;
        }

        public List<String> getPossiblePlayers() {
            return possiblePlayers;
        }

        /**
         * Contrsuctor. Sets all the parameter listed below with the values/lists goteen as input
         * @param possiblePlayers List of players the user can target with the selected action
         * @param possibleSquares List of squares the user can target with the selected action
         * @param possiblePowerUps List of powerups the user can target with the selected action
         * @param discardablePowerUps List of powerups the user discard to pay a cost
         * @param discardableAmmos List of ammos the user discard to pay a cost
         * @param possibleWeapons List of loaded weapons the user can use to fire after choosing a "shooting" weapon
         * @param canBeDone Boolean which represents whether or not the action can be concluded without taking
         *                  further basic steps.
         */
        public Data(List<String> possiblePlayers, List<String> possibleSquares, List<String> possiblePowerUps, List<String> discardablePowerUps, List<String> discardableAmmos, List<String> possibleWeapons,  boolean canBeDone)
        {
            this.possiblePlayers = new ArrayList<>(possiblePlayers);
            this.possibleSquares = new ArrayList<>(possibleSquares);
            this.possiblePowerUps = new ArrayList<>(possiblePowerUps);
            this.discardablePowerUps = new ArrayList<>(discardablePowerUps);
            this.discardableAmmos = new ArrayList<>(discardableAmmos);
            this.possibleWeapons = new ArrayList<>(possibleWeapons);
            this.canBeDone = canBeDone;
        }
    }
}
