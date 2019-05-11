package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.network.RemoteAction;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class RemoteActionSocket extends RemoteAction implements Serializable
{
    private int index;
    private TCPClient server;
    private List<PublicPlayerSnapshot> possiblePlayers;
    private List<SquareSnapshot> possibleSquares;

    public RemoteActionSocket(int index){
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
        //server.handleAction(RemoteActionsHandlerSocket.ADD_PLAYER, possiblePlayers.indexOf(target));
        server.out().sendInt(RemoteActionsHandlerSocket.ADD_PLAYER);
        server.out().sendInt(possiblePlayers.indexOf(target));
    }

    @Override
    public void addTarget(SquareSnapshot target) throws IOException
    {
        //server.handleAction(RemoteActionsHandlerSocket.ADD_SQUARE, possibleSquares.indexOf(target));

        server.out().sendInt(RemoteActionsHandlerSocket.ADD_SQUARE);
        server.out().sendInt(possibleSquares.indexOf(target));
    }

    @Override
    public void doAction() throws IOException
    {
        //server.handleAction(RemoteActionsHandlerSocket.DO_ACTION, 0);

        server.out().sendInt(RemoteActionsHandlerSocket.DO_ACTION);
    }

    @Override
    public List<PublicPlayerSnapshot> getPossiblePlayers() throws IOException, ClassNotFoundException
    {
        server.out().sendInt(RemoteActionsHandlerSocket.GET_PLAYERS);
        return server.in().getObject();

    }

    @Override
    public List<SquareSnapshot> getPossibleSquares() throws IOException, ClassNotFoundException
    {
        server.out().sendInt(RemoteActionsHandlerSocket.GET_SQUARES);
        return server.in().getObject();
    }

    @Override
    public boolean canBeDone() throws IOException {
        server.out().sendInt(RemoteActionsHandlerSocket.CAN_BE_DONE);
        return server.in().getBool();
    }
}
