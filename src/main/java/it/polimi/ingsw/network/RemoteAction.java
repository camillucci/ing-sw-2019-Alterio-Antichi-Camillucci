package it.polimi.ingsw.network;

import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract class RemoteAction implements Serializable
{
    protected int index;
    protected ArrayList<PublicPlayerSnapshot> possiblePlayers;
    protected ArrayList<SquareSnapshot> possibleSquares;

    public RemoteAction(int index){
        this.index = index;
    }

    public abstract void addTarget(PublicPlayerSnapshot target) throws IOException;
    public abstract void addTarget(SquareSnapshot target) throws IOException;
    public abstract void doAction() throws IOException;
    public abstract List<PublicPlayerSnapshot> getPossiblePlayers() throws IOException, ClassNotFoundException;
    public abstract List<SquareSnapshot> getPossibleSquares() throws IOException, ClassNotFoundException;
    public abstract boolean canBeDone() throws IOException;
}
