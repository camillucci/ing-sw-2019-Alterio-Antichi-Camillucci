package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.snapshots.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RemoteAction implements Serializable
{
    protected transient PrivatePlayerSnapshot ownerPlayer;
    public final int index;
    public final Visualizable visualizable;
    private Data data;

    public RemoteAction(int index, Visualizable visualizable){
        this.index = index;
        this.visualizable = visualizable;
    }
    public void initialize(PrivatePlayerSnapshot player)
    {
        this.ownerPlayer = player;
    }

    public Data getData(){return data;}
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


    public static class Data implements Serializable
    {
        private final List<String> possiblePlayers;
        private final List<String> possibleSquares;
        private final List<String> possiblePowerUps;
        private final List<String> discardablePowerUps;
        private final List<String> discardableAmmos;
        private final List<String> possibleWeapons;
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
