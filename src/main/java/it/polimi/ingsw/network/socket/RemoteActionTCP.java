package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.network.RemoteAction;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class RemoteActionTCP extends RemoteAction implements Serializable
{
    private int index;
    private TCPClient server;
    private List<PublicPlayerSnapshot> possiblePlayers;
    private List<SquareSnapshot> possibleSquares;

    public RemoteActionTCP(int index){
        super(index);
        this.index = index;
    }

    public void inizialize(TCPClient server) throws IOException
    {
        this.server = server;
        server.out().sendInt(this.index);
    }

    @Override
    public void addTarget(PublicPlayerSnapshot target) throws IOException
    {
        //server.handleAction(TCPRemoteActionsHandler.ADD_PLAYER, possiblePlayers.indexOf(target));
        server.out().sendInt(TCPRemoteActionsHandler.ADD_PLAYER);
        server.out().sendInt(possiblePlayers.indexOf(target));
    }

    @Override
    public void addTarget(SquareSnapshot target) throws IOException
    {
        //server.handleAction(TCPRemoteActionsHandler.ADD_SQUARE, possibleSquares.indexOf(target));

        server.out().sendInt(TCPRemoteActionsHandler.ADD_SQUARE);
        server.out().sendInt(possibleSquares.indexOf(target));
    }

    @Override
    public void doAction() throws IOException
    {
        //server.handleAction(TCPRemoteActionsHandler.DO_ACTION, 0);

        server.out().sendInt(TCPRemoteActionsHandler.DO_ACTION);
    }

    @Override
    public List<PublicPlayerSnapshot> getPossiblePlayers() throws IOException, ClassNotFoundException
    {
        server.out().sendInt(TCPRemoteActionsHandler.GET_PLAYERS);
        return server.in().getObject();

    }

    @Override
    public List<SquareSnapshot> getPossibleSquares() throws IOException, ClassNotFoundException
    {
        server.out().sendInt(TCPRemoteActionsHandler.GET_SQUARES);
        return server.in().getObject();
    }

    @Override
    public boolean canBeDone() throws IOException {
        server.out().sendInt(TCPRemoteActionsHandler.CAN_BE_DONE);
        return server.in().getBool();
    }
}
