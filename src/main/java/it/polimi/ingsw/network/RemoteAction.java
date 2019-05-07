package it.polimi.ingsw.network;

import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;

import java.io.Serializable;
import java.util.List;

public class RemoteAction implements Serializable
{
    private Client server;
    public void iniialize(Client server)
    {
        this.server = server;
    }

    public void addTarget(PublicPlayerSnapshot target)
    {

    }

    public void addTarget(SquareSnapshot target)
    {
    }

    public void doAction()
    {

    }

    public List<PublicPlayerSnapshot> getPossiblePlayers(){return null;}
    public List<SquareSnapshot> getPossibleSquares(){return null; }
}
