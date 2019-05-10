package it.polimi.ingsw.network;

import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;

import java.io.Serializable;
import java.util.List;

public class RemoteAction implements Serializable
{
    private Client server;
    private boolean canBeDone;
    private List<PublicPlayerSnapshot> possiblePlayers;
    private List<SquareSnapshot> possibleSquares;

    public void inizialize(Client server)
    {
        this.server = server;
    }

    public void addTarget(PublicPlayerSnapshot target) throws Exception
    {
        //server.handleAction(RemoteActionsHandler.ADD_PLAYER, possiblePlayers.indexOf(target));

        server.out().sendInt(RemoteActionsHandler.ADD_PLAYER);
        server.out().sendInt(possiblePlayers.indexOf(target));
    }

    public void addTarget(SquareSnapshot target) throws Exception
    {
        //server.handleAction(RemoteActionsHandler.ADD_SQUARE, possibleSquares.indexOf(target));

        server.out().sendInt(RemoteActionsHandler.ADD_SQUARE);
        server.out().sendInt(possibleSquares.indexOf(target));
    }

    public void doAction() throws Exception
    {
        //server.handleAction(RemoteActionsHandler.DO_ACTION, 0);

        server.out().sendInt(RemoteActionsHandler.DO_ACTION);
    }

    public List<PublicPlayerSnapshot> getPossiblePlayers() throws Exception
    {
        server.out().sendInt(RemoteActionsHandler.GET_PLAYERS);
        return server.in().getObject();
    }
    public List<SquareSnapshot> getPossibleSquares() throws Exception
    {
        server.out().sendInt(RemoteActionsHandler.GET_SQUARES);
        return server.in().getObject();
    }
    public boolean canBeDone() {
        return canBeDone;
    }
}
