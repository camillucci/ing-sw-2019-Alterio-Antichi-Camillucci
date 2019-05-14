package it.polimi.ingsw.network;

import it.polimi.ingsw.model.snapshots.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class RemoteAction implements Serializable
{
    protected transient PrivatePlayerSnapshot ownerPlayer;
    protected int index;
    protected ArrayList<String> possiblePlayers;
    protected ArrayList<String> possibleSquares;
    protected ArrayList<String> possiblePowerUps;
    protected ArrayList<String> discardablePowerUps;

    public RemoteAction(int index){
        this.index = index;
    }

    public void initialize(PrivatePlayerSnapshot player)
    {
        this.ownerPlayer = player;
    }

    public abstract void addTargetPlayer(String target) throws IOException;
    public abstract void addTargetSquare(String target) throws IOException;
    public abstract void usePowerUp(String powerUp) throws IOException;
    public abstract void addDiscardable(String powerUp) throws IOException;
    public abstract void addWeapon(String weapon) throws IOException;
    public abstract List<String> getPossiblePlayers() throws IOException, ClassNotFoundException;
    public abstract List<String> getPossibleSquares() throws IOException, ClassNotFoundException;
    public abstract List<String> getPossiblePowerups() throws IOException, ClassNotFoundException;
    public abstract List<String> getDiscardablePowerups() throws IOException, ClassNotFoundException;
    public abstract boolean canBeDone() throws IOException;
    public abstract void doAction() throws IOException;
    public abstract String toString();
}
