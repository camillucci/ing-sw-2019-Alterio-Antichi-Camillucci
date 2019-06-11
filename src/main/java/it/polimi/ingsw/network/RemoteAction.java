package it.polimi.ingsw.network;

import it.polimi.ingsw.model.snapshots.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class RemoteAction implements Serializable
{
    protected transient PrivatePlayerSnapshot ownerPlayer;
    public final int index;
    public final String text;
    private Data data;

    public RemoteAction(int index, String text){
        this.index = index;
        this.text = text;
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

    public String toString() {
        return text;
    }

    public static class Data implements Serializable
    {
        public final List<String> possiblePlayers;
        public final List<String> possibleSquares;
        public final List<String> possiblePowerUps;
        public final List<String> discardablePowerUps;
        public final List<String> discardableAmmos;
        public final List<String> possibleWeapons;
        public final boolean canBeDone;

        public Data(List<String> possiblePlayers, List<String> possibleSquares, List<String> possiblePowerUps, List<String> discardablePowerUps, List<String> discardableAmmos, List<String> possibleWeapons,  boolean canBeDone)
        {
            this.possiblePlayers = possiblePlayers;
            this.possibleSquares = possibleSquares;
            this.possiblePowerUps = possiblePowerUps;
            this.discardablePowerUps = discardablePowerUps;
            this.discardableAmmos = discardableAmmos;
            this.possibleWeapons = possibleWeapons;
            this.canBeDone = canBeDone;
        }
    }
}
